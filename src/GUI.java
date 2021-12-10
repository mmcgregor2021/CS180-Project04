import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GUI extends JComponent{
    private static JFrame frame;

	//button to go back to main menu
	private static JButton mainBack = new JButton("Back");

    //Variables for opening page
    private static JButton signUpButton = new JButton("Sign Up");
    private static JButton logInButton = new JButton("Log In");
    private static JButton firstBack = new JButton("Back");
    private static JButton signUpContinue = new JButton("Continue");
    private static JButton logInContinue = new JButton("Continue");

    //Session variables
    private static Integer signupID = 0;
    private static Integer sessionID;
    private static String sessionName;
    private static boolean sessionAuthority;

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
    private static JButton addBoard = new JButton("Add Discussion Board");

    //Variables for displaying boards and comments
    private static JLabel boardTitleLabel = new JLabel("This is where the forum topic will go");
    private static JButton viewBoardBackButton = new JButton("Back");
    private static JButton replyButton;
    private static JButton voteButton;
    private static JLabel repliesLabel;
    //kris

    //Variables for edit account
    private static JTextField passwordChange;
    private static JTextField firstNameChange;
    private static JTextField lastNameChange;
    private static JButton processEdit = new JButton("Process changes");

    //Variables for creating a course
    private static String[] uploadChoices = {"Direct text", "File name"};
    private static JComboBox<String> methodChoice = new JComboBox<>(uploadChoices);
    private static JLabel chosenMethod = new JLabel("Type in the forum topic for the course's first board: ");
    private static JTextField forumTopic;
    private static JLabel chooseMethod = new JLabel("How would you like to input the forum topic?");
    private static JTextField courseName;
    private static JButton createCourse = new JButton("Create course");
    private static JButton newCourseBack = new JButton("Back");

    //variables for adding boards
    /*
    Reusing coursesCombo selection box
    Reusing methodChoice selection box
    Reusing chooseMethod label
    */
    private static JLabel courseSelectionLabel = new JLabel("Select a course: ");
    private static JLabel boardTopicLabel = new JLabel("Type in the topic of the discussion board: ");
    private static JTextField boardTopicField = new JTextField();
    private static JButton createBoard = new JButton("Create Discussion Board");

    //Variables for viewing all courses
    private static JButton viewCoursesBack = new JButton("Back");
    private static JButton selectCourse = new JButton("Select course");
    private static JButton selectBoard = new JButton("Select Board");
    private static JComboBox<String> coursesCombo;
    private static JComboBox<String> discussionBoardsCombo;

    //Variables for grading all of a student's posts
	private static JButton viewContent = new JButton("View Content of Comment");
    private static JComboBox<String> studentIDs = new JComboBox<>();
	private static JComboBox<String> studentPosts = new JComboBox<>();
    private static JButton selectStudent = new JButton("Select this student");
    private static JTextArea studentComment = new JTextArea();
    private static JTextField grade = new JTextField("Enter the grade here");
    private static JButton enterGrade = new JButton("Assign Grade");

	//Server Variables
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
                                    sendRequest("sessionVariable;" + sessionID, socket);
                                    //determines session authority
                                    sessionAuthority = in.readLine().split(";")[3].equals("Teacher");
		                            firstMenu();
		                        }
							} catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(null, "Please make sure entered ID is an integer",
	                                    "Error", JOptionPane.ERROR_MESSAGE);
							} catch (IOException ex) {
                                //DO NOTHING
                            }
						}
                    }
                });

                edit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        editAccount();
                    }
                });

                mainBack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
						studentPosts.removeAllItems();
						grade.setVisible(false);
				        enterGrade.setVisible(false);
                        firstMenu();
                    }
                });

                viewBoardBackButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        viewAllCourses();
                    }
                });

                processEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Add code to change user info
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

                addBoard.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addBoard();
                    }
                });

				createBoard.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (boardTopicField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please make sure there are no empty fields",
                                    "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							boolean boardCreationError = false;
							String topic = "";
							String course = (String)coursesCombo.getSelectedItem();
							String method = (String)methodChoice.getSelectedItem();
							if (method.equals("Direct text")) {
								topic = boardTopicField.getText();
							} else {
								String fileName = boardTopicField.getText();
								try {
									topic = getTextFromFile(fileName);
								} catch (IOException ex) {
									boardCreationError = true;
									JOptionPane.showMessageDialog(null, "Error reading file!",
		                                    "Error", JOptionPane.ERROR_MESSAGE);
								}
							}
							if (!boardCreationError) {
								String payload = "createBoard;" + course + ";" + topic;
								sendRequest(payload, socket);
								JOptionPane.showMessageDialog(null, "The board with topic: " + topic +
								       "\nhas been added to course: " + course, "Board Added", JOptionPane.INFORMATION_MESSAGE);
							}
							boardTopicField.setText("");
						}
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
                            String deletionPayload = "deleteAccount;" + sessionID;
                            out.println(deletionPayload);
                            out.flush();
                            JOptionPane.showMessageDialog(null, "Your account has been deleted. " +
                                   "Thank you for using our platform!", "Closing System", JOptionPane.INFORMATION_MESSAGE);
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
						String selectedCourse = (String)coursesCombo.getSelectedItem();
                   		String[] discussionBoards = findBoardsByCourse(selectedCourse, socket);
                        viewBoards(discussionBoards);
                    }
                });

                selectBoard.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        viewDiscussionPage();
                    }
                });

                methodChoice.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        if (methodChoice.getSelectedItem().equals("Direct text")) {
                            chosenMethod.setText("Type in the initial first board topic: ");
                            boardTopicLabel.setText("Type in the topic of the discussion board: ");
                        } else {
                            chosenMethod.setText("Enter the file name: ");
                            boardTopicLabel.setText("Enter the file name: ");
                        }
                    }
                });

                newCourses.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        createNewCourse();
                    }
                });

                createCourse.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JTextField[] fields = {forumTopic, courseName};
                        if (!areFieldsFull(fields)) {
                            JOptionPane.showMessageDialog(null, "Please make sure there are no empty fields",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String name = courseName.getText();
                            String topic = forumTopic.getText();
                            String method = (String)methodChoice.getSelectedItem();
                            if (method.equals("Direct text")) {
                                sendRequest("createCourse;" + name + ";" + topic, socket);
								JOptionPane.showMessageDialog(frame, "The course: " + name + "\nand\nDiscussion Board: " + topic +
	                                   "\nhave been successfully created", "Course Created", JOptionPane.INFORMATION_MESSAGE);
								firstMenu();
                            } else {
								try {
									topic = getTextFromFile(topic);
	                                sendRequest("createCourse;" + name + ";" + topic, socket);
									JOptionPane.showMessageDialog(frame, "The course: " + name + "\nand\nDiscussion Board: " + topic +
		                                   "\nhave been successfully created", "Course Created", JOptionPane.INFORMATION_MESSAGE);
									firstMenu();
								} catch (IOException ex) {
									JOptionPane.showMessageDialog(null, "Error reading file!",
		                                    "Error", JOptionPane.ERROR_MESSAGE);
								}

                            }
                        }
                    }
                });

                logout.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        JOptionPane.showMessageDialog(frame, "Thank you for using our platform!",
                               "Logout", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                viewGrades.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        viewPostsAndGrades();
                    }
                });

				viewContent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	String commentID = (String)studentPosts.getSelectedItem();
						if (commentID != null) {
							String payload = "commentContent;" + commentID;
							sendRequest(payload, socket);
							try {
								String line = in.readLine();
								String course = line.split(";")[0];
								String topic = line.split(";")[1];
								String post = line.split(";")[2];
								String grade = line.split(";")[3];
								String content = "Course: " + course + "\nForum Topic: " + topic +
								       "\nContent: " + post + "\nCurrent Grade: " + grade;
							    JOptionPane.showMessageDialog(null, content, "Post Content", JOptionPane.INFORMATION_MESSAGE);
							} catch (IOException ex) {
								//DO NOTHING
							}
						}
                    }
                });

                selectStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
						String idToGrade = (String)studentIDs.getSelectedItem();
						idToGrade = idToGrade.split("ID: ")[1];
						String[] postsOfStudent = findPostsByStudent(idToGrade, socket);
						if (postsOfStudent[0].equals("|EMPTY|")) {
							JOptionPane.showMessageDialog(null, "This student has not posted anything yet!",
							       "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							studentPosts.removeAllItems();
							for (String s: postsOfStudent) {
								studentPosts.addItem(s);
							}
						    frame.repaint();
							frame.pack();
	                        enterGrade.setVisible(true);
	                        grade.setVisible(true);
						}
                    }
                });

                gradePosts.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        gradeStudentPosts();
                    }
                });

				enterGrade.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
						String commentID = (String)studentPosts.getSelectedItem();
						Integer gradeAssigned = 0;
						try {
							gradeAssigned = Integer.parseInt(grade.getText());
							if (gradeAssigned < 0 || gradeAssigned > 100) {
								JOptionPane.showMessageDialog(null, "Please enter an integer between 0 and 100 inclusive.",
			                            "Error", JOptionPane.ERROR_MESSAGE);
							} else {
								String payload = "gradeComment;" + commentID + ";" + gradeAssigned;
								sendRequest(payload, socket);
								String confirmMessage = "The grade for comment ID: " + commentID + " has been set to: " + gradeAssigned;
								JOptionPane.showMessageDialog(null, confirmMessage, "Grade Set!", JOptionPane.INFORMATION_MESSAGE);
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Please enter a valid integer!",
		                            "Error", JOptionPane.ERROR_MESSAGE);
						}
						grade.setText("");
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
            frame.setLayout(new GridLayout(4, 2));
            frame.add(newCourses);
            frame.add(gradePosts);
            frame.add(addBoard);
        } else
            frame.add(viewGrades);

        frame.repaint();
        frame.pack();
    }

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
        frame.add(mainBack); frame.add(processEdit);

        frame.repaint();
        frame.pack();
    }

    public static void viewAllCourses() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 2));
        String allCourses = "";
        try {
            sendRequest("listAllCourses", socket);
            allCourses = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        coursesCombo = new JComboBox<String>(allCourses.split(";"));
        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(viewCoursesBack);
        frame.pack();
    }

    //TODO have the selectBoard button take you to the next page
    public static void viewBoards(String[] boards) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3,2));
        discussionBoardsCombo = new JComboBox<String>(boards);
        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(discussionBoardsCombo);
        frame.add(selectBoard);
        frame.add(viewCoursesBack);

        frame.pack();
    }

    public static void addBoard() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(8, 1));
        String allCourses = "";
        try {
            sendRequest("listAllCourses", socket);
            allCourses = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        coursesCombo = new JComboBox<String>(allCourses.split(";"));
        frame.add(courseSelectionLabel); //JLabel
        frame.add(coursesCombo); //JComboBox
        frame.add(chooseMethod); //JLabel
        frame.add(methodChoice); //JComboBox
        frame.add(boardTopicLabel); //JLabel
        frame.add(boardTopicField); //JTextField
        frame.add(createBoard); //JButton
        frame.add(mainBack); //JButton

        frame.pack();
    }

    public static void createNewCourse() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 2));
        JLabel newName = new JLabel("Enter the name of the new course");
        courseName = new JTextField();
        forumTopic = new JTextField();

        frame.add(newName);
        frame.add(courseName);
        frame.add(chooseMethod);
        frame.add(methodChoice);
        frame.add(chosenMethod);
        frame.add(forumTopic);
        frame.add(mainBack);
        frame.add(createCourse);

        frame.pack();

    }

    public static void gradeStudentPosts() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4,2));

		grade.setText("Enter the grade here");
		studentIDs = new JComboBox<String>(listAllStudents(socket));
        frame.add(studentIDs);
        frame.add(selectStudent);
        frame.add(studentPosts);
        frame.add(grade);
        frame.add(viewContent);
		frame.add(enterGrade);
		frame.add(mainBack);

        grade.setVisible(false);
        enterGrade.setVisible(false);

        frame.pack();
    }

    public static void viewDiscussionPage() {
        frame.getContentPane().removeAll();
		if (sessionAuthority) {
			frame.setLayout(new GridLayout(3,1));
		} else {
			frame.setLayout(new GridLayout(4,1));
		}
        String boardID = (String)discussionBoardsCombo.getSelectedItem();
        boardID = boardID.split(" - ID: ")[1];
        String boardInfo = findBoardInfo(boardID, socket);
        int numberOfComments = Integer.parseInt(boardInfo.split(";")[0]);
        String topic = boardInfo.split(";")[1];
        boardTitleLabel.setText(topic);
        ArrayList<Comment> boardComments = findCommentsByBoardID(boardID, socket);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4 * numberOfComments, 1));
        //panel.add(comment)
        //panel.add(replyButton)
        //panel.add(voteButton)
        //panel.add(repliesLabel)
        JScrollPane commentsPane = new JScrollPane(panel);
		commentsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(boardTitleLabel);
		boardTitleLabel.setHorizontalAlignment(JLabel.CENTER);
		boardTitleLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        frame.add(viewBoardBackButton);
        frame.add(commentsPane);

        frame.repaint();
        frame.pack();
		frame.setSize(500,300);
        //kris
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

	public static int getNumComments(String boardID, Socket socket) {
		int size = 0;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String payload = "getComments;" + boardID;
	        sendRequest(payload, socket);
			size = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

    public static ArrayList<Comment> findCommentsByBoardID(String boardID, Socket socket) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
			int size = getNumComments(boardID, socket);
			if (size != 0) {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Comment[] commentArr = (Comment[])ois.readObject();
				for (Comment c: commentArr) {
					comments.add(c);
					System.out.println(c.getContent()); //DELETE
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    public static String findBoardInfo(String boardID, Socket socket) {
        String infoToReturn = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String payload = "boardInfo;" + boardID;
            sendRequest(payload, socket);
            infoToReturn = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infoToReturn;
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

	public static String[] findPostsByStudent(String studentID, Socket socket) {
		String postsString = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String payload = "listAllPostsByStudent;" + studentID;
			sendRequest(payload, socket);
			postsString = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		return postsString.split(";");
	}

	public static String[] findBoardsByCourse(String courseName, Socket socket) {
		String boardsString = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String payload = "listAllBoards;" + courseName;
			sendRequest(payload, socket);
			boardsString = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		return boardsString.split(";");
	}

	public static String[] listAllStudents(Socket socket) {
		String studentsString = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String payload = "listAllStudents";
			sendRequest(payload, socket);
			studentsString = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		return studentsString.split(";");
	}

    public static boolean areFieldsFull(JTextField[] fields) {
        for(JTextField field: fields) {
            if(((String)field.getText()).equals("")) {
                return false;
            }
        }
        return true;
    }

    public static String getTextFromFile(String fileName) throws IOException {
        String line;
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            line = bfr.readLine();
        } catch (IOException e) {
            throw e;
        }
        return line;
    }
}
