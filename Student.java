public class Student extends Person {

    public Student(String firstName, String lastName, String password, int id) {
        super(firstName, lastName, password, id);
    }

    //students don't have the authority to perform certain actions
    public getAuthority() {
        return false;
    }

}
