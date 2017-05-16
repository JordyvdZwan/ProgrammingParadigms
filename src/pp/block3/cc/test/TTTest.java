package pp.block3.cc.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import pp.block3.cc.antlr.*;
import pp.block3.cc.antlr.CalcAttrParser.ExprContext;

import static org.junit.Assert.assertEquals;

public class TTTest {
	private final ParseTreeWalker walker = new ParseTreeWalker();
	private final TypeDeterminer tdet = new TypeDeterminer();

	@Test
	public void test() {
		test(Type.NUM, "NUM+NUM");
		test(Type.STR, "STR+STR");
		test(Type.STR, "STR^NUM");
	}

	private void test(Type expected, String expr) {
		assertEquals(expected, parseTTAttr(expr).val);
		ParseTree tree = parseCalc(expr);
		this.tdet.init();
		this.walker.walk(this.tdet, tree);
		assertEquals(expected, this.tdet.val(tree));
	}

	private ParseTree parseCalc(String text) {
		CharStream chars = CharStreams.fromString(text);
		Lexer lexer = new TTLexer(chars);
		TokenStream tokens = new CommonTokenStream(lexer);
		TTParser parser = new TTParser(tokens);
		return parser.ttt();
	}

	private TTAttrParser.TttContext parseTTAttr(String text) {
		CharStream chars = CharStreams.fromString(text);
		Lexer lexer = new TTAttrLexer(chars);
		TokenStream tokens = new CommonTokenStream(lexer);
		TTAttrParser parser = new TTAttrParser(tokens);
		return parser.ttt();
	}
}
