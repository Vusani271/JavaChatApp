/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapplication;

/**
 * class representing a single message in the system.
 * Each message has a sequential ID, a unique Hash ID, content, status, and recipient.
 */
public class Message {
    // These fields are declared as final to ensure they are set only during object creation.
    private final String messageID;
    private final String messageHash; // Unique identifier based on ID and content
    
    // These fields are non-final as they might be updated (e.g., in a draft)
    private String content;
    private String status; // Status: "Sent", "Stored", or "Disregarded"
    private String recipient;

    /**
     * The primary constructor for the Message class.
     * * @param messageID The sequential ID (e.g., MSG001) provided by MessageStorage.
     * @param messageID
     * @param content The text content of the message.
     * @param status The current status ("Sent", "Stored", "Disregarded").
     * @param recipient The cellphone number or ID of the recipient.
     */
    public Message(String messageID, String content, String status, String recipient) {
        // Initializes fields
        this.messageID = messageID;
        this.content = content;
        this.status = status;
        this.recipient = recipient;
        
        // Generates the unique hash ID upon creation
        this.messageHash = generateMessageHash();
    }

    /**
     * Generates a unique hash ID that incorporates the sequential ID number and
     * a truncated numeric part derived from the message content (digits or length),
     * fulfilling the requirement for a numeric hash 'similar' to the message.
     * The format is: HASH-[SEQUENTIAL_ID_NUMBER]-[NUMERIC_CONTENT_PART]
     * * @return The unique and content-descriptive message hash.
     */
    private String generateMessageHash() {
        // Extract the sequential number part from messageID (e.g., "001" from "MSG001")
        String seqNumberPart = messageID.replaceAll("[^0-9]", ""); 
        
        // 1. Extract only digits from the content
        String contentDigits = content.replaceAll("[^0-9]", "");
        
        // Fallback: If no digits in content, use the content length (which is a number)
        if (contentDigits.isEmpty()) {
            contentDigits = String.valueOf(content.length());
        }

        // Truncate the content part if it's too long (max 15 digits for readability/practicality)
        String numericContentPart = contentDigits.length() > 15 
                               ? contentDigits.substring(0, 15) 
                               : contentDigits;
        
        // 2. Combine them to form the final hash
        return String.format("HASH-%s-%s", seqNumberPart, numericContentPart);
    }
    
    // ----------------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------------

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    
    /**
     * Returns a short string representation of the message for display in lists.
     * @return 
     */
    public String toShortString() {
        return String.format("[ID: %s] [Hash: %s] [Status: %s] to %s: %.50s...", 
                messageID, messageHash, status, recipient, content.trim());
    }
    
    /**
     * Returns a full string representation of the message for detailed viewing.
     * @return 
     */
    @Override
    public String toString() {
        return String.format("""
                Message Details:
                  ID:      %s
                  Hash:    %s
                  Status:  %s
                  Recipient: %s
                  Content: %s
                """, messageID, messageHash, status, recipient, content);
    }
}