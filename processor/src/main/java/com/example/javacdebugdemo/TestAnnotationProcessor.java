package com.example.javacdebugdemo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner9;

import java.io.IOException;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@SupportedAnnotationTypes("com.example.javacdebugdemo.TestAnnotation")
public class TestAnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        AtomicReference<String> packageName = new AtomicReference<>();
        AtomicReference<String> originalTypeName = new AtomicReference<>();

        var visitor = new ElementScanner9<Void, Void>() {

            @Override
            public Void visitPackage(PackageElement e, Void unused) {
                packageName.set(e.getQualifiedName().toString());
                return super.visitPackage(e, unused);
            }

            @Override
            public Void visitType(TypeElement e, Void unused) {
                originalTypeName.set(e.getSimpleName().toString());
                return super.visitType(e, unused);
            }
        };

        if (annotations.isEmpty()) return false;

        roundEnv.getRootElements().forEach(element -> {

            visitor.scan(element);

            try {

                var sourceObject = processingEnv.getFiler().createSourceFile(
                    originalTypeName.get() + "Impl", element
                );

                try (Writer writer = sourceObject.openWriter()) {
                    writer.write("/* Generated by " + getClass().getSimpleName() + " on " + ZonedDateTime.now() + " */\n");
                    writer.write(
                        "public class " + originalTypeName.get() + "Impl implements " + originalTypeName.get() + " { " +
                            "public static void main(String[] args) { " +
                            "System.out.println(\"Hello, world!\"); " +
                            "} " +
                            "}"
                    );
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return false;
    }

}