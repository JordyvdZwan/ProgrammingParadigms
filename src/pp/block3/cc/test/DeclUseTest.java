package pp.block3.cc.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import pp.block3.cc.antlr.*;
import pp.block3.cc.symbol.DeclUseLexer;
import pp.block3.cc.symbol.DeclUseListenerImplOurButAlsoActuallyMine;
import pp.block3.cc.symbol.DeclUseParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeclUseTest {
	private final ParseTreeWalker walker = new ParseTreeWalker();
	private final DeclUseListenerImplOurButAlsoActuallyMine declUseListenerImplOurButAlsoActuallyMine = new DeclUseListenerImplOurButAlsoActuallyMine();

	@Test
	public void test() throws IOException {
		List<String> t1 = new ArrayList<>();
		List<String> t2 = new ArrayList<>();
		t2.add("ERROR: Variable not declared:'hoi' at line number: 1 Column number: 54.");
		List<String> t3 = new ArrayList<>();
		t3.add("ERROR: Variable already in scope:'HELLO' at line number: 1 Column number: 55.");
		test(t1, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\symbol\\t1");
		test(t2, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\symbol\\t2");
		test(t3, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\symbol\\t3");
	}

	private void test(List<String> expected, String expr) throws IOException {
//		assertEquals(expected, parseTTAttr(expr).val);
		ParseTree tree = parseCalc(expr);
//		this.declUseListenerImplOurButAlsoActuallyMine.init();
		this.walker.walk(this.declUseListenerImplOurButAlsoActuallyMine, tree);
		System.out.println(this.declUseListenerImplOurButAlsoActuallyMine.val(tree));
		assertEquals(expected, this.declUseListenerImplOurButAlsoActuallyMine.val(tree));
	}

	private ParseTree parseCalc(String text) throws IOException {
		CharStream chars = CharStreams.fromFileName(text);
		Lexer lexer = new DeclUseLexer(chars);
		TokenStream tokens = new CommonTokenStream(lexer);
		DeclUseParser parser = new DeclUseParser(tokens);
		return parser.program();
	}

//	private TTAttrParser.TttContext parseTTAttr(String text) {
//		CharStream chars = CharStreams.fromString(text);
//		Lexer lexer = new TTAttrLexer(chars);
//		TokenStream tokens = new CommonTokenStream(lexer);
//		TTAttrParser parser = new TTAttrParser(tokens);
//		return parser.ttt();
//	}
}
