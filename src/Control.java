import java.util.*;
import java.io.*;
public class Control {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int input;
        int id;
        String password;
        String first;
        String last;
        Person person;
        System.out.println("Welcome to the Discussion Board! Please enter your ID number.");
        do {
            try {
                id = Integer.parseInt(scan.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid ID number");
            }
        } while (true);
        System.out.println("Please enter your password.");
        do {
            password = scan.nextLine();
            if (password.length() != 0) {
                break;
            } else {
                System.out.println("Please enter a valid password.");
            }
        } while (true);
        System.out.println("Please enter your first name.");
        do {
            first = scan.nextLine();
            if (first.length() != 0) {
                break;
            } else {
                System.out.println("You must have a first name.");
            }
        } while (true);
        System.out.println("Please enter your last name.")
        do {
            last = scan.nextLine();
            if (last.length() != 0) {
                break;
            } else {
                System.out.println("You must have a last name.");
            }
        } while (true);
        System.out.println("Are you a teacher? (y for yes, anything else for no)");
        if (scan.nextLine().equals("y")) {
            person = new Teacher(first, last, password, id);
        } else {
            person = new Student(first, last, password, id);
        }
        System.out.println("What would you like to do?");
        do {
            System.out.println("1. Edit account\n2. Delete account\n3. View courses\n4. Logout");
            input = scan.nextInt();
            if (input == 1) {

            }
        }
    }
}
