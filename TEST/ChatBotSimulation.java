import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChatBotSimulation {
    private Class<?> simulationClass;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() throws Exception {
        simulationClass = Class.forName("ChatBotSimulation");
        // Setup output capture
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testMainMethodSequence() throws Exception {
        // Get the main method
        Method mainMethod = simulationClass.getMethod("main", String[].class);
        
        // Call main method
        String[] args = new String[0];
        mainMethod.invoke(null, (Object) args);
        
        // Get the output
        String output = outputStream.toString();
        
        // Reset System.out
        System.setOut(originalOut);
        
        // Test requirement 1: Hello World! printed first
        assertTrue(output.startsWith("Hello World!"), 
            "Program should start with 'Hello World!'");
        
        // Test requirement 3 & 4: ChatBots added and initial list printed
        assertTrue(output.contains("Your ChatBots"), 
            "Program should display list of ChatBots");
        assertTrue(output.contains("Bot Number: 0"), 
            "Program should show numbered ChatBots");
        assertTrue(output.contains("Total Messages Used: 0"), 
            "Initial messages used should be 0");
        
        // Test requirement 5: Verify interactions occurred
        int messageCount = countOccurrences(output, "Response from");
        assertTrue(messageCount > 0 && messageCount <= 15, 
            "Should have between 1 and 15 bot interactions");
        
        // Test requirement 6: Final list printed
        assertTrue(output.contains("Total Messages Used:"), 
            "Should show final message count");
        assertTrue(output.contains("Total Messages Remaining:"), 
            "Should show remaining messages");
    }

    @Test
    void testChatBotVarietyInSimulation() throws Exception {
        Method mainMethod = simulationClass.getMethod("main", String[].class);
        String[] args = new String[0];
        mainMethod.invoke(null, (Object) args);
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        // Verify at least one of each type of ChatBot was created
        assertTrue(output.contains("ChatGPT-3.5"), "Should have ChatGPT-3.5 bot");
        assertTrue(output.contains("LLaMa"), "Should have LLaMa bot");
        assertTrue(output.contains("Mistral7B"), "Should have Mistral7B bot");
        assertTrue(output.contains("Bard"), "Should have Bard bot");
        assertTrue(output.contains("Claude"), "Should have Claude bot");
        assertTrue(output.contains("Solar"), "Should have Solar bot");
    }

    @Test
    void testMessageLimitHandling() throws Exception {
        Method mainMethod = simulationClass.getMethod("main", String[].class);
        String[] args = new String[0];
        mainMethod.invoke(null, (Object) args);
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        // Check if limit message appears after maximum messages
        int limitMessageCount = countOccurrences(output, 
            "Daily Limit Reached. Wait 24 hours to resume chatbot usage");
        assertTrue(limitMessageCount > 0, 
            "Should show limit reached message after maximum messages");
    }

    @Test
    void testIncorrectBotNumberHandling() throws Exception {
        Method mainMethod = simulationClass.getMethod("main", String[].class);
        String[] args = new String[0];
        mainMethod.invoke(null, (Object) args);
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        // Verify incorrect bot number handling
        assertTrue(output.contains("Incorrect Bot Number"), 
            "Should handle incorrect bot numbers appropriately");
    }

    // Helper method to count occurrences of a string
    private int countOccurrences(String text, String search) {
        return (text.length() - text.replace(search, "").length()) / search.length();
    }
    
    // Additional helper method to verify output formatting
    @Test
    void testOutputFormatting() throws Exception {
        Method mainMethod = simulationClass.getMethod("main", String[].class);
        String[] args = new String[0];
        mainMethod.invoke(null, (Object) args);
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        // Check message format
        assertTrue(output.matches(".*\\(Message# \\d+\\) Response from .+ >> .*"), 
            "Messages should follow the correct format");
        
        // Check ChatBot list format
        assertTrue(output.matches(".*Bot Number: \\d+ ChatBot Name: .+ Number Messages Used: \\d+.*"), 
            "ChatBot list should follow the correct format");
        
        // Verify separator lines
        assertTrue(output.contains("----------------------"), 
            "Output should include proper separators");
    }
}