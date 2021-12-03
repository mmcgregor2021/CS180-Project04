/**
* A testing class that tests all the functionalities of the Teacher and Student classes
* @author Kris Leungwattanakij
* @version November 15, 2021
**/
public class AccountTester {

    public static void main(String[] args) {

        Student student = new Student("Jainam", "Doshi", "password123", 1);
        Teacher teacher = new Teacher("Purdue", "Pete", "boilerup", 2);

        //testing getters for Student
        Boolean condition1 = student.getFirstName().equals("Jainam");
        Boolean condition2 = student.getLastName().equals("Doshi");
        Boolean condition3 = student.getPassword().equals("password123");
        Boolean condition4 = (student.getID() == 1);
        Boolean condition5 = (student.getAuthority() == false);
        if (condition1 && condition2 && condition3 && condition4 && condition5) {
            System.out.println("Getters passed test cases for Student class");
        } else {
            System.out.println("Getters failed test cases for Student class");
        }

        //testing getters for Teacher
        condition1 = teacher.getFirstName().equals("Purdue");
        condition2 = teacher.getLastName().equals("Pete");
        condition3 = teacher.getPassword().equals("boilerup");
        condition4 = (teacher.getID() == 2);
        condition5 = (teacher.getAuthority() == true);
        if (condition1 && condition2 && condition3 && condition4 && condition5) {
            System.out.println("Getters passed test cases for Teacher class");
        } else {
            System.out.println("Getters failed test cases for Teacher class");
        }

        //testing setters for Student
        student.setFirstName("Grant");
        student.setLastName("McCord");
        student.setPassword("password234");
        condition1 = student.getFirstName().equals("Grant");
        condition2 = student.getLastName().equals("McCord");
        condition3 = student.getPassword().equals("password234");
        if (condition1 && condition2 && condition3) {
            System.out.println("Setters passed test cases for Student class");
        } else {
            System.out.println("Setters failed test cases for Student class");
        }

        //testing setters for Teacher
        teacher.setFirstName("Jeffrey");
        teacher.setLastName("Turkstra");
        teacher.setPassword("CS180");
        condition1 = teacher.getFirstName().equals("Jeffrey");
        condition2 = teacher.getLastName().equals("Turkstra");
        condition3 = teacher.getPassword().equals("CS180");
        if (condition1 && condition2 && condition3) {
            System.out.println("Setters passed test cases for Teacher class");
        } else {
            System.out.println("Setters failed test cases for TeacherStu class");
        }

    }

}
