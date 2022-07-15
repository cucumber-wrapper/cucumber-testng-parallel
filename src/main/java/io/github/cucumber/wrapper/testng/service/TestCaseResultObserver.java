package io.github.cucumber.wrapper.testng.service;

import io.cucumber.core.eventbus.EventBus;
import io.cucumber.plugin.event.EventPublisher;
import org.testng.SkipException;

import java.util.function.Function;

public class TestCaseResultObserver implements AutoCloseable {

    private static final String SKIP_MESSAGE = "This scenario is skipped";
    private final io.cucumber.core.runtime.TestCaseResultObserver delegate;

    private TestCaseResultObserver(EventPublisher bus) {
        this.delegate = new io.cucumber.core.runtime.TestCaseResultObserver(bus);
    }

    public static TestCaseResultObserver observe(EventBus bus) {
        return new TestCaseResultObserver(bus);
    }

    public synchronized void assertTestCasePassed() {
        delegate.assertTestCasePassed(
                () -> new SkipException(SKIP_MESSAGE),
                (exception) -> exception instanceof SkipException
                        ? exception
                        : new SkipException(exception.getMessage(), exception),
                (l) -> new SkipException(""),
                Function.identity());
    }

    @Override
    public void close() {
        delegate.close();
    }
}
