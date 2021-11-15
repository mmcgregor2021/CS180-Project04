import java.util.*;
import java.io.*;
/**
 * A learning management discussion board system
 * @author Astrid Popovici, Grant McCord, Jainam Doshi, Kathryn McGregor, Kris Leungwattanakij
 * @version November 8, 2021
 */
public class Control {

	public static String grabCommentIndexAndID(Scanner scan, ArrayList<Comment> currentBoardComments) {
		int indexOfParentComment = 0;
		String parentCommentID = "";
		while (true) {
			System.out.println("Please enter the id of your selected comment.");
			parentCommentID = scan.nextLine();
			if (parentCommentID.equals("")) {
				System.out.println("The comment id cannot be blank.");
			} else {
				for (int i = 0; i < currentBoardComments.size(); i++) {
					parentCommentID = parentCommentID.toUpperCase();
					if (parentCommentID.equals(currentBoardComments.get(i).getCommentID())) {
						return i + " " + parentCommentID;
					}
				}
				System.out.println("Inputted comment ID cannot be found. Please try again.");
			}
		}
	}

	public static String grabSessionName(int sessionID, ArrayList<Student> students,
	       ArrayList<Teacher> teachers) {
		String firstName = "";
		String lastName = "";
		if (students.size() > 0) {
			for (int i = 0; i < students.size(); i++) {
				if (sessionID == students.get(i).getID()) {
					firstName = students.get(i).getFirstName();
					lastName = students.get(i).getLastName();
					return firstName + " " + lastName;
				}
			}
		}
		for (int i = 0; i < teachers.size(); i++) {
			if (sessionID == teachers.get(i).getID()) {
				firstName = teachers.get(i).getFirstName();
				lastName = teachers.get(i).getLastName();
			}
		}
		return firstName + " " + lastName;
	}

