package io.github.cucumber.wrapper.testng.service;

import io.cucumber.core.options.CucumberOptionsAnnotationParser;
import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;

public interface ParallelCucumberOptions extends CucumberOptionsAnnotationParser.CucumberOptions {

    CucumberOptions.ParallelOptions parallelOptions();
}
