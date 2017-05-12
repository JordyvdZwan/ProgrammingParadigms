package pp.block2.cc.test;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.junit.Assert;
import org.junit.Test;
import pp.block2.cc.ParseException;
import pp.block2.cc.antlr.CC10Lexer;
import pp.block2.cc.antlr.Calculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

/**
 * Created by Jordy van der Zwan on 12-May-17.
 */
public class CalculatorTest {

    Calculator calculator = new Calculator();
    private Class<? extends Lexer> lexerType = CC10Lexer.class;

    @Test
    public void testCalculator() throws ParseException {
        check("5*20", 100);
        check("5+2*10", 25);
        check("(5+2)*10", 70);
        check("(5-2)-10", -7);
        check("3^2^4", 43046721);
        check("(3^2)^4", 6561);
    }

    private void check(String input, int expectedOutput) throws ParseException {
        Lexer lexer = scan(input);
        Assert.assertEquals(BigInteger.valueOf(expectedOutput), calculator.calculate(lexer));
    }

    private Lexer scan(String text) {
        Lexer result = null;
        CharStream stream = CharStreams.fromString(text);
        try {
            Constructor<? extends Lexer> lexerConstr = this.lexerType
                    .getConstructor(CharStream.class);
            result = lexerConstr.newInstance(stream);
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            // should never occur, as all Antlr-generated lexers are
            // well-behaved
            e.printStackTrace();
        }
        return result;
    }

}
