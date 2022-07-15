package io.github.cucumber.wrapper.testng.annotation.provider;

import io.cucumber.core.options.CucumberOptionsAnnotationParser;
import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;
import io.github.cucumber.wrapper.testng.annotation.CucumberOptionsImpl;

import java.util.Optional;

public class CucumberOptionsProvider implements CucumberOptionsAnnotationParser.OptionsProvider {

    @Override
    public CucumberOptionsImpl getOptions(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(CucumberOptions.class))
                .map(CucumberOptionsImpl::new)
                .orElse(null);
    }
}
