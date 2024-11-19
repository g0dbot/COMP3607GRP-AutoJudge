package com.eval.tests;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Method;

public class ChatBotTest {

    @TestFactory
    List<DynamicTest> generateDynamicTests() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            Class<?> chatBotClass = Class.forName("com.eval.submissions.ChatBot");
            Object chatBotInstance = chatBotClass.getDeclaredConstructor().newInstance();

            Method getChatBotNameMethod = chatBotClass.getMethod("getChatBotName");
            dynamicTests.add(DynamicTest.dynamicTest("Test getChatBotName", () -> {
                assertEquals("ChatGPT-3.5", getChatBotNameMethod.invoke(chatBotInstance));
            }));

            Method getNumResponsesGeneratedMethod = chatBotClass.getMethod("getNumResponsesGenerated");
            dynamicTests.add(DynamicTest.dynamicTest("Test getNumResponsesGenerated", () -> {
                assertEquals(0, getNumResponsesGeneratedMethod.invoke(chatBotInstance));
            }));

            Method promptMethod = chatBotClass.getMethod("prompt", String.class);
            dynamicTests.add(DynamicTest.dynamicTest("Test prompt with a message", () -> {
                assertEquals("(Message# 1) Response from ChatGPT-3.5 >> generatedTextHere",
                        promptMethod.invoke(chatBotInstance, "test message"));
            }));

            Method getTotalNumResponsesGeneratedMethod = chatBotClass.getMethod("getTotalNumResponsesGenerated");
            dynamicTests.add(DynamicTest.dynamicTest("Test getTotalNumResponsesGenerated", () -> {
                assertEquals(0, getTotalNumResponsesGeneratedMethod.invoke(null));
            }));

            Method getTotalNumMessagesRemainingMethod = chatBotClass.getMethod("getTotalNumMessagesRemaining");
            dynamicTests.add(DynamicTest.dynamicTest("Test getTotalNumMessagesRemaining", () -> {
                assertEquals(10, getTotalNumMessagesRemainingMethod.invoke(null));
            }));

            Method limitReachedMethod = chatBotClass.getMethod("limitReached");
            dynamicTests.add(DynamicTest.dynamicTest("Test limitReached", () -> {
                assertEquals(false, limitReachedMethod.invoke(null));
            }));

        } catch (Exception e) {
            System.err.println("Error setting up dynamic tests: " + e.getMessage());
            e.printStackTrace();
        }

        return dynamicTests;
    }
}
