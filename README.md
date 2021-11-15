# CS180 Project 4 - Group 41
## Group Members
- Astrid Popovici
- Grant McCord
- Kathryn McGregor
- Jainam Doshi
- Kris Leungwattanakij 

## How to Compile and Run the Project
Compile and run the Control.java file in the command line interface of your choice.

## Project Submission
Jainam Doshi-submitted report on Brightspace
Grant McCord--submitted Vocareum workspace

## Descriptions of classes
### Board
Represents a discussion board.  Tested 

### Comment
Represents a reply to a discussion board, or a comment on a reply.

### Control
Handles control flow for the project.  This allows the user to interact with the classes and files using the command line interface.  We tested this class manually, and using the test cases in the TestCases class.

### LogInTest
Implements methods used to sign up, log in, and edit and delete accounts.

### Person
Defines a person with a first name, last name, password, and ID.

### SimpleTester
Tests some functionality of the Board, Comment and LogInTest classes using print statements.

### Student (extends Person)
A type of person without the authority to do teacher-specific actions.

### Teacher (extends Person)
A type of person with the authority to do teacher-specific actions.

### TestCases
JUnitTests for certain parts of Control--signing up, editing an account, deleting an account, and logging out.
