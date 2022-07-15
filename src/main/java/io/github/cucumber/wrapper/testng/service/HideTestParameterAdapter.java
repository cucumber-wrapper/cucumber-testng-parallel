package io.github.cucumber.wrapper.testng.service;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.concurrent.atomic.AtomicInteger;

public class HideTestParameterAdapter extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult result) {
        result.setParameters(new Object[]{});
    }
}
