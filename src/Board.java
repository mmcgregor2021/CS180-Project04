import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

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
    private Date dateAndTime;
    private ArrayList<Comment> comments;

    public Board(String course, String owner, String topic, String boardID, Date dateAndTime, ArrayList<Comment> comments) {
        this.course = course;
        this.owner = owner;
        this.topic = topic;
        this.boardID = boardID;
        this.dateAndTime = dateAndTime;
        this.comments = comments;
    }

    //this creates a comment on the board itself (first level comment)
    public void createComment(int ownerID, String commentID, String content, int likes, Date theDateAndTime,
                              ArrayList<Person> usersWhoLiked, ArrayList<Comment> repliesToComment) {
        comments.add(new Comment(this.boardID, commentID, ownerID, content, likes, theDateAndTime,
                usersWhoLiked, repliesToComment));
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
        toReturn += "\n" + topic + " | " + dateAndTime;
        //then print each comment in the array that has a board ID as parent
        //below each comment, print the replies to that comment
        //TODO
        for (int i = 0; i < comments.size(); i++) {

        }

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

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
