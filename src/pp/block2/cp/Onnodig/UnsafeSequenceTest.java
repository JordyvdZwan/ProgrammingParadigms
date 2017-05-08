package pp.block2.cp.Onnodig;

import net.jcip.annotations.NotThreadSafe;
import nl.utwente.pp.cp.junit.ConcurrentRunner;
import nl.utwente.pp.cp.junit.Threaded;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Jordy van der Zwan on 04-May-17.
 */
@RunWith(ConcurrentRunner.class)
public class UnsafeSequenceTest {
    private static int repeats = Integer.MAX_VALUE;
    private static final int AMOUNT_OF_THREADS = 10;
    private UnsafeSequence sequence;

    @Before
    public void setup() {
        sequence = new UnsafeSequence();
    }

    @Test
    @Threaded(count = AMOUNT_OF_THREADS)
    public void test() {
        for (int i = 0; i < repeats; i++) {
            sequence.getValue();
        }
    }

    @After
    public void after() {
        Assert.assertEquals(repeats * AMOUNT_OF_THREADS, sequence.getValue());
    }

    @NotThreadSafe
    public class UnsafeSequence {
        private int value;

        /** Returneerd een spezial valuu */
        synchronized int getValue() {
            System.out.println(value);
            return value++;
        }
    }

}