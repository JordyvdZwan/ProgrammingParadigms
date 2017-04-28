package pp.block1.cc.test;

import pp.block1.cc.antlr.quotation;
import org.junit.Test;

/**
 * Created by reinier on 28-4-2017.
 */

public class QuotationTest {
    private static LexerTester tester = new LexerTester(quotation.class);

    @Test
    public void succeedingTest() {
        tester.correct("\"test\"");
        tester.wrong("\"te\"st\"");
        tester.correct("\"te\"\"st\"");
    }
}
