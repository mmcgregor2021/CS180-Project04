import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUItest extends JComponent implements Runnable {

	JLabel welcomeLabel;
	JButton signInButton;
	JButton signUpButton;
	ArrayList<Comment> comments = readComments("comments.txt");
	ArrayList<Board> boards = readBoards("boards.txt");
	ArrayList<Teacher> teachers = readTeachers("teachers.txt");
	ArrayList<Student> students = readStudents("teachers.txt");
	int personCounter = readCounters("counters.txt")[0];
	int boardCounter = readCounters("counters.txt")[1];
	int commentCounter = readCounters("counters.txt")[2];


	//serializes the comment objects and stores them in a txt file
	public static void saveComments(ArrayList<Comment> comments, String fileName) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(comments.size());
			for (int i = 0; i < comments.size(); i++) {
				out.writeObject(comments.get(i));
			}
		} catch (Exception e) {
			System.out.println("Failed to save comments to file!");
		}
	}

	//serializes the board objects and stores them in a txt file
	public static void saveBoards(ArrayList<Board> boards, String fileName) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(boards.size());
			for (int i = 0; i < boards.size(); i++) {
				out.writeObject(boards.get(i));
			}
		} catch (Exception e) {
			System.out.println("Failed to save boards to file!");
		}
	}

	//serializes the student objects and stores them in a txt file
	public static void saveStudents(ArrayList<Student> students, String fileName) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(students.size());
			for (int i = 0; i < students.size(); i++) {
				out.writeObject(students.get(i));
			}
		} catch (Exception e) {
			System.out.println("Failed to save students to file!");
		}
	}

	//serializes the teacher objects and stores them in a txt file
	public static void saveTeachers(ArrayList<Teacher> teachers, String fileName) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(teachers.size());
			for (int i = 0; i < teachers.size(); i++) {
				out.writeObject(teachers.get(i));
			}
		} catch (Exception e) {
			System.out.println("Failed to save teachers to file!");
		}
	}

	//deserializes comment objects from the txt file and returns an arraylist of the board objects
	public static ArrayList<Comment> readComments(String fileName) {
		ArrayList<Comment> comments = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			int count = (int) in.readObject();
			for (int i = 0; i < count; i++) {
				Comment comment = (Comment) in.readObject();
				comments.add(comment);
			}
		} catch (Exception e) {
			//DO NOTHING
		}
		return comments;
	}

	//deserializes board objects from the txt file and returns an arraylist of the board objects
	public static ArrayList<Board> readBoards(String fileName) {
		ArrayList<Board> boards = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			int count = (int) in.readObject();
			for (int i = 0; i < count; i++) {
				Board board = (Board) in.readObject();
				boards.add(board);
			}
		} catch (Exception e) {
			//DO NOTHING
		}
		return boards;
	}

	//deserializes student objects from the txt file and returns an arraylist of the student objects
	public static ArrayList<Student> readStudents(String fileName) {
		ArrayList<Student> students = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			int count = (int) in.readObject();
			for (int i = 0; i < count; i++) {
				Student student = (Student) in.readObject();
				students.add(student);
			}
		} catch (Exception e) {
			//DO NOTHING
		}
		return students;
	}

	//deserializes teacher objects from the txt file and returns an arraylist of the teacher objects
	public static ArrayList<Teacher> readTeachers(String fileName) {
		ArrayList<Teacher> teachers = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			int count = (int) in.readObject();
			for (int i = 0; i < count; i++) {
				Teacher teacher = (Teacher) in.readObject();
				teachers.add(teacher);
			}
		} catch (Exception e) {
			//DO NOTHING
		}
		return teachers;
	}

	//saves all three counters to one line separated by ';' to a txt file
	public static void saveCounters(int personCounter, int boardCounter,
		   int commentCounter, String fileName) {
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
			//DO NOTHING
		}
		return arr;
	}

	@Override
	//Event Dispatch Thread
	public void run() {
		JFrame frame = new JFrame("Discussion Board Learning Management System");
		Container content = frame.getContentPane();

		JPanel mainAccessPanel = new JPanel();
		mainAccessPanel.setLayout(new BoxLayout(mainAccessPanel, BoxLayout.Y_AXIS));

		frame.setSize(350, 200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		JPanel welcomePanel = new JPanel();
		welcomeLabel = new JLabel("Welcome!");
		welcomeLabel.setFont(new Font("Roboto", Font.PLAIN, 25));
		welcomePanel.add(welcomeLabel);
		mainAccessPanel.add(welcomePanel);

		JPanel accessPanel = new JPanel();
		accessPanel.setLayout(new BoxLayout(accessPanel, BoxLayout.Y_AXIS));
		signInButton = new JButton("Sign in");
		//action listener for sign in
		signInButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.remove(mainAccessPanel);
			frame.revalidate();
			frame.repaint();
		}
	});
		signUpButton = new JButton("Sign up");
		signUpButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//SIGN UP CODE
		}
	});
 		//adding space between the compontents
		accessPanel.add(signInButton);
		accessPanel.add(Box.createVerticalStrut(20));
		accessPanel.add(signUpButton);
		accessPanel.add(Box.createVerticalStrut(10));
		signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainAccessPanel.add(accessPanel);

		content.add(mainAccessPanel);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new GUItest());
	}
}
