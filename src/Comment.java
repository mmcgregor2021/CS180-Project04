import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Defines a comment on a discussion board.
 * @author Astrid Popovici
 * @version November 8, 2021
 */
public class Comment implements java.io.Serializable {
    private String parentID;
    private String commentID;
    private int ownerID;
    private String content;
    private int likes;
    private int grade;
    private String dateAndTime;
    private ArrayList<Person> usersWhoLiked;
    private ArrayList<Comment> repliesToComment;

    public Comment(String parentID, String commentID, int ownerID, String content, int likes, int grade, String dateAndTime,
                   ArrayList<Person> usersWhoLiked, ArrayList<Comment> repliesToComment) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.ownerID = ownerID;
        this.content = content;
        this.likes = likes;
        this.grade = grade;
        this.dateAndTime = dateAndTime;
        this.usersWhoLiked = usersWhoLiked;
        this.repliesToComment = repliesToComment;
    }

    public Comment(String parentID, String commentID, int ownerID, String content, int likes, int grade, String dateAndTime, ArrayList<Person> usersWhoLiked) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.ownerID = ownerID;
        this.content = content;
        this.likes = likes;
        this.grade = grade;
        this.dateAndTime = dateAndTime;
        this.usersWhoLiked = usersWhoLiked;
        ArrayList<Comment> repliesToComment = new ArrayList<>();
        this.repliesToComment = repliesToComment;
    }

    public Comment(String parentID, String commentID, int ownerID, String content, int likes, int grade, String dateAndTime) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.ownerID = ownerID;
        this.content = content;
        this.likes = likes;
        this.grade = grade;
        this.dateAndTime = dateAndTime;
        ArrayList<Person> usersWhoLiked = new ArrayList<>();
        ArrayList<Comment> repliesToComment = new ArrayList<>();
        this.usersWhoLiked = usersWhoLiked;
        this.repliesToComment = repliesToComment;
    }

    public String toString() {
        String string = ("Content of the comment: " + content + "\n Current grade on the comment: " + grade);
        return string;
    }

    //returns true if the like was added successfully, and false if the
    //passed in user has already liked the comment
    public boolean addLike(Person user) {
        if (usersWhoLiked.contains(user)) {
            return false;
        } else {
            likes++;
            usersWhoLiked.add(user);
            return true;
        }
    }

    public void createReplyToComment(String theCommentID, int theOwnerID, String theContent,
                                     int theLikes, String theDateAndTime, ArrayList<Person> theUsersWhoLiked) {
        repliesToComment.add(new Comment(this.commentID, theCommentID, theOwnerID, theContent, theLikes, 0,
                theDateAndTime, theUsersWhoLiked));
    }

    public void deleteReplyToComment(String commentID) {
        for (Comment c: repliesToComment) {
            if(c.getCommentID().equals(commentID)) {
                repliesToComment.remove(c);
            }
        }
    }

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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public ArrayList<Person> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(ArrayList<Person> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }

    public ArrayList<Comment> getRepliesToComment() {
        return repliesToComment;
    }

    public void setRepliesToComment(ArrayList<Comment> repliesToComment) {
        this.repliesToComment = repliesToComment;
    }
}
