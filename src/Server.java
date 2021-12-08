import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    private static ArrayList<Comment> comments = readComments("comments.txt");
    private static ArrayList<Board> boards = readBoards("boards.txt");
    public static ArrayList<String> courses = populateCourses(boards);
    private static ArrayList<Teacher> teachers = readTeachers("teachers.txt");
    private static ArrayList<Student> students = readStudents("students.txt");
    private HashMap<Integer, Person> users = populateHashMap();
    private static Integer[] counters = readCounters("counters.txt");

    public static void main(String[] args) {
        ServerSocket server = null;
        System.out.println("Server Starting up...");
        try {
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            while (true) {
                Socket client = server.accept();
                System.out.println("Client connected: " + client.getInetAddress().getHostAddress());

                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                /*
                TODO
                View dashboard //idk if we have to do this in server or GUI side

                */
				while (true) {
					String line = in.readLine();
					while (line != null) {
						switch (line.split(";")[0]) {
							//login request
							case "login":
								Integer userID = Integer.parseInt(line.split(";")[1]);
								String password = line.split(";")[2];
								Integer loginResult = logIn(userID, password, students, teachers);
								out.println(loginResult);
								break;
							//signup request
							case "signup":
								Integer assignedID = Integer.parseInt(line.split(";")[1]);
								String assignedPassword = line.split(";")[2];
								String assignedRole = line.split(";")[3];
								String assignedFirstName = line.split(";")[4];
								String assignedLastName = line.split(";")[5];
								if (assignedRole.equals("Student")) {
									//locking students ArrayList for modification
									synchronized (students) {
										students.add(new Student(assignedFirstName, assignedLastName, assignedPassword, assignedID));
										saveStudents(students, "students.txt");
									}
								} else {
									//locking teachers ArrayList for modification
									synchronized (teachers) {
										teachers.add(new Teacher(assignedFirstName, assignedLastName, assignedPassword, assignedID));
										saveTeachers(teachers, "teachers.txt");
									}
								}
								break;
                            //requesting to modify fields of a student or teacher object
                            case "editAccount":
                                String newPassword = line.split(";")[1];
                                String newFirstName = line.split(";")[2];
                                String newLastName = line.split(";")[3];
                                Integer givenID = Integer.parseInt(line.split(";")[4]);
                                synchronized (students) {
                                    for (Student s: students) {
                                        if(s.getID() == givenID) {
                                            s.setFirstName(newFirstName);
                                            s.setLastName(newLastName);
                                            s.setPassword(newPassword);
                                        }
                                    }
                                    saveStudents(students, "students.txt");
                                }
                                synchronized(teachers) {
                                    for (Teacher t: teachers) {
                                        if(t.getID() == givenID) {
                                            t.setFirstName(newFirstName);
                                            t.setLastName(newLastName);
                                            t.setPassword(newPassword);
                                        }
                                    }
                                    saveTeachers(teachers, "teachers.txt");
                                }
                                break;
							//new ID assignment request
							case "newID":
								//locking personCounter for modification
								synchronized (counters) {
									//counter[0] is personCounter
									counters[0]++;
									out.println(counters[0]);
                                    saveCounters(counters, "counters.txt");
								}
                                break;
                                //TODO create course
                            case "createCourse":
                                String courseName = line.split(";")[1];
                                String boardTopic = line.split(";")[2];
                                String idOfBoard;
                                synchronized (courses) {
                                    courses.add(courseName);
                                }
                                synchronized (counters) {
                                    counters[1]++;
                                    idOfBoard = "B" + counters[1];
                                    saveCounters(counters, "counters.txt");
                                }
                                synchronized (boards) {
                                    Date date = new Date();
                                    boards.add(new Board(courseName, boardTopic,
                                           idOfBoard, date.toString()));
                                    saveBoards(boards, "boards.txt");
                                }
                                break;
                            case "createBoard":
								String boardID = "";
                                String course = line.split(";")[1];
                                String topic = line.split(";")[2];
								synchronized (counters) {
									counters[1]++;
									boardID = "B" + counters[1];
									saveCounters(counters, "counters.txt");
								}
                                Date boardCreationDate = new Date();
                                synchronized (boards) {
                                    boards.add(new Board(course, topic, boardID, boardCreationDate.toString()));
                                    saveBoards(boards, "boards.txt");
                                }
                                break;
                            case "createComment":
                                String parentID = line.split(";")[1];
                                String commentID = line.split(";")[2];
                                int ownerID = Integer.parseInt(line.split(";")[3]);
                                String content = line.split(";")[4];
                                int likes = Integer.parseInt(line.split(";")[5]);
                                int grade = Integer.parseInt(line.split(";")[6]);
                                String commentDateAndTime = line.split(";")[7];
                                synchronized (comments) {
                                    comments.add(new Comment(parentID, commentID, ownerID, content, likes, grade, commentDateAndTime));
                                    saveComments(comments, "comments.txt");
                                }
                                break;
                            //requesting session name and password from server
                            case "sessionVariable":
                                String theUserID = line.split(";")[1];
                                //search through student and teacher arraylists for someone with the given userID
                                for (Student s: students) {
                                    if(s.getID() == Integer.parseInt(theUserID)) {
                                        out.println(s.getFirstName() + ";" + s.getLastName() + ";" + s.getPassword() + ";Student");
                                        out.flush();
                                    }
                                }
                                for (Teacher t: teachers) {
                                    if(t.getID() == Integer.parseInt(theUserID)) {
                                        out.println(t.getFirstName() + ";" + t.getLastName() + ";" + t.getPassword() + ";Teacher");
                                        out.flush();
                                    }
                                }
                                break;
                            case "deleteAccount":
                                Integer idToDelete = Integer.parseInt(line.split(";")[1]);
                                synchronized (students) {
                                    for (int i = 0; i < students.size(); i++) {
                                        if (students.get(i).getID() == idToDelete) {
                                            students.remove(i);
                                        }
                                    }
                                    saveStudents(students, "students.txt");
                                }
                                synchronized (teachers) {
                                    for (int i = 0; i < teachers.size(); i++) {
                                        if (teachers.get(i).getID() == idToDelete) {
                                            teachers.remove(i);
                                        }
                                    }
                                    saveTeachers(teachers, "teachers.txt");
                                }
                                break;

                            case "addTopic": //assigns/edits topic for a current board
                                String newTopic = line.split(";")[1];
                                String currentBoardID = line.split(";")[2];
                                for (int i = 0; i < boards.size(); i++) {
                                    if (currentBoardID.equals(boards.get(i).getBoardID()))
                                        boards.get(i).setTopic(newTopic);
                                }
								break;
                            case "deleteBoard": //deletes board and all comments associated with board
                                String deleteBoardID = line.split(";")[1];
                                synchronized(comments) {
                                    for (int j = 0 ; j < comments.size(); j++) {
                                        if (comments.get(j).getParentID().equals(deleteBoardID)) {
                                            comments.remove(j);
                                        }
                                    }
                                    saveComments(comments, "comments.txt");
                                }
                                synchronized(boards) {
                                    for (int i = 0; i < boards.size(); i++) {
                                        if (boards.get(i).getBoardID().equals(deleteBoardID)) {
                                            boards.remove(i);
                                        }
                                    }
                                    saveBoards(boards, "boards.txt");
                                }
                                break;
                            case "listAllCourses":
                                String coursesToReturn = "";
                                if (courses.size() != 0) {
                                    for (String c: courses) {
                                        coursesToReturn += c + ";";
                                    }
                                    coursesToReturn = coursesToReturn.substring(0, coursesToReturn.length() - 1);
                                }
                                out.println(coursesToReturn);
                                out.flush();
                                break;
							case "listAllBoards":
								String boardsToReturn = "";
								String selectedCourse = line.split(";")[1];
								for (Board b: boards) {
									if (b.getCourse().equals(selectedCourse)) {
										boardsToReturn += b.getTopic() + ";";
									}
								}
								boardsToReturn = boardsToReturn.substring(0, boardsToReturn.length() - 1);
								out.println(boardsToReturn);
								out.flush();
								break;
							//sends back a string of all student ids
							case "listAllStudents":
								String studentsToReturn = "";
								for (Student s: students) {
									studentsToReturn += s.getFirstName() + " - ID: " + s.getID() + ";";
								}
								studentsToReturn = studentsToReturn.substring(0, studentsToReturn.length() - 1);
								out.println(studentsToReturn);
								out.flush();
								break;
							case "listAllPostsByStudent":
								String postsToReturn = "";
								if (comments.size() == 0) {
									postsToReturn = "|EMPTY|";
								} else {
									Integer requestedStudentID = Integer.parseInt(line.split(";")[1]);
									for (Comment c: comments) {
										if (c.getOwnerID() == requestedStudentID) {
											postsToReturn += c.getCommentID() + ";";
										}
									}
									postsToReturn = postsToReturn.substring(0, postsToReturn.length() - 1);
								}
								out.println(postsToReturn);
								out.flush();
								break;
							case "commentContent":
								String cContent = "";
								Integer cGrade = 0;
								String cCourse = "";
								String cTopic = "";
								for (Comment c: comments) {
									if (c.getCommentID().equals(line.split(";")[1])) {
										cContent = c.getContent();
										cGrade = c.getGrade();
										for (Board b: boards) {
											if (b.getBoardID().equals(c.getParentID())) {
												cCourse = b.getCourse();
												cTopic = b.getTopic();
											}
										}
									}
								}
								String contentToReturn = cCourse + ";" + cTopic + ";" + cContent + ";" + cGrade;
								out.println(contentToReturn);
								out.flush();
								break;
                            case "voteComment": // adding a vote to specific comment through comment ID and sessionID of user
                                int boardIndex = Integer.parseInt(line.split(";")[1]);
                                int commentIndex = Integer.parseInt(line.split(";")[2]);
                                int sessionID = Integer.parseInt(line.split(";")[3]);
                                ArrayList<Comment> currentBoardComments = boards.get(boardIndex - 1).getComments();
                                int currentNumVotes = currentBoardComments
												       .get(commentIndex).getLikes();
								currentBoardComments.get(commentIndex)
												         .setLikes(currentNumVotes + 1);
                                boards.get(boardIndex - 1).setComments(currentBoardComments);
                                boards.get(boardIndex - 1).addUsersWhoVoted(sessionID);
								break;
                            case "replyComment": // code to reply to a comment// NOT SURE IF THIS CODE IS VALID
                                boardIndex = Integer.parseInt(line.split(";")[1]);
                                commentIndex = Integer.parseInt(line.split(";")[2]);
                                sessionID = Integer.parseInt(line.split(";")[3]);
                                String replyToComment = line.split(";")[4];
                                currentBoardComments = boards.get(boardIndex - 1).getComments();
                                parentID = currentBoardComments.get(commentIndex).getCommentID();
                                Date replyDate = new Date();
								String replyID = "Reply - NO ID";
							    Comment createdReply = new Comment(parentID, replyID,
								       sessionID, replyToComment, 0, 0, replyDate.toString());
								currentBoardComments.get(commentIndex)
								       .getRepliesToComment().add(createdReply);
								boards.get(boardIndex - 1).setComments(currentBoardComments);
							case "gradeComment":
								String selectedCommentID = line.split(";")[1];
								int gradeAssigned = Integer.parseInt(line.split(";")[2]);
								synchronized (comments) {
									commentSearchingLoop:
									for (Comment c: comments) {
										if (c.getCommentID().equals(selectedCommentID)) {
											c.setGrade(gradeAssigned);
											saveComments(comments, "comments.txt");
											break commentSearchingLoop;
										}
									}
								}
								break;

						}
						//resetting line to null, so requests do not get spammed
						line = null;
					}
				}
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getSessionVariable(String payload, ArrayList<Student> students, ArrayList<Teacher> teachers) {
        Integer sentSessionID = Integer.parseInt(payload.split(";")[1]);
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == sentSessionID) {
                String firstName = students.get(i).getFirstName();
                String lastName = students.get(i).getLastName();
                String password = students.get(i).getPassword();
                return firstName + ";" + lastName + ";" + password;
            }
        }
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getID() == sentSessionID) {
                String firstName = teachers.get(i).getFirstName();
                String lastName = teachers.get(i).getLastName();
                String password = teachers.get(i).getPassword();
                return firstName + ";" + lastName + ";" + password;
            }
        }
        return "";
    }

    //takes the inputted id and password and checks to see if the login was successful
    //returns either 1, 2, or 3 based on the result of the login operation
    public static int logIn(int id, String password, ArrayList<Student> students, ArrayList<Teacher> teachers) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == id) {
                if (students.get(i).getPassword().equals(password)) {
                    return 3; //correct login
                }
                return 2; //wrong password
            }
        }

        for (int j = 0; j < teachers.size(); j++) {
            if (teachers.get(j).getID() == id) {
                if (teachers.get(j).getPassword().equals(password)) {
                    return 3; //correct login
                }
                return 2; //wrong password
            }
        }
        return 1; //id does not exist
    }

    public HashMap<Integer, Person> populateHashMap() {
        HashMap<Integer, Person> users = new HashMap<>();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            Integer studentID = student.getID();
            users.put(studentID, student);
        }
        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            Integer teacherID = teacher.getID();
            users.put(teacherID, teacher);
        }
        return users;
    }

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
    public static void saveCounters(Integer[] counters, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
			Integer personCounter = counters[0];
			Integer boardCounter = counters[1];
			Integer commentCounter = counters[2];
            pw.println(personCounter + ";" + boardCounter + ";" + commentCounter);
        } catch (Exception e) {
            System.out.println("Failed to save counter!");
        }
    }

    //reads all three counters from the txt file to an Integer array
    //Integer Array Format: [personCounter, boardCounter, commentCounter]
    public static Integer[] readCounters(String fileName) {
        Integer[] arr = new Integer[3];
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

}
