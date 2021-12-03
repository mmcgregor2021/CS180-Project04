import java.util.*;
import java.io.*;
/*
	A class used for viewing the serialized students arraylist (for debugging only);
*/
public class AKV {

	public static void main(String[] args) {
		ArrayList<Student> students = readStudents("students.txt");
		for (int i = 0; i < students.size(); i++) {
			System.out.println(students.get(i).getID() + " " + students.get(i).getFirstName());
		}
	}

	//deserializes student objects from the txt file and returns an arraylist of the student objects
	public static ArrayList<Student> readStudents(String fileName) {
		ArrayList<Student> students = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			int count = (int) in.readObject();
			for (int i = 0; i < count; i++) {
				Student student = (Student) in.readObject();
				students.add(student);
			}
		} catch (Exception e) {
			//DO NOTHING
		}
		return students;
	}

}
