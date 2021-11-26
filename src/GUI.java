import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JComponent{
    private static JFrame frame;
    private static JButton signUpButton = new JButton("Sign Up");
    private static JButton logInButton = new JButton("Log In");
    private static JButton firstBack = new JButton("Back");
    private static JButton firstContinue = new JButton("Continue");


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeGUI();
                openMenu();

                logInButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
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
        String[] options = {"Student", "Teacher"};
        JComboBox<String> combo = new JComboBox<String>(options);


        frame.add(IDMessage1); frame.add(IDMessage2);
        frame.add(passMessage); frame.add(password);
        frame.add(firstMessage); frame.add(firstName);
        frame.add(lastMessage); frame.add(lastName);
        frame.add(studentTeacher); frame.add(combo);
        frame.add(firstBack); frame.add(firstContinue);

        frame.repaint();
        frame.pack();
    }


}
