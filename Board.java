import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Defines a discussion board.
 * @author Astrid Popovici
 * @version November 8, 2021
 */

public class Board {
    private String course;
    private String owner;
    private String topic;
    private String boardID;
    private ArrayList<Comment> comments;

    public Board(String course, String owner, String topic) {
        this.course = course;
        this.owner = owner;
        this.topic = topic;
    }

    //this creates a comment on the board itself
    public void createComment(int ownerID, String content) {
        comments.add(new Comment(this.boardID, ownerID, content));
    }

    public void deleteComment(String commentID) {
        for (Comment c: comments) {
            if(c.getCommentID().equals(commentID)) {
                comments.remove(c);
            }
        }
    }

    public String toString() {
        String toReturn = "";
        //start by printing the board topic
        //then print each comment in the array that has a board ID as parent
        //below each comment, print the replies to that comment

        return toReturn;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
