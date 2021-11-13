import java.util.*;
public class contentCreation {

	public static Board createBoard(Scanner scan, String courseName, int boardCounter) {
		System.out.println("Please enter the forum topic.");
		String topic = "";
		while (true) {
			topic = scan.nextLine();
			if (topic.equals("")) {
				System.out.println("Please enter a non-blank forum topic.");
			} else {
				break;
			}
		}
		ArrayList<Comment> boardComments = new ArrayList<Comment>();
		Date date = new Date();
		String boardID = "B" + boardCounter;
		Board board = new Board(courseName, topic, boardID, date.toString(), boardComments);
		System.out.println("Board was successfully created.");

		return board;
	}


}
