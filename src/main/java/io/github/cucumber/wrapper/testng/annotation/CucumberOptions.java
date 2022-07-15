package io.github.cucumber.wrapper.testng.annotation;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.snippets.SnippetType;
import io.github.cucumber.wrapper.testng.service.NoObjectFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CucumberOptions {

    boolean dryRun() default false;

    boolean strict() default true;

    String[] features() default {};

    String[] glue() default {};

    String[] extraGlue() default {};

    String tags() default "";

    String[] plugin() default {};

    boolean publish() default false;

    boolean monochrome() default false;

    String[] name() default {};

    SnippetType snippets() default SnippetType.UNDERSCORE;

    Class<? extends ObjectFactory> objectFactory() default NoObjectFactory.class;

    ParallelOptions parallelOptions() default @ParallelOptions;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface ParallelOptions {
        int threads() default 1;
        String parallelTag() default "@Independent";
    }
}
