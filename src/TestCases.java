//IMPORTANT NOTE: please reset the counters file to 0;0;0 and empty all other files before running the tests

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
 * @author Purdue CS, Astrid Popovici, Jainam Doshi
 * @version November 13, 2021
 */

public class TestCases {
    private final InputStream originalInput = System.in;
    private final PrintStream originalOutput = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    public static void main(String[] args) {
        //reset all counters to 0
        try (PrintWriter pw = new PrintWriter(new FileWriter("counters.txt"))) {
            pw.println("0;0;0");
        } catch (Exception e) {
            System.out.println("Failed to reset counters to 0.");
        }

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
    //USER ID: 1
    public void signUpAndLogOutTeacher() {
        try {
            String expected = "Welcome to the Discussion Board! What would you like to do?\n"  +
                    "1. Sign up\n2. Log in\n" + //user enters 3
                    "Please select a valid option\n" +
                    "1. Sign up\n2. Log in\n" + //user enters 1
                    "Your UserID is " + 1 + ". Please remember this number!\n" +
                    "Please enter a password.\n" + //user enters a blank password
                    "Please enter a non-blank password.\n" +
                    "Please enter your first name.\n" + //user enters a blank first name
                    "You must have a first name.\n" +
                    "Please enter your last name.\n" + //user enters a non-blank first name
                    "You must have a last name.\n" +
                    "Are you a teacher? (y for yes, anything else for no)\n" + //user enters y
                    "Successfully Signed up\n" +
                    "\n" +
                    "User: mitch daniels\n" +
                    "What would you like to do?\n" +
                    "1. Edit account\n" +
                    "2. Delete account\n" +
                    "3. View courses\n" +
                    "4. Logout\n" +
                    "5. Create new course\n" +
                    "6. Grade student posts\n" +
                    "Goodbye! Have a nice day!\n";

            String input = "3\n" +
                    "1\n" +
                    "\n" +
                    "hammerd0wn\n" +
                    "\n" +
                    "mitch\n" +
                    "\n" +
                    "daniels\n" +
                    "y\n" +
                    "4\n"; //log out

            receiveInput(input);
            Control.main(new String[0]);
            String actual = getOutput();
            Assert.assertEquals("The output doesn't match the expected output!", expected, actual);
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout = 1000)
    //USER ID: 2
    public void signUpAndEditAccountStudent() {
        try {
            String expected = "Welcome to the Discussion Board! What would you like to do?\n"  +
                    "1. Sign up\n2. Log in\n" + //user enters 1
                    "Your UserID is " + 2 + ". Please remember this number!\n" +
                    "Please enter a password.\n" + //b0ilerup
                    "Please enter your first name.\n" + //purdue
                    "Please enter your last name.\n" + //pete
                    "Are you a teacher? (y for yes, anything else for no)\n" + //user enters n
                    "Successfully Signed up\n\n" +
                    "User: purdue pete\n" + //double newline char is intentional
                    "What would you like to do?\n" +
                    "1. Edit account\n" +
                    "2. Delete account\n" +
                    "3. View courses\n" +
                    "4. Logout\n" +
                    "5. View your comments and their grades\n" +
                    "Which field would you like to modify?\n" +
                    "1. password\n" +
                    "2. first name\n" +
                    "3. last name\n" +
                    "4. go back\n" +
                    "Please enter a new password.\n" +
                    "Your password has been changed.\n" +
                    "Which field would you like to modify?\n" +
                    "1. password\n" +
                    "2. first name\n" +
                    "3. last name\n" +
                    "4. go back\n" +
                    "Please enter a new first name.\n" +
                    "Your first name has been changed.\n" +
                    "Which field would you like to modify?\n" +
                    "1. password\n" +
                    "2. first name\n" +
                    "3. last name\n" +
                    "4. go back\n" +
                    "Please enter a new last name.\n" +
                    "Your last name has been changed.\n" +
                    "Which field would you like to modify?\n" +
                    "1. password\n" +
                    "2. first name\n" +
                    "3. last name\n" +
                    "4. go back\n" +
                    "\n" +
                    "User: iu paulina\n" +
                    "What would you like to do?\n" +
                    "1. Edit account\n" +
                    "2. Delete account\n" +
                    "3. View courses\n" +
                    "4. Logout\n" +
                    "5. View your comments and their grades\n" +
                    "Are you sure you would like to delete your account?(y for yes, anything else for no)\n" +
                    "Your account has been deleted!\n" +
                    "Goodbye! Have a nice day!\n";

            String input = "1\n" +
                    "b0ilerup\n" +
                    "purdue\n" +
                    "pete\n" +
                    "n\n" +
                    "1\n" + //edit account
                    "1\n" + //edit password
                    "hailpurdue\n" +
                    "2\n" + //edit first name
                    "iu\n" +
                    "3\n" + //edit last name
                    "paulina\n" +
                    "4\n" + //go back
                    "2\n" +
                    "y\n"; //delete account

            receiveInput(input);
            Control.main(new String[0]);
            String actual = getOutput();
            Assert.assertEquals("The output doesn't match the expected output!", expected, actual);
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout = 1000)
    public void signIn() {
         try {
            String expected = "Welcome to the Discussion Board! What would you like to do?\n" +
            "1. Sign up\n2. Log in\n" + // user enters 2
            "Please enter your ID number.\n" + //user enters bob
            "Please enter a valid ID number\n" + //user enters 1`
            "Please enter your password.\n" + //user enters ""
            "Please enter a valid password.\n" +
            "The entered password is incorrect\n" + //user enters a random string
            "Goodbye! Have a nice day!\n";

            String input = "2\n" +
                        "bob\n" +
                        "1\n" +
                        "\n" +
                        "as;dlkfjasdlf;kjsad\n";
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