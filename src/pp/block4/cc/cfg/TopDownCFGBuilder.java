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
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;
import pp.block4.cc.cfg.FragmentParser.ContStatContext;
import pp.block4.cc.cfg.FragmentParser.DeclContext;
import pp.block4.cc.cfg.FragmentParser.IfStatContext;
import pp.block4.cc.cfg.FragmentParser.PrintStatContext;
import pp.block4.cc.cfg.FragmentParser.ProgramContext;

/** Template top-down CFG builder. */
public class TopDownCFGBuilder extends FragmentBaseListener {
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
			ProgramContext tree = parser.program();
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

	private ParseTreeProperty<ParseTree> entryParseTree = new ParseTreeProperty<>();
	private ParseTreeProperty<ParseTree> exitParseTree = new ParseTreeProperty<>();

	/** Builds the CFG for a program given as an ANTLR parse tree. */
	public Graph build(ProgramContext tree) {
		this.graph = new Graph();
		this.walker.walk(this, tree);
		try {
			this.graph.writeDOT("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\cfg\\graphTopDown.dot", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.graph;
	}

	@Override
	public void enterProgram(ProgramContext ctx) {
		for (int i = 1; i < ctx.stat().size(); i++) {
			exitParseTree.put(ctx.stat(i), ctx.stat(i-1));
		}
	}

	@Override
	public void enterDecl(DeclContext ctx) {
		Node node = addNode(ctx, "Declare: " + ctx.ID().getText());
		exit.put(ctx, node);
		if (exitParseTree.get(ctx) != null) {
			exit.get(exitParseTree.get(ctx)).addEdge(node);
		}
		if (entryParseTree.get(ctx) != null) {
			node.addEdge(entry.get(entryParseTree.get(ctx)));
		}
	}

	@Override
	public void enterAssignStat(FragmentParser.AssignStatContext ctx) {
		Node node = addNode(ctx, "Assign: " + ctx.target().getText());
		exit.put(ctx, node);
		if (exitParseTree.get(ctx) != null) {
			exit.get(exitParseTree.get(ctx)).addEdge(node);
		}
		if (entryParseTree.get(ctx) != null) {
			node.addEdge(entry.get(entryParseTree.get(ctx)));
		}
	}

	@Override
	public void enterIfStat(IfStatContext ctx) {
		Node node = addNode(ctx, "If: " + ctx.expr().getText());
		if (exitParseTree.get(ctx) != null) {
			exit.get(exitParseTree.get(ctx)).addEdge(node);
		}
		Node exitNode = addNode(ctx, "IfExit");

		if (entryParseTree.get(ctx) != null) {
			exitNode.addEdge(entry.get(entryParseTree.get(ctx)));
		}
		exit.put(ctx, exitNode);
		entry.put(ctx, exitNode);
		entryParseTree.put(ctx.stat(0), ctx);
		exit.put(ctx.expr(), node);
		exitParseTree.put(ctx.stat(0), ctx.expr());
		if (ctx.ELSE() != null) {
			Node elseNode = addNode(ctx, "Else");
			node.addEdge(elseNode);
			entry.put(ctx.ELSE(), exitNode);
			exit.put(ctx.ELSE(), elseNode);
			entryParseTree.put(ctx.stat(1), ctx.ELSE());
			exitParseTree.put(ctx.stat(1), ctx.ELSE());
		} else {
			node.addEdge(exitNode);
		}
	}

	@Override
	public void enterWhileStat(FragmentParser.WhileStatContext ctx) {
		Node node = addNode(ctx, "While: " + ctx.expr().getText());
		if (exitParseTree.get(ctx) != null) {
			exit.get(exitParseTree.get(ctx)).addEdge(node);
		}
		Node exitNode = addNode(ctx, "WhileExit");

		if (entryParseTree.get(ctx) != null) {
			exitNode.addEdge(entry.get(entryParseTree.get(ctx)));
		}
		exit.put(ctx, exitNode);
		exit.put(ctx.expr(), node);
		exitParseTree.put(ctx.stat(), ctx.expr());
		entry.put(ctx, node);
		entryParseTree.put(ctx.stat(), ctx);
		node.addEdge(exitNode);
	}

	@Override
	public void enterBlockStat(FragmentParser.BlockStatContext ctx) {
		for (int i = 1; i < ctx.stat().size(); i++) {
			exitParseTree.put(ctx.stat(i), ctx.stat(i-1));
		}
		exitParseTree.put(ctx.stat(0), exitParseTree.get(ctx));
		entryParseTree.put(ctx.stat(ctx.stat().size() - 1), entryParseTree.get(ctx));
	}

	@Override
	public void enterPrintStat(PrintStatContext ctx) {
		Node node = addNode(ctx, "Print");
		exit.put(ctx, node);
		if (exitParseTree.get(ctx) != null) {
			exit.get(exitParseTree.get(ctx)).addEdge(node);
		}
		if (entryParseTree.get(ctx) != null) {
			node.addEdge(entry.get(entryParseTree.get(ctx)));
		}
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
		TopDownCFGBuilder builder = new TopDownCFGBuilder();
		for (String filename : args) {
			File file = new File(filename);
			System.out.println(filename);
			System.out.println(builder.build(file));
		}
	}
}
