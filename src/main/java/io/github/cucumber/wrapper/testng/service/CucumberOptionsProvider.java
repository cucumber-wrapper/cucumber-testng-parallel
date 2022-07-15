package io.github.cucumber.wrapper.testng.service;

import io.cucumber.core.options.CucumberOptionsAnnotationParser;
import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;

import java.util.Optional;

public class CucumberOptionsProvider implements CucumberOptionsAnnotationParser.OptionsProvider {

    @Override
    public CucumberOptionsAnnotationParser.CucumberOptions getOptions(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(CucumberOptions.class))
                .map(CucumberOptionsImpl::new)
                .orElse(null);
    }
}
