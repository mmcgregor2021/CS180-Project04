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

}
