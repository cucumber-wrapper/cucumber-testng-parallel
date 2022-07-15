package io.github.cucumber.wrapper.testng.service;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.options.CucumberOptionsAnnotationParser;
import io.cucumber.core.snippets.SnippetType;
import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;

public class CucumberOptionsImpl implements ParallelCucumberOptions {

    private final CucumberOptions annotation;

    CucumberOptionsImpl(CucumberOptions annotation) {
        this.annotation = annotation;
    }
    @Override
    public boolean dryRun() {
        return annotation.dryRun();
    }

    @Override
    public boolean strict() {
        return annotation.strict();
    }

    @Override
    public String[] features() {
        return annotation.features();
    }

    @Override
    public String[] glue() {
        return annotation.glue();
    }

    @Override
    public String[] extraGlue() {
        return annotation.extraGlue();
    }

    @Override
    public String tags() {
        return annotation.tags();
    }

    @Override
    public String[] plugin() {
        return annotation.plugin();
    }

    @Override
    public boolean publish() {
        return annotation.publish();
    }

    @Override
    public boolean monochrome() {
        return annotation.monochrome();
    }

    @Override
    public String[] name() {
        return annotation.name();
    }

    @Override
    public SnippetType snippets() {
        return annotation.snippets();
    }

    @Override
    public Class<? extends ObjectFactory> objectFactory() {
        return (annotation.objectFactory() == NoObjectFactory.class) ? null : annotation.objectFactory();
    }

    @Override
    public CucumberOptions.ParallelOptions parallelOptions() {
        return annotation.parallelOptions();
    }
}
