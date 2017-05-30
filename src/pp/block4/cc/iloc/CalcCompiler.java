package pp.block4.cc.iloc;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.iloc.CalcParser.CompleteContext;
import pp.iloc.Assembler;
import pp.iloc.Simulator;
import pp.iloc.model.Op;
import pp.iloc.model.OpCode;
import pp.iloc.model.Operand;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;

import java.io.IOException;

import static org.junit.Assert.fail;

/** Compiler from Calc.g4 to ILOC. */
public class CalcCompiler extends CalcBaseListener {
	/** Program under construction. */
	private Program prog;
	// Attribute maps and other fields

	/** Compiles a given expression string into an ILOC program. */
	public Program compile(String text) {
		Program result = null;
		ErrorListener listener = new ErrorListener();
		CharStream chars = CharStreams.fromString(text);
		Lexer lexer = new CalcLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		TokenStream tokens = new CommonTokenStream(lexer);
		CalcParser parser = new CalcParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		ParseTree tree = parser.complete();
		if (listener.hasErrors()) {
			System.out.printf("Parse errors in %s:%n", text);
			for (String error : listener.getErrors()) {
				System.err.println(error);
			}
		} else {
			result = compile(tree);
		}
		return result;
	}

	private Assembler assembler = Assembler.instance();
	ParseTreeProperty<String> program = new ParseTreeProperty<>();
	ParseTreeProperty<String> registers = new ParseTreeProperty<>();
	int registerCounter = 0;

	/** Compiles a given Calc-parse tree into an ILOC program. */
	public Program compile(ParseTree tree) {
		Program program = null;

		new ParseTreeWalker().walk(this, tree);
		String programString = this.program.get(tree);
		System.out.println(programString);
		try {
			program = this.assembler.assemble(programString);
		} catch (FormatException e) {
			return null;
		}

		return program;
	}

	@Override
	public void exitComplete(CompleteContext ctx) {
		program.put(ctx, program.get(ctx.expr()) + "out \"Outcome: \", " + registers.get(ctx.expr()) + "\n");
	}

	@Override
	public void exitPar(CalcParser.ParContext ctx) {
		program.put(ctx, program.get(ctx.expr()));
		registers.put(ctx, registers.get(ctx.expr()));
	}

	@Override
	public void exitMinus(CalcParser.MinusContext ctx) {
		String result = "";
		result = result.concat(program.get(ctx.expr()));
		String outRegister = "r_" + registerCounter++;
		result = result.concat("multI " +  registers.get(ctx.expr()) + ", -1 => " + outRegister + "\n");
		registers.put(ctx, outRegister);
		program.put(ctx, result);
	}

	@Override
	public void exitNumber(CalcParser.NumberContext ctx) {
		String result = "";
		String outRegister = "r_" + registerCounter++;
		result = result.concat("loadI " + Integer.parseInt(ctx.NUMBER().getText()) + " => " + outRegister + "\n");
		registers.put(ctx, outRegister);
		program.put(ctx, result);
	}

	@Override
	public void exitTimes(CalcParser.TimesContext ctx) {
		String result = "";
		result = result.concat(program.get(ctx.expr(0)));
		result = result.concat(program.get(ctx.expr(1)));
		String outRegister = "r_" + registerCounter++;
		result = result.concat("mult " +  registers.get(ctx.expr(0)) + ", "+ registers.get(ctx.expr(1)) +" => " + outRegister + "\n");
		registers.put(ctx, outRegister);
		program.put(ctx, result);
	}

	@Override
	public void exitPlus(CalcParser.PlusContext ctx) {
		String result = "";
		result = result.concat(program.get(ctx.expr(0)));
		result = result.concat(program.get(ctx.expr(1)));
		String outRegister = "r_" + registerCounter++;
		result = result.concat("add " +  registers.get(ctx.expr(0)) + ", "+ registers.get(ctx.expr(1)) +" => " + outRegister + "\n");
		registers.put(ctx, outRegister);
		program.put(ctx, result);
	}

	/** Constructs an operation from the parameters
	 * and adds it to the program under construction. */
	private void emit(OpCode opCode, Operand... args) {
		this.prog.addInstr(new Op(opCode, args));
	}

	/** Calls the compiler, and simulates and prints the compiled program. */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: [expr]+");
			return;
		}
		CalcCompiler compiler = new CalcCompiler();
		for (String expr : args) {
			System.out.println("Processing " + expr);
			Program prog = compiler.compile(expr);
			new Simulator(prog).run();
			System.out.println(prog.prettyPrint());
		}
	}
}
