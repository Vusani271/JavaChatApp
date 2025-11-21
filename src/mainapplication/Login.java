/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapplication;

import java.util.regex.Pattern;

public class Login {
    private final String name;
    private final String surname;
    private String registeredUsername;
    private String registeredPassword;
    private String phonenumber; // Field to store the validated phone number

    public Login(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    //  Static Validation Methods (used for Name/Surname) 

    /**
     * Checks if a name or surname contains at least one uppercase letter.
     * @param input The name or surname string.
     * @return true if valid, false otherwise.
     */
    public static boolean checkName(String input) {
        // Regex: Checks for at least one uppercase letter [A-Z] anywhere in the string.
        return Pattern.compile("[A-Z]").matcher(input).find();
    }

    // Instance Validation Methods 

    /**
     * Checks if the username is 5 characters or less AND includes an underscore (_).
     * @param username The username string.
     * @return true if valid, false otherwise.
     */
    public boolean checkUserName(String username) {
        if (username.length() > 5) {
            return false;
        }
        return username.contains("_");
    }

    /**
     * Checks password complexity: Max 8 chars, 1 uppercase, 1 special char, 1 number.
     * @param password The password string.
     * @return true if valid, false otherwise.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password.length() > 8) {
            return false;
        }

        // Regex for complexity:
        // (?=.*[A-Z])   - must contain at least one uppercase letter
        // (?=.*[0-9])   - must contain at least one digit
        // (?=.*[^a-zA-Z0-9\s]) - must contain at least one special character (not letter, digit, or whitespace)
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{1,8}$";
        return Pattern.compile(regex).matcher(password).matches();
    }

    /**
     * Checks if the phone number is +27 followed by 9 digits (total length 12).
     * @param phoneNumber The phone number string.
     * @return true if valid, false otherwise.
     */
    public boolean checkPhoneNumber(String phoneNumber) {
        // Regex: ^\+27 - starts with +27
        // [0-9]{9}$ - followed by exactly 9 digits, and ends there.
        String regex = "^\\+27[0-9]{9}$";
        return Pattern.compile(regex).matcher(phoneNumber).matches();
    }
    
    //  Registration and Login Logic 

    /**
     * Stores the username and password for subsequent login.
     * @param username The valid username.
     * @param password The valid password.
     */
    public void stringregisterUser(String username, String password) {
        this.registeredUsername = username;
        this.registeredPassword = password;
    }

    /**
     * Validates the provided login credentials against the registered ones.
     * @param password The password entered by the user.
     * @param username The username entered by the user.
     * @return "Login successful" or a descriptive failure message.
     */
    public String returnLoginStatus(String password, String username) {
        if (this.registeredUsername == null || this.registeredPassword == null) {
            return "Error: No user registered.";
        }
        
        if (!this.registeredUsername.equals(username)) {
            return "Username incorrect.";
        }

        if (!this.registeredPassword.equals(password)) {
            return "Password incorrect.";
        }

        return "Login successful";
    }

    // Getters and Setter for Phone Number (New)
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }

    //Getters 
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}