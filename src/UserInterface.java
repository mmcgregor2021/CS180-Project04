import java.util.ArrayList;
import java.util.Scanner;
public class UserInterface {

    ArrayList<Student> students = new ArrayList<Student>();
    ArrayList<Teacher> teachers = new ArrayList<Teacher>();
    public UserInterface() {

    }
    public static void main(String[] args) {
        UserInterface test = new UserInterface();


        //Get login info from user
        boolean success;
        do {
            success = true;
            Scanner sc = new Scanner(System.in);
            System.out.print("Please select an option \n (1)Login \n (2)Make an account \n");
            String in = sc.nextLine();
            int input = 0;

            try {
                input = Integer.parseInt(in);
            } catch (NumberFormatException e) {
                input = 0;
            }

            if (input == 1)
                test.login(sc);
            else if (input == 2)
                test.createNewUser(sc);
            else {
                System.out.println("Invalid choice. Please try again.");
                success = false;
            }
        } while(!success);



    }

    //Make sure the user is using a valid login
    public void login(Scanner sc) {
        int id;
        String password;
        boolean found = false;

        do {
            System.out.print("ID: ");
            id = sc.nextInt();
            sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();

            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getID() == id) {
                    if (students.get(i).getPassword().equals(password)) {
                        found = true;
                    }
                }

            }

            for (int i = 0; i < teachers.size(); i++) {
                if (teachers.get(i).getID() == id) {
                    if (teachers.get(i).getPassword().equals(password)) {
                        found = true;
                    }
                }

            }
            if (!found)
                System.out.println("Invalid username or password. Please try again.");
        } while (!found);

    }

    //Craeates a new student or teacher based on user input.
    public void createNewUser(Scanner sc) {
        System.out.println("Are you a teacher or a student?");
        String personType = sc.nextLine();
        System.out.print("First name: ");
        String firstName = sc.nextLine();
        System.out.print("Last name: ");
        String lastName = sc.nextLine();
        System.out.print("New id: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if(personType.equals("teacher"))
            teachers.add( new Teacher(firstName, lastName, password, id));
        else if (personType.equals("student"))
            students.add( new Student(firstName, lastName, password, id));


    }
}

