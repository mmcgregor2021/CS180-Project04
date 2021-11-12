import java.io.*;
import java.util.*;
public class dataPersistenceMethods {

    //deserializes board objects from the txt file and returns an arraylist of the board objects
    public static ArrayList<Board> readBoards(String fileName) {
        ArrayList<Board> boards = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                Board board = (Board) in.readObject();
                boards.add(board);
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!")
        }
        return boards;
    }

    //serializes the board objects and stores them in a txt file
    public static void saveBoards(ArrayList<Board> boards, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (int i = 0; i < boards.size(); i++) {
                out.writeObject(boards.get(i));
            }
        } catch (Exception e) {
            System.out.println("Failed to save boards to file!");
        }
    }

    //stores each student object as a line in a txt file in format:
    //firstName;lastName;password;id
    public static void saveStudents(ArrayList<Student> arr, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < arr.size(); i++) {
                Student student = arr.get(i);
                String firstName = student.getFirstName();
                String lastName = student.getLastName();
                String password = student.getPassword();
                int id = student.getID();
                String line = firstName + ";" + lastName + ";" + password + ";" + id;
                pw.println(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to save students to file!");
        }
    }

    //stores each teacher object as a line in a txt file in format:
    //firstName;lastName;password;id
    public static void saveTeachers(ArrayList<Teacher> arr, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < arr.size(); i++) {
                Teacher teacher = arr.get(i);
                String firstName = teacher.getFirstName();
                String lastName = teacher.getLastName();
                String password = teacher.getPassword();
                int id = teacher.getID();
                String line = firstName + ";" + lastName + ";" + password + ";" + id;
                pw.println(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to save teachers to file!");
        }
    }

    //reads student objects from the txt file created by saveStudents() and returns and ArrayList of students
    public static ArrayList<Student> readStudents(String fileName) {
        ArrayList<Student> arr = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] lineArr = line.split(";");
                String firstName = lineArr[0];
                String lastName = lineArr[1];
                String password = lineArr[2];
                int id = Integer.parseInt(lineArr[3]);
                arr.add(new Student(firstName, lastName, password, id));
                line = bfr.readLine();
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    //reads teacher objects from the txt file created by saveTeachers() and returns and ArrayList of teachers
    public static ArrayList<Teacher> readTeachers(String fileName) {
        ArrayList<Teacher> arr = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] lineArr = line.split(";");
                String firstName = lineArr[0];
                String lastName = lineArr[1];
                String password = lineArr[2];
                int id = Integer.parseInt(lineArr[3]);
                arr.add(new Teacher(firstName, lastName, password, id));
                line = bfr.readLine();
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    //saves all three counters to one line separated by ';' to a txt file
    public static void saveCounters(int personCounter, int boardCounter, int commentCounter, String fileName) {
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
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    //Main method for testing purposes
    public static void main(String[] args) {
    }
}
