# CS180 Project 4 - Group 41
## Group Members
- Astrid Popovici
- Grant McCord
- Kathryn McGregor
- Jainam Doshi
- Kris Leungwattanakij 

## How to Compile and Run the Project
Compile and run the Control.java file in the command line interface of your choice.  The text files (students.txt, teachers.txt, boards.txt, comments.txt, and counters.txt) will be generated during the first run.

## Project Submission
- Jainam Doshi: submitted report on Brightspace
- Grant McCord: submitted Vocareum workspace

## Descriptions of classes
### Person
Defines a person with a first name, last name, password, and ID.
- Implements Java's Serializable interface
- Inherited by Student and Teacher

### Student 
A type of person with the authority to do student-specific actions.
- Implements Java's Serializable interface
- Extends Person

### Teacher 
A type of person with the authority to do teacher-specific actions.
- Implements Java's Serializable interface
- Extends Person
  
### Board
Represents a discussion board.  Has a field of type ArrayList<Comment>.  Tested in the SimpleTester class.  
- Implements Java's Serializable interface

### Comment
Represents a reply to a discussion board, or a comment on a reply.  Tested in the SimpleTester class.
- Implements Java's Serializable interface

### Control
Handles control flow for the project.  This allows the user to interact with the classes and files using the command line interface.  We tested this class manually, and using the test cases in the TestCases class.  Instantiates objects of type:
- Student
- Teacher
- Board
- Comment

### SimpleTester
Tests some functionality of the Board, Comment and LogInTest classes using print statements.

### LogInTest
Implements methods used to sign up, log in, and edit and delete accounts.  Tested in the SimpleTester class.
  
### TestCases
JUnitTests for certain parts of Control--signing up, editing an account, deleting an account, and logging out.
