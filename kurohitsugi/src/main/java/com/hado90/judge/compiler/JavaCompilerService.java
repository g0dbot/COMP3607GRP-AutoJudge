package com.hado90.judge.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.util.List;

/**
 * This class provides a service for compiling Java source files using the system's Java compiler.
 * It utilizes the Java Compiler API to compile Java code provided as a list of compile options.
 */
public class JavaCompilerService {

    /**
     * Compiles Java source files using the provided compile options.
     * The options should include the file paths to the Java source files to be compiled, 
     * as well as any necessary compiler arguments.
     * 
     * @param compileOptions a list of compile options, including paths to Java files and compiler arguments.
     * @throws RuntimeException if the Java compiler is not available or if compilation fails.
     */
    public void compile(List<String> compileOptions) {
        // Get the system's Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        // Check if the compiler is available (i.e., JDK is installed)
        if (compiler == null) {
            throw new RuntimeException("Java Compiler is not available. Ensure JDK is installed.");
        }
        
        // Run the compiler with the provided compile options
        compiler.run(null, null, null, compileOptions.toArray(new String[0]));
    }
}

