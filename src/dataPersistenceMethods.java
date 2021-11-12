import java.io.*;
import java.util.*;
public class dataPersistenceMethods {

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

    //serializes the student objects and stores them in a txt file
    public static void saveStudents(ArrayList<Student> students, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
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
            for (int i = 0; i < teachers.size(); i++) {
                out.writeObject(teachers.get(i));
            }
        } catch (Exception e) {
            System.out.println("Failed to save teachers to file!");
        }
    }

    //deserializes board objects from the txt file and returns an arraylist of the board objects
    public static ArrayList<Board> readBoard(String fileName) {
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

    //deserializes student objects from the txt file and returns an arraylist of the student objects
    public static ArrayList<Student> readStudents(String fileName) {
        ArrayList<Student> students = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                Student student = (Student) in.readObject();
                students.add(student);
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!")
        }
        return students;
    }

    //deserializes teacher objects from the txt file and returns an arraylist of the teacher objects
    public static ArrayList<Teacher> readTeachers(String fileName) {
        ArrayList<Teacher> teachers = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                Teacher teacher = (Teacher) in.readObject();
                teachers.add(teacher);
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!")
        }
        return teachers;
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
