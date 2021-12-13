import java.net.*;
import java.io.*;
import java.util.*;
/**
 * Multi-threaded server for a learning management discussion board system
 * @author Astrid Popovici, Grant McCord, Jainam Doshi, Kathryn McGregor, Kris Leungwattanakij
 * @version December 11, 2021
 */
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
                out = new PrintWriter(clientSocket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				while (true) {
                    String selectedBoardID = "";
					String line = in.readLine();
					while (line != null) {
						switch (line.split(";")[0]) {
							//login request
							case "login":
								Integer userID = Integer.parseInt(line.split(";")[1]);
								String password = line.split(";")[2];
								Integer loginResult = logIn(userID, password, students, teachers);
								out.println(loginResult);
								out.flush();
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
									out.flush();
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
                                int ownerID = Integer.parseInt(line.split(";")[2]);
                                String content = line.split(";")[3];
                                String commentID = "";
                                Date commentDate = new Date();
                                synchronized (counters) {
                                    counters[2]++;
                                    commentID = "C" + counters[2];
                                    saveCounters(counters, "counters.txt");
                                }
                                synchronized (comments) {
                                    comments.add(new Comment(parentID, commentID, ownerID,
                                           content, 0, 0, commentDate.toString()));
                                    saveComments(comments, "comments.txt");
                                }
                                break;
							case "createReply":
								String parentCommentID = line.split(";")[1];
                                int replyOwnerID = Integer.parseInt(line.split(";")[2]);
								String replyToComment = line.split(";")[3];
								Date replyDate = new Date();
								synchronized (comments) {
									for (Comment c: comments) {
										if (c.getCommentID().equals(parentCommentID)) {
											c.getRepliesToComment().add(new Comment(parentCommentID,
                                                   "REPLY", replyOwnerID, replyToComment, 0, 0,
                                                          replyDate.toString()));
                                            break;
										}
									}
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
										boardsToReturn += b.getTopic() + " - ID: " + b.getBoardID() + ";";
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
                                    if (postsToReturn.equals("")) {
                                        postsToReturn = "|EMPTY|";
                                    } else {
                                        postsToReturn = postsToReturn.substring(0, postsToReturn.length() - 1);
                                    }
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
								int voterID = Integer.parseInt(line.split(";")[1]);
								String voteCommentID = line.split(";")[2];
								String voteBoardID = line.split(";")[3];
								boolean alreadyVoted = false;
								//checking to see if user has already voted; if yes, sets alreadyVoted to true.
								synchronized (boards) {
									for (Board b: boards) {
										if (b.getBoardID().equals(voteBoardID)) {
											if (b.getUsersWhoVoted().contains(voterID)) {
												alreadyVoted = true;
											} else {
												b.getUsersWhoVoted().add(voterID);
												saveBoards(boards, "boards.txt");
											}
											break;
										}
									}
								}
								if (!alreadyVoted) {
									synchronized (comments) {
										for (Comment c: comments) {
											if (c.getCommentID().equals(voteCommentID)) {
												c.addLike();
												break;
											}
										}
										saveComments(comments, "comments.txt");
										out.println("voted");
										out.flush();
									}
								} else {
									out.println("already voted");
									out.flush();
								}
								break;
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
                            case "boardInfo":
                                selectedBoardID = line.split(";")[1];
                                for (Board b: boards) {
                                    if (b.getBoardID().equals(selectedBoardID)) {
                                        String infoToReturn = b.getComments().size() +
                                               ";" + b.getTopic() + ";" + b.getDateAndTime();
                                        out.println(infoToReturn);
                                        out.flush();
                                        break;
                                    }
                                }
                                break;
							case "getPostsOnBoard":
								postsToReturn = "<html>";
								String postBoardID = line.split(";")[1];
								String sortType = line.split(";")[2];
								ArrayList<Comment> unsortedComments = new ArrayList<>();
								for (Comment c: comments) {
                                    if (c.getParentID().equals(postBoardID)) {
                                        unsortedComments.add(c);
                                    }
                                }
								//sorts by descending by default
								ArrayList<Comment> sortedComments = sortComments(unsortedComments);
								if (sortType.equals("Sort by ascending order")) {
									Collections.reverse(sortedComments);
								}
								for (Comment c: sortedComments) {
									String postContent = c.getContent();
									String postDate = c.getDateAndTime();
									int postVotes = c.getLikes();
									int idOfPoster = c.getOwnerID();
									postsToReturn += "Comment: " + postContent + "<br/>" + "Votes: "
								           + postVotes + "<br/>" + "Student ID: " + idOfPoster +
										          "<br/>" + "Date Posted: " + postDate + "<br/><br/>";
								}
								postsToReturn += "</html>";

								out.println(postsToReturn);
								out.flush();
								break;
                            case "getComments":
                                selectedBoardID = line.split(";")[1];
                                String commentArrayString = "";
                                String replyArrayString = "";
								for (Comment c: comments) {
                                    if (c.getParentID().equals(selectedBoardID)) {
                                        String replyString = "";
                                        commentArrayString += deconstructComment(c) + "/br/";
                                        if (c.getRepliesToComment().size() == 0) {
                                            replyString = "[EMPTY]";
                                        } else {
                                            for (Comment r: c.getRepliesToComment()) {
                                                replyString += deconstructComment(r) + "/br/";
                                            }
                                            replyString = replyString.substring(0,
                                                   replyString.length() - 4);
                                        }
                                        replyArrayString += replyString + "/~/";
                                    }
                                }
                                if (!commentArrayString.equals("")) {
                                    commentArrayString = commentArrayString.substring(0,
                                           commentArrayString.length() - 4);
                                }
                                if (!replyArrayString.equals("")) {
                                    replyArrayString = replyArrayString.substring(0,
                                           replyArrayString.length() - 4);
                                }
                                out.println(commentArrayString);
                                out.flush();
                                out.println(replyArrayString);
                                out.flush();
                                break;
							case "getPostsAndGrades":
								postsToReturn = "<html>";
								int postStudentID = Integer.parseInt(line.split(";")[1]);

								for (Comment c: comments) {
									if (c.getOwnerID() == postStudentID) {
										String postComment = c.getContent();
										int postGrade = c.getGrade();
										String[] boardAndCourseName = findBoardAndCourseName(boards,
										       c.getParentID());
										String postCourse = boardAndCourseName[0];
										String postTopic = boardAndCourseName[1];
										postsToReturn += "Course: " + postCourse + "<br/>" + "Topic: "
									           + postTopic + "<br/>" + "Comment: " + postComment +
											          "<br/>" + "Grade Assigned: " + postGrade + "<br/><br/>";
									}
								}
								postsToReturn += "</html>";
								out.println(postsToReturn);
								out.flush();
								break;
						}
						//resetting line to null, so requests do not get spammed
						line = null;
					}
				}
            } catch (IOException e) {
                //DO NOTHING
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

	public static ArrayList<Comment> sortComments(ArrayList<Comment> comments) {
		boolean done = true;
		ArrayList<Comment> currentBoardComments = comments;
		ArrayList<Comment> sortedComments = currentBoardComments;
		for (int x = 1; x < currentBoardComments.size(); x++) {
			done = true;
			for (int y = 0; y < currentBoardComments.size() - 1 - x; y++) {
				if (sortedComments.get(y).getLikes()
					   < sortedComments.get(y + 1).getLikes()) {
					done = false;
					Collections.swap(sortedComments, y, y + 1);
				}
			}
			if (done) {
				break;
			}
			sortedComments.add(currentBoardComments.get(x));
		}
		return sortedComments;
	}

	public static String[] findBoardAndCourseName(ArrayList<Board> boards,
	       String boardID) {
		String[] boardAndCourseName = new String[2];
		for (Board b: boards) {
			if (b.getBoardID().equals(boardID)) {
				boardAndCourseName[0] = b.getCourse();
				boardAndCourseName[1] = b.getTopic();
				break;
			}
		}
		return boardAndCourseName;
	}

    public static String deconstructComment(Comment comment) {
        //dc = deconstructed comment
        String dc = "";
        String parentID = comment.getParentID();
        dc += parentID + ";";
        String id = comment.getCommentID();
        dc += id + ";";
        int ownerID = comment.getOwnerID();
        dc += ownerID + ";";
        String content = comment.getContent();
        dc += content + ";";
        int likes = comment.getLikes();
        dc += likes + ";";
        int grade = comment.getGrade();
        dc += grade + ";";
        String dateAndTime = comment.getDateAndTime();
        dc += dateAndTime;
        return dc;
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
