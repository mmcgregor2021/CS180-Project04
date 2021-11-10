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
        System.out.println("Welcome to the Discussion Board! What would you like to do?");
        System.out.println("1. Sign up\n2. Log in");
        do {
            try {
                input = Integer.parseInt(scan.nextLine());
                if (input == 1 || input == 2) {
                    break;
                } else {
                    System.out.println("Please enter a valid number");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        } while (true);
        //sets up the account
        if (input == 1) {
            System.out.println("Please enter your ID number");
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
        } else if (input == 2) {
            //checks login info
            System.out.println("Please enter your ID number");
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
            //implement sign in stuff later
        }
        System.out.println("What would you like to do?");
        //main method once logged in
        do {
            System.out.println("1. Edit account\n2. Delete account\n3. View courses\n4. Logout");
            do {
                try {
                    input = Integer.parseInt(scan.nextLine());
                    if (input >= 1 && input <= 4) {
                        break;
                    } else {
                        System.out.println("Please enter a valid number");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number");
                }
            } while (true);
            if (input == 1) {
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
                
            } else if (input == 2) {
                System.out.println("Are you sure you would like to delete your account? (y for yes, anything else for no)");
                if (scan.nextLine().equals("y")) {
                    //remove the account
                }
            } else if (input == 3) {
                //view all of the stuff
            }
        } while (input != 4);
        System.out.println("Goodbye! Have a nice day!");
    }
}
