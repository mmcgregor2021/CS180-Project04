import java.util.ArrayList;
/**
 * LogIn class that deals with log in info and changing of those settings
 * @author Jainam Doshi
 * @version November 9, 2021
 **/
public class LogInTest {
    
    ArrayList<Student> students = new ArrayList<Student>();
    ArrayList<Teacher> teachers = new ArrayList<Teacher>();

    public LogInTest() {

    }
    public static void main(String[] args) {
        LogInTest obj = new LogInTest();
        obj.run();
    }

    public void run() {
        
    }

    /**
    * Parses through arraylist for both teacher and student and returns a boolean
    * based on wether or not a matching id and password was found within known accounts
    * @param  id  integer to identify the account
    * @param  password password to verify correct user entry
    * @return      return 1 if id incorrect, return 2 if pass is incorrect, return 3 if it all is correct
    */
    public int logIn(int id, String password) {

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == id) {
                if (students.get(i).getPassword().equals(password)) {
                    return 3;
                }

                return 2;
            }
           
        }

        for (int j = 0; j <teachers.size(); j++) {
            if (teachers.get(j).getID() == id) {
                if (teachers.get(j).getPassword().equals(password)) {
                    return 3;
                }

                return 2;
            }

        }

        return 1;
    }
    /**
    * Creates a new teacher of student object based on sign in options and adds it to the arraylist
    * @param  firstName  first name of user
    * @param  lastName   last name of user
    * @param  password   desird passcode for new user
    * @param  id         id for new user
    * @param  option     1 or 2 depending on if the new user is a teacher of a student
    * @return      boolean prompting user successful or failure of login
    */
    public void signUp(String firstName, String lastName, String password, int id, int option) {

        if (option == 1) {
            students.add( new Student(firstName, lastName, password, id));
        } else {
            teachers.add( new Teacher(firstName, lastName, password, id));
        }
    }

    /**
    * Creates a new teacher of student object based on sign in options and adds it to the arraylist
    * @param  id         id for new user
    * @return      boolean prompting user successful or failure of login
    */
    public void delete(int id) {

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == id) {
                students.remove(i);
            }
        }

        for (int j = 0; j < teachers.size(); j++) {
            if (teachers.get(j).getID() == id) {
                teachers.remove(j);
            }
        }
    }

    
}