	public static String inputInfo(Scanner scan) {
		boolean again = false;
		String part1 = "Would you like to input through the terminal or with a file path?\n";
		String part2 = "(t for terminal, anything else for file path)";
		System.out.println(part1 + part2);
		String response = scan.nextLine();
		String content = "";
		if (response.equals("t")) {
			while (true) {
				System.out.println("What would you like to input?");
				content = scan.nextLine();
				if (content.equals("")) {
					System.out.println("Comment cannot be empty!");
				} else {
					break;
				}
			}
		} else {
			String format1 = "**Please only have 1 line in your txt file ";
			String format2 = "with either the forum topic or comment content**";
			System.out.println(format1 + format2);
			String fileName = "";

			while (true) {
				while (true) {
					System.out.println("What is the file name for your input?");
					fileName = scan.nextLine();
					if (fileName.equals("")) {
						System.out.println("File name cannot be empty. Please try again.");
					} else {
						break;
					}
				}
				try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
					String line = bfr.readLine();
					content = line;
					if (content.equals("")) {
						System.out.println("File content cannot be empty. Please try again.");
					} else {
						break;
					}
				} catch (FileNotFoundException e) {
					System.out.println("File not found. Please try again.");
				} catch (Exception e) {
					System.out.println("Issue processing file. Please try again.");
				}
			}
		}
		return content;
	}

	//prompts user to enter information and returns a new board object
	public static Board createBoard(Scanner scan, String courseName, int boardCounter) {
		System.out.println("Please enter the forum topic.");
		String topic = inputInfo(scan);
		while (true) {
			if (topic.equals("")) {
				System.out.println("Please enter a non-blank forum topic.");
			} else {
				break;
			}
		}
		ArrayList<Comment> boardComments = new ArrayList<Comment>();
		Date date = new Date();
		String boardID = "B" + boardCounter;
		Board board = new Board(courseName, topic, boardID, date.toString(), boardComments);
		System.out.println("Board was successfully created.");

		return board;
	}


	//returns an arraylist of every unique course in String format
	public static ArrayList<String> populateCourses(ArrayList<Board> boards) {
		ArrayList<String> courses = new ArrayList<>();
		for (int i = 0; i < boards.size(); i++) {
			Board board = boards.get(i);
			if (!courses.contains(board.getCourse())) {
				courses.add(board.getCourse());
			}
		}
		return courses;
	}

    public static void logOut(ArrayList<Student> students, ArrayList<Teacher> teachers, ArrayList<Board> boards,
	       ArrayList<Comment> comments, int personCounter, int boardCounter, int commentCounter) {
        saveStudents(students, "students.txt");
        saveTeachers(teachers, "teachers.txt");
		saveBoards(boards, "boards.txt");
		saveComments(comments, "comments.txt");
        saveCounters(personCounter, boardCounter, commentCounter, "counters.txt");
    }

    public static int logIn(int id, String password, ArrayList<Student> students,
	       ArrayList<Teacher> teachers) {
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

	// Beginning of Data Persistence Methods

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

	// End of Data Persistence Methods

    public static void main(String[] args) {

		final String invalidOption = "Please enter a valid option.";
		final String notInteger = "Please enter an integer.";

		ArrayList<Board> boards = readBoards("boards.txt");
		ArrayList<String> courses = populateCourses(boards);
		ArrayList<Comment> comments = readComments("comments.txt");
        ArrayList<Student> students = readStudents("students.txt");
        ArrayList<Teacher> teachers = readTeachers("teachers.txt");

        int[] counterArray = readCounters("counters.txt");
        int personCounter = counterArray[0];
        int boardCounter = counterArray[1];
        int commentCounter = counterArray[2];

        Scanner scan = new Scanner(System.in);
        int sessionID = 0; // ID number of the current logged in user
		boolean sessionAuthority = false; // Authority of current logged in user
        int input;
        int id = 0;
        String password = "";
        String first = "";
        String last = "";
        boolean access = false;
        System.out.println("Welcome to the Discussion Board! What would you like to do?");

		authenticationLoop:
        while (true) {
			System.out.println("1. Sign up\n2. Log in");
            try {
                input = Integer.parseInt(scan.nextLine());
                if (input == 1 || input == 2) {
                    break authenticationLoop;
                } else {
                    System.out.println("Please select a valid option");
                }
            } catch (NumberFormatException e) {
                System.out.println(notInteger);
            }
        }

        //Beginning of Signup route
        if (input == 1) {
			id = personCounter + 1;
			personCounter++;
            System.out.println("Your UserID is " + id + ". Please remember this number!");

            System.out.println("Please enter a password.");
            while (true) {
                password = scan.nextLine();
                if (password.length() != 0) {
                    break;
                } else {
                    System.out.println("Please enter a non-blank password.");
                }
            }

            System.out.println("Please enter your first name.");
            while (true) {
                first = scan.nextLine();
                if (first.length() != 0) {
                    break;
                } else {
                    System.out.println("You must have a first name.");
                }
            }

            System.out.println("Please enter your last name.");
            while (true) {
                last = scan.nextLine();
                if (last.length() != 0) {
                    break;
                } else {
                    System.out.println("You must have a last name.");
                }
            }

            System.out.println("Are you a teacher? (y for yes, anything else for no)");
            if (scan.nextLine().equals("y")) {
                teachers.add(new Teacher(first, last, password, id));
				sessionAuthority = true;
            } else {
                students.add(new Student(first, last, password, id));
				sessionAuthority = false;
            }
            System.out.println("Successfully Signed up");
            access = true;
            sessionID = id;
        //End of Signup Route

		//Beginning of Login route
        } else if (input == 2) {
            //checks login info
            System.out.println("Please enter your ID number.");
            while (true) {
                try {
                    id = Integer.parseInt(scan.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid ID number");
                }
            }

            System.out.println("Please enter your password.");
            while (true) {
                password = scan.nextLine();
                if (password.length() != 0) {
                    break;
                } else {
                    System.out.println("Please enter a valid password.");
                }
            }

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

					//Checks to see if user is a teacher or a student
					//if user is a student, sessionAuthority = false
					//if user is a teacher, sessionAuthority = true
			        sessionAuthority = false;
			        for (int i = 0; i < teachers.size(); i++) {
			            if (sessionID == teachers.get(i).getID()) {
			                sessionAuthority = true;
			        	}
					}
                    break;
        }
	}
	//End of Login route

    //main loop once logged in
		if (access) {

			String sessionName = grabSessionName(sessionID, students, teachers);

            mainLoop:
            do {
				System.out.println("\nUser: " + sessionName);
                System.out.println("What would you like to do?");
				String choicesPrompt = "1. Edit account\n2. Delete account\n3. View courses\n4. Logout";
				if (sessionAuthority) {
					choicesPrompt += "\n5. Create new course\n6. Grade student posts";
				} else
					choicesPrompt += "\n5. View your comments and their grades";
                System.out.println(choicesPrompt);

                while (true) {
                    try {
                        input = Integer.parseInt(scan.nextLine());
                        if (input >= 1 && input <= 6) {
                            break;
                        } else {
                            System.out.println(invalidOption);
                            System.out.println(choicesPrompt);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(notInteger);
                        System.out.println(choicesPrompt);
                    }
                }

                if (input == 1) {
                    //edit account
                    editLoop:
                    do {
                        System.out.println("Which field would you like to modify?");
                        System.out.println("1. password\n2. first name\n3. last name\n4. go back");
                        int editChoice = Integer.parseInt(scan.nextLine());
                        switch (editChoice) {
                            case 1:
                                System.out.println("Please enter a new password.");
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
                                System.out.println("Please enter a new first name.");
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
                                System.out.println("Please enter a new last name.");
                                do {
                                    last = scan.nextLine();
                                    if (first.length() != 0) {
                                        for (int i = 0; i < students.size(); i++) {
                                            if (students.get(i).getID() == sessionID) {
                                                students.get(i).setLastName(last);
                                            }
                                        }
                                        for (int i = 0; i < teachers.size(); i++) {
                                            if (teachers.get(i).getID() == sessionID) {
                                                teachers.get(i).setLastName(last);
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
						sessionName = grabSessionName(sessionID, students, teachers);
                    } while (true);
                } else if (input == 2) {
                    //delete account
					String deleteAccountConfirm = "Are you sure you would like to delete your account?";
                    System.out.println(deleteAccountConfirm + "(y for yes, anything else for no)");
                    if (scan.nextLine().equals("y")) {
						if (students.size() != 0 ) {
	                        for (int i = 0; i < students.size(); i++) {
	                            if (students.get(i).getID() == sessionID) {
	                                students.remove(i);
									System.out.println("Your account has been deleted!");
	                                break mainLoop; //logs user out after deleting their account
	                            }
	                        }
						}
                        for (int i = 0; i < teachers.size(); i++) {
                            if (teachers.get(i).getID() == sessionID) {
                                students.remove(i);
                                break mainLoop; //logs user out after deleting their account
                            }
                        }
                    }

				//viewing boards
                } else if (input == 3) {
					boardViewingLoop:
					while (true) {
						//Display all courses.
						boolean again;
						int courseSelection = 0;
						do {
							System.out.println("Select one of the following options");
							again = false;
							if (courses.size() == 0) {
								System.out.println("No courses have been created yet.");
							}
							for (int i = 0; i < courses.size(); i++) {
								System.out.println((i + 1) + ". " + courses.get(i));
							}
							System.out.println((courses.size() + 1) + ". Go back");
							try {
								courseSelection = Integer.parseInt(scan.nextLine());
								if (courseSelection == courses.size() + 1) {
									break boardViewingLoop;
								}
							} catch (Exception e) {
								System.out.println(notInteger);
								again = true;
							}
							if (courseSelection > courses.size() + 1 && sessionAuthority) {
								System.out.println(invalidOption);
								again = true;
							} else if (courseSelection > courses.size() && !sessionAuthority) {
								System.out.println(invalidOption);
								again = true;
							}
						} while (again);

						int boardSelection = 0;
						String selectedCourse = null;

						//Display the boards for the selected course.
						boardSelectionLoop:
						while (true) {
							int counter = 1;
							do {
								counter = 1;
								again = false;
								if (courseSelection < courses.size() + 1) {
									System.out.println("Select one of the following options: ");
									selectedCourse = courses.get(courseSelection - 1);
									for (int i = 0; i < boards.size(); i++) {
										if (boards.get(i).getCourse().equals(selectedCourse)) {
											System.out.println(counter + ". Discussion Board: " + boards.get(i).getTopic());
											counter++;
										}
									}
								}

								//Give teachers the option to add a board or view all the comments of a specific student.
								if (sessionAuthority) {
									System.out.println((counter) + ". Add a board");
									counter++;
								}
								System.out.println((counter) + ". Go back");

								//Give error message if user enters an invalid number.
								try {
									boardSelection = Integer.parseInt(scan.nextLine());
									if (boardSelection == counter) {
										break boardSelectionLoop;
									}
								} catch (Exception e) {
									System.out.println(notInteger);
									again = true;
								}
								if (boardSelection > boards.size() + 2 && sessionAuthority) {
									System.out.println(invalidOption);
									again = true;
								} else if (boardSelection > boards.size() + 1 && !sessionAuthority) {
									System.out.println(invalidOption);
									again = true;
								}
							} while (again);

							//Create a new board
							if (boardSelection == boards.size() + 1 && sessionAuthority) {
								System.out.println("Creating a new board: ");
								boardCounter++;
								boards.add(createBoard(scan, selectedCourse, boardCounter));
							}

							//Print all comments on a board.
							int commentSelection = 0;
							if (boardSelection <= boards.size()) {
								System.out.println(boards.get(boardSelection - 1).toString());
								if (boards.get(boardSelection - 1).getComments().size() == 0) {
									System.out.println("There are no comments on this board yet.\n");
								}

								while (true) {
									try {
										if (sessionAuthority) {
											System.out.println("1. Edit forum topic\n2. Delete board\n3. Reply to a comment\n4. View dashboard\n5. Go back");
										} else {
											System.out.println("1. Add comment\n2. Vote for a comment\n3. Reply to a comment\n4. Go back");
										}
										commentSelection = Integer.parseInt(scan.nextLine());
										break;
									} catch (NumberFormatException e) {
										System.out.println(notInteger);
									}
								}

								ArrayList<Comment> currentBoardComments = boards.get(boardSelection - 1).getComments();
								int indexOfParentComment = 0;
								String replyToComment = "";
								String parentCommentID = "";
								if (sessionAuthority) {
									//switch statement for teachers
									switch (commentSelection) {
										case 1:
											String newTopic = "";
											while (true) {
												newTopic = inputInfo(scan);
												boards.get(boardSelection - 1).setTopic(newTopic);
												System.out.println("New forum topic has been set!");
												break;
											}
										case 2:
											System.out.println("Are you sure you would like to delete the board?\n(y for yes, anything else for no)");
											String deleteBoardConfirmation = scan.nextLine();
											if (deleteBoardConfirmation.equals("y")) {
												String deletedBoardID = boards.get(boardSelection - 1).getBoardID();
												for (int i = 0; i < comments.size(); i++) {
													if (comments.get(i).getParentID().equals(deletedBoardID)) {
														comments.remove(i);
													}
												}
												boards.remove(boardSelection - 1);
												System.out.println("The board and all of its comments were successfully deleted!");
											}
											break;
										case 3:
											//code to reply to a comment
											if (currentBoardComments.size() == 0) {
												System.out.println("Sorry. This board does not have any comments yet.");
											} else {
												String[] indexAndID = grabCommentIndexAndID(scan, currentBoardComments).split(" ");
												indexOfParentComment = Integer.parseInt(indexAndID[0]);
												parentCommentID = indexAndID[1];
												while (true) {
													System.out.println("Please enter a reply.");
													replyToComment = scan.nextLine();
													if (replyToComment.equals("")) {
														System.out.println("Reply cannot be empty.");
													} else {
														break;
													}
												}
												Date teacherReplyDate = new Date();
												String teacherReplyID = "Teacher Reply - NO ID";
												Comment createdTeacherReply = new Comment(parentCommentID, teacherReplyID, sessionID,
												       "Teacher Reply - " + replyToComment, 0, 0, teacherReplyDate.toString());
												currentBoardComments.get(indexOfParentComment).getRepliesToComment().add(createdTeacherReply);
												boards.get(boardSelection - 1).setComments(currentBoardComments);
												System.out.println("Reply successfully addded.");
											}
											break;
										case 4:
											if (currentBoardComments.size() == 0) {
												System.out.println("Sorry. This board does not have any comments yet.");
											} else {
												System.out.println("Would you like to view the comments in ascending or descending order?");
												System.out.println("(a for ascending, anything else for descending)");
												String sortSelection = scan.nextLine();
												boolean done = true;
												ArrayList<Comment> sortedComments = new ArrayList<>();
												for (int x = 0; x < currentBoardComments.size(); x++) {
													sortedComments.add(currentBoardComments.get(x));
												}
												for (int x = 1; x < currentBoardComments.size(); x++) {
													done = true;
													for (int y = 0; y < currentBoardComments.size() - 1 - x; y++) {
														if (sortedComments.get(y).getLikes() < sortedComments.get(y + 1).getLikes()) {
															done = false;
															Collections.swap(sortedComments, y, y + 1);
														}
													}
													if (done) {
														break;
													}
													sortedComments.add(currentBoardComments.get(x));
												}
												if (!sortSelection.equals("a")) {
													for (int x = 0; x < sortedComments.size(); x++) {
														System.out.println(sortedComments.get(x).dashboardToString(students));
													}
												} else {
													Collections.reverse(sortedComments);
													for (int x = 0; x < sortedComments.size(); x++) {
														System.out.println(sortedComments.get(x).dashboardToString(students));
													}
												}
											}
										case 5:
											//code to go back
											break;
										default:
											System.out.println(invalidOption);
											break;
									}
								} else {
									//switch statement for students
									switch (commentSelection) {
										case 1:
											//code to add a comment
											String content = inputInfo(scan);
											String parentBoardID = boards.get(boardSelection - 1).getBoardID();
											commentCounter++;
											Date commentDate = new Date();
											String commentID = "C" + commentCounter;
											Comment createdComment = new Comment(parentBoardID, commentID, sessionID,
											       content, 0, 0, commentDate.toString());
											boards.get(boardSelection - 1).getComments().add(createdComment);
											comments.add(createdComment);
											System.out.println("Comment was successfully created!\n");
											break;
										case 2:
											//code to vote on a comment
											if (currentBoardComments.size() == 0) {
												System.out.println("Sorry. This board does not have any comments yet.");
											}
											else if (boards.get(boardSelection - 1).getUsersWhoVoted().contains(sessionID)) {
												System.out.println("Sorry. You have already voted on this board.");
											} else {
												String[] commentIndexIDArray = grabCommentIndexAndID(scan, currentBoardComments).split(" ");
												int indexOfSelectedComment = Integer.parseInt(commentIndexIDArray[0]);
												int currentNumVotes = currentBoardComments.get(indexOfSelectedComment).getLikes();
												currentBoardComments.get(indexOfSelectedComment).setLikes(currentNumVotes + 1);
												boards.get(boardSelection - 1).setComments(currentBoardComments);
												boards.get(boardSelection - 1).addUsersWhoVoted(sessionID);
												System.out.println("Vote was successfully added.");
											}
											break;
										case 3:
											//code to reply to a comment
											if (currentBoardComments.size() == 0) {
												System.out.println("Sorry. This board does not have any comments yet.");
											} else {
												String[] commentIndexAndID = grabCommentIndexAndID(scan, currentBoardComments).split(" ");
												indexOfParentComment = Integer.parseInt(commentIndexAndID[0]);
												parentCommentID = commentIndexAndID[1];
												while (true) {
													System.out.println("Please enter a reply.");
													replyToComment = scan.nextLine();
													if (replyToComment.equals("")) {
														System.out.println("Reply cannot be empty.");
													} else {
														break;
													}
												}
												Date replyDate = new Date();
												String replyID = "Reply - NO ID";
												Comment createdReply = new Comment(parentCommentID, replyID,
												       sessionID, replyToComment, 0, 0, replyDate.toString());
												currentBoardComments.get(indexOfParentComment).getRepliesToComment().add(createdReply);
												boards.get(boardSelection - 1).setComments(currentBoardComments);
												System.out.println("Reply successfully addded.");
											}
											break;
										case 4:
											//code to go back
											break;
										default:
											System.out.println(invalidOption);
											break;
									}
								}
								System.out.println("Redirecting you back to the previous menu.\n");
							}
						}
					}
                } else if (input == 5) {
					if (sessionAuthority) {
						System.out.println("Please enter the name of the new course.");
						String courseName = "";
						while (true) {
							courseName = scan.nextLine();
							if (courseName.equals("")) {
								System.out.println("Please enter a non-blank course name.");
							} else {
								break;
							}
						}
						System.out.println("The course has been created!");
						courses.add(courseName);
						boardCounter++;
						System.out.println("Please initialize a discussion board for the course.");
						boards.add(createBoard(scan, courseName, boardCounter));
					} else {
						//Student views all their posts and their grades.
						ArrayList<Comment> studentComments = new ArrayList<>();
						for (int i = 0; i < comments.size(); i++) {
							if (comments.get(i).getOwnerID() == sessionID) {
								studentComments.add(comments.get(i));
							}
						}
						if (studentComments.size() == 0) {
							System.out.println("You have not posted to any boards yet.");
						} else {
							System.out.println("These are all the comments for ID " + sessionID + "\n");
							for (int i = 0; i < studentComments.size(); i++){
								System.out.println(studentComments.get(i).toString(boards));
							}
						}
						System.out.println("Press enter to return to menu.");
						String returnToMenu = scan.nextLine();
					}
				} else if (input == 6 && sessionAuthority) {
					//Print all the comments from a specific student.
					if (comments.size() == 0) {
						System.out.println("There are no comments that have been posted to any boards yet.");
					} else {
						int studentID = 0;
						studentIDLoop:
						while (true) {
							System.out.println("Enter the student ID of the student whose comments you want to grade.");
							try {
								studentID = Integer.parseInt(scan.nextLine());
								for (int i = 0; i < students.size(); i++) {
									if (students.get(i).getID() == studentID) {
										break studentIDLoop;
									}
								}
								System.out.println("Inputted student ID cannot be found. Please try again.");
							} catch (Exception e) {
								System.out.println(notInteger);
							}
						}

						System.out.println("These are all the comments for ID " + studentID);
						for (int i = 0; i < comments.size(); i++) {
							if (comments.get(i).getOwnerID() == studentID) {
								System.out.println(comments.get(i).toString(boards));
							}
						}

						//Code to assign a grade to each student comment
						for (int i = 0; i < comments.size(); i++) {
							if (comments.get(i).getOwnerID() == studentID) {
								System.out.println("What grade would you like to assign to comment " + (i + 1) + "? (0-100)");
								String gradeRepeat1 = "If a grade has already been assigned and you do not wish to change it,";
								String gradeRepeat2 = " type in the same grade.";
								System.out.println(gradeRepeat1 + gradeRepeat2);
								int grade = 0;
								while (true) {
									try {
										grade = Integer.parseInt(scan.nextLine());
										if (grade < 0 || grade > 100) {
											System.out.println("Invalid grade, please try again");
										} else {
											break;
										}
									} catch (Exception e) {
										System.out.println("Invalid grade, please try again");
									}
								}
								comments.get(i).setGrade(grade);
								System.out.println("The grade is set to " + comments.get(i).getGrade());
							}
						}
					}
				}
            } while (input != 4);
		}
            System.out.println("Goodbye! Have a nice day!");
            logOut(students, teachers, boards, comments, personCounter, boardCounter, commentCounter);
        }
    }
