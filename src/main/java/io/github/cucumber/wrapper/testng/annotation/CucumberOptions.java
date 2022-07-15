package io.github.cucumber.wrapper.testng.annotation;

import io.cucumber.core.snippets.SnippetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CucumberOptions {

    boolean dryRun() default false;

    String[] features() default {};

    String[] glue() default {};

    String[] extraGlue() default {};

    String tags() default "";

    String[] plugin() default {};

    boolean publish() default false;

    boolean monochrome() default false;

    String[] name() default {};

    SnippetType snippets() default SnippetType.UNDERSCORE;
}
