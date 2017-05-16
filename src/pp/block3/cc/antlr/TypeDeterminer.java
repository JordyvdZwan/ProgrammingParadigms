package pp.block3.cc.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by Jordy van der Zwan on 16-May-17.
 */
public class TypeDeterminer extends TTBaseListener {
    /** Map storing the val attribute for all parse tree nodes. */
    private ParseTreeProperty<Type> vals;

    /** Initialises the calculator before using it to walk a tree. */
    public void init() {
        this.vals = new ParseTreeProperty<Type>();
    }

    @Override
    public void exitPar(TTParser.ParContext ctx) {
        set(ctx, val(ctx.ttt()));
    }

    @Override
    public void exitEquals(TTParser.EqualsContext ctx) {
        set(ctx, ((val(ctx.ttt(0)) == Type.NUM && val(ctx.ttt(1)) == Type.NUM)
                || (val(ctx.ttt(0)) == Type.STR && val(ctx.ttt(1)) == Type.STR)
                || (val(ctx.ttt(0)) == Type.BOOL && val(ctx.ttt(1)) == Type.BOOL))
                ? Type.BOOL
                : Type.ERR);
    }

    @Override
    public void exitPower(TTParser.PowerContext ctx) {
        set(ctx, (val(ctx.ttt(0)) == Type.NUM && val(ctx.ttt(1)) == Type.NUM)
                ? Type.NUM
                : ((val(ctx.ttt(0)) == Type.STR && val(ctx.ttt(1)) == Type.NUM)
                ? Type.STR
                : Type.ERR));
    }

    @Override
    public void exitType(TTParser.TypeContext ctx) {
        set(ctx, getValue(ctx.TYPE().getText()));
    }

    @Override
    public void exitPlus(TTParser.PlusContext ctx) {
        set(ctx, (val(ctx.ttt(0)) == Type.NUM && val(ctx.ttt(1)) == Type.NUM)
                ? Type.NUM
                : ((val(ctx.ttt(0)) == Type.STR && val(ctx.ttt(1)) == Type.STR)
                ? Type.STR
                : ((val(ctx.ttt(0)) == Type.BOOL && val(ctx.ttt(1)) == Type.BOOL)
                ? Type.BOOL
                : Type.ERR)));
    }

    /** Sets the val attribute of a given node. */
    private void set(ParseTree node, Type val) {
        this.vals.put(node, val);
    }

    /** Retrieves the val attribute of a given node. */
    public Type val(ParseTree node) {
        return this.vals.get(node);
    }


    private Type getValue(String text) {
        switch (text) {
            case "NUM":
                return Type.NUM;
            case "STR":
                return Type.STR;
            case "BOOL":
                return Type.BOOL;
        }
        return Type.ERR;
    }
}
