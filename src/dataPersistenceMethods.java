import java.io.*;
import java.util.*;
public class dataPersistenceMethods {

    //reads board objects from txt file and returns and ArrayList with all of them
    //Important: This method can only be called after readComments() as the comments ArrayList is a required parameter
    public static ArrayList<Board> readBoards(String fileName, ArrayList<Comment> comments) {
        ArrayList<Board> arr = new ArrayList<>();
        ArrayList<String> sArr = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while(line != null) {
                sArr.add(line);
                line = bfr.readLine();
            }

            String boardFields = "";
            String commentIDs = "";
            ArrayList<Comment> boardComments = new ArrayList<>();
            for (int i = 0; i < sArr.size(); i++) {
                if ((i + 1) % 2 == 0) {
                    commentIDs = sArr.get(i);
                    String[] fieldsArr = boardFields.split(";");
                    String[] commentIDArr = commentIDs.split("|");

                    String course = fieldsArr[0];
                    String topic = fieldsArr[1];
                    String boardID = fieldsArr[2];
                    String dateAndTime = fieldsArr[3];

                    //puts all the comments that belong to the board in boardComments
                    for (int x = 0; x < commentIDArr.length; x++) {
                        String currentID = commentIDArr[x];
                        for (int y = 0; y < comments.size(); y++) {
                            if (currentID.equals(comments.get(i).getCommentID())) {
                                Comment matchedComment = comments.get(i);
                                boardComments.add(matchedComment);
                                comments.remove(i);
                                break;
                            }
                        }
                    }
                    //end of comment matching proccess

                    Board board = new Board(course, topic, boardID, dateAndTime, boardComments);
                    arr.add(board);

                } else {
                    boardFields = sArr.get(i);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to parse text file!");
        }
        return arr;
    }

    //stores each board object as two lines in atxt file in format:
    //course;topic;boardID;dateAndTime
    //commentID1|commentID2|commentID3...
    public static void saveBoards(ArrayList<Board> arr, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < arr.size(); i++) {
                Board board = arr.get(i);
                String course = board.getCourse();
                String topic = board.getTopic();
                String boardID = board.getBoardID();
                String dateAndTime = board.getDateAndTime();
                ArrayList<Comment> comments = board.getComments();
                String commentIDS = "";

                //grabbing the commentID of each comment obj in comments and putting it in commentIDS
                for (int x = 0; x < comments.size(); x++) {
                    Comment comment = comments.get(x);
                    commentIDS += comment.getCommentID() + "|";
                }
                commentIDS = commentIDS.substring(0, commentIDS.length() - 1); //truncating the final '|'

                String line1 = course + ";" + topic + ";" + boardID + ";" + dateAndTime;
                String line = line1 + "\n" + commentIDS;

                pw.println(line);
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
