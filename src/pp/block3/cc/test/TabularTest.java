package pp.block3.cc.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import pp.block3.cc.symbol.DeclUseListenerImplOurButAlsoActuallyMine;
import pp.block3.cc.tabular.MyErrorListener;
import pp.block3.cc.tabular.OurTabularGrammerYetStillMineLexer;
import pp.block3.cc.tabular.OurTabularGrammerYetStillMineParser;
import pp.block3.cc.tabular.TabularToHTML;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TabularTest {
	private final ParseTreeWalker walker = new ParseTreeWalker();
	private final TabularToHTML tabularToHTML = new TabularToHTML();

	String t1 = "<html><body><table border=\"1\"><tr><td>Aap</td><td>Noot</td><td>Mies</td></tr><tr><td>Wim</td><td>Zus</td><td>Jet</td></tr><tr><td>1</td><td>2</td><td>3</td></tr><tr><td>Teun</td><td>Vuur</td><td>Gijs</td></tr></table></body></html>";
	String t2 = "<html><body><table border=\"1\"><tr><td>1</td><td>ma</td><td>14</td><td>april</td><td>Introduction</td></tr><tr><td>2</td><td>wo</td><td>16</td><td>april</td><td>SyntaxAnalysis</td></tr><tr><td>3</td><td>ma</td><td>21</td><td>april</td><td>ContextualAnalysis</td></tr><tr><td>4</td><td>wo</td><td>23</td><td>april</td><td>ANTLR1</td></tr><tr><td>5</td><td>wo</td><td>7</td><td>mei</td><td>RunTime</td></tr><tr><td>6</td><td>wo</td><td>14</td><td>mei</td><td>CodeGeneration</td></tr><tr><td>7</td><td>ma</td><td>19</td><td>mei</td><td>CodeOptimization</td></tr><tr><td>8</td><td>wo</td><td>21</td><td>mei</td><td>GarbageCollection</td></tr><tr><td>9</td><td>ma</td><td>26</td><td>mei</td><td>ANTLR2</td></tr></table></body></html>";
	String t3 = "<html><body><table border=\"1\"><tr><td></td><td></td><td>V</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>V</td><td></td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td></td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td></td><td>V</td><td>V</td><td></td><td></td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td>V</td><td>V</td><td>V</td><td></td><td></td><td></td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>V</td><td>V</td><td>V</td><td></td><td></td><td></td><td></td><td></td><td></td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td>B</td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr><tr><td>I</td><td></td><td>S</td><td>S</td><td>S</td><td>S</td><td></td><td></td><td></td><td>L</td><td></td><td></td><td></td><td>E</td><td>E</td><td>E</td><td></td><td>U</td><td></td><td></td><td>U</td><td></td><td>K</td><td></td><td></td><td>K</td><td></td><td></td><td></td><td>X</td></tr><tr><td>I</td><td></td><td>S</td><td></td><td></td><td></td><td></td><td></td><td></td><td>L</td><td></td><td></td><td></td><td>E</td><td></td><td></td><td></td><td>U</td><td></td><td></td><td>U</td><td></td><td>K</td><td></td><td>K</td><td></td><td></td><td></td><td></td><td>X</td></tr><tr><td>I</td><td></td><td>S</td><td>S</td><td>S</td><td>S</td><td></td><td></td><td></td><td>L</td><td></td><td></td><td></td><td>E</td><td>E</td><td>E</td><td></td><td>U</td><td></td><td></td><td>U</td><td></td><td>K</td><td>K</td><td></td><td></td><td></td><td></td><td></td><td>X</td></tr><tr><td>I</td><td></td><td></td><td></td><td></td><td>S</td><td></td><td></td><td></td><td>L</td><td></td><td></td><td></td><td>E</td><td></td><td></td><td></td><td>U</td><td></td><td></td><td>U</td><td></td><td>K</td><td></td><td>K</td><td></td><td></td><td></td><td></td><td></td></tr><tr><td>I</td><td></td><td>S</td><td>S</td><td>S</td><td>S</td><td></td><td></td><td></td><td>L</td><td>L</td><td>L</td><td></td><td>E</td><td>E</td><td>E</td><td></td><td>U</td><td>U</td><td>U</td><td>U</td><td></td><td>K</td><td></td><td></td><td>K</td><td></td><td></td><td></td><td>O</td></tr></table></body></html>";
	String t5 = "<html><body><table border=\"1\"><tr><td></td><td>X</td><td>XX</td><td>XX</td><td></td><td></td><td>X</td><td>XX</td><td></td><td>XX</td><td>X</td><td></td><td></td><td>XX</td><td>X</td><td>XX</td></tr><tr><td></td><td>X</td><td></td><td></td><td></td><td></td><td>X</td><td></td><td>XX</td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td></tr><tr><td></td><td>X</td><td>XX</td><td></td><td></td><td></td><td>X</td><td></td><td>X</td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td></tr><tr><td></td><td>X</td><td></td><td></td><td></td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td></tr><tr><td></td><td>X</td><td></td><td></td><td></td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td></tr><tr><td></td><td>X</td><td></td><td></td><td></td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td><td></td><td></td><td>X</td><td></td></tr></table></body></html>\n";

	@Test
	public void test() throws IOException {
		test(t1, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\tabular\\tabular-1.tex");
		test(t2, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\tabular\\tabular-2.tex");
		test(t3, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\tabular\\tabular-3.tex");
		test(null, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\tabular\\tabular-4.tex");
		test(t5, "C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block3\\cc\\tabular\\tabular-5.tex");
	}

	private void test(String expected, String expr) throws IOException {
		ParseTree tree = parseCalc(expr);
		if (tree == null) {
			System.out.println("Unable to convert to HTML invalid text in file.");
			assertEquals(expected, null);
		} else {
			this.walker.walk(this.tabularToHTML, tree);
			System.out.println(this.tabularToHTML.val(tree));
			assertEquals(expected, this.tabularToHTML.val(tree));
		}
	}

	private static MyErrorListener errorListener;

	private ParseTree parseCalc(String text) throws IOException {
		CharStream chars = CharStreams.fromFileName(text);
		Lexer lexer = new OurTabularGrammerYetStillMineLexer(chars);
		lexer.removeErrorListeners();
		errorListener = new MyErrorListener();
		lexer.addErrorListener(errorListener);
		if ((getErrorListener(lexer.getErrorListeners())) != null && (getErrorListener(lexer.getErrorListeners())).getErrorMessages().size() > 0) {
			return null;
		}

		TokenStream tokens = new CommonTokenStream(lexer);
		OurTabularGrammerYetStillMineParser parser = new OurTabularGrammerYetStillMineParser(tokens);
		parser.removeErrorListeners();
		errorListener = new MyErrorListener();
		parser.addErrorListener(errorListener);
		ParseTree result = parser.table();
		if ((getErrorListener(parser.getErrorListeners())) != null && (getErrorListener(parser.getErrorListeners())).getErrorMessages().size() > 0) {
			return null;
		}
		return result;
	}

	private MyErrorListener getErrorListener(List<? extends ANTLRErrorListener> x) {
		for (ANTLRErrorListener y : x) {
			if (y instanceof MyErrorListener) {
				return (MyErrorListener) y;
			}
		}
		return null;
	}

//	private TTAttrParser.TttContext parseTTAttr(String text) {
//		CharStream chars = CharStreams.fromString(text);
//		Lexer lexer = new TTAttrLexer(chars);
//		TokenStream tokens = new CommonTokenStream(lexer);
//		TTAttrParser parser = new TTAttrParser(tokens);
//		return parser.ttt();
//	}
}
