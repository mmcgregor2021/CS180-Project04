import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    ArrayList<Comment> comments = readComments("comments.txt");
    ArrayList<Board> boards = readBoards("boards.txt");
    private static ArrayList<Teacher> teachers = readTeachers("teachers.txt");
    private static ArrayList<Student> students = readStudents("students.txt");
    HashMap<Integer, Person> users = populateHashMap();
    int personCounter = readCounters("counters.txt")[0];
    int boardCounter = readCounters("counters.txt")[1];
    int commentCounter = readCounters("counters.txt")[2];

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

        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //TO DO
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
						case "logout":
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
							break;
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
                }
                catch (IOException e) {
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


}
