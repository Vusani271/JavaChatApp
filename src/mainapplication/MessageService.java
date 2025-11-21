
package mainapplication;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * MessageService handles the user interaction logic for message creation and management,
 * implementing all the required functions (2.a through 2.f), plus the stored message report (2.g).
 * It relies on the MessageStorage Singleton for data management.
 */
public class MessageService {
    private final MessageStorage storage;
    private final Login sender; // To hold the registered user/sender details

    /**
     * Initializes the service, getting the Singleton instance of MessageStorage
     * and storing the details of the authenticated sender.
     * @param sender The authenticated Login object.
     */
    public MessageService(Login sender) {
        this.storage = MessageStorage.getInstance();
        this.sender = sender;
    }

    /**
     * 1. Send New Message / Save Draft (Main Menu Option 1)
     * Allows the user to input details and send multiple messages or save them as Stored/Disregarded.
     */
    public void sendMessages() {
        // NOTE: Assuming sender.getPhonenumber(), getName(), and getSurname() methods exist on Login.
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

        // Loop for each message entry
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
            
            // Ask for Status ("Sent", "Stored", or "Disregarded")
            String statusChoice = JOptionPane.showInputDialog(null, """
                                
                                Do you want to (S)end, (T)ore as Draft, or (D)isregard this message? (Enter S, T, or D)
                                S = Sent, T = Stored (Draft), D = Disregarded""", 
                "Action Choice", JOptionPane.QUESTION_MESSAGE);
            
            String status;
            if (statusChoice != null && statusChoice.trim().toUpperCase().startsWith("S")) {
                status = "Sent";
            } else if (statusChoice != null && statusChoice.trim().toUpperCase().startsWith("T")) {
                status = "Stored";
            } else if (statusChoice != null && statusChoice.trim().toUpperCase().startsWith("D")) {
                status = "Disregarded";
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice. Saving as Draft (Stored status).", "Warning", JOptionPane.WARNING_MESSAGE);
                status = "Stored"; // Default for unsent messages
            }
            
            // Add the message to storage
           
            Message lastMessage = storage.addMessage(content.trim(), status, recipientNumber.trim());
            
            String confirmationMessage = String.format("""
                                Message %d processed!
                                Action: %s
                                Recipient: %s
                                Message ID (Sequential): %s
                                Hash ID (Unique): %s""", 
                i, status, 
                recipientNumber.trim(),
                lastMessage.getMessageID(), 
                lastMessage.getMessageHash());
                
            JOptionPane.showMessageDialog(null, confirmationMessage, "Message Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * 2. View All Stored/Sent/Disregarded Messages (Main Menu Option 2)
     * Displays the secondary menu for message management options.
     */
    public void showMessages() {
        String subChoice = JOptionPane.showInputDialog(null, """
                                Select a view option:
                                1. Display Sender and Recipient of All Sent Messages (2.a)
                                2. Display Longest Sent Message (2.b)
                                3. Search Message by ID (2.c)
                                4. Search Messages by Recipient (2.d)
                                5. Delete Message by Hash/ID (2.e)
                                6. Display Full Sent Message Report (2.f)
                                7. Display Full Stored Message Report (2.g)
                                8. Show All Messages (Drafts, Sent, Disregarded)
                                9. Back to Main Menu""", 
            "Message Management Menu", JOptionPane.PLAIN_MESSAGE);

        if (subChoice == null || subChoice.trim().equals("9")) {
             return;
        }

        switch (subChoice.trim()) {
            case "1" -> displaySenderAndRecipient();
            case "2" -> displayLongestSentMessage();
            case "3" -> searchMessageByID();
            case "4" -> searchMessagesByRecipient();
            case "5" -> deleteMessageByHash();
            case "6" -> displaySentMessageReport();
            case "7" -> displayStoredMessageReport(); 
            case "8" -> {
                // Displays a simple list of all messages (Sent, Stored, Disregarded)
                List<Message> messages = storage.getAllMessages();
                if (messages.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No messages or drafts found.", "Message Viewer", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StringBuilder display = new StringBuilder("--- All Messages & Drafts ---\n\n");
                for (int i = 0; i < messages.size(); i++) {
                    Message m = messages.get(i);
                    // NOTE: Assuming m.toShortString() method exists.
                    display.append(String.format("%d. %s\n", (i + 1), m.toShortString()));
                }
                JOptionPane.showMessageDialog(null, display.toString(), "All Message Viewer", JOptionPane.PLAIN_MESSAGE);
            }
            default -> JOptionPane.showMessageDialog(null, "Invalid choice. Please select 1-9.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ----------------------------------------------------------------------
    //  Message Management Functions 
    // ----------------------------------------------------------------------

    /**
     * Display the sender and recipient of all sent messages .
     */
    public void displaySenderAndRecipient() {
        //  Assuming storage.getSentMessages() method exists.
        List<Message> sentMessages = storage.getSentMessages();
        
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.", "Sent Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        //  Assuming sender getters exist.
        StringBuilder output = new StringBuilder("--- Sender and Recipient of All Sent Messages ---\n");
        output.append("Sender: ").append(sender.getName()).append(" ").append(sender.getSurname()).append(" (").append(sender.getPhonenumber()).append(")\n\n");
        
        for (Message m : sentMessages) {
            //  Assuming m.getMessageID(), m.getMessageHash(), and m.getRecipient() methods exist.
            output.append(String.format("ID: %s | Hash: %s | Recipient: %s\n", m.getMessageID(), m.getMessageHash(), m.getRecipient()));
        }
        
        JOptionPane.showMessageDialog(null, output.toString(), " Sent Messages Detail", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Display the longest sent message .
     */
    public void displayLongestSentMessage() {
        //  Assuming storage.getLongestSentMessage() method exists.
        Message longest = storage.getLongestSentMessage();
        
        if (longest == null) {
            JOptionPane.showMessageDialog(null, "No sent messages found to determine the longest one.", " Longest Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        //  Assuming longest getters exist.
        String output = String.format("""
                    Longest Sent Message (Length: %d) 
                      ID: %s
                      Hash: %s
                      Recipient: %s
                      Message: %s""", 
                             longest.getContent().length(),
                             longest.getMessageID(), 
                             longest.getMessageHash(), 
                             longest.getRecipient(), 
                             longest.getContent());
        
        JOptionPane.showMessageDialog(null, output, " Longest Sent Message", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Search for a message ID and display the corresponding recipient and message .
     */
    public void searchMessageByID() {
        String searchID = JOptionPane.showInputDialog(null, "Enter the Message ID to search for (e.g., MSG001):", " Search by ID", JOptionPane.QUESTION_MESSAGE);
        
        if (searchID == null || searchID.trim().isEmpty()) return;
        
        //  Assuming storage.findMessageByID(String) method exists.
        Message found = storage.findMessageByID(searchID.trim());
        
        if (found != null) {
            //Assuming all required getters on 'found' exist.
            String output = String.format("""
                                Message Found 
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
            JOptionPane.showMessageDialog(null, output, " Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Message with ID " + searchID + " not found.", " Search Results", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Search for all the messages sent or stored regarding a particular recipient.
     */
    public void searchMessagesByRecipient() {
        String recipient = JOptionPane.showInputDialog(null, "Enter the Recipient ID/Number to search for:", " Search by Recipient", JOptionPane.QUESTION_MESSAGE);
        
        if (recipient == null || recipient.trim().isEmpty()) return;
        
        //  Assuming storage.searchMessagesByRecipient(String) method exists.
        List<Message> foundMessages = storage.searchMessagesByRecipient(recipient.trim());
        
        if (!foundMessages.isEmpty()) {
            StringBuilder output = new StringBuilder("--- Messages Found for Recipient: ").append(recipient).append(" ---\n\n");
            for (Message m : foundMessages) {
                // Assuming m.getMessageID(), m.getMessageHash(), m.getStatus(), and m.getContent() exist.
                output.append(String.format("ID: %s | Hash: %s | Status: %s | Content: %.50s...\n", m.getMessageID(), m.getMessageHash(), m.getStatus(), m.getContent()));
            }
            JOptionPane.showMessageDialog(null, output.toString(), " Recipient Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No messages found for recipient " + recipient + ".", " Search Results", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete a message using the message hash (ID)).
     */
    public void deleteMessageByHash() {
        String hash = JOptionPane.showInputDialog(null, "Enter the Message Hash/ID to delete (e.g., MSG001 or a full Hash ID):", " Delete by Hash", JOptionPane.QUESTION_MESSAGE);
        if (hash == null || hash.trim().isEmpty()) return;
        
        // Find the message content before deleting for the required success message (2.e image requirement)
        Message messageToDelete = storage.getAllMessages().stream()
                .filter(m -> m.getMessageHash().equalsIgnoreCase(hash.trim()) || m.getMessageID().equalsIgnoreCase(hash.trim()))
                .findFirst()
                .orElse(null);

        // Assuming storage.deleteMessageByHash(String) method exists.
        if (messageToDelete != null && storage.deleteMessageByHash(hash.trim())) {
            // System returns the success message as required by the unit tests (image_c53ffb.png)
            String successMessage = String.format("Message \"%s\" successfully deleted.", messageToDelete.getContent());
            JOptionPane.showMessageDialog(null, successMessage, " Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Message with Hash/ID " + hash + " not found.", " Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays a report that lists the full details of all the sent messages.
     */
    public void displaySentMessageReport() {
        //Assuming storage.getSentMessages() method exists.
        List<Message> sentMessages = storage.getSentMessages();
        
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found for the report.", " Sent Message Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Assuming sender getters exist.
        StringBuilder report = new StringBuilder(" --- SENT MESSAGE REPORT ---\n");
        report.append("Sender: ").append(sender.getName()).append(" ").append(sender.getSurname()).append(" (").append(sender.getPhonenumber()).append(")\n\n");
        
        // Report required fields: Message ID, Message Hash, Recipient, Message
        for (Message m : sentMessages) {
            // Assuming m getters exist.
            report.append("---------------------------------\n");
            report.append("Message ID:   ").append(m.getMessageID()).append("\n");
            report.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            report.append("Recipient:    ").append(m.getRecipient()).append("\n");
            report.append("Message:      ").append(m.getContent()).append("\n");
        }
        report.append("---------------------------------\n");
        
        JOptionPane.showMessageDialog(null, report.toString(), " Sent Message Report", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Displays a report that lists the full details of all the stored messages (Drafts) (Item 2.g).
     */
    public void displayStoredMessageReport() {
        // Assuming storage.getStoredMessages() method exists.
        List<Message> storedMessages = storage.getStoredMessages();
        
        if (storedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No stored messages (drafts) found for the report.", " Stored Message Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder report = new StringBuilder(" --- STORED MESSAGE (DRAFT) REPORT ---\n");
        report.append("Total Drafts: ").append(storedMessages.size()).append("\n\n");
        
        // Report required fields: Message ID, Message Hash, Recipient, Message
        for (Message m : storedMessages) {
            // Assuming m getters exist.
            report.append("---------------------------------\n");
            report.append("Message ID:   ").append(m.getMessageID()).append("\n");
            report.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            report.append("Recipient:    ").append(m.getRecipient()).append("\n");
            report.append("Message:      ").append(m.getContent()).append("\n");
        }
        report.append("---------------------------------\n");
        
        JOptionPane.showMessageDialog(null, report.toString(), " Stored Message Report", JOptionPane.PLAIN_MESSAGE);
    }
}