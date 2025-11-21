/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapplication;

// The Message class represents a single message in the system.
public class Message {
    private String messageID;
    private String content;
    private String status; // e.g., "Draft", "Sent", "Disregard", "Stored"
    private String recipient;
    // messageHash is the unique identifier used for deletion.
    private String messageHash; 

    public Message(String messageID, String content, String status, String recipient, String messageHash) {
        this.messageID = messageID;
        this.content = content;
        this.status = status;
        this.recipient = recipient;
        this.messageHash = messageHash;
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getMessageHash() {
        return messageHash;
    }

    // Setter (in case we want to change status later)
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // Full detail view
        return String.format("ID: %s | Hash: %s | Recipient: %s | Status: %s | Content: %s", messageID, messageHash, recipient, status, content);
    }
    
    // toString for Report/Short view
    public String toShortString() {
        // Shortens the content for list views
        String shortenedContent = content.length() > 50 ? content.substring(0, 50) + "..." : content;
        return String.format("ID: %s | Status: %s | Recipient: %s | Content: %s", messageID, status, recipient, shortenedContent);
    }
}