package io.github.cucumber.wrapper.testng.observer;

import io.cucumber.core.runtime.TestCaseResultObserver;
import io.cucumber.plugin.event.EventPublisher;

public class ScenarioResultObserver implements AutoCloseable {

    private final TestCaseResultObserver delegate;

    public ScenarioResultObserver(EventPublisher bus) {
        this.delegate = new TestCaseResultObserver(bus);
    }

    @Override
    public void close() {
        delegate.close();
    }
}
