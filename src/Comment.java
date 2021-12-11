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
    private ArrayList<Comment> repliesToComment;

    public Comment(String parentID, String commentID, int ownerID, String content, int likes,
           int grade, String dateAndTime, ArrayList<Comment> repliesToComment) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.ownerID = ownerID;
        this.content = content;
        this.likes = likes;
        this.grade = grade;
        this.dateAndTime = dateAndTime;
        this.repliesToComment = repliesToComment;
    }

    public Comment(String parentID, String commentID, int ownerID, String content,
           int likes, int grade, String dateAndTime) {
        this.parentID = parentID;
        this.commentID = commentID;
        this.ownerID = ownerID;
        this.content = content;
        this.likes = likes;
        this.grade = grade;
        this.dateAndTime = dateAndTime;
        ArrayList<Comment> commentReplies = new ArrayList<>();
        this.repliesToComment = commentReplies;
    }

    public String toString(ArrayList<Board> boards) {
        String course = "";
        String topic = "";
        for (int i = 0; i < boards.size(); i++) {
            if (parentID.equals(boards.get(i).getBoardID())) {
                course = boards.get(i).getCourse();
                topic = boards.get(i).getTopic();
                break;
            }
        }
        String part1 = "Course: " + course + "\n";
        String part2 = "Forum Topic: " + topic + "\n";
        String part3 = "Comment: " + content + "\n";
        String part4 = "Grade Assigned: " + grade + "\n";
        if (grade == 0) {
            part4 += "(a grade of 0 could mean that the comment has not been graded yet)\n";
        }
        return part1 + part2 + part3 + part4;
    }

    public String dashboardToString(ArrayList<Student> students) {
        String name = "";
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getID() == ownerID) {
                name = student.getFirstName() + " " + student.getLastName();
            }
        }
        String part1 = "Student Name: " + name + "\n";
        String part2 = "Vote Count: " + likes + "\n";
        String part3 = "Comment: " + content + "\n";
        String part4 = "Time Posted: " + dateAndTime + "\n";

        return part1 + part2 + part3 + part4;
    }

	//adds a vote/like to a comment
    public void addLike() {
        likes++;
    }

    public void createReplyToComment(String theCommentID, int theOwnerID,
           String theContent, int theLikes, String theDateAndTime) {
        repliesToComment.add(new Comment(this.commentID, theCommentID, theOwnerID, theContent, theLikes, 0,
                theDateAndTime));
    }

    public void deleteReplyToComment(String idOfComment) {
        for (Comment c: repliesToComment) {
            if (c.getCommentID().equals(idOfComment)) {
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

    public ArrayList<Comment> getRepliesToComment() {
        return repliesToComment;
    }

    public void setRepliesToComment(ArrayList<Comment> repliesToComment) {
        this.repliesToComment = repliesToComment;
    }
}
