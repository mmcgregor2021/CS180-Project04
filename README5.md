
# CS180 Project 4 - Group 41
## Group Members
- Astrid Popovici
- Grant McCord
- Kathryn McGregor
- Jainam Doshi
- Kris Leungwattanakij 


## How to Compile and Run the Project
Compile and run the Server.java class in the command line interface of your choice. Then compile and run GUI.java to begin running the project.

## Project Submission
- Jainam Doshi: submitted report on Brightspace
- Kris Leungwattanakij: submitted Vocareum workspace

## Descriptions of classes
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

### Server
This program does all of the processing for the program.  Receives inputs from GUI.java and returns the appropriate response.  This program also implements concurrency.  We tested this class manually.  Instantiates objects of type:
- Student
- Teacher
- Board
- Comment

### GUI
Handles control flow for the project.  Sends outputs to Server.java and changes menus based on the response.  Allows the user to interact with the classes and files using GUIs.  We tested this class manually. Instantiates objects of type: 
- Student
- Teacher
- Board
- Comment