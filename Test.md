Test Procedures for Project 5

  Test 1: User Login
  1. User launches application
  2. User clicks the "Log In" button
  3. User enters their ID into the text field
  4. User enter their password into the text field
  5. User clicks the "Log In" button
  
  Expected result: Application verifies the ID and password are correct and the JFrame becomes the main menu
  
  Test Status: Passed
  
  
  Test 2: User Signup
  1. User launches application
  2. User clicks the "Sign Up" button
  3. User is given an ID
  4. User enter their desired first name, last name, password, and whether they are a student or not
  5. User clicks the "Sign Up" button
  
  Expected result: Application creates a new student or teacher object (depending on what the user designated), and the JFrame 
  becomes the main menu
  
  Test Status: Passed 
  
  
  Test 3: Edit Account
  1. User clicks "Edit account" button from the main menu
  2. Fields containing the user's password, first name, and last name are displayed
  3. User can edit those fields to change their information
  4. User clicks the "Process Changes" button
  
  Expected Result: A JOptionPane with the all of the user's information appears which they then close and are back to the main menu.
  
  Test Status: Passed
  
  
  Test 4: Delete Account
  1. User clicks the "Delete account" button from the main menu
  2. A JOptionPane opens asking the user if they are sure they want to delete their account
  
  Expected Result: If the user hits yes, their account is deleted and the program closes, otherwise, they are returned to the mian menu.
  
  Test Status: Passed
  
  
  Test 5: View a Discussion Board
  1. User clicks the "View courses" button from the main menu
  2. The user can select the course they want from a JComboBox
  3. User clicks "Select course"
  4. A JComboBox with all the discussion boards under that course appears.
  5. User clicks "Select Board"
  
  Expected Result: The discussion board with all of its comments appears
  
  Test Status: Passed
  
  
  Test 6: Logout
  
  Expected Result: User can click the "X" on the frame at any time to log out
  
  Test Status: Passed
  
  
  Test 7: Student views all their posts and grades
  1. User clicks the "View posts and grades" button
  
  Expected Result: A page displaying all of the user's posts and grades appears.
  
  Test Status: Passed
  
  
  Test 8: Reply to comment
  1. User is viewing a discussion board
  2. User clicks the "Reply to comment" button under the comment they want to reply to
  3. A JOptionPane appears asking the user to enter their text
  4. User clicks the "Process as filepath" if the text they entered is the filepath to the reply they want to appear on the board
  5. User clicks the "Process as direct text" if the text they entered is what they want their reply to be
  
  Expected Result: The reply is displayed under the comment on the discussion board 
  
  Test Status: Not passed 
  
  
  Test 8: Add comment
  1. User is viewing a discussion board 
  2. User clicks the "Add Comment" button at the top of the page
  3. User clicks the "Process as filepath" if the text they entered is the filepath to the comment they want to appear on the board
  3. User clicks the "Process as direct text" if the text they entered is what they want their comment to be
  
  Expected Result: The comment is displayed on the discussion board and added to the comments ArrayList
  
  Test Status: Not passed
  
  
  Test 9: Vote for a comment
  1. User is viewing a discussion board
  2. Click the "Vote for comment" button under the post they would like to vote for
  3. A JOptionPane opens telling the user their vote has been counted 
  
  Expected Result: The vote count on the comment goes up as long as it is the user's first time voting on this board
  
  Test Status: Passed
  
  
  Test 10: Teacher creates a new course
  1. User clicks the "Create new course" button from the main menu
  2. User enters the name of the new course into the text field
  3. User selects from a combo box whether they want input their forum topic directly or through a filepath and 
  puts the info into the text field
  4. User clicks "Create course"
  5. A JOptionPane displays the name of the course and its initial discussion board.
  
  Expected Result: A new Course object is created and added to the courses ArrayList
  
  Test Status: Passed
  
  
  Test 11: Teacher grades student posts
  1. User clicks the "Grade student posts" from the main menu
  2. User selects a student from the combo box holding all students
  3. User clicks the "Select this student" button
  4. User selects which comment they would like to grade from that student
  5. User clicks the "View Content of Comment" button 
  6. JOptionPane opens containing the course the comment was from, the forum topic, the content of the comment, and the 
  comment's current grade
  7. User enters the grade they want to assign into the text field
  8. User clicks the "Assign Grade" button
  
  Expected Result: A JOptionPane containing the comment ID and the grade assigned opens. The grade is updated for the comment.
  
  Test Status: Not passed 
  
  
  Test 12: Teacher adds a discussion board 
  1. User selects a course from a combo box
  2. User selects from a combo box whether they want input their forum topic directly or through a filepath and 
  puts the info into the text field
  3. User clicks "Create Discussion Board"
  
  Expected Result: A JOptionPane containing the topic of the board and the course it belongs to opens up
  
  Test Status: Passed 
  
  
  
  










