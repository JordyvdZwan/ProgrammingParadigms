package pp.block4.cc.cfg;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.cfg.FragmentParser.BlockStatContext;
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;
import pp.block4.cc.cfg.FragmentParser.ContStatContext;
import pp.block4.cc.cfg.FragmentParser.DeclContext;
import pp.block4.cc.cfg.FragmentParser.IfStatContext;
import pp.block4.cc.cfg.FragmentParser.PrintStatContext;
import pp.block4.cc.cfg.FragmentParser.ProgramContext;
import pp.block4.cc.cfg.FragmentParser.WhileStatContext;

/** Template bottom-up CFG builder. */
public class BottomUpCFGBuilder extends FragmentBaseListener {
	/** The CFG being built. */
	private Graph graph;

	/** Builds the CFG for a program contained in a given file. */
	public Graph build(File file) {
		Graph result = null;
		ErrorListener listener = new ErrorListener();
		try {
			CharStream chars = CharStreams.fromPath(file.toPath());
			Lexer lexer = new FragmentLexer(chars);
			lexer.removeErrorListeners();
			lexer.addErrorListener(listener);
			TokenStream tokens = new CommonTokenStream(lexer);
			FragmentParser parser = new FragmentParser(tokens);
			parser.removeErrorListeners();
			parser.addErrorListener(listener);
			ParseTree tree = parser.program();
			if (listener.hasErrors()) {
				System.out.printf("Parse errors in %s:%n", file.getPath());
				for (String error : listener.getErrors()) {
					System.err.println(error);
				}
			} else {
				result = build(tree);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private final ParseTreeWalker walker = new ParseTreeWalker();

	private ParseTreeProperty<Node> entry = new ParseTreeProperty<>();
	private ParseTreeProperty<Node> exit = new ParseTreeProperty<>();

	/** Builds the CFG for a program given as an ANTLR parse tree. */
	public Graph build(ParseTree tree) {
		this.graph = new Graph();
		this.walker.walk(this, tree);
		try {
			this.graph.writeDOT("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\cfg\\graphBottomUp.dot", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.graph;
	}

	@Override
	public void exitProgram(ProgramContext ctx) {
		for (int i = 1; i < ctx.stat().size(); i++) {
			exit.get(ctx.stat(i-1)).addEdge(entry.get(ctx.stat(i)));
		}
	}

	@Override
	public void exitDecl(DeclContext ctx) {
		Node node = addNode(ctx, "Declare: " + ctx.ID().getText());
		entry.put(ctx, node);
		exit.put(ctx, node);
	}

	@Override
	public void exitAssignStat(FragmentParser.AssignStatContext ctx) {
		Node node = addNode(ctx, "Assign: " + ctx.target().getText());
		entry.put(ctx, node);
		exit.put(ctx, node);
	}

	@Override
	public void exitIfStat(IfStatContext ctx) {
		Node entry = addNode(ctx, "If: " + ctx.expr().getText());
		entry.addEdge(this.entry.get(ctx.stat(0)));
		Node exit = addNode(ctx, "IfExit");
		this.exit.get(ctx.stat(0)).addEdge(exit);
		this.entry.put(ctx, entry);
		this.exit.put(ctx, exit);
		if (ctx.ELSE() != null) {
			Node elseNode = addNode(ctx, "Else");
			entry.addEdge(elseNode);
			elseNode.addEdge(this.entry.get(ctx.stat(1)));
			this.exit.get(ctx.stat(1)).addEdge(exit);
		} else {
			entry.addEdge(exit);
		}
	}

	@Override
	public void exitWhileStat(WhileStatContext ctx) {
		Node entry = addNode(ctx, "While: " + ctx.expr().getText());
//		Node exit = addNode(ctx, "WhileExit");
		this.entry.put(ctx, entry);
		this.exit.put(ctx, entry);
		entry.addEdge(this.entry.get(ctx.stat()));
		this.exit.get(ctx.stat()).addEdge(entry);
//		entry.addEdge(exit);
	}

	@Override
	public void exitBlockStat(BlockStatContext ctx) {
		for (int i = 1; i < ctx.stat().size(); i++) {
			exit.get(ctx.stat(i-1)).addEdge(entry.get(ctx.stat(i)));
		}
		//Errors, errors everywhere!
		if (ctx.stat().size() < 1) {
			entry.put(ctx, entry.get(ctx.stat(0)));
			exit.put(ctx, entry.get(ctx.stat(ctx.stat().size() - 1)));
		}
	}

	@Override
	public void exitPrintStat(PrintStatContext ctx) {
		Node node = addNode(ctx, "Print");
		entry.put(ctx, node);
		exit.put(ctx, node);
	}

	@Override
	public void enterBreakStat(BreakStatContext ctx) {
		throw new IllegalArgumentException("Break not supported");
	}

	@Override
	public void enterContStat(ContStatContext ctx) {
		throw new IllegalArgumentException("Continue not supported");
	}

	/** Adds a node to he CGF, based on a given parse tree node.
	 * Gives the CFG node a meaningful ID, consisting of line number and 
	 * a further indicator.
	 */
	private Node addNode(ParserRuleContext node, String text) {
		return this.graph.addNode(node.getStart().getLine() + ": " + text);
	}

	/** Main method to build and print the CFG of a simple Java program. */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: [filename]+");
			return;
		}
		BottomUpCFGBuilder builder = new BottomUpCFGBuilder();
		for (String filename : args) {
			File file = new File(filename);
			System.out.println(filename);
			System.out.println(builder.build(file));
		}
	}
}
