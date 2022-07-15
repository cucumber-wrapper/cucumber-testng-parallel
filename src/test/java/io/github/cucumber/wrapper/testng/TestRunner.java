package io.github.cucumber.wrapper.testng;

import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;
import io.github.cucumber.wrapper.testng.annotation.ParallelOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        plugin = "pretty"
)
@ParallelOptions(threads = 10, parallelTag = "@Parallel")
public class TestRunner extends CucumberTests {}
