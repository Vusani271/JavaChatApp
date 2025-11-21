
package mainapplication;

import javax.swing.JOptionPane;
import java.util.List;

// MessageService handles the user interaction logic for message creation and management,
// implementing all the required functions (2.a through 2.f).
public class MessageService {
    private final MessageStorage storage;
    private final Login sender; // To hold the registered user/sender details

    public MessageService(Login sender) {
        // Use the singleton instance of MessageStorage
        this.storage = MessageStorage.getInstance();
        this.sender = sender;
    }

    /**
     * 1. Send New Message / Save Draft (Main Menu Option 1)
     * Allows the user to input details and send multiple messages or save them as Stored.
     * This method handles the user input for the number of messages, recipient details, 
     * content, and choice to "Send" or "Store" (Draft) the message.
     */
    public void sendMessages() {
        // Use sender details for context
        String senderInfo = sender.getName() + " " + sender.getSurname() + " (" + sender.getPhonenumber() + ")";
        
        String numMessagesStr = JOptionPane.showInputDialog(null, 
            "You are sending messages as: " + senderInfo + "\n\n" +
            "How many messages do you want to process in this session?", 
            "Message Sender", JOptionPane.QUESTION_MESSAGE);
        
        if (numMessagesStr == null) return;
        
        int numMessages;
        try {
            numMessages = Integer.parseInt(numMessagesStr.trim());
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Number of messages must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered. Operation cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Loop for each message
        for (int i = 1; i <= numMessages; i++) {
            JOptionPane.showMessageDialog(null, "--- Starting Message " + i + " of " + numMessages + " ---", "Message Entry", JOptionPane.INFORMATION_MESSAGE);
            
            // Get Recipient Cellphone Number/ID
            String recipientNumber = JOptionPane.showInputDialog(null, "Enter Recipient Cellphone Number/ID:", "Recipient Details", JOptionPane.QUESTION_MESSAGE);
            if (recipientNumber == null || recipientNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Recipient number cannot be empty. Skipping message " + i + ".", "Error", JOptionPane.ERROR_MESSAGE);
                continue; 
            }
            
            // Get Message Content
            String content = JOptionPane.showInputDialog(null, "Enter the message content:", "Message Content", JOptionPane.PLAIN_MESSAGE);
            if (content == null || content.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Message content cannot be empty. Skipping message " + i + ".", "Error", JOptionPane.ERROR_MESSAGE);
                continue; 
            }
            
            // Ask for Status ("Sent" or "Stored")
            String statusChoice = JOptionPane.showInputDialog(null, """
                                                                    Do you want to (S)end or (T)ore this message as a Draft? (Enter S or T)
                                                                    S = Sent, T = Stored (Draft)""", 
                "Action Choice", JOptionPane.QUESTION_MESSAGE);
            
            String status;
            if (statusChoice != null && statusChoice.trim().toUpperCase().startsWith("S")) {
                status = "Sent";
            } else if (statusChoice != null && statusChoice.trim().toUpperCase().startsWith("T")) {
                status = "Stored";
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice. Saving as Draft (Stored status).", "Warning", JOptionPane.WARNING_MESSAGE);
                status = "Stored"; // Default for unsent messages
            }
            
            // Add the message and display the new ID/Hash
            Message lastMessage = storage.addMessage(content.trim(), status, recipientNumber.trim());
            
            String confirmationMessage = String.format("""
                                                       Message %d processed!
                                                       Action: %s
                                                       Recipient: %s
                                                       Message ID: %s
                                                       Hash ID: %s""", 
                i, status, 
                recipientNumber.trim(),
                lastMessage.getMessageID(), 
                lastMessage.getMessageHash());
                
            JOptionPane.showMessageDialog(null, confirmationMessage, "Message Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * 2. View All Stored/Sent/Disregarded Messages (Main Menu Option 2)
     */
    public void showMessages() {
        List<Message> messages = storage.getAllMessages();

        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages or drafts found.", "Message Viewer", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Build the display string for all messages
        StringBuilder display = new StringBuilder("--- All Messages & Drafts ---\n\n");
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            display.append(String.format("%d. %s\n", (i + 1), m.toShortString()));
        }
        
        JOptionPane.showMessageDialog(null, display.toString(), "All Message Viewer", JOptionPane.PLAIN_MESSAGE);
    }
    
    // ----------------------------------------------------------------------
    // --- Message Management Functions (Item 2.a - 2.f) ---
    // ----------------------------------------------------------------------

    /**
     * Display the sender and recipient of all sent messages (Item 2.a).
     */
    public void displaySenderAndRecipient() {
        List<Message> sentMessages = storage.getSentMessages();
        
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.", "Sent Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder output = new StringBuilder("--- Sender and Recipient of All Sent Messages ---\n");
        output.append("Sender: ").append(sender.getName()).append(" ").append(sender.getSurname()).append(" (").append(sender.getPhonenumber()).append(")\n\n");
        
        for (Message m : sentMessages) {
            output.append(String.format("ID: %s | Recipient: %s\n", m.getMessageID(), m.getRecipient()));
        }
        
        JOptionPane.showMessageDialog(null, output.toString(), "2.a Sent Messages Detail", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Display the longest sent message (Item 2.b).
     */
    public void displayLongestSentMessage() {
        Message longest = storage.getLongestSentMessage();
        
        if (longest == null) {
            JOptionPane.showMessageDialog(null, "No sent messages found to determine the longest one.", "2.b Longest Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String output = String.format("""
                                      --- Longest Sent Message (Length: %d) ---
                                      ID: %s
                                      Recipient: %s
                                      Message: %s""", 
                                      longest.getContent().length(),
                                      longest.getMessageID(), 
                                      longest.getRecipient(), 
                                      longest.getContent());
        
        JOptionPane.showMessageDialog(null, output, "2.b Longest Sent Message", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Search for a message ID and display the corresponding recipient and message (Item 2.c).
     */
    public void searchMessageByID() {
        String searchID = JOptionPane.showInputDialog(null, "Enter the Message ID to search for (e.g., MSG001):", "2.c Search by ID", JOptionPane.QUESTION_MESSAGE);
        
        if (searchID == null || searchID.trim().isEmpty()) return;
        
        Message found = storage.findMessageByID(searchID.trim());
        
        if (found != null) {
            String output = String.format("""
                                          --- Message Found ---
                                          ID: %s
                                          Hash ID: %s
                                          Status: %s
                                          Recipient: %s
                                          Message: %s""", 
                                          found.getMessageID(), 
                                          found.getMessageHash(),
                                          found.getStatus(),
                                          found.getRecipient(), 
                                          found.getContent());
            JOptionPane.showMessageDialog(null, output, "2.c Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Message with ID " + searchID + " not found.", "2.c Search Results", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Search for all the messages sent or stored regarding a particular recipient (Item 2.d).
     */
    public void searchMessagesByRecipient() {
        String recipient = JOptionPane.showInputDialog(null, "Enter the Recipient ID/Number to search for:", "2.d Search by Recipient", JOptionPane.QUESTION_MESSAGE);
        
        if (recipient == null || recipient.trim().isEmpty()) return;
        
        List<Message> foundMessages = storage.searchMessagesByRecipient(recipient.trim());
        
        if (!foundMessages.isEmpty()) {
            StringBuilder output = new StringBuilder("--- Messages Found for Recipient: ").append(recipient).append(" ---\n\n");
            for (Message m : foundMessages) {
                output.append(String.format("ID: %s | Status: %s | Content: %.50s...\n", m.getMessageID(), m.getStatus(), m.getContent()));
            }
            JOptionPane.showMessageDialog(null, output.toString(), "2.d Recipient Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No messages found for recipient " + recipient + ".", "2.d Search Results", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete a message using the message hash (ID) (Item 2.e).
     */
    public void deleteMessageByHash() {
        String hash = JOptionPane.showInputDialog(null, "Enter the Message Hash/ID to delete (e.g., MSG001-123 or MSG002):", "2.e Delete by Hash", JOptionPane.QUESTION_MESSAGE);
        if (hash == null || hash.trim().isEmpty()) return;
        
        if (storage.deleteMessageByHash(hash.trim())) {
            // System returns the success message as required
            JOptionPane.showMessageDialog(null, "Message with Hash/ID: " + hash + " successfully deleted.", "2.e Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Message with Hash/ID " + hash + " not found.", "2.e Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Display a report that lists the full details of all the sent messages (Item 2.f).
     */
    public void displaySentMessageReport() {
        List<Message> sentMessages = storage.getSentMessages();
        
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found for the report.", "2.f Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder report = new StringBuilder("--- SENT MESSAGE REPORT ---\n");
        report.append("Sender: ").append(sender.getName()).append(" ").append(sender.getSurname()).append(" (").append(sender.getPhonenumber()).append(")\n\n");
        
        // Report required fields: Message Hash, Recipient, Message
        for (Message m : sentMessages) {
            report.append("---------------------------------\n");
            report.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            report.append("Recipient:    ").append(m.getRecipient()).append("\n");
            report.append("Message:      ").append(m.getContent()).append("\n");
        }
        report.append("---------------------------------\n");
        
        JOptionPane.showMessageDialog(null, report.toString(), "2.f Sent Message Report", JOptionPane.PLAIN_MESSAGE);
    }
}