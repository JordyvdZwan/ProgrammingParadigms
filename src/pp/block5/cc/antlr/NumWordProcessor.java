package pp.block5.cc.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import pp.block5.cc.ErrorListener;
import pp.block5.cc.ParseException;

import static java.lang.Integer.parseInt;

/** Prettyprints a (number, word)-sentence and sums up the numbers. */
public class NumWordProcessor extends NumWordBaseVisitor<Integer> {
	public static void main(String[] args) {
		NumWordProcessor grouper = new NumWordProcessor();
		if (args.length == 0) {
			process(grouper, "1sock2shoes 3 holes");
			process(grouper, "3 strands 10 blocks 11 weeks 15 credits");
			process(grouper, "1 2 3");
		} else {
			for (String text : args) {
				process(grouper, text);
			}
		}
	}

	private static void process(NumWordProcessor grouper, String text) {
		try {
			System.out.printf("Processing '%s':%n", text);
			int result = grouper.group(text);
			System.out.println("  Total: " + result);
		} catch (ParseException exc) {
			exc.print();
		}
	}

	/** Groups a given sentence and prints it to stdout.
	 * Returns the sum of the numbers in the sentence.
	 */
	public int group(String text) throws ParseException {
		CharStream chars = CharStreams.fromString(text);
		ErrorListener listener = new ErrorListener();
		Lexer lexer = new NumWordLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		TokenStream tokens = new CommonTokenStream(lexer);
		NumWordParser parser = new NumWordParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		ParseTree tree = parser.sentence();
		listener.throwException();
		return visit(tree);
	}

	// Override the visitor methods.
	// Each visitor method should call visit(child)
	// if and when it wants to visit that child node.

	@Override
	public Integer visitSentence(NumWordParser.SentenceContext ctx) {
		int result = 0;
		int childs = ctx.word().size();
		result += visit(ctx.number(0));
		result += visit(ctx.word(0));
		for (int i = 1; i < childs - 1; i++) {
			System.out.printf(", ");
			result += visit(ctx.number(i));
			result += visit(ctx.word(i));
		}
		System.out.printf(" and ");
		result += visit(ctx.number(childs - 1));
		result += visit(ctx.word(childs - 1));
		return result;
	}

	@Override
	public Integer visitNumber(NumWordParser.NumberContext ctx) {
		int result = parseInt(ctx.NUMBER().getText());
		System.out.printf(ctx.NUMBER().getText() + " ");
		return result;
	}

	@Override
	public Integer visitWord(NumWordParser.WordContext ctx) {
		System.out.printf(ctx.WORD().getText());
		return 0;
	}
}
