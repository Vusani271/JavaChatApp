/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mainapplication;

import javax.swing.JOptionPane;

// The MainApplication class handles user registration and login/validation, 
// and launches the interactive message application menu with all required functions.
 
public class MainApplication {

    // The main method serves as the entry point, handling the entire process.
    public static void main(String[] args) {
        
        // Prepares variables outside the loops
        String name = "";
        String surname = "";
        String username = "";
        String password = "";
        String phonenumber = "";
        Login user1;

        JOptionPane.showMessageDialog(null, "Welcome to the ChatApp! Click OK to register.", "Registration Start", JOptionPane.INFORMATION_MESSAGE);
        
        //Registration Process 

        // Name Validation Loop
        while (true) {
            name = JOptionPane.showInputDialog(null, "Please enter your name (must contain at least one uppercase letter):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (name == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            // Uses the static checkName method from the external Login class
            if (name.trim().isEmpty() || !Login.checkName(name)) { 
                JOptionPane.showMessageDialog(null, "Error: Name cannot be empty and must contain at least one uppercase letter. Please try again.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
        
        // Surname Validation Loop
        while (true) {
            surname = JOptionPane.showInputDialog(null, "Please enter your surname (must contain at least one uppercase letter):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (surname == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            // Uses the static checkName method from the external Login class
            if (surname.trim().isEmpty() || !Login.checkName(surname)) { 
                JOptionPane.showMessageDialog(null, "Error: Surname cannot be empty and must contain at least one uppercase letter. Please try again.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
        
        // Instantiate the Login object after name and surname are confirmed
        user1 = new Login(name, surname);

        // Username Validation Loop
        while (true) {
            username = JOptionPane.showInputDialog(null, "Enter your username (must be 5 characters or less AND include an underscore (_)):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (username == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (!user1.checkUserName(username)) {
                JOptionPane.showMessageDialog(null, "Error: Username is not in the correct format. Please try again. (Max 5 chars, requires '_')", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        // Password Validation Loop
        while (true) {
            password = JOptionPane.showInputDialog(null, "Enter your password (Max 8 characters, must have 1 uppercase, 1 special character, 1 number):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (password == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (!user1.checkPasswordComplexity(password)) {
                JOptionPane.showMessageDialog(null, "Error: Password is not in the correct format. Please try again. (Max 8 chars, 1 Uppercase, 1 Special Char, 1 Number)", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        // Phone Number Validation Loop
        while (true) {
            phonenumber = JOptionPane.showInputDialog(null, "Please enter phone number (must be +27 followed by 9 digits, total length 12 characters):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (phonenumber == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (!user1.checkPhoneNumber(phonenumber)) {
                JOptionPane.showMessageDialog(null, "Error: Phone number is not in the correct format. Please try again. (Must be +27 followed by 9 digits)", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Stores registration data and confirms the registration
                user1.setPhonenumber(phonenumber); // Save the validated number in the Login object
                user1.stringregisterUser(username, password);
                JOptionPane.showMessageDialog(null, "Registration successful!", "Registration Status", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        JOptionPane.showMessageDialog(null, "Registration complete. Click ok to login.", "Login", JOptionPane.INFORMATION_MESSAGE);

        // Login Process
        String loginUsername;
        String loginPassword;

        // Login Validation Loop
        while (true) {
            loginUsername = JOptionPane.showInputDialog(null, "Please log in by entering your username:", "ChatApp Login", JOptionPane.QUESTION_MESSAGE);
            if (loginUsername == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            
            loginPassword = JOptionPane.showInputDialog(null, "Please log in by entering your password:", "ChatApp Login", JOptionPane.QUESTION_MESSAGE);
            if (loginPassword == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            
            String loginStatus = user1.returnLoginStatus(loginPassword, loginUsername);
            
            if (loginStatus.equals("Login successful")) {
                JOptionPane.showMessageDialog(null, "Login successful. Welcome to the app, " + user1.getName() + " " + user1.getSurname() + "!", "Login Status", JOptionPane.INFORMATION_MESSAGE);
                break; // Exit login loop
            } else {
                // Displays descriptive failure message
                JOptionPane.showMessageDialog(null, "Login unsuccessful. Please check your credentials and try again. Status: " + loginStatus, "Login Status", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Pass the authenticated user object to MessageService as required by its constructor.
        MessageService service = new MessageService(user1); 
        
        // Initialize storage arrays and test data
        MessageStorage.getInstance(); 
        
        //  Message Application Menu (Last Part of Program) 
        while (true) {
            // Displays the complete menu with all required options
            String menuOptions = 
                """
                Select an option:
                1. Send Messages / Save Drafts
                2. View All Messages (Sent, Stored, Disregarded)
                ----------------------------------
                3. Display Sender/Recipient of Sent Messages 
                4. Display Longest Sent Message 
                5. Search Message by ID 
                6. Search Messages by Recipient 
                7. Delete Message by Hash/ID 
                8. Display Report of Sent Messages 
                9. Exit""" 
            ;
            
            String choice = JOptionPane.showInputDialog(null, menuOptions, "Message Application Menu", JOptionPane.PLAIN_MESSAGE);
            
            // Handles the user pressing Cancel (choice is null) or selecting '9'
            if (choice == null || choice.trim().equals("9")) {
                JOptionPane.showMessageDialog(null, "Exiting application. Goodbye! 👋", "Exit", JOptionPane.INFORMATION_MESSAGE);
                break; // Exit the loop and the application
            }
            
            // Handles the users choices
            switch (choice.trim()) {
                case "1" -> service.sendMessages(); // Send/Store messages
                case "2" -> service.showMessages(); // View all messages
                case "3" -> service.displaySenderAndRecipient(); // 
                case "4" -> service.displayLongestSentMessage(); // 
                case "5" -> service.searchMessageByID(); // 
                case "6" -> service.searchMessagesByRecipient(); // 
                case "7" -> service.deleteMessageByHash(); // 
                case "8" -> service.displaySentMessageReport(); // 
                default -> // Inform the user of an invalid input
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please select an option from 1 to 9.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}