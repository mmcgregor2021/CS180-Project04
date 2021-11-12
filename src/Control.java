import java.util.*;
import java.io.*;
public class Control {

    //saves everything to txt files (STILL NEEDS TO SAVE COURSES, BOARDS, and COMMENTS)
    public static void logOut(ArrayList<Student> students, ArrayList<Teacher> teachers, int personCounter, int boardCounter, int commentCounter) {

        saveStudents(students, "students.txt");
        saveTeachers(teachers, "teachers.txt");
        saveCounters(personCounter, boardCounter, commentCounter, "counters.txt");

    }

    public static int logIn(int id, String password, ArrayList<Student> students, ArrayList<Teacher> teachers) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == id) {
                if (students.get(i).getPassword().equals(password)) {
                    return 3; //correct login
                }

                return 2; //wrong password
            }

        }

        for (int j = 0; j <teachers.size(); j++) {
            if (teachers.get(j).getID() == id) {
                if (teachers.get(j).getPassword().equals(password)) {
                    return 3; //correct login
                }

                return 2; //wrong password
            }

        }

        return 1; //id does not exist
    }


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

        ArrayList<Student> students = readStudents("students.txt");
        ArrayList<Teacher> teachers = readTeachers("teachers.txt");
        int[] counterArray = readCounters("counters.txt");
        int personCounter = counterArray[0];
        int boardCounter = counterArray[1];
        int commentCounter = counterArray[2];

        Scanner scan = new Scanner(System.in);
        int sessionID = 0; // ID number of the current logged in user
        int input;
        int id;
        String password = "";
        String first = "";
        String last = "";
        boolean access = false;
        System.out.println("Welcome to the Discussion Board! What would you like to do?");
        System.out.println("1. Sign up\n2. Log in");

        do {
            try {
                input = Integer.parseInt(scan.nextLine());
                if (input == 1 || input == 2) {
                    break;
                } else {
                    System.out.println("Please enter a valid number");
                    System.out.println("1. Sign up\n2. Log in");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
                System.out.println("1. Sign up\n2. Log in");
            }
        } while (true);

        //sets up the account
        if (input == 1) {
            id = personCounter + 1;
            personCounter++;
            System.out.println("Your UserID is " + id);

            //add userID generator
            System.out.println("Please enter your password.");
            do {
                password = scan.nextLine();
                if (password.length() != 0) {
                    break;
                } else {
                    System.out.println("Please enter a non-blank password.");
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
            System.out.println("Please enter your last name.");
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
                teachers.add(new Teacher(first, last, password, id));
            } else {
                students.add(new Student(first, last, password, id));
            }
            System.out.println("Successfully Logged in");
            access = true;
            sessionID = id;
            //End of Signup Route

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
            switch (logIn(id, password, students, teachers)) {
                case 1:
                    System.out.println("The account with this ID does not exist");
                    access = false;
                    break;
                case 2:
                    System.out.println("The entered password is incorrect");
                    access = false;
                    break;
                case 3:
                    System.out.println("Successfully Logged in");
                    access = true;
                    sessionID = id;
                    break;
            }
            //LOGIN
        }

        boolean sessionAuthority = false;
        for(int i = 0; i < teachers.size(); i++) {
            if (sessionID == teachers.get(i).getID)
                sessionAuthority = true;
        }

            //main loop once logged in
            mainLoop:
            do {
                System.out.println("What would you like to do?");
                System.out.println("1. Edit account\n2. Delete account\n3. View courses\n4. Logout");
                do {
                    try {
                        input = Integer.parseInt(scan.nextLine());
                        if (input >= 1 && input <= 4) {
                            break;
                        } else {
                            System.out.println("Please enter a valid number");
                            System.out.println("1. Edit account\n2. Delete account\n3. View courses\n4. Logout");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                        System.out.println("1. Edit account\n2. Delete account\n3. View courses\n4. Logout");
                    }
                } while (true);
                if (input == 1) {
                    //edit account
                    editLoop:
                    do {
                        System.out.println("Which field would you like to modify?");
                        System.out.println("1. password\n2. first name\n3. last name\n4. go back");
                        int editChoice = Integer.parseInt(scan.nextLine());
                        switch (editChoice) {
                            case 1:
                                System.out.println("Please enter your password.");
                                do {
                                    password = scan.nextLine();
                                    if (password.length() != 0) {
                                        for (int i = 0; i < students.size(); i++) {
                                            if (students.get(i).getID() == sessionID) {
                                                students.get(i).setPassword(password);
                                            }
                                        }
                                        for (int i = 0; i < teachers.size(); i++) {
                                            if (teachers.get(i).getID() == sessionID) {
                                                teachers.get(i).setPassword(password);
                                            }
                                        }
                                        System.out.println("Your password has been changed.");
                                        break;
                                    } else {
                                        System.out.println("Please enter a non-blank password.");
                                    }
                                } while (true);
                                break;
                            case 2:
                                System.out.println("Please enter your first name.");
                                do {
                                    first = scan.nextLine();
                                    if (first.length() != 0) {
                                        for (int i = 0; i < students.size(); i++) {
                                            if (students.get(i).getID() == sessionID) {
                                                students.get(i).setFirstName(first);
                                            }
                                        }
                                        for (int i = 0; i < teachers.size(); i++) {
                                            if (teachers.get(i).getID() == sessionID) {
                                                teachers.get(i).setFirstName(first);
                                            }
                                        }
                                        System.out.println("Your first name has been changed.");
                                        break;
                                    } else {
                                        System.out.println("Please enter a valid last name.");
                                    }
                                } while (true);
                                break;
                            case 3:
                                System.out.println("Please enter your last name.");
                                do {
                                    last = scan.nextLine();
                                    if (first.length() != 0) {
                                        for (int i = 0; i < students.size(); i++) {
                                            if (students.get(i).getID() == sessionID) {
                                                students.get(i).setLastName(password);
                                            }
                                        }
                                        for (int i = 0; i < teachers.size(); i++) {
                                            if (teachers.get(i).getID() == sessionID) {
                                                teachers.get(i).setLastName(password);
                                            }
                                        }
                                        System.out.println("Your last name has been changed.");
                                        break;
                                    } else {
                                        System.out.println("Please enter a valid last name.");
                                    }
                                } while (true);
                                break;
                            case 4:
                                break editLoop;
                            case 5:
                                System.out.println("Please enter a valid number");
                                break;
                        }
                    } while (true);
                } else if (input == 2) {
                    //delete account
                    System.out.println("Are you sure you would like to delete your account? (y for yes, anything else for no)");
                    if (scan.nextLine().equals("y")) {
                        for (int i = 0; i < students.size(); i++) {
                            if (students.get(i).getID() == sessionID) {
                                students.remove(i);
                                break mainLoop; //logs user out after deleting their account
                            }
                        }
                        for (int i = 0; i < teachers.size(); i++) {
                            if (teachers.get(i).getID() == sessionID) {
                                students.remove(i);
                                break mainLoop; //logs user out after deleting their account
                            }
                        }
                    }
                } else if (input == 3) {
                    //Display all courses.
                    boolean again;
                    int courseSelection = 0;
                    do {
                        System.out.println("Select one of the following options");
                        again = false;
                        for (int i = 0; i < courses.size(); i++) {
                            System.out.println(i + ". " + courses.get(i));
                        }
                        if (sessionAuthority)
                            System.out.println((courses.size()) + ". Add a course.");
                        try {
                            courseSelection = Integer.parseInt(scan.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid entry, please try again.");
                            again = true;
                        }
                        if (courseSelection > courses.size() && sessionAuthority) {
                            System.out.println("Invalid entry, please try again.");
                            again = true;
                        } else if (courseSelection > courses.size() - 1 && !sessionAuthority) {
                            System.out.println("Invalid entry, please try again.");
                        again = true;
                        }
                    } while (again);

                    int boardSelection = 0;
                    int counter = 0;
                    String selectedCourse = null;
                    //Display the boards for the selected course.
                    do {
                        again = false;
                        if (courseSelection < courses.size()) {
                            System.out.println("Select one of the following options: ");
                            selectedCourse = courses.get(courseSelection);
                            for (int i = 0; i < boards.length; i++) {
                                if (boards.get(i).getCourse.equals(selectedCourse)) {
                                    System.out.println(counter + ". " + boards.get(i).getTopic());
                                    counter++;
                                }
                            }
                        }

                        if (sessionAuthority) {
                            System.out.println((counter + 1) + ". Add a board");
                            System.out.println((counter + 2) + ". View all comments from a specific student");
                        }

                        try {
                            boardSelection = Integer.parseInt(scan.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid entry, please try again.");
                            again = true;
                        }

                        if (boardSelection > boards.size() + 2 && sessionAuthority) {
                            System.out.println("Invalid entry, please try again.");
                            again = true;
                        } else if (boardSelection > boards.size() && !sessionAuthority) {
                            System.out.println("Invalid entry, please try again.");
                            again = true;
                        }
                    } while (again);

                        if (boardSelection == boards.size()) {
                            System.out.println("Creating a new board: ");
                            String course;
                            String boardID = String.valueOf(boardCounter + 1);
                            boardCounter++;
                            System.out.println("What is the topic of this board?");
                            String topic = scan.nextLine();
                            ArrayList<Comment> comments = new ArrayList<Comment>();
                            Date date = new Date();
                            boards.add(new Board(selectedCourse, topic, boardID, date, comments));
                        }
                        int studentID = 0;
                        if(boardSelection == boards.size() + 1) {
                            do {
                                System.out.println("Enter the student ID of the student comments you want to see");
                                try {
                                    again = false;
                                    studentID = Integer.parseInt(scan.nextLine());
                                    for (int i = 0; i < students.size(); i++) {
                                        if (students.get(i).getID() == studentID) {
                                            again = true;
                                        }
                                    }
                                    if (again)
                                        System.out.println("Invalid ID, please try again");
                                } catch (Exception e) {
                                    System.out.println("Invalid ID, please try again.");
                                }
                            } while(again);

                            for (int i = 0; i < comments.size(); i++) {
                                if(comments.get(i).getCommentID() == studentID) {
                                    comments.get(i).toString();
                                }
                            }
                        }

                }
                        //add in when content of boards is able to be printed out
                        /*
                        if (courseSelection == courses.size() + 1) {
                            ArrayList<Student> sortedStudents = new ArrayList<>();
                            index = 0;
                            for (int x = 0; x < students.length; x++) {
                                if (students.get(x).getLikes() > students.get(index).getLikes()) {
                                    index = x;
                                }
                            }
                            sortedStudents.add(students.get(x));
                            for (int x = 0; x < sortedStudents.size(); x++) {
                                System.out.println(sortedStudents.get(x));
                            }
                        }
                        */
                        //add in once boards have been printed out
                        /*
                        System.out.println((courses.size() + 2) + "What comment would you like to upvote?");
                        do {
                            try {
                                commentNumber = Integer.parseInt(scan.nextLine());
                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid entry, please try again.");
                            }
                        } while (true);
                        for (int x = 0; x < boards.size(); x++) {
                            if (boards.get(x).getID() == commentNumber) {
                                boards.get(x).setLikes(boards.get(x).getLikes + 1);
                            }
                        }
                        */
                    } while(again);
            } while (input != 4);
            System.out.println("Goodbye! Have a nice day!");
            logOut(students, teachers, personCounter, boardCounter, commentCounter);
        }
    }

