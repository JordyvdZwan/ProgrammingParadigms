package pp.block3.cc.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import pp.block3.cc.symbol.DeclUseListenerImplOurButAlsoActuallyMine;
import pp.block3.cc.tabular.OurTabularGrammerYetStillMineLexer;
import pp.block3.cc.tabular.OurTabularGrammerYetStillMineParser;

import java.io.IOException;
import java.util.List;

public class TabularTest {
	private final ParseTreeWalker walker = new ParseTreeWalker();
	private final DeclUseListenerImplOurButAlsoActuallyMine declUseListenerImplOurButAlsoActuallyMine = new DeclUseListenerImplOurButAlsoActuallyMine();

	@Test
	public void test() throws IOException {
		test(null, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\tabular\\tabular-1.tex");
	}

	private void test(List<String> expected, String expr) throws IOException {
//		assertEquals(expected, parseTTAttr(expr).val);
		ParseTree tree = parseCalc(expr);
		System.out.println(tree);
//		this.declUseListenerImplOurButAlsoActuallyMine.init();
//		this.walker.walk(this.declUseListenerImplOurButAlsoActuallyMine, tree);
//		System.out.println(this.declUseListenerImplOurButAlsoActuallyMine.val(tree));
//		assertEquals(expected, this.declUseListenerImplOurButAlsoActuallyMine.val(tree));
	}

	private ParseTree parseCalc(String text) throws IOException {
		CharStream chars = CharStreams.fromFileName(text);
		Lexer lexer = new OurTabularGrammerYetStillMineLexer(chars);
		TokenStream tokens = new CommonTokenStream(lexer);
		OurTabularGrammerYetStillMineParser parser = new OurTabularGrammerYetStillMineParser(tokens);
		return parser.table();
	}

//	private TTAttrParser.TttContext parseTTAttr(String text) {
//		CharStream chars = CharStreams.fromString(text);
//		Lexer lexer = new TTAttrLexer(chars);
//		TokenStream tokens = new CommonTokenStream(lexer);
//		TTAttrParser parser = new TTAttrParser(tokens);
//		return parser.ttt();
//	}
}
