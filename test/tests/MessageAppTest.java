/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tests;

import mainapplication.Login;
import mainapplication.Message;
import mainapplication.MessageStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Comprehensive JUnit Test Suite for Login, Message, and MessageStorage classes.
 * * Note: Since MessageStorage is a Singleton, the @BeforeEach method uses reflection 
 * to reset its state before each test. This is necessary to ensure tests are 
 * independent and start with the expected 5 pre-populated test messages.
 */
public class MessageAppTest {

    private Login user;

    /**
     * Resets the Singleton instance of MessageStorage before each test.
     * This is crucial for isolated testing of Singleton behavior.
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setup() throws Exception {
        // Use reflection to set the private static 'instance' field in MessageStorage to null
        Field instanceField = MessageStorage.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        // Re-initialize the MessageStorage to run the private constructor 
        // which repopulates the 5 test messages.
        MessageStorage.getInstance(); 
        
        // Setup a standard Login user for testing
        user = new Login("Test", "User");
        user.stringregisterUser("T_user", "Pass!123");
        user.setPhonenumber("+27123456789");
    }
    
    // ======================================================================
    // 1. Login.java Tests
    // ======================================================================

    @Test
    void testCheckName_Valid() {
        assertTrue(Login.checkName("John"), "Name should contain an uppercase letter.");
        assertTrue(Login.checkName("jOhN"), "Name should contain an uppercase letter.");
        assertFalse(Login.checkName("john"), "Name should fail if no uppercase letter is present.");
    }
    
    @Test
    void testCheckName_Invalid() {
        assertFalse(Login.checkName("john"), "Name should fail if only lowercase.");
        assertFalse(Login.checkName(""), "Name should fail if empty.");
    }

    @Test
    void testCheckUserName_Valid() {
        assertTrue(user.checkUserName("a_b"), "Valid: 3 chars, contains '_'.");
        assertTrue(user.checkUserName("a_b_c"), "Valid: 5 chars, contains '_'.");
    }

    @Test
    void testCheckUserName_Invalid() {
        assertFalse(user.checkUserName("abcdef"), "Invalid: Too long (>5 chars).");
        assertFalse(user.checkUserName("test"), "Invalid: Missing '_'.");
        assertFalse(user.checkUserName("tst__t"), "Invalid: Too long (>5 chars).");
    }

    @Test
    void testCheckPasswordComplexity_Valid() {
        // Max 8 chars, 1 uppercase, 1 special, 1 number
        assertTrue(user.checkPasswordComplexity("P@ss123"), "Valid password.");
        assertTrue(user.checkPasswordComplexity("1Aa*bbcc"), "Valid password (exactly 8 chars).");
    }

    @Test
    void testCheckPasswordComplexity_Invalid() {
        assertFalse(user.checkPasswordComplexity("LongP@ss123"), "Invalid: Too long (>8 chars).");
        assertFalse(user.checkPasswordComplexity("password!1"), "Invalid: Too long (>8 chars).");
        assertFalse(user.checkPasswordComplexity("p@ss123"), "Invalid: Missing uppercase.");
        assertFalse(user.checkPasswordComplexity("P@ssword"), "Invalid: Missing number.");
        assertFalse(user.checkPasswordComplexity("PWord123"), "Invalid: Missing special character.");
    }

    @Test
    void testCheckPhoneNumber_Valid() {
        // Must be +27 followed by 9 digits (total length 12)
        assertTrue(user.checkPhoneNumber("+27123456789"), "Valid phone number.");
    }

    @Test
    void testCheckPhoneNumber_Invalid() {
        assertFalse(user.checkPhoneNumber("271234567890"), "Invalid: Missing '+' prefix.");
        assertFalse(user.checkPhoneNumber("+2712345678"), "Invalid: Too short (11 chars).");
        assertFalse(user.checkPhoneNumber("+271234567890"), "Invalid: Too long (13 chars).");
        assertFalse(user.checkPhoneNumber("+27ABCDEFGHI"), "Invalid: Contains letters.");
    }
    
    @Test
    void testReturnLoginStatus() {
        // User registered with username "T_user" and password "Pass!123" in setup
        assertEquals("Login successful", user.returnLoginStatus("Pass!123", "T_user"), "Should successfully log in.");
        assertEquals("Login unsuccessful", user.returnLoginStatus("WrongPass", "T_user"), "Should fail due to wrong password.");
        assertEquals("Login unsuccessful", user.returnLoginStatus("Pass!123", "W_user"), "Should fail due to wrong username.");
    }

    // ======================================================================
    // 2. Message.java Tests
    // ======================================================================
    
    @Test
    void testMessageHashGeneration_WithDigits() {
        // MSG001 -> Sequential number is 1. Content digits are 123. Hash should use both.
        Message m = new Message("MSG001", "This is content 123.", "Sent", "1");
        assertEquals("HASH-001-123", m.getMessageHash(), "Hash should include sequential ID (001) and content digits (123).");
    }

    @Test
    void testMessageHashGeneration_NoDigits() {
        // MSG002 -> Sequential number is 2. Content length is 20. Hash should use length.
        Message m = new Message("MSG002", "Content with no digits", "Sent", "1");
        // Length of "Content with no digits" is 22.
        assertEquals("HASH-002-22", m.getMessageHash(), "Hash should include sequential ID (002) and content length (22) as fallback.");
    }

    @Test
    void testMessageHashGeneration_LongDigits() {
        // MSG003 -> Sequential number is 3. Content digits are very long.
        String longDigits = "12345678901234567890"; // 20 digits, should be truncated to 15
        Message m = new Message("MSG003", "Text " + longDigits + " End", "Sent", "1");
        assertEquals("HASH-003-123456789012345", m.getMessageHash(), "Hash should truncate long numeric content part (max 15 digits).");
    }


    // ======================================================================
    // 3. MessageStorage.java Tests (Singleton)
    // ======================================================================
    
    @Test
    void testInitialPopulation() {
        MessageStorage storage = MessageStorage.getInstance();
        assertEquals(2, storage.getSentMessages().size(), "Should start with 2 Sent messages (MSG001, MSG004).");
        assertEquals(2, storage.getStoredMessages().size(), "Should start with 2 Stored messages (MSG002, MSG005).");
        assertEquals(1, storage.getDisregardedMessages().size(), "Should start with 1 Disregarded message (MSG003).");
        assertEquals(5, storage.getAllMessages().size(), "Should start with 5 total messages.");
    }

    @Test
    void testAddMessage() {
        MessageStorage storage = MessageStorage.getInstance();
        
        // Current sequence should be 6 after 5 initial messages
        Message newMessage = storage.addMessage("New test message", "Sent", "123");
        assertEquals("MSG006", newMessage.getMessageID(), "New message ID should be MSG006.");
        assertEquals(3, storage.getSentMessages().size(), "Sent list should increase to 3.");
        assertEquals(6, storage.getAllMessages().size(), "Total messages should increase to 6.");
        assertTrue(newMessage.getMessageHash().startsWith("HASH-006-"), "New message hash should start correctly.");
    }

    @Test
    void testGetLongestSentMessage() {
        MessageStorage storage = MessageStorage.getInstance();
        
        // Initial Sent messages:
        // 1. "Did you get the cake?" (Length 23)
        // 4. "It is dinner time !" (Length 19)
        
        Message longest = storage.getLongestSentMessage();
        assertNotNull(longest, "Should find a longest message.");
        assertEquals("MSG001", longest.getMessageID(), "MSG001 ('Did you get the cake?') should be the longest initial message.");
        assertEquals(23, longest.getContent().length(), "Longest message length should be 23.");

        // Add an even longer message
        Message longer = storage.addMessage("This is an even longer message that should win the length contest.", "Sent", "999"); // Length 59
        Message newLongest = storage.getLongestSentMessage();
        assertEquals("MSG006", newLongest.getMessageID(), "MSG006 should now be the longest message.");
        assertEquals(59, newLongest.getContent().length(), "New longest message length should be 59.");
    }

    @Test
    void testFindMessageByID() {
        MessageStorage storage = MessageStorage.getInstance();
        
        Message found = storage.findMessageByID("MSG002");
        assertNotNull(found, "MSG002 should be found.");
        assertEquals("Stored", found.getStatus(), "MSG002 should have status 'Stored'.");
        
        assertNull(storage.findMessageByID("MSG999"), "Non-existent message should return null.");
    }

    @Test
    void testSearchMessagesByRecipient() {
        MessageStorage storage = MessageStorage.getInstance();
        
        // MSG002 and MSG005 share recipient +27838884567
        List<Message> results = storage.searchMessagesByRecipient("+27838884567");
        assertEquals(2, results.size(), "Should find 2 messages for recipient +27838884567.");
        assertTrue(results.stream().anyMatch(m -> m.getMessageID().equals("MSG002")), "Should contain MSG002.");
        assertTrue(results.stream().anyMatch(m -> m.getMessageID().equals("MSG005")), "Should contain MSG005.");
        
        assertEquals(0, storage.searchMessagesByRecipient("999999999999").size(), "Should find 0 messages for unknown recipient.");
    }

    @Test
    void testDeleteMessageByHashOrID() {
        MessageStorage storage = MessageStorage.getInstance();
        
        // 1. Delete by ID (MSG003 is Disregarded)
        assertTrue(storage.deleteMessageByHash("MSG003"), "Should successfully delete MSG003.");
        assertEquals(4, storage.getAllMessages().size(), "Total messages should decrease to 4.");
        assertNull(storage.findMessageByID("MSG003"), "MSG003 should no longer be found.");
        assertEquals(0, storage.getDisregardedMessages().size(), "Disregarded list should be empty.");
        
        // 2. Delete by Hash (MSG001 is Sent)
        Message m1 = storage.findMessageByID("MSG001");
        String hash1 = m1.getMessageHash();
        assertTrue(storage.deleteMessageByHash(hash1), "Should successfully delete MSG001 by hash.");
        assertEquals(3, storage.getAllMessages().size(), "Total messages should decrease to 3.");
        assertEquals(1, storage.getSentMessages().size(), "Sent list should decrease to 1.");
        
        // 3. Delete non-existent
        assertFalse(storage.deleteMessageByHash("FAKEHASH"), "Should fail to delete non-existent hash/ID.");
        assertEquals(3, storage.getAllMessages().size(), "Total messages should remain 3 after failed deletion.");
    }
}