import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ChatBotPlatform {
    private Class<?> chatBotPlatformClass;
    private Object chatBotPlatformInstance;

    @BeforeEach
    void setUp() throws Exception {
        chatBotPlatformClass = Class.forName("ChatBotPlatform");
        chatBotPlatformInstance = chatBotPlatformClass.getDeclaredConstructor().newInstance();
    }

    @TestFactory
    List<DynamicTest> generatePlatformTests() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            // Test addChatBot
            Method addChatBotMethod = chatBotPlatformClass.getMethod("addChatBot", int.class);
            dynamicTests.add(DynamicTest.dynamicTest("Test addChatBot", 
                createBooleanExecutable(chatBotPlatformInstance, addChatBotMethod, 1, true)));

            // Test getChatBotList
            Method getChatBotListMethod = chatBotPlatformClass.getMethod("getChatBotList");
            dynamicTests.add(DynamicTest.dynamicTest("Test getChatBotList empty", 
                createStringExecutable(chatBotPlatformInstance, getChatBotListMethod, "")));

            // Test interactWithBot
            Method interactWithBotMethod = chatBotPlatformClass.getMethod("interactWithBot", int.class, String.class);
            dynamicTests.add(DynamicTest.dynamicTest("Test interactWithBot invalid number", 
                createInteractWithBotExecutable(chatBotPlatformInstance, interactWithBotMethod, 7, "test message", 
                "Incorrect Bot Number (7) Selected. Try again")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicTests;
    }

    private Executable createBooleanExecutable(Object instance, Method method, Object param, boolean expected) {
        return () -> {
            boolean result = (boolean) method.invoke(instance, param);
            assert result == expected : "Test failed: expected " + expected + " but got " + result;
        };
    }

    private Executable createStringExecutable(Object instance, Method method, String expected) {
        return () -> {
            String result = (String) method.invoke(instance);
            assert result.equals(expected) : "Test failed: expected " + expected + " but got " + result;
        };
    }

    private Executable createInteractWithBotExecutable(Object instance, Method method, int botNumber, 
            String message, String expected) {
        return () -> {
            String result = (String) method.invoke(instance, botNumber, message);
            assert result.equals(expected) : "Test failed: expected " + expected + " but got " + result;
        };
    }
}