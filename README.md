# CS180 Project 5 - Group 41
## Group Members
- Astrid Popovici
- Grant McCord
- Kathryn McGregor
- Jainam Doshi
- Kris Leungwattanakij 

## How to Compile and Run the Project
1) Compile and run the Server.java file in the command line interface of your choice on the computer you wish to host your server on. 
2) Open GUI.java in an editor of your choice and replace "localhost" on line 144 with the IP address of the computer running the server. 
3) Compile and run the GUI.java file in the command line interface of your choice. (make sure that the computer this is running on is connected to the same network as the computer you are running your server on.
4) The text files (students.txt, teachers.txt, boards.txt, comments.txt, and counters.txt) will be generated during the first run.

## Project Submission
- Jainam Doshi: submitted report and video presentation on Brightspace
- Kris Leungwattanakij: submitted Vocareum workspace

## Descriptions of classes
### Server
A multi-threaded server class which is able to process requests from multiple clients simultaneously.
- Connects with GUI using sockets
- Has a nested class called ClientHandler
- Instantiates objects of types:
  - Student
  - Teacher
  - Board 
  - Comment

### ClientHandler
A client handler thread class that is instantiated and started by the Server class every time a client connects to the server.  Handles individual client requests.
- Is a nested class inside Server
- Implements Java's Runnable interface

### GUI
A client class that connects to and allows the user to interact with the server by providing them with a GUI.  Uses JFrames and JOptionPanes from Swing to provide users with a GUI.  Runs GUI related code within the Event Dispatch Thread (EDT).
- Extends the JComponent class
- Connects with Server using sockets
- Has a nested class called UpdateListener
- Instantiates objects of type:
  - Comment

### UpdateListener
A GUI updater thread class that is instantiated and started by the GUI class when it is compiled and ran.  Checks what page the user is on every five seconds and runs the corresponding update method to provide the user with real-time updates.
- Is a nested class inside GUI
- Implements Java's Runnable interface

### Person
Defines a person with a first name, last name, password, and ID.  
- Implements Java's Serializable interface
- Inherited by Student and Teacher, which are both tested in the AccountTester class.

### Student 
A type of person with the authority to do student-specific actions.  Tested in the AccountTester class.
- Implements Java's Serializable interface
- Extends Person

### Teacher 
A type of person with the authority to do teacher-specific actions.  Tested in the AccountTester class.
- Implements Java's Serializable interface
- Extends Person
  
### Board
Represents a discussion board.  Has a field of type ArrayList<Comment>.  Tested in the SimpleTester class.  
- Implements Java's Serializable interface

### Comment
Represents a reply to a discussion board, or a comment on a reply.  Tested in the SimpleTester class.
- Implements Java's Serializable interface

### SimpleTester
Tests some functionality of the Board, Comment and LogInTest classes using print statements.

### AccountTester
Tests the getter and setter methods in both the Student and Teacher classes.
