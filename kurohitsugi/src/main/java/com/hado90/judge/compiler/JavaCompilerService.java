package com.hado90.judge.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import java.util.List;

public class JavaCompilerService {

    public void compile(List<String> compileOptions) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new RuntimeException("Java Compiler is not available. Ensure JDK is installed.");
        }
        compiler.run(null, null, null, compileOptions.toArray(new String[0]));
    }
}
