/**
 * A Student Class that extends the Person Class
 * @author Kris Leungwattanakij
 * @version November 8, 2021
 **/
public class Student extends Person implements java.io.Serializable {

    public Student(String firstName, String lastName, String password, int id) {
        super(firstName, lastName, password, id);
    }

    //students don't have the authority to perform certain actions
    public boolean getAuthority() {
        return false;
    }

}
