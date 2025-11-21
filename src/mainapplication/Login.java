/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapplication;

import java.util.regex.Pattern;

/**
 * The Login class handles user registration rules, stores user credentials, 
 * and provides authentication validation.
 */
public class Login {
    private final String name;
    private final String surname;
    private String username;
    private String password;
    private String phonenumber;

    // Constructor to initialize basic user details
    public Login(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /**
     * Static method to check if a name (first or last) contains at least one uppercase letter.
     * @param input The name string.
     * @return true if valid, false otherwise.
     */
    public static boolean checkName(String input) {
        return input.matches(".*[A-Z].*");
    }

    /**
     * Checks if the username is 5 characters or less AND includes an underscore (_).
     * @param username The username string.
     * @return true if valid, false otherwise.
     */
    public boolean checkUserName(String username) {
        return username.length() <= 5 && username.contains("_");
    }

    /**
     * Checks if the password meets complexity requirements: Max 8 characters, 
     * 1 uppercase, 1 special character, 1 number.
     * @param password The password string.
     * @return true if valid, false otherwise.
     */
    public boolean checkPasswordComplexity(String password) {
        // Max 8 characters
        if (password.length() > 8) {
            return false;
        }
        // Must have at least 1 uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // Must have at least 1 number
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        // Must have at least 1 special character (using common special chars)
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the phone number is +27 followed by 9 digits (total length 12).
     * @param phonenumber The phone number string.
     * @return true if valid, false otherwise.
     */
    public boolean checkPhoneNumber(String phonenumber) {
        // Uses regex to check for the required pattern
        return Pattern.matches("\\+27[0-9]{9}", phonenumber);
    }
    
    /**
     * Stores the final validated user registration data.
     * @param username The validated username.
     * @param password The validated password.
     */
    public void stringregisterUser(String username, String password) {
        this.username = username;
        this.password = password;
        // Phone number is set during the validation loop in MainApplication (not in this method)
    }
    
    /**
     * Sets the phone number after validation.
     * @param phonenumber The validated phone number.
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * Authenticates the user based on provided credentials.
     * @param loginPassword The password entered during login.
     * @param loginUsername The username entered during login.
     * @return "Login successful" or "Login unsuccessful".
     */
    public String returnLoginStatus(String loginPassword, String loginUsername) {
        if (loginUsername.equals(this.username) && loginPassword.equals(this.password)) {
            return "Login successful";
        }
        return "Login unsuccessful";
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }
}