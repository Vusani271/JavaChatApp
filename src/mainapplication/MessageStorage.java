/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * MessageStorage implements the pattern to manage all message data.
 * It is responsible for centralizing the storage of messages into separate
 * lists (Sent, Disregarded, Stored) as required by the assignment, and tracking
 * all Message Hashes and IDs.
 */
public class MessageStorage {

    // Singleton instance
    private static MessageStorage instance;
    
    // Storage Lists based on requirements (1. Users should be able to use to populate the following arrays)
    private final List<Message> sentMessages;
    private final List<Message> disregardedMessages;
    private final List<Message> storedMessages; // Also used for drafts
    private final List<String> messageHashes;
    private final List<String> messageIDs;
    
    // Sequence counter for generating unique sequential IDs (MSG001, MSG002, etc.)
    private int nextMessageSequence = 1;

    /**
     * Private constructor for pattern.
     * Initializes all storage lists and pre-populates the system with 5 test messages.
     */
    private MessageStorage() {
        sentMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        messageHashes = new ArrayList<>();
        messageIDs = new ArrayList<>();
        
    }
    /**
     * Gets the single instance of MessageStorage (Singleton access point).
     * @return the singleton instance.
     */
    public static MessageStorage getInstance() {
        if (instance == null) {
            instance = new MessageStorage();
        }
        return instance;
    }

    /**
     * Creates a new Message object, stores it in the correct list based on status,
     * and updates the ID/Hash tracking lists.
     * * @param content The message content.
     * @param content
     * @param status The status ("Sent", "Stored", "Disregarded").
     * @param recipient The recipient ID/Number.
     * @return The newly created Message object.
     */
    public Message addMessage(String content, String status, String recipient) {
        // Generate unique sequential ID (e.g., MSG006)
        String messageID = String.format("MSG%03d", nextMessageSequence++);
        
        // Create the Message object (which handles its own Hash generation)
        Message newMessage = new Message(messageID, content, status, recipient);

        // Store in appropriate status list
        switch (status) {
            case "Sent" -> sentMessages.add(newMessage);
            case "Stored" -> storedMessages.add(newMessage);
            case "Disregarded" -> disregardedMessages.add(newMessage);
            default -> {
                // Should not happen, but safe to default to Stored
                JOptionPane.showMessageDialog(null, "Warning: Unknown status assigned. Storing as Draft.", "Status Error", JOptionPane.WARNING_MESSAGE);
                storedMessages.add(newMessage);
            }
        }
        
        // Store ID and Hash for tracking
        messageIDs.add(newMessage.getMessageID());
        messageHashes.add(newMessage.getMessageHash());
        
        return newMessage;
    }
    
    // ----------------------------------------------------------------------
    // Message Retrieval/Search Functions
    // ----------------------------------------------------------------------
    
    /**
     * Returns a consolidated list of all messages (Sent, Stored, Disregarded).
     * @return 
     */
    public List<Message> getAllMessages() {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        return all;
    }
    
    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public List<Message> getDisregardedMessages() {
        return disregardedMessages;
    }

    public List<Message> getStoredMessages() {
        return storedMessages;
    }
    
    /**
     * Finds the message with the longest content length from the sentMessages list .
     * @return The longest Message, or null if the list is empty.
     */
    public Message getLongestSentMessage() {
        if (sentMessages.isEmpty()) {
            return null;
        }
        // Use streams to find the message with the maximum content length
        return sentMessages.stream()
                .max((m1, m2) -> Integer.compare(m1.getContent().length(), m2.getContent().length()))
                .orElse(null);
    }
    
    /**
     * Searches for a message by its sequential ID .
     * @param messageID The ID to search for (e.g., "MSG001").
     * @return The found Message, or null if not found.
     */
    public Message findMessageByID(String messageID) {
        return getAllMessages().stream()
                .filter(m -> m.getMessageID().equalsIgnoreCase(messageID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches for all messages (Sent, Stored, Disregarded) belonging to a specific recipient .
     * @param recipient The recipient ID/Number to search for.
     * @return A list of matching messages.
     */
    public List<Message> searchMessagesByRecipient(String recipient) {
        return getAllMessages().stream()
                .filter(m -> m.getRecipient().equalsIgnoreCase(recipient))
                .toList(); // Modern Java way to collect to a List
    }
    
    /**
     * Deletes a message using its Message Hash or sequential Message ID .
     * Also removes the ID and Hash from their respective tracking lists.
     * * @param hashOrId The Message Hash or ID to delete.
     * @param hashOrId
     * @return true if the message was successfully deleted, false otherwise.
     */
    public boolean deleteMessageByHash(String hashOrId) {
        // Search across all lists
        Optional<Message> foundMessage = getAllMessages().stream()
            .filter(m -> m.getMessageHash().equalsIgnoreCase(hashOrId) || m.getMessageID().equalsIgnoreCase(hashOrId))
            .findFirst();

        if (foundMessage.isPresent()) {
            Message m = foundMessage.get();
            
            // Remove from the specific status list
            boolean removed = switch (m.getStatus()) {
                case "Sent" -> sentMessages.remove(m);
                case "Stored" -> storedMessages.remove(m);
                case "Disregarded" -> disregardedMessages.remove(m);
                default -> false;
            };

            if (removed) {
                // Remove from tracking lists
                messageIDs.remove(m.getMessageID());
                messageHashes.remove(m.getMessageHash());
                return true;
            }
        }
        return false;
    }
}