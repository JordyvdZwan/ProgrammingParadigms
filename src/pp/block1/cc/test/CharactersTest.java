package pp.block1.cc.test;

import pp.block1.cc.antlr.characters;
import org.junit.Test;

/**
 * Created by reinier on 28-4-2017.
 */

public class CharactersTest {
    private static LexerTester tester = new LexerTester(characters.class);

    @Test
    public void succeedingTest() {
        tester.correct("a12345");
        tester.wrong("aa2d");
        tester.correct("a13425 a23456");
        tester.wrong("2+3");
        tester.wrong("a123456");
        tester.yields("a23dgf", characters.ACCEPTING);
    }
}
