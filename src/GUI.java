import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;
/**
 * GUI for a learning management discussion board system
 * @author Astrid Popovici, Grant McCord, Jainam Doshi, Kathryn McGregor, Kris Leungwattanakij
 * @version December 11, 2021
 */
public class GUI extends JComponent {
    private static JFrame frame;

	//button to go back to main menu
	private static JButton mainBack = new JButton("Back");

	//Update variables
	private static String lastFetchedCourses = "";
	private static String[] lastFetchedBoards;
	private static String lastSelectedCourse = "";
	private static String lastDashboardText = "";
	private static String[] lastListOfStudents;
	private static String lastIDToGrade = "";
	private static String[] lastSelectedStudentPosts;
    private static ArrayList<Comment> lastFetchedBoardComments = new ArrayList<>();
	//kris

    //Variables for opening page
    private static JButton signUpButton = new JButton("Sign Up");
    private static JButton logInButton = new JButton("Log In");
    private static JButton firstBack = new JButton("Back");
    private static JButton signUpContinue = new JButton("Continue");
    private static JButton logInContinue = new JButton("Continue");

    //Session variables
    private static boolean running = true;
    private static String currentBoard = "";
    private static Integer signupID = 0;
    private static Integer sessionID;
    private static String sessionName;
    private static boolean sessionAuthority;
    private static String currentPage = "";

    //Variables for signing up
    private static String[] options = {"Student", "Teacher"};
    private static JComboBox<String> combo = new JComboBox<String>(options);
    private static JTextField signUpFirstName;
    private static JTextField signUpLastName;
    private static JTextField signUpPassword;
    private static JTextField logInUserID;
    private static JTextField logInPassword;
    private static JLabel idMessage2;

    //Main menu buttons
    private static JButton edit = new JButton("Edit account");
    private static JButton delete = new JButton("Delete account");
    private static JButton viewCourses = new JButton("View courses");
    private static JButton newCourses = new JButton("Create new course");
    private static JButton gradePosts = new JButton("Grade student posts");
    private static JButton viewGrades = new JButton("View posts and grades");
    private static JButton addBoard = new JButton("Add Discussion Board");

    //Variables for displaying boards and comments
    private static JLabel boardTitleLabel = new JLabel("This is where the forum topic will go");
    private static JButton viewBoardBackButton = new JButton("Back");
    private static JLabel commentLabel;
    private static JButton replyButton = new JButton("Reply Button");
    private static JButton voteButton = new JButton("Vote Button");
    private static JButton addCommentButton = new JButton("Add Comment");
    private static JButton viewDashboardButton = new JButton("View Dashboard");
	private static JButton deleteBoardButton = new JButton("Delete Board");

	//Variables for displaying teacher dashboard
	private static JButton sortButton = new JButton("Sort by descending order");
	private static JButton dashboardBack = new JButton("Back");
	private static JLabel postLabel = new JLabel("comments go here");

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

    //Variables for viewing student posts and their grades
    private static JLabel postsAndGradesLabel = new JLabel("Posts and Grades");

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

        UpdateListener listener = new UpdateListener();
        new Thread(listener).start();

		//GUI related code running in EDT
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    /*
                        Please replace localhost with the IP address of the server when
                        using this system.
                    */
                    socket = new Socket("localhost", 1234);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
								out.println("signup;" + signupID + ";" + password + ";" +
                                       role + ";" + firstName + ";" + lastName);
								out.flush();
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
									JOptionPane.showMessageDialog(null, "Please make sure entered" +
                                           " ID is an integer", "Error", JOptionPane.ERROR_MESSAGE);
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
							currentPage = "mainMenu";
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

					deleteBoardButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String payload = "deleteBoard;" + currentBoard;
							sendRequest(payload, socket);
							JOptionPane.showMessageDialog(null, "The discussion board has been deleted!",
							       "Board Deleted", JOptionPane.INFORMATION_MESSAGE);
							viewAllCourses();
						}
					});

					viewDashboardButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							viewDashboard("Sort by descending order");
						}
					});

					dashboardBack.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							viewDiscussionPage("DEFAULT");
						}
					});

					sortButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (sortButton.getText().equals("Sort by descending order")) {
								sortButton.getText().equals("Sort by ascending order");
								viewDashboard("Sort by descending order");
							} else {
								sortButton.getText().equals("Sort by descending order");
								viewDashboard("Sort by ascending order");
							}
						}
					});

					addCommentButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean repeat = false;
							do {
								repeat = false;
                                Object[] inputOptions = {"Direct Text", "File Name"};
                                int method = JOptionPane.showOptionDialog(frame,
                                       "How would you like to enter your comment?", "Add a comment",
                                              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                     null, inputOptions, inputOptions[0]);
                                //direct text = 0
                                //file name = 0
                                String content = "";
                                if (method == 0) {
                                    content = JOptionPane.showInputDialog(null, "Type in your comment: ",
    									   "Add a comment", JOptionPane.QUESTION_MESSAGE);
                                    if (content != null && content.equals("")) {
           								JOptionPane.showMessageDialog(null, "Your comment cannot be empty",
           								       "Error", JOptionPane.ERROR_MESSAGE);
           								repeat = true;
           							}
                                    if (content != null) {
                                        String payload = "createComment;" + currentBoard +
                                               ";" + sessionID + ";" + content;
                                        sendRequest(payload, socket);
                                        JOptionPane.showMessageDialog(null, "Your comment has been added.",
                                                "Comment Addded", JOptionPane.INFORMATION_MESSAGE);
                                        viewAllCourses(); //fixes view discussion page bug.
                                        viewDiscussionPage(currentBoard);
                                    }
                                } else if (method == 1) {
                                    String fileContent = "";
                                    try {
                                        String fileName = JOptionPane.showInputDialog(null,
                                               "Enter your file name: ", "Add a comment",
                                                      JOptionPane.QUESTION_MESSAGE);
                                        fileContent = getTextFromFile(fileName);
                                        if (fileContent != null && fileContent.equals("")) {
                                            JOptionPane.showMessageDialog(null, "Your comment cannot be empty",
               								                         "Error", JOptionPane.ERROR_MESSAGE);
               								repeat = true;
                                        } else {
                                            if (fileContent != null) {
                                                String payload = "createComment;" + currentBoard +
                                                       ";" + sessionID + ";" + content;
                                                sendRequest(payload, socket);
                                                JOptionPane.showMessageDialog(null, "Your comment has been added.",
                                                        "Comment Addded", JOptionPane.INFORMATION_MESSAGE);
                                                viewAllCourses(); //fixes view discussion page bug.
                                                viewDiscussionPage(currentBoard);
                                            }
                                        }
                                    } catch (IOException ex) {
                                        JOptionPane.showMessageDialog(null, "File could not be found",
           								                         "Error", JOptionPane.ERROR_MESSAGE);
                                        repeat = true;
                                    }
                                }
							} while (repeat);
						}
					});

					processEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							//Add code to change user info
							String password = passwordChange.getText();
							String firstName = firstNameChange.getText();
							String lastName = lastNameChange.getText();
							String modificationPayload = "editAccount;" + password +
                                   ";" + firstName + ";" + lastName + ";" + sessionID;
							out.println(modificationPayload);
							out.flush();
							String infoMessage = "This is now your saved user information:" +
                                   "\nPassword: " + password + "\nFirst Name: " + firstName
								          + "\nLast Name: " + lastName;
							JOptionPane.showMessageDialog(null, infoMessage,
                                   "Changes Made", JOptionPane.INFORMATION_MESSAGE);
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
										   "\nhas been added to course: " + course,
                                                  "Board Added", JOptionPane.INFORMATION_MESSAGE);
								}
								boardTopicField.setText("");
							}
						}
					});

					delete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int result = JOptionPane.showConfirmDialog(frame, "Are you sure you" +
                                   " want to delete your account?", "Delete Account",
                                          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (result == JOptionPane.NO_OPTION) {
								firstMenu();
							} else {
								frame.dispose();
								String deletionPayload = "deleteAccount;" + sessionID;
								out.println(deletionPayload);
								out.flush();
								JOptionPane.showMessageDialog(null, "Your account has been deleted. " +
									   "Thank you for using our platform!", "Closing System",
                                              JOptionPane.INFORMATION_MESSAGE);
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
							lastSelectedCourse = selectedCourse;
							if (!selectedCourse.equals("")) {
								String[] discussionBoards = findBoardsByCourse(selectedCourse, socket);
								viewBoards(discussionBoards);
							}
						}
					});

					selectBoard.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							viewDiscussionPage("DEFAULT");
						}
					});

					methodChoice.addActionListener(new ActionListener() {
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
									JOptionPane.showMessageDialog(frame, "The course: " +
                                             name + "\nand\nDiscussion Board: " + topic +
										            "\nhave been successfully created", "Course Created",
                                                           JOptionPane.INFORMATION_MESSAGE);
									firstMenu();
								} else {
									try {
										topic = getTextFromFile(topic);
										sendRequest("createCourse;" + name + ";" + topic, socket);
										JOptionPane.showMessageDialog(frame, "The course: " +
                                                 name + "\nand\nDiscussion Board: " + topic +
											            "\nhave been successfully created", "Course Created",
                                                               JOptionPane.INFORMATION_MESSAGE);
										firstMenu();
									} catch (IOException ex) {
										JOptionPane.showMessageDialog(null, "Error reading file!",
												  "Error", JOptionPane.ERROR_MESSAGE);
									}

								}
							}
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
									String gradeAssigned = line.split(";")[3];
									String content = "Course: " + course + "\nForum Topic: " + topic +
										   "\nContent: " + post + "\nCurrent Grade: " + gradeAssigned;
									JOptionPane.showMessageDialog(null, content,
                                             "Post Content", JOptionPane.INFORMATION_MESSAGE);
								} catch (IOException ex) {
									//DO NOTHING
								}
							}
						}
					});

					selectStudent.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							currentPage = "gradeStudentPosts2";
							String idToGrade = (String)studentIDs.getSelectedItem();
							idToGrade = idToGrade.split("ID: ")[1];
							lastIDToGrade = idToGrade;
							String[] postsOfStudent = findPostsByStudent(idToGrade, socket);
							if (postsOfStudent[0].equals("|EMPTY|")) {
								JOptionPane.showMessageDialog(null, "This student has not posted anything yet!",
									   "Error", JOptionPane.ERROR_MESSAGE);
                                grade.setVisible(false);
                                enterGrade.setVisible(false);
                                studentPosts.removeAllItems();
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
                            if (commentID != null || !commentID.equals("")) {
                                Integer gradeAssigned = 0;
    							try {
    								gradeAssigned = Integer.parseInt(grade.getText());
    								if (gradeAssigned < 0 || gradeAssigned > 100) {
    									JOptionPane.showMessageDialog(null, "Please enter an integer" +
                                               " between 0 and 100 inclusive.", "Error",
                                                      JOptionPane.ERROR_MESSAGE);
    								} else {
    									String payload = "gradeComment;" + commentID + ";" + gradeAssigned;
    									sendRequest(payload, socket);
    									String confirmMessage = "The grade for comment ID: " + commentID +
                                               " has been set to: " + gradeAssigned;
    									JOptionPane.showMessageDialog(null, confirmMessage,
                                               "Grade Set!", JOptionPane.INFORMATION_MESSAGE);
    								}
    							} catch (Exception ex) {
    								JOptionPane.showMessageDialog(null, "Please enter a valid integer!",
    										  "Error", JOptionPane.ERROR_MESSAGE);
    							}
                            }
							grade.setText("");
						}
					});
                } catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Cannot establish connection with server." +
					       "\nTerminating Program.", "Error", JOptionPane.ERROR_MESSAGE);
                    running = false;
                }
            }
        });
    }

    /**
     * Real-Time Update Thread Class
     * @author Astrid Popovici, Grant McCord, Jainam Doshi, Kathryn McGregor, Kris Leungwattanakij
     * @version December 11, 2021
     */
    private static class UpdateListener implements Runnable {

        public UpdateListener() {
        }

        public void run() {
            try {
                while (running) {
                    TimeUnit.SECONDS.sleep(5);
                    switch (currentPage) {
                        case "viewDiscussionPage":
                            updateViewDiscussionPage();
                            break;
                        case "viewAllCourses":
                            updateAddBoard(); //addBoard and viewAllCourse use the same combobox
                            break;
						case "viewBoards":
							updateAddBoard();
							updateViewBoards();
							break;
						case "viewDashboard":
							updateViewDashboard();
							break;
                        case "viewPostsAndGrades":
                            updatePostsAndGrades();
                            break;
                        case "gradeStudentPosts1":
                            updateGradeStudentPosts1();
                            break;
						case "gradeStudentPosts2":
                            updateGradeStudentPosts1();
							updateGradeStudentPosts2();
							break;
						case "addBoard":
							updateAddBoard();
							break;
                    }
                }
            } catch (Exception e) {
                //DO NOTHING
            }
        }
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
        frame.add(welcomeMessage1);
        frame.add(welcomeMessage2);
        frame.add(signUpButton);
        frame.add(logInButton);
        frame.pack();
    }

    public static void logIn() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel username = new JLabel("Enter your userID: ");
        logInUserID = new JTextField(15);
        JLabel pass = new JLabel("Enter your password: ");
        logInPassword = new JTextField(15);

        frame.add(username);
        frame.add(logInUserID);
        frame.add(pass);
        frame.add(logInPassword);
        frame.add(firstBack);
        frame.add(logInContinue);

        frame.repaint();
        frame.pack();
    }

    public static void signUp() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(6, 2));

        JLabel idMessage1 = new JLabel("Your new ID is: ");
        int requestedID = requestNewID(socket);

        sessionID = requestedID;

        idMessage2 = new JLabel(String.valueOf(requestedID));
        signupID = requestedID;
        JLabel passMessage = new JLabel("Please enter a password: ");
        signUpPassword = new JTextField(15);
        JLabel firstMessage = new JLabel("Please enter your first name: ");
        signUpFirstName = new JTextField(15);
        JLabel lastMessage = new JLabel("Please enter your last name: ");
        signUpLastName = new JTextField(15);
        JLabel studentTeacher = new JLabel("I am a ");

        frame.add(idMessage1);
        frame.add(idMessage2);
        frame.add(passMessage);
        frame.add(signUpPassword);
        frame.add(firstMessage);
        frame.add(signUpFirstName);
        frame.add(lastMessage);
        frame.add(signUpLastName);
        frame.add(studentTeacher);
        frame.add(combo);
        frame.add(firstBack);
        frame.add(signUpContinue);

        frame.repaint();
        frame.pack();
    }

    public static void firstMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 2));
        frame.add(edit);
        frame.add(delete);
        frame.add(viewCourses);

        if (sessionAuthority) {
            frame.setLayout(new GridLayout(3, 2));
            frame.add(newCourses);
            frame.add(gradePosts);
            frame.add(addBoard);
        } else
            frame.add(viewGrades);

        frame.repaint();
        frame.pack();
    }

    public static void editAccount() {
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
            //DO NOTHING;
        }

        JLabel message = new JLabel("Enter any changes you would like to make in the text fields below. ");
        JLabel empty = new JLabel("");
        JLabel pass = new JLabel("Password: ");
        passwordChange = new JTextField(password);
        JLabel first = new JLabel("First Name: ");
        firstNameChange = new JTextField(firstName);
        JLabel last = new JLabel("Last Name: ");
        lastNameChange = new JTextField(lastName);

        frame.add(message);
        frame.add(empty);
        frame.add(pass);
        frame.add(passwordChange);
        frame.add(first);
        frame.add(firstNameChange);
        frame.add(last);
        frame.add(lastNameChange);
        frame.add(mainBack);
        frame.add(processEdit);

        frame.repaint();
        frame.pack();
    }

    public static void viewAllCourses() {
        currentPage = "viewAllCourses";
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 2));
        String allCourses = "";
        try {
            sendRequest("listAllCourses", socket);
            allCourses = in.readLine();
        } catch (IOException e) {
            //DO NOTHING;
        }
		lastFetchedCourses = allCourses;
        coursesCombo = new JComboBox<String>(allCourses.split(";"));
        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(viewCoursesBack);
        frame.pack();
    }

	public static void updateViewBoards() {
		String selectedCourse = lastSelectedCourse;
		if (!selectedCourse.equals("")) {
			String[] discussionBoards = findBoardsByCourse(selectedCourse, socket);
			if (lastFetchedBoards != discussionBoards) {
				discussionBoardsCombo.removeAllItems();
				for (String board: discussionBoards) {
					discussionBoardsCombo.addItem(board);
				}
			}
		}
	}

    public static void viewBoards(String[] boards) {
		currentPage = "viewBoards";
		lastFetchedBoards = boards;
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));
        discussionBoardsCombo = new JComboBox<String>(boards);
        frame.add(coursesCombo);
        frame.add(selectCourse);
        frame.add(discussionBoardsCombo);
        frame.add(selectBoard);
        frame.add(viewCoursesBack);

        frame.pack();
    }

	public static void updateAddBoard() {
		String allCourses = "";
        try {
            sendRequest("listAllCourses", socket);
            allCourses = in.readLine();
        } catch (IOException e) {
            //DO NOTHING;
        }
		if (!lastFetchedCourses.equals(allCourses)) {
			lastFetchedCourses = allCourses;
			coursesCombo.removeAllItems();
			for (String course: allCourses.split(";")) {
				coursesCombo.addItem(course);
			}
		}
	}

    public static void addBoard() {
		currentPage = "addBoard";
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(8, 1));
        String allCourses = "";
        try {
            sendRequest("listAllCourses", socket);
            allCourses = in.readLine();
        } catch (IOException e) {
            //DO NOTHING;
        }
		lastFetchedCourses = allCourses;
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

	public static void updateGradeStudentPosts1() {
		String[] listOfStudents = listAllStudents(socket);
		if (listOfStudents != lastListOfStudents) {
			lastListOfStudents = listOfStudents;
			studentIDs.removeAllItems();
			for (String studentID: listOfStudents) {
				studentIDs.addItem(studentID);
			}
		}
	}

	public static void updateGradeStudentPosts2() {
		String[] postsOfStudent = findPostsByStudent(lastIDToGrade, socket);
		if (postsOfStudent != lastSelectedStudentPosts) {
			studentPosts.removeAllItems();
			for (String s: postsOfStudent) {
				studentPosts.addItem(s);
			}
		}
	}

    public static void gradeStudentPosts() {
        currentPage = "gradeStudentPosts1";
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 2));

		grade.setText("Enter the grade here");
		String[] listOfStudents = listAllStudents(socket);
		lastListOfStudents = listOfStudents;
		studentIDs = new JComboBox<String>(listOfStudents);
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

	public static void updateViewDashboard() {
		String sortType = "";
		if (sortButton.getText().equals("Sort by descending order")) {
			sortType = "Sort by ascending order";
		} else {
			sortType = ("Sort by descending order");
		}

		String payload = "getPostsOnBoard;" + currentBoard + ";" + sortType;
		sendRequest(payload, socket);
		String dashboardText = "";
		try {
			dashboardText = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		if (!lastDashboardText.equals(dashboardText)) {
			lastDashboardText = dashboardText;
			postLabel.setText(dashboardText);
		}
	}

	public static void viewDashboard(String sortType) {
		currentPage = "viewDashboard";
		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout());

		String boardInfo = findBoardInfo(currentBoard, socket);
		JLabel boardLabel = new JLabel(boardInfo.split(";")[1]);
		boardLabel.setHorizontalAlignment(JLabel.CENTER);
		boardLabel.setFont(new Font("Calibri", Font.PLAIN, 20));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(dashboardBack);
		if (sortType.equals("Sort by descending order")) {
			sortButton.setText("Sort by ascending order");
		} else {
			sortButton.setText("Sort by descending order");
		}
		buttonPanel.add(sortButton);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 1));
		topPanel.add(boardLabel);
		topPanel.add(buttonPanel);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 1));
		String payload = "getPostsOnBoard;" + currentBoard + ";" + sortType;
		sendRequest(payload, socket);
		String dashboardText = "";
		try {
			dashboardText = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		lastDashboardText = dashboardText;
		postLabel.setText(dashboardText);
		mainPanel.add(postLabel);

		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(scrollPane);

		frame.pack();
		frame.setSize(500, 600);
	}

    public static void updateViewDiscussionPage() {
        ArrayList<Comment> boardComments = findCommentsByBoardID(currentBoard, socket);
        boolean comparisonResults = compareBoardComments(boardComments, lastFetchedBoardComments);
        if (!comparisonResults) {
            viewDiscussionPage(currentBoard);
        }
    }

    public static void viewDiscussionPage(String currentBoardID) {
        currentPage = "viewDiscussionPage";
        frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout());
		String boardID = "";
		if (currentBoardID.equals("DEFAULT")) {
			boardID = (String)discussionBoardsCombo.getSelectedItem();
	        boardID = boardID.split(" - ID: ")[1];
			currentBoard = boardID;
		} else {
			boardID = currentBoardID;
		}
        String boardInfo = findBoardInfo(boardID, socket);
        int numberOfComments = Integer.parseInt(boardInfo.split(";")[0]);
        String topic = boardInfo.split(";")[1];
        String dateAndTime = boardInfo.split(";")[2];
        topic = "<html>" + topic + "<br/>" + dateAndTime + "</html>";
        boardTitleLabel.setText(topic);
        ArrayList<Comment> boardComments = findCommentsByBoardID(boardID, socket);
        lastFetchedBoardComments = boardComments;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4 * numberOfComments, 1));
        for (Comment c: boardComments) {
			String commentInfo = "<html>User ID: " + c.getOwnerID() + " | " +
			       c.getDateAndTime() + " | " + c.getLikes() + " votes<br/>"
                          + c.getContent();
			if (c.getRepliesToComment().size() != 0) {
  				commentInfo += createReplyLabel(c.getRepliesToComment());
  			}
			commentInfo += "</html>";
            commentLabel = new JLabel(commentInfo);
            panel.add(commentLabel);
            JPanel commentInteract = new JPanel();
            commentInteract.setLayout(new GridLayout(1, 2));

            replyButton = new JButton("Reply to comment ID: " + c.getCommentID());
			//action listener has to appear here to be unique to each button
			replyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String commentID = "C" + String.valueOf(e).charAt(70);
					Boolean repeat = false;
					do {
						repeat = false;
						String reply = JOptionPane.showInputDialog(null, "Enter your reply:",
                                 "Add Reply", JOptionPane.QUESTION_MESSAGE);
						if (reply != null && reply.equals("")) {
							JOptionPane.showMessageDialog(null, "Your reply cannot be empty",
									  "Error", JOptionPane.ERROR_MESSAGE);
							repeat = true;
						} else {
							if (reply != null) {
                                String replyPayload = "createReply;" + commentID +
                                       ";" + sessionID + ";" + reply;
                                sendRequest(replyPayload, socket);
                                JOptionPane.showMessageDialog(null, "Your reply has been added.",
    									                       "Reply Added!", JOptionPane.INFORMATION_MESSAGE);
								viewAllCourses(); //fixes view discussion page bug.
                                viewDiscussionPage(currentBoard);
							}
						}
					} while (repeat);
				}
			});
            commentInteract.add(replyButton);
            voteButton = new JButton("Vote for comment ID: " + c.getCommentID());
			//action listener has to appear here to be unique to each button
			voteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String commentID = "C" + String.valueOf(e).charAt(70);
					String payload = "voteComment;" + sessionID + ";" + commentID + ";" + currentBoard;
					sendRequest(payload, socket);
					try {
						String response = in.readLine();
						if (response.equals("already voted")) {
							JOptionPane.showMessageDialog(null, "You have already voted on this board!",
									  "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "Your vote has been added.",
									  "Vote Added!", JOptionPane.INFORMATION_MESSAGE);
							viewAllCourses(); //fixes view discussion page bug.
							viewDiscussionPage(currentBoard);
						}
					} catch (IOException ex) {
						//DO NOTHING
					}
				}
			});
			if (!sessionAuthority) {
				commentInteract.add(voteButton);
			}
            panel.add(commentInteract);
        }
        JScrollPane commentsPane = new JScrollPane(panel);
		commentsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel boardInfoPanel = new JPanel();
        boardInfoPanel.setLayout(new GridLayout(2, 1));
        boardInfoPanel.add(boardTitleLabel);
        JPanel boardButtonPanel = new JPanel();
        boardButtonPanel.setLayout(new GridLayout(1, 2));
        boardButtonPanel.add(viewBoardBackButton);
        if (sessionAuthority) {
			boardButtonPanel.setLayout(new GridLayout(1, 3));
            boardButtonPanel.add(viewDashboardButton);
			boardButtonPanel.add(deleteBoardButton);
        } else {
            boardButtonPanel.add(addCommentButton);
        }
        boardInfoPanel.add(boardButtonPanel);
        frame.add(boardInfoPanel, BorderLayout.NORTH);

		boardTitleLabel.setHorizontalAlignment(JLabel.CENTER);
		boardTitleLabel.setFont(new Font("Calibri", Font.PLAIN, 20));

        frame.add(commentsPane, BorderLayout.CENTER);

        frame.repaint();
        frame.pack();
		frame.setSize(500, 600);
    }

    public static void updatePostsAndGrades() {
        String payload = "getPostsAndGrades;" + sessionID;
		sendRequest(payload, socket);
		String postsAndGradesText = "";
		try {
			postsAndGradesText = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
        postsAndGradesLabel.setText(postsAndGradesText);
    }

    public static void viewPostsAndGrades() {
        currentPage = "viewPostsAndGrades";
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
		frame.add(mainBack, BorderLayout.NORTH);

		String payload = "getPostsAndGrades;" + sessionID;
		sendRequest(payload, socket);
		String postsAndGradesText = "";
		try {
			postsAndGradesText = in.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}

        JPanel panel = new JPanel();
		postsAndGradesLabel.setText(postsAndGradesText);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(postsAndGradesLabel);
		panel.add(Box.createVerticalGlue());
        JScrollPane postsAndGrades = new JScrollPane(panel);
        postsAndGrades.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.pack();
		frame.add(postsAndGrades, BorderLayout.CENTER);
        frame.setSize(400, 300);
    }

    public static boolean compareBoardComments(ArrayList<Comment> comments1,
           ArrayList<Comment> comments2) {
        if (comments1.size() != comments2.size()) {
            return false;
        }
        int totalReplies1 = 0;
        int totalVotes1 = 0;
        for (Comment c: comments1) {
            totalReplies1 += c.getRepliesToComment().size();
            totalVotes1 += c.getLikes();
        }
        int totalReplies2 = 0;
        int totalVotes2 = 0;
        for (Comment c: comments2) {
            totalReplies2 += c.getRepliesToComment().size();
            totalVotes2 += c.getLikes();
        }
        return !(totalReplies1 != totalReplies2 || totalVotes1 != totalVotes2);
    }

	public static String createReplyLabel(ArrayList<Comment> replies) {
		String label = "";
		for (Comment r: replies) {
			String line1 = "<br/>&emsp;&emsp;&emsp;User ID: " + r.getOwnerID() +
			       " | " + r.getDateAndTime() + "<br/>";
			String line2 = "&emsp;&emsp;&emsp;" + r.getContent();
			String replyString = line1 + line2;
			label += replyString;
		}
		label = label.substring(0, label.length());
		return label;
	}

    public static void signUp(Integer userID, String firstName, String lastName,
             String password, String role, Socket serverSocket) {
        try {
            PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
            String signUpPayload = "signup;" + userID + ";" + password + ";" + role + ";" + firstName + ";" + lastName;
            pw.println(signUpPayload);
            pw.flush();
        } catch (IOException e) {
            //DO NOTHING;
        }
    }

    public static ArrayList<Comment> findCommentsByBoardID(String boardID, Socket serverSocket) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String payload = "getComments;" + boardID;
            sendRequest(payload, serverSocket);
            String commentArrayString = bfr.readLine();
            String replyArrayString = bfr.readLine();
            if (!commentArrayString.equals("")) {
                String[] commentArray = commentArrayString.split("/br/");
                String[] replyArray = replyArrayString.split("/~/");
                for (int i = 0; i < commentArray.length; i++) {
                    ArrayList<Comment> replies = new ArrayList<>();
                    boolean passed = (!replyArray[i].contains("[EMPTY"));
                    if (passed) {
                        String[] repliesToComment = replyArray[i].split("/br/");
                        for (String replyInfo: repliesToComment) {
                            replies.add(constructReply(replyInfo));
                        }
                    }
                    String commentInfo = commentArray[i];
                    comments.add(constructComment(commentInfo, replies));
                }
            }
        } catch (IOException e) {
            //DO NOTHING;
        }
        return comments;
    }

    public static Comment constructComment(String commentString, ArrayList<Comment> replies) {
        Comment comment = constructReply(commentString);
        comment.setRepliesToComment(replies);
        return comment;
    }

    public static Comment constructReply(String replyString) {
        String[] replyInfo = replyString.split(";");
        String parentID = replyInfo[0];
        String id = replyInfo[1];
        int ownerID = Integer.parseInt(replyInfo[2]);
        String content = replyInfo[3];
        int likes = Integer.parseInt(replyInfo[4]);
        int commentGrade = Integer.parseInt(replyInfo[5]);
        String dateAndTime = replyInfo[6];
        Comment reply = new Comment(parentID, id, ownerID, content,
               likes, commentGrade, dateAndTime);
        return reply;
    }

    public static String findBoardInfo(String boardID, Socket serverSocket) {
        String infoToReturn = "";
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String payload = "boardInfo;" + boardID;
            sendRequest(payload, serverSocket);
            infoToReturn = bfr.readLine();
        } catch (IOException e) {
            //DO NOTHING;
        }
        return infoToReturn;
    }

    public static Integer requestNewID(Socket serverSocket) {
        try {
            PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            //sending a request to the server for a new ID to be assigned
            pw.println("newID");
            pw.flush();
            return Integer.parseInt(bfr.readLine());
        } catch (IOException e) {
            //DO NOTHING;
        }
        return 0;
    }

    public static Boolean logIn(String userID, String password, Socket serverSocket) {
        try {
            PrintWriter pw = new PrintWriter(serverSocket.getOutputStream());
            BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String verificationPayload = "login;" + userID + ";" + password;
            pw.println(verificationPayload);
            pw.flush();
            switch (Integer.parseInt(bfr.readLine())) {
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
            //DO NOTHING;
        }
        return true;
    }

    public static void sendRequest(String requestPayload, Socket serverSocket) {
        try {
            PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
            pw.println(requestPayload);
            pw.flush();
        } catch (IOException e) {
            //DO NOTHING;
        }
    }

	public static String[] findPostsByStudent(String studentID, Socket serverSocket) {
		String postsString = "";
		try {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			String payload = "listAllPostsByStudent;" + studentID;
			sendRequest(payload, serverSocket);
			postsString = bfr.readLine();
            if (postsString.equals("|EMPTY|")) {
                String[] returnArr = {"|EMPTY|"};
                return returnArr;
            }
		} catch (IOException e) {
			//DO NOTHING
		}
		return postsString.split(";");
	}

	public static String[] findBoardsByCourse(String nameOfCourse, Socket serverSocket) {
		String boardsString = "";
		try {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			String payload = "listAllBoards;" + nameOfCourse;
			sendRequest(payload, serverSocket);
			boardsString = bfr.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		return boardsString.split(";");
	}

	public static String[] listAllStudents(Socket serverSocket) {
		String studentsString = "";
		try {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			String payload = "listAllStudents";
			sendRequest(payload, serverSocket);
			studentsString = bfr.readLine();
		} catch (IOException e) {
			//DO NOTHING
		}
		return studentsString.split(";");
	}

    public static boolean areFieldsFull(JTextField[] fields) {
        for (JTextField field: fields) {
            if (((String)field.getText()).equals("")) {
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
