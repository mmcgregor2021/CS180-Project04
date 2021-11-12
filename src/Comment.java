import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Defines a comment on a discussion board.
 * @author Astrid Popovici
 * @version November 8, 2021
 */
public class Comment {
    private String parentID;
    private String commentID;
    private int ownerID;
    private String content;
    private int likes;
    private String dateAndTime;

    public Comment(String parentID, String commentID, int ownerID, String content, int likes, String dateAndTime) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.ownerID = ownerID;
        this.content = content;
        this.likes = likes;
        this.dateAndTime = dateAndTime;
    }

    public void addLike() {
        likes++;
    }

//commented out for now
/**
    public void createReplyToComment(String theCommentID, int theOwnerID, String theContent,
                                     int theLikes, String theDateAndTime, ArrayList<Person> theUsersWhoLiked) {
        repliesToComment.add(new Comment(this.commentID, theCommentID, theOwnerID, theContent, theLikes,
                theDateAndTime, theUsersWhoLiked, null));
    }

    public void deleteReplyToComment(String commentID) {
        for (Comment c: repliesToComment) {
            if(c.getCommentID().equals(commentID)) {
                repliesToComment.remove(c);
            }
        }
    }
**/

    //getters and setters
    public String getParentID() {
        return parentID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
