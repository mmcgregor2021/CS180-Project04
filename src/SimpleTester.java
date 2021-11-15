import java.util.Date;
import java.util.ArrayList;

public class SimpleTester {
    public static void main(String[] args) {
        //Test LogInTest
        LogInTest logIn = new LogInTest();
        logIn.signUp("purdue", "pete", "passw0rd", 1, 1);
        System.out.println(logIn.getStudents()); //should have one object
        System.out.println(logIn.getTeachers()); //should be empty
        System.out.println(logIn.getStudents().get(0).getLastName()); //should be pete
        System.out.println(logIn.logIn(1, "passw0rd")); //should print 3
        System.out.println(logIn.logIn(1, "password")); //should print 2
        System.out.println(logIn.logIn(2, "password")); //should print 1
        logIn.change("purdue", "paul", "pa$sword", 1);
        System.out.println(logIn.getStudents().get(0).getLastName()); //should be paul
        //before we delete paul from the list, add him to usersWhoLiked for later
        ArrayList<Person> usersWhoLiked = new ArrayList<Person>();
        usersWhoLiked.add(logIn.getStudents().get(0));
        logIn.delete(1, logIn.getStudents(), logIn.getTeachers());
        System.out.println(logIn.getStudents()); //should be empty

        //Test Comment and Board
        Date date = new Date();
        ArrayList<Integer> usersWhoVoted = new ArrayList<Integer>();
        usersWhoVoted.add(1);

        ArrayList<Comment> repliesToComment = new ArrayList<Comment>();
        Comment r = new Comment("C1", "C2", 1,
                "Idk man", 3, 0, date.toString());
        repliesToComment.add(r);

        Comment c = new Comment("B1", "C1", 2,
                "How do we complete project 4 pls help", 1, 95, date.toString(),
                usersWhoLiked, repliesToComment);

        ArrayList<Comment> commentList = new ArrayList<Comment>();
        commentList.add(c);

        Board b = new Board("CS 180", "Project 4", "B1", date.toString(),
                commentList, usersWhoVoted);
        System.out.println(b);
    }
}