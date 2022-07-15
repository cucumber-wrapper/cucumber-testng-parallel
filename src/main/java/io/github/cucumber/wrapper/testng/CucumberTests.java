package io.github.cucumber.wrapper.testng;

import io.github.cucumber.wrapper.testng.model.TestNGFeature;
import io.github.cucumber.wrapper.testng.model.TestNGScenario;
import io.github.cucumber.wrapper.testng.runner.CucumberRunner;
import io.github.cucumber.wrapper.testng.service.DataProviderAdapter;
import io.github.cucumber.wrapper.testng.service.HideTestParameterAdapter;
import org.testng.ITest;
import org.testng.TestNG;
import org.testng.annotations.*;
import org.testng.IAnnotationTransformer;

import java.lang.reflect.Method;
import java.util.Optional;

@Listeners({HideTestParameterAdapter.class, DataProviderAdapter.class})
public abstract class CucumberTests implements ITest {

    private final ThreadLocal<String> currentScenario = new ThreadLocal<>();
    private CucumberRunner cucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        cucumberRunner = new CucumberRunner(this.getClass());
    }

    @BeforeMethod(alwaysRun = true)
    public void renameMethod(Method testMethod, Object[] testParameters) {
        String featureName = testParameters[1].toString();
        String scenarioName = testParameters[0].toString();
        currentScenario.set(featureName + " -> " + scenarioName);
    }

    @SuppressWarnings("unused")
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void runScenarioSequentially(TestNGScenario scenario, TestNGFeature feature) {
        cucumberRunner.runScenario(scenario);
    }

    @DataProvider
    public Object[][] scenarios() {
        return Optional.ofNullable(cucumberRunner)
                .map(CucumberRunner::provideScenarios)
                .orElseGet(() -> new Object[0][0]);
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        Optional.ofNullable(cucumberRunner).ifPresentOrElse(
                CucumberRunner::finish,
                () -> {}
        );
    }

    @Override
    public String getTestName() {
        return currentScenario.get();
    }
}