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
    private LocalTime timeStamp;
    private ArrayList<Person> usersWhoLiked;

    public Comment(String parentID, int ownerID, String content) {
        this.parentID = parentID;
        //TODO: generate comment ID
        this.commentID = "0";
        this.ownerID = ownerID;
        this.content = content;
        this.likes = 0;
        this.timeStamp = java.time.LocalTime.now();
        this.usersWhoLiked = new ArrayList<Person>();
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

    public LocalTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Person> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(ArrayList<Person> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }
}