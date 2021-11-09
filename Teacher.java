/**
 * A Teacher Class that extends the Person Class
 * @author Kris Leungwattanakij
 * @version November 8, 2021
 **/
public class Teacher extends Person {

    public Teacher(String firstName, String lastName, String password, int id) {
        super(firstName, lastName, password, id);
    }

    public boolean getAuthority() {
        return true;
    }

}
