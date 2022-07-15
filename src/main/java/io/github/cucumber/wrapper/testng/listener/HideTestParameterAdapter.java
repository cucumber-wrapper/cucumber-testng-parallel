package io.github.cucumber.wrapper.testng.listener;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

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
