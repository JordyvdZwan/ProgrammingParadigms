package pp.block1.cc.test;

import pp.block1.cc.antlr.Lala;
import org.junit.Test;

/**
 * Created by reinier on 28-4-2017.
 */

public class LaLaTest {
    private static LexerTester tester = new LexerTester(Lala.class);

    @Test
    public void succeedingTest() {
        tester.correct("La");
        tester.correct("Laaa");
        tester.correct("Laaaa Laaa");
        tester.correct("La La Laa Li");
        tester.correct("LaLa");
    }
}
