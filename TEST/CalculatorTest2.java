package com.eval.tests;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Method;

public class CalculatorTest2 {

    public CalculatorTest2() {
    }

    @TestFactory
    List<DynamicTest> generateDynamicTests() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            // Assuming Calculator class is already loaded and instance is created
            Class<?> calculatorClass = Class.forName("com.eval.submissions.Calculator2");
            Object calculatorInstance = calculatorClass.getDeclaredConstructor().newInstance();

            Method addMethod = calculatorClass.getMethod("add2", int.class, int.class);
            dynamicTests.add(DynamicTest.dynamicTest("Test Calculator2.add2", () -> {
                try {
                    assertEquals(5, addMethod.invoke(calculatorInstance, 2, 3));

                } catch (AssertionError e) {
                    System.out.println(
                            "Expected add2(2, 3) to return 5, but got " + addMethod.invoke(calculatorInstance, 2, 3));
                    throw new AssertionError("test faileds");
                }
            }));
            // for assertion errors to return the helpful message, check javatestrunner ->
            // test execution listener
            Method subtractMethod = calculatorClass.getMethod("subtract", int.class, int.class);
            dynamicTests.add(DynamicTest.dynamicTest("Test Calculator2.subtract", () -> {
                try {
                    assertEquals(12, subtractMethod.invoke(calculatorInstance, 5, 3));

                } catch (AssertionError e) {

                    System.out.println("Expected subtract(5, 3) to return 2, but got "
                            + subtractMethod.invoke(calculatorInstance, 5, 3));
                    throw new AssertionError("Expected subtract(5, 3) to return 2, but got "
                            + subtractMethod.invoke(calculatorInstance, 5, 3));
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicTests;
    }
}
