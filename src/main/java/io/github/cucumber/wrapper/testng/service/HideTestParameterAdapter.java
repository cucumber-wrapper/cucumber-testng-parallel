package io.github.cucumber.wrapper.testng.service;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.concurrent.atomic.AtomicInteger;

public class HideTestParameterAdapter extends TestListenerAdapter {

    private final ThreadLocal<Object[]> originalParameters = new ThreadLocal<>();

    @Override
    public synchronized void onTestStart(ITestResult result) {
        originalParameters.set(result.getParameters());
        result.setParameters(new Object[]{});
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        result.setParameters(originalParameters.get());
        super.onTestFailure(result);
    }
}
