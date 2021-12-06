import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class GUI extends JComponent{
    private static JFrame frame;

    //Variables for opening page
    private static JButton signUpButton = new JButton("Sign Up");
    private static JButton logInButton = new JButton("Log In");
    private static JButton firstBack = new JButton("Back");
    private static JButton signUpContinue = new JButton("Continue");
    private static JButton logInContinue = new JButton("Continue");

    static boolean sessionAuthority;

    //Session variables
    private static Integer signupID = 0;
    private static Integer sessionID;
    private static String sessionName;

    //Variables for signing up
    private static String[] options = {"Student", "Teacher"};
    private static JComboBox<String> combo = new JComboBox<String>(options);
    private static JTextField signUpFirstName;
    private static JTextField signUpLastName;
    private static JTextField signUpPassword;
    private static JTextField logInUserID;
    private static JTextField logInPassword;
    private static JLabel IDMessage2;

    //Main menu buttons
    private static JButton edit = new JButton("Edit account");
    private static JButton delete = new JButton("Delete account");
    private static JButton viewCourses = new JButton("View courses");
    private static JButton logout = new JButton("Logout");
    private static JButton newCourses = new JButton("Create new course");
    private static JButton gradePosts = new JButton("Grade student posts");
    private static JButton viewGrades = new JButton("View posts and grades");

    //Variables for edit account
    private static JTextField passwordChange;
    private static JTextField firstNameChange;
    private static JTextField lastNameChange;
    private static JButton processEdit = new JButton("Process changes");
    private static JButton backEdit = new JButton("Back");

    //Variables for creating a course
    private static String[] uploadChoices = {"Direct text", "File path"};
    private static JComboBox<String> methodChoice = new JComboBox<>(uploadChoices);
    private static JLabel chosenMethod = new JLabel("Type in the forum topic you want: ");
    private static JButton createCourse = new JButton("Create course");
    private static JButton newCourseBack = new JButton("Back");

    //Variables for viewing all courses
    private static JButton viewCoursesBack = new JButton("Back");
    private static JButton selectCourse = new JButton("Select course");
    private static String[] coursesArray = {"array of all courses here", "Option 2"};
    private static JComboBox<String> coursesCombo = new JComboBox<String>(coursesArray);

    //Variables for grading all of a student's posts
    private static JComboBox<String> studentIDs = new JComboBox<>();
    private static JButton selectStudent = new JButton("Select this student");
    private static JTextArea studentComment = new JTextArea();
    private static JTextField grade = new JTextField("Enter the grade here");
    private static JButton enterGrade = new JButton("Enter grade");

    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    socket = new Socket("localhost", 1234);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                initializeGUI();
                openMenu();

                logInButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Add code to make sure user has put in valid info.
                        logIn();
                    }
                });

                signUpButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        signUp();
                    }
                });

                firstBack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openMenu();
                    }
                });

                //this is pressed after the user enters their signup information
                signUpContinue.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //if any fields are empty, give the user an error and try again
                        JTextField[] signUpfields = {signUpFirstName, signUpLastName, signUpPassword};
                        if (!areFieldsFull(signUpfields)) {
                            JOptionPane.showMessageDialog(null, "Please make sure there are no empty fields",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String firstName = signUpFirstName.getText();
                            String lastName = signUpLastName.getText();
                            String password = signUpPassword.getText();
                            String role = (String)combo.getSelectedItem();
                            out.println("signup;" + signupID + ";" + password + ";" + role + ";" + firstName + ";" + lastName);
                            sessionAuthority = combo.getSelectedItem().equals("Teacher");
                            firstMenu();
                        }
                    }
                });

                logInContinue.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
						JTextField[] logInfields = {logInUserID, logInPassword};
						if (!areFieldsFull(logInfields)) {
                            JOptionPane.showMessageDialog(null, "Please make sure there are no empty fields",
                                    "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							//checks to make sure entered ID is an integer
							try {
								String user = logInUserID.getText();
								Integer testID = Integer.parseInt(user);
		                        String pass = logInPassword.getText();
		                        if (logIn(user, pass, socket)) {
                                    sessionID = testID;
		                            firstMenu();
		                        }
							} catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(null, "Please make sure entered ID is an integer",
	                                    "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
                    }
                });

                edit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        editAccount();
                    }
                });

                backEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        firstMenu();
                    }
                });

                processEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Add code to change user info
                        //TODO replace template with actual user details here
                        String password = passwordChange.getText();
                        String firstName = firstNameChange.getText();
                        String lastName = lastNameChange.getText();
                        String modificationPayload = "editAccount;" + password + ";" + firstName + ";" + lastName + ";" + sessionID;
                        out.println(modificationPayload);
                        out.flush();
                        String infoMessage = "This is now your saved user information:\nPassword: " + password +
                               "\nFirst Name: " + firstName + "\nLast Name: " + lastName;
                        JOptionPane.showMessageDialog(null, infoMessage, "Changes Made", JOptionPane.INFORMATION_MESSAGE);
                        firstMenu();
                    }
                });



                delete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int result = JOptionPane.showConfirmDialog(frame,"Are you sure you want to delete your account?", "Delete Account",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if(result == JOptionPane.NO_OPTION) {
                            firstMenu();
                        } else {
                            frame.dispose();
                            //Add code to delete the account
                            JOptionPane.showMessageDialog(null, "Thank you for using our platform!", "Closing System", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });

                viewCourses.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        viewAllCourses();
                    }
                });

                viewCoursesBack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        firstMenu();
                    }
                });

                selectCourse.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Eventually this array will be replaced with the array that corresponds to the selected course.
                        String[] discussionBoards = {"Array of all the boards under the selected course", "Option 2"};
                        viewBoards(discussionBoards);
                    }
                });

                methodChoice.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        if(methodChoice.getSelectedItem().equals("Direct text"))
                            chosenMethod.setText("Type in the forum topic you want: ");
                        else
                            chosenMethod.setText("Enter the file path to the forum topic you want: ");

                    }
                });

                newCourses.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        createNewCourse();
                    }
                });

                logout.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        JOptionPane.showMessageDialog(frame, "Thank you for using our platform!", "Logout", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                viewGrades.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        viewPostsAndGrades();
                    }
                });

                selectStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        studentComment.setVisible(true);
                        enterGrade.setVisible(true);
                        grade.setVisible(true);
                    }
                });

                gradePosts.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        gradeStudentPosts();
                    }
                });
            }
        });
    }

    public static void initializeGUI() {
        frame = new JFrame("Discussion Board Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void openMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 2));
        JLabel welcomeMessage1 = new JLabel("Welcome to our discussion board! ");
        JLabel welcomeMessage2 = new JLabel("Please Login or Sign up below.");
        frame.add(welcomeMessage1);frame.add(welcomeMessage2);
        frame.add(signUpButton);frame.add(logInButton);
        frame.pack();
    }

    //TODO check if what the user entered is an accurate login.
    public static void logIn() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel username = new JLabel("Enter your userID: ");
        logInUserID = new JTextField(15);
        JLabel pass = new JLabel("Enter your password: ");
        logInPassword = new JTextField(15);

        frame.add(username);frame.add(logInUserID);
        frame.add(pass);frame.add(logInPassword);
        frame.add(firstBack);frame.add(logInContinue);

        frame.repaint();
        frame.pack();
    }

    public static void signUp() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(6, 2));

        JLabel IDMessage1 = new JLabel("Your new ID is: ");
        int requestedID = requestNewID(socket);

        sessionID = requestedID;

        IDMessage2 = new JLabel(String.valueOf(requestedID));
        signupID = requestedID;
        JLabel passMessage = new JLabel("Please enter a password: ");
        signUpPassword = new JTextField(15);
        JLabel firstMessage = new JLabel("Please enter your first name: ");
        signUpFirstName = new JTextField(15);
        JLabel lastMessage = new JLabel("Please enter your last name: ");
        signUpLastName = new JTextField(15);
        JLabel studentTeacher = new JLabel ("I am a ");

        frame.add(IDMessage1); frame.add(IDMessage2);
        frame.add(passMessage); frame.add(signUpPassword);
        frame.add(firstMessage); frame.add(signUpFirstName);
        frame.add(lastMessage); frame.add(signUpLastName);
        frame.add(studentTeacher); frame.add(combo);
        frame.add(firstBack); frame.add(signUpContinue);

        frame.repaint();
        frame.pack();
    }

    public static void firstMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));
        frame.add(edit); frame.add(delete);
        frame.add(viewCourses); frame.add(logout);

        if(sessionAuthority) {
            frame.add(newCourses);
            frame.add(gradePosts);
        } else
            frame.add(viewGrades);

        frame.repaint();
        frame.pack();
    }

    //TODO save this edited account info
    //TODO update the GUI pop up with the user info.
    //TODO: somewhere in here (either client or server side) we need to re-save the files to match the new arrays
    //TODO: figure out why the userID doesn't increment when new accounts are created
    //bug: when i use the sign up functionality multiple times on multiple starts of the program, i think multiple users
    //are created with an ID number of 2.  this is making account editing not work
    public static void editAccount() {
        //The idea here is to print all the current information in the text fields and allow the user to change it.
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5, 2));

        //request the session name and password from the server
        out.println("sessionVariable;" + sessionID);
        out.flush();
        //read in the name and password.  format: firstname;lastname;password
        String firstName = "";
        String lastName = "";
        String password = "";
        try {
            String line = in.readLine();
            firstName = line.split(";")[0];
            lastName = line.split(";")[1];
            password = line.split(";")[2];
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel message = new JLabel("Enter any changes you would like to make in the text fields below. ");
        JLabel empty = new JLabel("");
        JLabel pass = new JLabel("Password: ");
        passwordChange = new JTextField(password);
        JLabel first = new JLabel("First Name: ");
        firstNameChange = new JTextField(firstName);
        JLabel last = new JLabel("Last Name: ");
        lastNameChange = new JTextField(lastName);

        frame.add(message); frame.add(empty);
        frame.add(pass); frame.add(passwordChange);
        frame.add(first); frame.add(firstNameChange);
        frame.add(last); frame.add(lastNameChange);
        frame.add(backEdit); frame.add(processEdit);

        frame.repaint();
        frame.pack();
    }

    //TODO have all courses appear in the combo box
    public static void viewAllCourses() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 2));

        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(viewCoursesBack);

        frame.pack();


    }

    //TODO have all corresponding boards appear in the combo box
    //TODO have the selectBoard button take you to the next page
    public static void viewBoards(String[] boards) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3,2));

        JButton selectBoard = new JButton("Select Board");
        JComboBox<String> discussionBoardsCombo = new JComboBox<String>(boards);

        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(discussionBoardsCombo);
        frame.add(selectBoard);
        frame.add(viewCoursesBack);

        frame.pack();
    }

    //TODO save the created course
    public static void createNewCourse() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 2));
        JLabel newName = new JLabel("Enter the name of the new course");
        JTextField name = new JTextField();
        JLabel chooseMethod = new JLabel("How would you like to input the forum topic?");
        JTextField forumTopic = new JTextField();

        frame.add(newName);
        frame.add(name);
        frame.add(chooseMethod);
        frame.add(methodChoice);
        frame.add(chosenMethod);
        frame.add(forumTopic);
        frame.add(backEdit);
        frame.add(createCourse);

        frame.pack();

    }

    //TODO populate combo box with all student IDs
    //TODO iterate through all of a student's posts, print each in the text field one at a time
    //TODO save the grades the teacher puts in
    public static void gradeStudentPosts() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3,2));

        studentComment.setEditable(false);

        frame.add(studentIDs);
        frame.add(selectStudent);
        frame.add(studentComment);
        frame.add(grade);
        frame.add(enterGrade);

        grade.setVisible(false);
        studentComment.setVisible(false);
        enterGrade.setVisible(false);

        frame.pack();
    }

    //TODO populate the scrollPane with all the relative comments
    public static void viewPostsAndGrades() {
        frame.getContentPane().removeAll();
        frame.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        JScrollPane postsAndGrades = new JScrollPane(panel);
        JLabel test = new JLabel("Test        Test");
        JLabel test2 = new JLabel("Test 2");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        postsAndGrades.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(test);
        panel.add(test2);

        panel.add(Box.createVerticalGlue());


        frame.pack();
        frame.getContentPane().add(postsAndGrades);
        frame.setSize(500,300);
    }

    public static void signUp(Integer userID, String firstName, String lastName, String password, String role, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String signUpPayload = "signup;" + userID + ";" + password + ";" + role + ";" + firstName + ";" + lastName;
            out.println(signUpPayload);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer requestNewID(Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //sending a request to the server for a new ID to be assigned
            out.println("newID");
            out.flush();
            return Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Boolean logIn(String userID, String password, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String verificationPayload = "login;" + userID + ";" + password;
            out.println(verificationPayload);
            out.flush();
            switch(Integer.parseInt(in.readLine())) {
                case 1:
                    JOptionPane.showMessageDialog(null, "The entered ID does not exist!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                case 2:
                    JOptionPane.showMessageDialog(null, "The entered password is incorrect!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                case 3:
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void sendRequest(String requestPayload, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(requestPayload);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean areFieldsFull(JTextField[] fields) {
        for(JTextField field: fields) {
            if(((String)field.getText()).equals("")) {
                return false;
            }
        }
        return true;
    }
}
