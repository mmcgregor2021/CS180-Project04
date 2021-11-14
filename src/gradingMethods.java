import java.util.ArrayList;

public class gradingMethods {

    //returns an ArrayList of comments to a discussion board made by a specific student
    public static ArrayList<Comment> sortCommentByStudentID(int id, ArrayList<Comment> comments) {
        ArrayList<Comment> arr = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getOwnerID() == id) {
                arr.add(comments.get(i));
            }
        }
        return arr;
    }

    // obtains arraylist of their comments and then displays the date and time of their comment with the grade associated with it
    public void displayGrades(int id, ArrayList<Comment> comments) {
        ArrayList<Comment> user = sortCommentByStudentID(id, comments);
        for (int i = 0; i < user.size(); i++) {
            String grade = String.format("Date: %s - Grade: %d /100", user.get(i).getDateAndTime(), user.get(i).getGrade());
            System.out.println(grade);
        }
    }

}
