/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mainapplication;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author vusan
 */
public class MessageStorageTest {
    
    public MessageStorageTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class MessageStorage.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        MessageStorage expResult = null;
        MessageStorage result = MessageStorage.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addMessage method, of class MessageStorage.
     */
    @Test
    public void testAddMessage() {
        System.out.println("addMessage");
        String content = "";
        String status = "";
        String recipient = "";
        MessageStorage instance = null;
        Message expResult = null;
        Message result = instance.addMessage(content, status, recipient);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMessages method, of class MessageStorage.
     */
    @Test
    public void testGetAllMessages() {
        System.out.println("getAllMessages");
        MessageStorage instance = null;
        List<Message> expResult = null;
        List<Message> result = instance.getAllMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSentMessages method, of class MessageStorage.
     */
    @Test
    public void testGetSentMessages() {
        System.out.println("getSentMessages");
        MessageStorage instance = null;
        List<Message> expResult = null;
        List<Message> result = instance.getSentMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisregardedMessages method, of class MessageStorage.
     */
    @Test
    public void testGetDisregardedMessages() {
        System.out.println("getDisregardedMessages");
        MessageStorage instance = null;
        List<Message> expResult = null;
        List<Message> result = instance.getDisregardedMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStoredMessages method, of class MessageStorage.
     */
    @Test
    public void testGetStoredMessages() {
        System.out.println("getStoredMessages");
        MessageStorage instance = null;
        List<Message> expResult = null;
        List<Message> result = instance.getStoredMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLongestSentMessage method, of class MessageStorage.
     */
    @Test
    public void testGetLongestSentMessage() {
        System.out.println("getLongestSentMessage");
        MessageStorage instance = null;
        Message expResult = null;
        Message result = instance.getLongestSentMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findMessageByID method, of class MessageStorage.
     */
    @Test
    public void testFindMessageByID() {
        System.out.println("findMessageByID");
        String messageID = "";
        MessageStorage instance = null;
        Message expResult = null;
        Message result = instance.findMessageByID(messageID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchMessagesByRecipient method, of class MessageStorage.
     */
    @Test
    public void testSearchMessagesByRecipient() {
        System.out.println("searchMessagesByRecipient");
        String recipient = "";
        MessageStorage instance = null;
        List<Message> expResult = null;
        List<Message> result = instance.searchMessagesByRecipient(recipient);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteMessageByHash method, of class MessageStorage.
     */
    @Test
    public void testDeleteMessageByHash() {
        System.out.println("deleteMessageByHash");
        String hashOrId = "";
        MessageStorage instance = null;
        boolean expResult = false;
        boolean result = instance.deleteMessageByHash(hashOrId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
