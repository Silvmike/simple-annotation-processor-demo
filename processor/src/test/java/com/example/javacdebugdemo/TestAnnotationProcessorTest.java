package com.example.javacdebugdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class TestAnnotationProcessorTest {

    @Test
    void test() throws IOException {

        Path tempOutputDir = Files.createTempDirectory("javac-debug-output-");
        Path tempSourceDir = Files.createTempDirectory("javac-debug-src-");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {

            Iterable<? extends JavaFileObject> sourcesFromResources =
                fileManager.getJavaFileObjectsFromFiles(List.of(copyResource().toFile()));

            JavaCompiler.CompilationTask compilerTask = compiler.getTask(
                null,
                fileManager,
                null,
                Arrays.asList(
                    "-d", tempOutputDir.toAbsolutePath().toString(),
                    "-s", tempSourceDir.toAbsolutePath().toString()
                ),
                null,
                sourcesFromResources);

            compilerTask.setProcessors(List.of(new TestAnnotationProcessor()));

            compilerTask.call();
        }

        Assertions.assertTrue(Files.exists(tempSourceDir.resolve("TestImpl.java")));
        Assertions.assertTrue(Files.exists(tempOutputDir.resolve("TestImpl.class")));
    }

    private static Path copyResource() throws IOException {
        Path tempDir = Files.createTempDirectory("test-src-");
        Path copy = tempDir.resolve("Test.java");
        try (InputStream input = TestAnnotationProcessor.class.getResourceAsStream("/test/src/Test.java")) {
            Files.copy(input, copy);
        }
        return copy;
    }

}
