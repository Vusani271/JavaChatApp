/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mainapplication;

import javax.swing.JOptionPane;


/**
 * The MainApplication class is the combined entry point for the application.
 * It first handles user registration and login/validation, using loops to enforce 
 * correct input. Upon successful login, it launches the interactive message application menu 
 * using the external Login and MessageService classes.
 */
public class MainApplication {

    /**
     * The main method serves as the entry point, handling the entire process.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        
        // Prepares variables outside the loops
        String name = "";
        String surname = "";
        String username = "";
        String password = "";
        String phonenumber = "";
        Login user1;

        JOptionPane.showMessageDialog(null, "Welcome to the ChatApp! Click OK to register.", "Registration Start", JOptionPane.INFORMATION_MESSAGE);
        
        //  Registration Process 

        // Name Validation Loop (must contain at least one uppercase letter)
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
        
        // Surname Validation Loop (must contain at least one uppercase letter)
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
        
        // Initialize the Login object after name and surname are confirmed
        user1 = new Login(name, surname);

        // Username Validation Loop (5 chars or less AND must include an underscore '_')
        while (true) {
            username = JOptionPane.showInputDialog(null, "Enter your username (must be 5 characters or less AND include an underscore (_)):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (username == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (!user1.checkUserName(username)) {
                JOptionPane.showMessageDialog(null, "Error: Username is not in the correct format. Please try again.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        // Password Validation Loop (Max 8 characters, 1 uppercase, 1 special character, 1 number)
        while (true) {
            password = JOptionPane.showInputDialog(null, "Enter your password (Max 8 characters, must have 1 uppercase, 1 special character, 1 number):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (password == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (!user1.checkPasswordComplexity(password)) {
                JOptionPane.showMessageDialog(null, "Error: Password is not in the correct format. Please try again.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        // Phone Number Validation Loop (+27 followed by 9 digits, total length 12)
        while (true) {
            phonenumber = JOptionPane.showInputDialog(null, "Please enter phone number (must be +27 followed by 9 digits, total length 12 characters):", "ChatApp Registration", JOptionPane.QUESTION_MESSAGE);
            if (phonenumber == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled. Program will now exit.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (!user1.checkPhoneNumber(phonenumber)) {
                JOptionPane.showMessageDialog(null, "Error: Phone number is not in the correct format. Please try again.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Stores registration data and confirms the registration
                user1.stringregisterUser(username, password);
                
                // Set the phone number on the Login object for use in MessageService
                user1.setPhonenumber(phonenumber); 
                
                JOptionPane.showMessageDialog(null, "Registration successful!", "Registration Status", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        JOptionPane.showMessageDialog(null, "Registration complete. Click ok to login.", "Login", JOptionPane.INFORMATION_MESSAGE);

        //  Login Process 
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
                JOptionPane.showMessageDialog(null, "Login unsuccessful. Please check your credentials and try again.", "Login Status", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Initialize MessageService after successful login, passing the authenticated user
        MessageService service = new MessageService(user1); 
        
        //  Main Application Menu Loop 
        while (true) {
            // Displays the main menu options
            String choice = JOptionPane.showInputDialog(null, 
                "Select an option:\n1. Send Messages\n2. View Sent Messages/Manage Drafts\n3. Exit", 
                "Message Application Menu", JOptionPane.PLAIN_MESSAGE);
            
            // Handles the user pressing Cancel (choice is null) or selecting '3'
            if (choice == null || (choice.trim().length() > 0 && choice.trim().equals("3"))) {
                JOptionPane.showMessageDialog(null, "Exiting application. Goodbye! 👋", "Exit", JOptionPane.INFORMATION_MESSAGE);
                break; // Exit the loop and the application
            }
            
            // Handles the users choices
            switch (choice.trim()) {
                case "1" -> service.sendMessages(); // Go to message entry
                case "2" -> service.showMessages(); // Go to message management submenu
                default -> // Inform the user of an invalid input
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please select 1, 2, or 3.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}