import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    private static ArrayList<Comment> comments = readComments("comments.txt");
    private static ArrayList<Board> boards = readBoards("boards.txt");
    ArrayList<String> courses = populateCourses(boards);
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

                //TO DO
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
							//new ID assignment request
							case "newID":
								//locking personCounter for modification
								synchronized (counters) {
									//counter[0] is personCounter
									counters[0]++;
									out.println(counters[0]);
								}
                                //TODO create course
                            case "createCourse":
                                String name = line.split(";")[1];
                            case "createBoard":
                                String course = line.split(";")[1];
                                String topic = line.split(";")[2];
                                String boardID = line.split(";")[3];
                                String dateAndTime = line.split(";")[4];
                                synchronized (boards) {
                                    boards.add(new Board(course, topic, boardID, dateAndTime));
                                    saveBoards(boards, "boards.txt");
                                }
                            case "createComment":
                                String parentID = line.split(";")[1];
                                String commentID = line.split(";")[1];
                                int ownerID = Integer.parseInt(line.split(";")[1]);
                                String content = line.split(";")[1];
                                int likes = Integer.parseInt(line.split(";")[1]);
                                int grade = Integer.parseInt(line.split(";")[1]);
                                String commentDateAndTime = line.split(";")[1];
                                synchronized (comments) {
                                    comments.add(new Comment(parentID, commentID, ownerID, content, likes, grade, commentDateAndTime));
                                    saveComments(comments, "comments.txt");
                                }

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

    //TODO replace print statements with JOptionPane windows
    public static void logIn(Integer userID, String password, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String verificationPayload = "login;" + userID + ";" + password;
            out.println(verificationPayload);
            out.flush();
            switch(Integer.parseInt(in.readLine())) {
                //TO DO: replace print statements below with JOptionPane messages
                case 1:
                    System.out.println("ID DOESN'T EXIST");
                    break;
                case 2:
                    System.out.println("WRONG PASSWORD");
                    break;
                case 3:
                    System.out.println("SUCCESFUL LOGIN");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
