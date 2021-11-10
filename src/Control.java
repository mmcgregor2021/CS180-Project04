import java.util.*;
import java.io.*;
public class Control {

    //stores each student object as a line in a txt file in format:
    //firstName;lastName;password;id
    public static void saveStudents(ArrayList<Student> arr, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < arr.size(); i++) {
                Student student = arr.get(i);
                String firstName = student.getFirstName();
                String lastName = student.getLastName();
                String password = student.getPassword();
                int id = student.getID();
                String line = firstName + ";" + lastName + ";" + password + ";" + id;
                pw.println(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to students to file!");
        }
    }

    //stores each teacher object as a line in a txt file in format:
    //firstName;lastName;password;id
    public static void saveTeachers(ArrayList<Teacher> arr, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < arr.size(); i++) {
                Teacher teacher = arr.get(i);
                String firstName = teacher.getFirstName();
                String lastName = teacher.getLastName();
                String password = teacher.getPassword();
                int id = teacher.getID();
                String line = firstName + ";" + lastName + ";" + password + ";" + id;
                pw.println(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to students to file!");
        }
    }

    //reads student objects from the txt file created by saveStudents() and returns and ArrayList of students
    public static ArrayList<Student> readStudents(String fileName) {
        ArrayList<Student> arr = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] lineArr = line.split(";");
                String firstName = lineArr[0];
                String lastName = lineArr[1];
                String password = lineArr[2];
                int id = Integer.parseInt(lineArr[3]);
                arr.add(new Student(firstName, lastName, password, id));
                line = bfr.readLine();
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    //reads teacher objects from the txt file created by saveTeachers() and returns and ArrayList of teachers
    public static ArrayList<Teacher> readTeachers(String fileName) {
        ArrayList<Teacher> arr = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] lineArr = line.split(";");
                String firstName = lineArr[0];
                String lastName = lineArr[1];
                String password = lineArr[2];
                int id = Integer.parseInt(lineArr[3]);
                arr.add(new Teacher(firstName, lastName, password, id));
                line = bfr.readLine();
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    //saves all three counters to one line separated by ';' to a txt file
    public static void saveCounters(int personCounter, int boardCounter, int commentCounter, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println(personCounter + ";" + boardCounter + ";" + commentCounter);
        } catch (Exception e) {
            System.out.println("Failed to save counter!");
        }
    }

    //reads all three counters from the txt file to an Integer array
    //Integer Array Format: [personCounter, boardCounter, commentCounter]
    public static int[] readCounters(String fileName) {
        int[] arr = new int[3];
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String[] lineArr = bfr.readLine().split(";");
            int personCounter = Integer.parseInt(lineArr[0]);
            int boardCounter = Integer.parseInt(lineArr[1]);
            int commentCounter = Integer.parseInt(lineArr[2]);
            arr[0] = personCounter;
            arr[1] = boardCounter;
            arr[2] = commentCounter;
        } catch (Exception e) {
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    public static void main(String[] args) {

        ArrayList<Student> students = readStudents(students.txt);
        ArrayList<Teacher> teachers = readTeachers(teachers.txt);
        int[] counterArray = readCounters(counters.txt);
        int personCounter = counterArray[0];
        int boardCounter = counterArray[1];
        int commentCounter = counterArray[2];

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
            // System.out.println("Please enter your ID number");
            // do {
            //     try {
            //         id = Integer.parseInt(scan.nextLine());
            //         break;
            //     } catch (NumberFormatException e) {
            //         System.out.println("Please enter a valid ID number");
            //     }
            // } while (true);
            System.out.println("Your UserID is ");
            //add userID generator
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
            //implement sign in stuff
        }
        System.out.println("What would you like to do?");
        //main loop once logged in
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
                //edit account
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
                //delete account
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
