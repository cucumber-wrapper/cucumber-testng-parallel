package io.github.cucumber.wrapper.testng.model;

import io.cucumber.core.gherkin.Pickle;

public class TestNGScenario implements ScenarioContainer {

    private final Pickle scenario;
    private final boolean isParametrized;

    public TestNGScenario(Pickle scenario) {
        this.scenario = scenario;
        this.isParametrized = scenario.getKeyword().equals("Scenario Outline");
    }

    @Override
    public Pickle retrieve() {
        return scenario;
    }

    @Override
    public synchronized String toString() {
        if (isParametrized) {
            return scenario.getName() + " [ParametersLine:" + scenario.getLocation().getLine() + "]";
        } else {
            return scenario.getName();
        }
    }
}
