import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

// Client class
class TestClient {

	// driver code
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// object of scanner class
			Scanner sc = new Scanner(System.in);
			String line = null;

			while (!"exit".equalsIgnoreCase(line)) {

				System.out.println("1. login\n2. signup");
				Integer choice = Integer.parseInt(sc.nextLine());
				switch (choice) {
					case 1:
						System.out.println("Enter user id:");
						Integer loginID = Integer.parseInt(sc.nextLine());
						System.out.println("Enter password:");
						String loginPassword = sc.nextLine();
						logIn(loginID, loginPassword, socket);
						break;
					case 2:
						Integer newID = requestNewID(socket);
						System.out.println("Your ID is: " + newID);
						System.out.println("Please enter your first name: ");
						String first = sc.nextLine();
						System.out.println("Please enter your last name: ");
						String last = sc.nextLine();
						System.out.println("Please enter a password: ");
						String pass = sc.nextLine();
						System.out.println("Please enter your role (Teacher or Student): ");
						String role = sc.nextLine();
						signUp(newID, first, last, pass, role, socket);
				}

			}
			// closing the scanner object
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void signUp(Integer userID, String firstName, String lastName, String password, String role, Socket socket) {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String signUpPayload = "signup;" + userID + ";" + password + ";" + role + ";" + firstName + ";" + lastName;
			out.println(signUpPayload);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Integer requestNewID(Socket socket) {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//sending a request to the server for a new ID to be assigned
			out.println("newID");
			out.flush();
			return Integer.parseInt(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

    public static void logIn(Integer userID, String password, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String verificationPayload = "login;" + userID + ";" + password;
            out.println(verificationPayload);
            out.flush();
            switch(Integer.parseInt(in.readLine())) {
                //TO DO: replace print statements below with JOptionPane messages
                case 1:
                    System.out.println("ID DOESN'T EXIST");
                    break;
                case 2:
                    System.out.println("WRONG PASSWORD");
                    break;
                case 3:
                    System.out.println("SUCCESFUL LOGIN");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequest(String requestPayload, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(requestPayload);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
