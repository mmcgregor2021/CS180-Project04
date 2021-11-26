import javax.swing.*;
import java.awt.event.*;

public class GUI extends JComponent{
    private static JFrame frame;
    private static JPanel content;
    private static JButton signUpButton;
    private static JButton logInButton;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeGUI();

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
            }
        });
    }

    public static void initializeGUI() {
        frame = new JFrame("Discussion Board Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpButton = new JButton("Sign Up");
        logInButton = new JButton("Log In");
        JLabel welcomeMessage = new JLabel("Welcome to our discussion board! Please Login or Sign up below.");
        content = new JPanel();
        frame.add(content);
        content.add(welcomeMessage);
        content.add(signUpButton);
        content.add(logInButton);
        frame.setSize(500,300);
        frame.setVisible(true);
    }

    public static void logIn() {
        content.removeAll();
        frame.repaint();

        JLabel username = new JLabel("Enter your userID: ");
        JTextField userID = new JTextField(15);
        JLabel pass = new JLabel("Enter your password: ");
        JTextField password = new JTextField(15);
        content.add(username);
        content.add(userID);
        content.add(pass);
        content.add(password);
        frame.pack();
        frame.setSize(500,300);
    }

    public static void signUp() {
        content.removeAll();
        frame.repaint();
    }


}
