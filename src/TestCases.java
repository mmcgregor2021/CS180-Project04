import java.io.*;
import org.junit.Test;
import org.junit.After;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import static org.junit.Assert.*;

/**
 * The test cases for the project.
 * @author Purdue CS, Astrid Popovici
 * @version November 13, 2021
 */

public class TestCases {
    private final InputStream originalInput = System.in;
    private final PrintStream originalOutput = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCases.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    @Test(timeout = 1000)
    public void fullOutputTestOne() {
        try {
            String expected = "";
            String input = "";
            receiveInput(input);
            Control.main(new String[0]);
            String actual = getOutput();
            Assert.assertEquals("The output doesn't match the expected output!", expected, actual);
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    private void receiveInput(String str) {
        testIn = new ByteArrayInputStream(str.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreInputAndOutput() {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }

    @Before
    public void outputStart() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }
}