public class Person {

    private String firstName;
    private String lastName;
    private String password;
    private String id;

    public Person(String firstName, String lastName, String password, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID() {
        return id;
    }

}
