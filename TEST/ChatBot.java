import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ChatBot {
    private Class<?> chatBotClass;
    private Object chatBotInstance;

    @BeforeEach
    void setUp() throws Exception {
        chatBotClass = Class.forName("ChatBot");
        chatBotInstance = chatBotClass.getDeclaredConstructor().newInstance();
    }

    @TestFactory
    List<DynamicTest> generateInstanceMethodTests() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            // Test instance methods
            Method getChatBotNameMethod = chatBotClass.getMethod("getChatBotName");
            dynamicTests.add(DynamicTest.dynamicTest("Test getChatBotName",
                    createStringExecutable(chatBotInstance, getChatBotNameMethod, "ChatGPT-3.5")));

            Method getNumResponsesGeneratedMethod = chatBotClass.getMethod("getNumResponsesGenerated");
            dynamicTests.add(DynamicTest.dynamicTest("Test getNumResponsesGenerated",
                    createIntExecutable(chatBotInstance, getNumResponsesGeneratedMethod, 0)));

            // Test prompt method
            Method promptMethod = chatBotClass.getMethod("prompt", String.class);
            dynamicTests.add(DynamicTest.dynamicTest("Test prompt with available messages",
                    createStringExecutable(chatBotInstance, promptMethod, "test message",
                            "(Message# 1) Response from ChatGPT-3.5 >> generatedTextHere")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicTests;
    }

    @TestFactory
    List<DynamicTest> generateStaticMethodTests() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            // Test static methods
            Method getTotalNumResponsesGeneratedMethod = chatBotClass.getMethod("getTotalNumResponsesGenerated");
            dynamicTests.add(DynamicTest.dynamicTest("Test getTotalNumResponsesGenerated",
                    createStaticIntExecutable(chatBotClass, getTotalNumResponsesGeneratedMethod, 0)));

            Method getTotalNumMessagesRemainingMethod = chatBotClass.getMethod("getTotalNumMessagesRemaining");
            dynamicTests.add(DynamicTest.dynamicTest("Test getTotalNumMessagesRemaining",
                    createStaticIntExecutable(chatBotClass, getTotalNumMessagesRemainingMethod, 10)));

            Method limitReachedMethod = chatBotClass.getMethod("limitReached");
            dynamicTests.add(DynamicTest.dynamicTest("Test limitReached",
                    createStaticBooleanExecutable(chatBotClass, limitReachedMethod, false)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicTests;
    }

    private Executable createStringExecutable(Object instance, Method method, String expected) {
        return () -> {
            String result = (String) method.invoke(instance);
            assert result.equals(expected) : "Test failed: expected " + expected + " but got " + result;
        };
    }

    private Executable createStringExecutable(Object instance, Method method, String input, String expected) {
        return () -> {
            String result = (String) method.invoke(instance, input);
            assert result.equals(expected) : "Test failed: expected " + expected + " but got " + result;
        };
    }

    private Executable createIntExecutable(Object instance, Method method, int expected) {
        return () -> {
            int result = (int) method.invoke(instance);
            assert result == expected : "Test failed: expected " + expected + " but got " + result;
        };
    }

    private Executable createStaticIntExecutable(Class<?> clazz, Method method, int expected) {
        return () -> {
            int result = (int) method.invoke(null);
            assert result == expected : "Test failed: expected " + expected + " but got " + result;
        };
    }

    private Executable createStaticBooleanExecutable(Class<?> clazz, Method method, boolean expected) {
        return () -> {
            boolean result = (boolean) method.invoke(null);
            assert result == expected : "Test failed: expected " + expected + " but got " + result;
        };
    }
}