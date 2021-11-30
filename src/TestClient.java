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

				System.out.println("Enter user id:");
				Integer loginID = Integer.parseInt(sc.nextLine());
                System.out.println("Enter password:");
				String loginPassword = sc.nextLine();

                logIn(loginID, loginPassword);
			}

			// closing the scanner object
			sc.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

    //GRANT
    public static void logIn(Integer userID, String password) {
        try (Socket socket = new Socket("localhost", 1234)) {
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

}
