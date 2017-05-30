package pp.block3.cc.tabular;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordy van der Zwan on 18-May-17.
 */
public class MyErrorListener extends BaseErrorListener{

    private List<String> errorMessages;

    public MyErrorListener() {
        this.errorMessages = new ArrayList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errorMessages.add("Error: " + (offendingSymbol != null ? offendingSymbol.toString() : "null") + " at line: " + line + " at pos: " + charPositionInLine + " Msg: " + msg);
        TabularToHTML.desperateTimes("Error: " + (offendingSymbol != null ? offendingSymbol.toString() : "null") + " at line: " + line + " at pos: " + charPositionInLine + " Msg: " + msg);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
