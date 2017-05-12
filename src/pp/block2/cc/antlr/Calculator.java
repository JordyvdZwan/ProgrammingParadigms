package pp.block2.cc.antlr;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;
import pp.block2.cc.AST;
import pp.block2.cc.ParseException;
import pp.block2.cc.SymbolFactory;

import java.math.BigInteger;

/**
 * Created by Jordy van der Zwan on 12-May-17.
 */
public class Calculator extends CC10BaseListener {
    private final SymbolFactory fact;

    private ParseTreeProperty<BigInteger> ptp = new ParseTreeProperty<>();
    private boolean change = false;
    public Calculator() {
        this.fact = new SymbolFactory(CC10Lexer.class);
    }

    public BigInteger calculate(Lexer lexer) throws ParseException {
        CC10Parser parser = new CC10Parser(new CommonTokenStream(lexer));
        ParseTree tree = parser.plus();
        new ParseTreeWalker().walk(this, tree);
        System.out.println(tree.toStringTree());
        if (change) {
            throw new ParseException();
        }
        return ptp.get(tree);
    }

    @Override
    public void exitExpression(CC10Parser.ExpressionContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(1)));
    }

    @Override
    public void exitDivLink(CC10Parser.DivLinkContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)));
    }

    @Override
    public void exitPowExpression(CC10Parser.PowExpressionContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)).pow(ptp.get(ctx.getChild(2)).intValue()));
    }

    @Override
    public void exitMultLink(CC10Parser.MultLinkContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)));
    }

    @Override
    public void exitDivExpression(CC10Parser.DivExpressionContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)).divide(ptp.get(ctx.getChild(2))));
    }

    @Override
    public void exitPlusLink(CC10Parser.PlusLinkContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)));
    }

    @Override
    public void exitMultExpression(CC10Parser.MultExpressionContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)).multiply(ptp.get(ctx.getChild(2))));
    }

    @Override
    public void exitMinusLink(CC10Parser.MinusLinkContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)));
    }

    @Override
    public void exitPlusExpression(CC10Parser.PlusExpressionContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)).add(ptp.get(ctx.getChild(2))));
    }

    @Override
    public void exitNegative(CC10Parser.NegativeContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)).negate());
    }

    @Override
    public void exitMinusExpression(CC10Parser.MinusExpressionContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)).subtract(ptp.get(ctx.getChild(2))));
    }

    @Override
    public void exitNum(CC10Parser.NumContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)));
    }

    @Override
    public void exitExpressionLoop(CC10Parser.ExpressionLoopContext ctx) {
        ptp.put(ctx, ptp.get(ctx.getChild(0)));
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        try {
            ptp.put(node, new BigInteger(node.getSymbol().getText()));
        } catch (NumberFormatException e) {
            ptp.put(node, null);
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        change = true;
    }
}
