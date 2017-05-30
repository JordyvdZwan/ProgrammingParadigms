package pp.block4.cc.iloc;

import org.junit.Test;
import pp.iloc.Assembler;
import pp.iloc.Simulator;
import pp.iloc.eval.Machine;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Jordy van der Zwan on 29-May-17.
 */
public class ILOCTest {

    private Program result;

    /*
     * CC-6
     * 3)
     * The reason for the multiplication by 4 is probably because the sytem works in bytes.
     * To represent an integer you will need 32 bits = 4 bytes and thus 4 bytes of room.
     * This suspicion is confirmed by the fact it says multiply by size of INT.
     */

    @Test
    public void testAssembler() {

        File file = new File("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\iloc\\max.iloc");
        try {
            result = Assembler.instance().assemble(file);
            String print = result.prettyPrint();
            System.out.println("Program " + file + ":");
            System.out.print(print);
            Program other = Assembler.instance().assemble(print);
            assertEquals(result, other);
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testSimulator() {

        File file = new File("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\iloc\\max.iloc");
        try {
            result = Assembler.instance().assemble(file);
            String print = result.prettyPrint();
            System.out.println("Program " + file + ":");
            System.out.print(print);
            Program other = Assembler.instance().assemble(print);
            assertEquals(result, other);
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
        }

        if (result == null) {
            fail("No result...");
        } else {
            Machine c = new Machine();
            c.init("a", 0, 1, 2, 3, 4, 5);
            c.setNum("alength", 6);
            new Simulator(result, c).run();
            System.out.println(c);
            assertEquals(5, c.getReg("r_max"));
        }
    }

}
