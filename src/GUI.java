import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JComponent{
    private static JFrame frame;
    private static JButton signUpButton = new JButton("Sign Up");
    private static JButton logInButton = new JButton("Log In");
    private static JButton firstBack = new JButton("Back");
    private static JButton firstContinue = new JButton("Continue");
    static boolean sessionAuthority;
    private static String[] options = {"Student", "Teacher"};
    private static JComboBox<String> combo = new JComboBox<String>(options);
    private static JButton edit = new JButton("Edit account");
    private static JButton delete = new JButton("Delete account");
    private static JButton viewCourses = new JButton("View courses");
    private static JButton logout = new JButton("Logout");
    private static JButton newCourses = new JButton("Create new course");
    private static JButton gradePosts = new JButton("Grade student posts");
    private static JButton viewGrades = new JButton("View posts and grades");
    private static JButton processEdit = new JButton("Process changes");
    private static JButton backEdit = new JButton("Back");
    private static JButton viewCoursesBack = new JButton("Back");
    private static JButton selectCourse = new JButton("Select course");
    private static String[] uploadChoices = {"Direct text", "File path"};
    private static JComboBox<String> methodChoice = new JComboBox<>(uploadChoices);
    private static JLabel chosenMethod = new JLabel("Type in the forum topic you want: ");
    private static JButton createCourse = new JButton("Create course");
    private static JButton newCourseBack = new JButton("Back");
    private static String[] coursesArray = {"array of all courses here"};
    private static JComboBox<String> coursesCombo = new JComboBox<String>(coursesArray);

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
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

                firstContinue.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Add code to make an account.
                        sessionAuthority = combo.getSelectedItem().equals("Teacher");
                        firstMenu();

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
                        String infoMessage = "This is now your saved user information: \n (Print user details here)";
                        JOptionPane.showMessageDialog(null, infoMessage, "Changes Made", JOptionPane.INFORMATION_MESSAGE);
                        firstMenu();
                    }
                });

                logout.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        JOptionPane.showMessageDialog(frame, "Thank you for using our platform!", "Logout", JOptionPane.INFORMATION_MESSAGE);
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
            }
        });
    }

    public static void initializeGUI() {
        frame = new JFrame("Discussion Board Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public static void logIn() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel username = new JLabel("Enter your userID: ");
        JTextField userID = new JTextField(15);
        JLabel pass = new JLabel("Enter your password: ");
        JTextField password = new JTextField(15);

        frame.add(username);frame.add(userID);
        frame.add(pass);frame.add(password);
        frame.add(firstBack);frame.add(firstContinue);

        frame.repaint();
        frame.pack();
    }

    public static void signUp() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(6, 2));

        JLabel IDMessage1 = new JLabel("Your new ID is: ");
        JLabel IDMessage2 = new JLabel("This is where the generated ID will go");
        JLabel passMessage = new JLabel("PLease enter a password: ");
        JTextField password = new JTextField(15);
        JLabel firstMessage = new JLabel("Please enter your first name: ");
        JTextField firstName = new JTextField(15);
        JLabel lastMessage = new JLabel("Please enter your last name: ");
        JTextField lastName = new JTextField(15);
        JLabel studentTeacher = new JLabel ("I am a ");

        frame.add(IDMessage1); frame.add(IDMessage2);
        frame.add(passMessage); frame.add(password);
        frame.add(firstMessage); frame.add(firstName);
        frame.add(lastMessage); frame.add(lastName);
        frame.add(studentTeacher); frame.add(combo);
        frame.add(firstBack); frame.add(firstContinue);

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

    public static void editAccount() {
        //The idea here is to print all the current information in the text fields and allow the user to change it.
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5, 2));

        JLabel message = new JLabel("Enter any changes you would like to make in the text fields below. ");
        JLabel empty = new JLabel("");
        JLabel pass = new JLabel("Password: ");
        JTextField passwordChange = new JTextField("Current password");
        JLabel first = new JLabel("First Name: ");
        JTextField firstNameChange = new JTextField("Current first name");
        JLabel last = new JLabel("Last Name: ");
        JTextField lastNameChange = new JTextField("Current last name");

        frame.add(message); frame.add(empty);
        frame.add(pass); frame.add(passwordChange);
        frame.add(first); frame.add(firstNameChange);
        frame.add(last); frame.add(lastNameChange);
        frame.add(backEdit); frame.add(processEdit);

        frame.repaint();
        frame.pack();
    }

    public static void viewAllCourses() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 2));

        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(viewCoursesBack);

        frame.pack();


    }

    public static void viewBoards(String[] boards) {
        //Still needs work
        frame.remove(viewCoursesBack);
        frame.setLayout(new GridLayout(3,2));

        JButton selectBoard = new JButton("Select Board");
        JComboBox<String> discussionBoardsCombo = new JComboBox<String>(boards);

        frame.add(discussionBoardsCombo);
        frame.add(selectBoard);
        frame.add(viewCoursesBack);

        frame.pack();
    }

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

    public static void gradeStudentPosts() {

    }




}
