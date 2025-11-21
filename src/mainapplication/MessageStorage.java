/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// MessageStorage handles the central storage for all message types using Lists (Arrays).
// It implements the Singleton pattern and includes initial test data.
public class MessageStorage {
    private static MessageStorage instance;
    
    // Arrays required for the application
    private final List<Message> sentMessages;
    private final List<Message> disregardedMessages;
    private final List<Message> storedMessages;
    private int messageCounter = 0; // Counter for unique Message IDs

    private MessageStorage() {
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        // Populate with initial data as requested (excluding Disregarded Messages array population)
    }

    // Singleton getInstance method
    public static MessageStorage getInstance() {
        if (instance == null) {
            instance = new MessageStorage();
        }
        return instance;
    }
    
    // --- Message Management Logic ---
    
    /**
     * Creates a new Message, assigns IDs, stores it in the correct array, and returns the message.
     * @param content
     * @param status
     * @param recipient
     * @return 
     */
    public Message addMessage(String content, String status, String recipient) {
        messageCounter++;
        String id = String.format("MSG%03d", messageCounter);
        // Message Hash must be unique for deletion - use ID + Random suffix for robustness
        String hash = id + "-" + new Random().nextInt(1000); 

        Message newMessage = new Message(id, content, status, recipient, hash);

        // Store in the appropriate array
        switch (status) {
            case "Sent" -> sentMessages.add(newMessage);
            case "Disregard" -> disregardedMessages.add(newMessage);
            case "Stored", "Draft" -> // Treat "Draft" status as a "Stored" message for storage purposes
                storedMessages.add(newMessage);
        }
        return newMessage;
    }
    
    /**
     * Finds a message by its unique Hash ID across all stored arrays for deletion.
     * @param hash The unique message hash (or ID) to find.
     * @return true if deleted, false otherwise.
     */
    public boolean deleteMessageByHash(String hash) {
        // Check Sent Messages
        if (sentMessages.removeIf(m -> m.getMessageHash().equals(hash) || m.getMessageID().equals(hash))) return true;

        // Check Disregarded Messages
        if (disregardedMessages.removeIf(m -> m.getMessageHash().equals(hash) || m.getMessageID().equals(hash))) return true;
        // Check Stored Messages
        
        return storedMessages.removeIf(m -> m.getMessageHash().equals(hash) || m.getMessageID().equals(hash));
    }
    
    /**
     * Finds a message by its Message ID.
     * @param id
     * @return 
     */
    public Message findMessageByID(String id) {
        // Search Sent Messages
        for (Message m : sentMessages) {
            if (m.getMessageID().equals(id)) return m;
        }
        // Search Stored Messages
        for (Message m : storedMessages) {
            if (m.getMessageID().equals(id)) return m;
        }
        // Search Disregarded Messages
        for (Message m : disregardedMessages) {
            if (m.getMessageID().equals(id)) return m;
        }
        return null;
    }
    
    /**
     * Searches for messages by recipient ID/number.
     * @param recipient
     * @return 
     */
    public List<Message> searchMessagesByRecipient(String recipient) {
        List<Message> found = new ArrayList<>();
        // Iterate over all three lists (Sent, Disregarded, Stored)
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages);
        allMessages.addAll(disregardedMessages); 
        
        for (Message m : allMessages) {
            if (m.getRecipient().equals(recipient)) {
                found.add(m);
            }
        }
        return found;
    }
    
    /**
     * Finds the longest message in the sentMessages array.
     */
    public Message getLongestSentMessage() {
        if (sentMessages.isEmpty()) {
            return null;
        }
        
        Message longest = sentMessages.get(0);
        for (Message m : sentMessages) {
            if (m.getContent().length() > longest.getContent().length()) {
                longest = m;
            }
        }
        return longest;
    }
    
    // --- Getters for Arrays ---

    /**
     * Gets all messages in a combined list (for viewing all messages/drafts).
     * @return 
     */
    public List<Message> getAllMessages() {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        return all;
    }

    /**
     * Gets the Sent Messages list.
     * @return 
     */
    public List<Message> getSentMessages() {
        return sentMessages;
    }
}