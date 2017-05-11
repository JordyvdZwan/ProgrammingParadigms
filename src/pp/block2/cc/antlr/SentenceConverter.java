package pp.block2.cc.antlr;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.*;
import pp.block2.cc.*;
import pp.block2.cc.ll.Sentence;

public class SentenceConverter 
		extends SentenceBaseListener implements Parser {
	/** Factory needed to create terminals of the {@link Sentence}
	 * grammar. See {@link pp.block2.cc.ll.SentenceParser} for
	 * example usage. */
	private final SymbolFactory fact;

	private ParseTreeProperty<AST> ptp = new ParseTreeProperty<AST>();
	private boolean change = false;
	public SentenceConverter() {
		this.fact = new SymbolFactory(Sentence.class);
	}

	@Override
	public AST parse(Lexer lexer) throws ParseException {
		SentenceParser parser = new SentenceParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.sentence();
		new ParseTreeWalker().walk(this, tree);
		System.out.println(tree.toStringTree());
		if (change) {
			throw new ParseException();
		}
		return ptp.get(tree);
	}

	@Override
	public void exitSentence(SentenceParser.SentenceContext ctx) {
		ptp.put(ctx, new AST(new NonTerm("Sentence")));
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ptp.get(ctx).addChild(ptp.get(ctx.getChild(i)));
		}
	}

	@Override
	public void exitSubject(SentenceParser.SubjectContext ctx) {
		ptp.put(ctx, new AST(new NonTerm("Subject")));
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ptp.get(ctx).addChild(ptp.get(ctx.getChild(i)));
		}
	}

	@Override
	public void exitObject(SentenceParser.ObjectContext ctx) {
		ptp.put(ctx, new AST(new NonTerm("Object")));
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ptp.get(ctx).addChild(ptp.get(ctx.getChild(i)));
		}
	}

	@Override
	public void exitModifier(SentenceParser.ModifierContext ctx) {
		ptp.put(ctx, new AST(new NonTerm("Modifier")));
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ptp.get(ctx).addChild(ptp.get(ctx.getChild(i)));
		}
	}

	@Override
	public void visitTerminal(TerminalNode node) {
		ptp.put(node, new AST(fact.getTerminal(node.getSymbol().getType()), node.getSymbol()));
	}

	@Override public void visitErrorNode(ErrorNode node) {
		change = true;
	}

	// Use an appropriate ParseTreeProperty to
	// store the correspondence from nodes to ASTs
}
