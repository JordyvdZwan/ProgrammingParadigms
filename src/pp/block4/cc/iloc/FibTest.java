package pp.block4.cc.iloc;

import org.junit.Assert;
import org.junit.Ignore;
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
public class FibTest {

    @Test
    public void fibRtRTest() {
        Program result = null;
        File file = new File("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\iloc\\fibRegisterToRegister.iloc");
        try {
            result = Assembler.instance().assemble(file);
            String print = result.prettyPrint();
            Program other = Assembler.instance().assemble(print);
            assertEquals(result, other);
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
        }

        Machine c = new Machine();
        c.setNum("n", 11);
        new Simulator(result, c).run();
        System.out.println(c);
        assertEquals(144, c.getReg("r_z"));
    }

    @Test
    @Ignore
    public void fibMtMTest() {
        Program result = null;
        File file = new File("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\iloc\\fibMemoryToMemory.iloc");
        try {
            result = Assembler.instance().assemble(file);
            String print = result.prettyPrint();
            Program other = Assembler.instance().assemble(print);
            assertEquals(result, other);
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
        }

        Machine c = new Machine();
        c.setNum("n", 11);
        new Simulator(result, c).run();
        System.out.println(c);
        assertEquals(144, c.getReg("r_z"));
    }

    //Unable to find a number for n for which it crashes.
    @Test
    @Ignore
    public void fibSizeTest() {
        Program result = null;
        File file = new File("C:\\Projects\\Project - CC UT\\Compiler Construction Lab Exercises\\src\\pp\\block4\\cc\\iloc\\fibRegisterToRegister.iloc");
        try {
            result = Assembler.instance().assemble(file);
            String print = result.prettyPrint();
            Program other = Assembler.instance().assemble(print);
            assertEquals(result, other);
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
        }

        Machine c = new Machine();
        c.setNum("n", 2000000000);
        new Simulator(result, c).run();
        System.out.println(c);
        Assert.assertTrue(true);
    }

}
