package io.github.cucumber.wrapper.testng;

import io.github.cucumber.wrapper.testng.model.TestNGFeature;
import io.github.cucumber.wrapper.testng.model.TestNGScenario;
import io.github.cucumber.wrapper.testng.runner.CucumberRunner;
import io.github.cucumber.wrapper.testng.listener.CucumberTestListener;
import io.github.cucumber.wrapper.testng.listener.ParallelDataProviderAdapter;
import io.github.cucumber.wrapper.testng.listener.HideTestParameterAdapter;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.xml.XmlSuite;

import java.lang.reflect.Method;
import java.util.Optional;

@Listeners({
        HideTestParameterAdapter.class,
        ParallelDataProviderAdapter.class,
        CucumberTestListener.class
})
public abstract class CucumberTests implements ITest {

    private final ThreadLocal<String> currentScenario = new ThreadLocal<>();
    private CucumberRunner cucumberRunner;

    @BeforeSuite(alwaysRun = true)
    public void setParallelSuites(ITestContext testContext) {
        testContext.getSuite().getXmlSuite().setVerbose(0);
        testContext.getSuite().getXmlSuite().setParallel(XmlSuite.ParallelMode.METHODS);
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        cucumberRunner = new CucumberRunner(this.getClass());
    }

    @BeforeMethod(alwaysRun = true)
    public synchronized void renameMethod(Method testMethod, Object[] testParameters) {
        String featureName = testParameters[1].toString();
        String scenarioName = testParameters[0].toString();
        currentScenario.set(featureName + " -> " + scenarioName);
    }

    @SuppressWarnings("unused")
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "sequentialScenarios")
    public void runScenarioSequentially(TestNGScenario scenario, TestNGFeature feature) {
        cucumberRunner.runScenario(scenario);
    }

    @SuppressWarnings("unused")
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "parallelScenarios")
    public void runScenarioInParallel(TestNGScenario scenario, TestNGFeature feature) {
        cucumberRunner.runScenario(scenario);
    }

    @DataProvider
    public Object[][] sequentialScenarios() {
        return Optional.ofNullable(cucumberRunner)
                .map(CucumberRunner::provideSequentialScenarios)
                .orElseGet(() -> new Object[0][0]);
    }

    @DataProvider(parallel = true)
    public Object[][] parallelScenarios() {
        return Optional.ofNullable(cucumberRunner)
                .map(CucumberRunner::provideParallelScenarios)
                .orElseGet(() -> new Object[0][0]);
    }

    @AfterClass(alwaysRun = true)
    public synchronized void tearDownClass(ITestContext iTestContext) {
        Optional.ofNullable(cucumberRunner).ifPresentOrElse(
                (r) -> {
                    r.finish();
                    cucumberRunner = null;
                },
                () -> {}
        );
    }

    @Override
    public synchronized String getTestName() {
        return currentScenario.get();
    }
}