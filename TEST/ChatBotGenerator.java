import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ChatBotGenerator{
    private Class<?> chatBotGeneratorClass;

    @BeforeEach
    void setUp() throws Exception {
        chatBotGeneratorClass = Class.forName("ChatBotGenerator");
    }

    @TestFactory
    List<DynamicTest> generateLLMTests() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            Method generateChatBotLLMMethod = chatBotGeneratorClass.getMethod("generateChatBotLLM", int.class);
            
            // Test all LLM codes
            dynamicTests.add(DynamicTest.dynamicTest("Test generateChatBotLLM - LLaMa", 
                createStaticStringExecutable(chatBotGeneratorClass, generateChatBotLLMMethod, 1, "LLaMa")));
            
            dynamicTests.add(DynamicTest.dynamicTest("Test generateChatBotLLM - Mistral7B", 
                createStaticStringExecutable(chatBotGeneratorClass, generateChatBotLLMMethod, 2, "Mistral7B")));
            
            dynamicTests.add(DynamicTest.dynamicTest("Test generateChatBotLLM - Bard", 
                createStaticStringExecutable(chatBotGeneratorClass, generateChatBotLLMMethod, 3, "Bard")));
            
            dynamicTests.add(DynamicTest.dynamicTest("Test generateChatBotLLM - Claude", 
                createStaticStringExecutable(chatBotGeneratorClass, generateChatBotLLMMethod, 4, "Claude")));
            
            dynamicTests.add(DynamicTest.dynamicTest("Test generateChatBotLLM - Solar", 
                createStaticStringExecutable(chatBotGeneratorClass, generateChatBotLLMMethod, 5, "Solar")));
            
            dynamicTests.add(DynamicTest.dynamicTest("Test generateChatBotLLM - Default", 
                createStaticStringExecutable(chatBotGeneratorClass, generateChatBotLLMMethod, 0, "ChatGPT-3.5")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicTests;
    }

    private Executable createStaticStringExecutable(Class<?> clazz, Method method, Object param, String expected) {
        return () -> {
            String result = (String) method.invoke(null, param);
            assert result.equals(expected) : "Test failed: expected " + expected + " but got " + result;
        };
    }
}