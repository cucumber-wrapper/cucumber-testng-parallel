package io.github.cucumber.wrapper.testng.service;

import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;
import org.testng.*;

import java.util.Iterator;
import java.util.Optional;

public class DataProviderAdapter implements IDataProviderInterceptor {

    @Override
    public synchronized Iterator<Object[]> intercept(Iterator<Object[]> original,
                                        IDataProviderMethod dataProviderMethod,
                                        ITestNGMethod method,
                                        ITestContext iTestContext) {
        CucumberOptions co = method.getTestClass().getRealClass().getAnnotation(CucumberOptions.class);
        if (dataProviderMethod.isParallel()) {
            iTestContext.getCurrentXmlTest().getSuite().setDataProviderThreadCount(co.parallelOptions().threads());
        }
        return original;
    }
}
