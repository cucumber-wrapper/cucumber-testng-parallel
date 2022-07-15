package io.github.cucumber.wrapper.testng;

import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        plugin = "pretty",
        parallelOptions = @CucumberOptions.ParallelOptions(threads = 7)
)
public class TestRunner extends CucumberTests {}
