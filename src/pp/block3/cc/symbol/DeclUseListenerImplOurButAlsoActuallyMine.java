package pp.block3.cc.symbol;


import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import pp.block3.cc.antlr.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordy van der Zwan on 18-May-17.
 */
public class DeclUseListenerImplOurButAlsoActuallyMine extends DeclUseBaseListener {

    private ParseTreeProperty<List<String>> vals;
    private OurSymbolTableButActuallyMine table = new OurSymbolTableButActuallyMine();

    public DeclUseListenerImplOurButAlsoActuallyMine() {
        this.vals = new ParseTreeProperty<>();
    }

    @Override
    public void exitProgram(DeclUseParser.ProgramContext ctx) {
        set(ctx, val(ctx.series()));
        System.out.println(val(ctx.series()).toString());
    }

    @Override
    public void enterSeries(DeclUseParser.SeriesContext ctx) {
        table.openScope();
    }

    @Override
    public void exitSeries(DeclUseParser.SeriesContext ctx) {
        set(ctx, new ArrayList<>());
        for (DeclUseParser.UnitContext unit : ctx.unit()) {
            List<String> l1 = val(ctx);
            List<String> l2 = val(unit);
            l1.addAll(l2);
            set(ctx, l1);
        }
        table.closeScope();
    }

    @Override
    public void exitUnit(DeclUseParser.UnitContext ctx) {
        if (ctx.decl() != null) {
            set(ctx, val(ctx.decl()));
        }
        if (ctx.series() != null) {
            set(ctx, val(ctx.series()));
        }
        if (ctx.use() != null) {
            set(ctx, val(ctx.use()));
        }
    }

    @Override
    public void exitDecl(DeclUseParser.DeclContext ctx) {
        if (!table.add(ctx.ID().getText())) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("ERROR: Variable already in scope:\'" + ctx.getToken(5, 0) + "\' at line number: " +
                    ctx.ID().getSymbol().getLine() + " Column number: " + ctx.ID().getSymbol().getCharPositionInLine() + ".");
            set(ctx, list);
        } else {
            set(ctx, new ArrayList<String>());
        }
    }

    @Override
    public void exitUse(DeclUseParser.UseContext ctx) {
        if (!table.contains(ctx.ID().getText())) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("ERROR: Variable not declared:\'" + ctx.getToken(5, 0) + "\' at line number: " +
                    ctx.ID().getSymbol().getLine() + " Column number: " + ctx.ID().getSymbol().getCharPositionInLine() + ".");
            set(ctx, list);
        } else {
            set(ctx, new ArrayList<String>());
        }
    }

//    @Override
//    public void visitTerminal(TerminalNode node) {
//        super.visitTerminal(node);
//    }

//    @Override
//    public void visitErrorNode(ErrorNode node) {
//        super.visitErrorNode(node);
//    }

    /**
     * Sets the val attribute of a given node.
     */
    private void set(ParseTree node, List<String> val) {
        this.vals.put(node, val);
    }

    /**
     * Retrieves the val attribute of a given node.
     */
    public List<String> val(ParseTree node) {
        return this.vals.get(node);
    }

}