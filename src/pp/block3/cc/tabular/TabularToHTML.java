package pp.block3.cc.tabular;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jordy van der Zwan on 18-May-17.
 */
public class TabularToHTML extends OurTabularGrammerYetStillMineBaseListener {

    private ParseTreeProperty<String> vals = new ParseTreeProperty<>();

    private void set(ParseTree node, String val) {
        this.vals.put(node, val);
    }

    public String val(ParseTree node) {
        return this.vals.get(node);
    }

    @Override
    public void enterTable(OurTabularGrammerYetStillMineParser.TableContext ctx) {
        set(ctx, "<html><body><table border=\"1\">");
    }

    @Override
    public void exitTable(OurTabularGrammerYetStillMineParser.TableContext ctx) {
        String body = "";
        for (OurTabularGrammerYetStillMineParser.RowContext row : ctx.row()) {
            body = body.concat(val(row));
        }
        set(ctx, val(ctx) + body + "</table></body></html>");
    }

    @Override
    public void enterRow(OurTabularGrammerYetStillMineParser.RowContext ctx) {
        set(ctx, "<tr>");
    }

    @Override
    public void exitRow(OurTabularGrammerYetStillMineParser.RowContext ctx) {
        String body = "";
        for (OurTabularGrammerYetStillMineParser.ColumnContext column : ctx.column()) {
            body = body.concat(val(column));
        }
        set(ctx, val(ctx) + body + "</tr>");
    }

    @Override
    public void enterColumn(OurTabularGrammerYetStillMineParser.ColumnContext ctx) {
        set(ctx, "<td>");
    }

    @Override
    public void exitColumn(OurTabularGrammerYetStillMineParser.ColumnContext ctx) {
        set(ctx, val(ctx) + (ctx.ELEM() != null ? ctx.ELEM().getText() : "") + "</td>");
    }

    private static final ParseTreeWalker walker = new ParseTreeWalker();
    private static final TabularToHTML tabularToHTML = new TabularToHTML();

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please type the name right in one go...");
            String text = scanner.nextLine();
            CharStream chars = null;
            chars = CharStreams.fromFileName(text);
            Lexer lexer = new OurTabularGrammerYetStillMineLexer(chars);
            lexer.removeErrorListeners();
            MyErrorListener errorListener = new MyErrorListener();
            lexer.addErrorListener(errorListener);
            ParseTree result = null;
            if (!(errorListener.getErrorMessages().size() > 0)) {
                TokenStream tokens = new CommonTokenStream(lexer);
                OurTabularGrammerYetStillMineParser parser = new OurTabularGrammerYetStillMineParser(tokens);
                parser.removeErrorListeners();
                errorListener = new MyErrorListener();
                parser.addErrorListener(errorListener);
                result = parser.table();
                if (things.size() > 0) {
                    result = null;
                }
                resetDesperation();
            }
            if (result == null) {
                System.out.println("Errors thus no HTML for you....");
            } else {
                walker.walk(tabularToHTML, result);
                PrintWriter out = new PrintWriter(text + ".html");
                out.print(tabularToHTML.val(result));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<String> things = new ArrayList<>();

    public static void desperateTimes(String string) {
        things.add(string);
    }

    private static void resetDesperation() {
        things.clear();
    }
}
//    C:\Projects\Project - CC UT\Compiler Construction Lab Exercises\src\pp\block3\cc\tabular\tabular-1.tex

//    @Override
//    public void visitTerminal(TerminalNode node) {
//        super.visitTerminal(node);
//    }
//
//    @Override
//    public void visitErrorNode(ErrorNode node) {
//        super.visitErrorNode(node);
//    }

