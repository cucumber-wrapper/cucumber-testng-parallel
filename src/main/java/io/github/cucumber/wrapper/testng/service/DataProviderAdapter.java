package io.github.cucumber.wrapper.testng.service;

import io.github.cucumber.wrapper.testng.annotation.CucumberOptions;
import io.github.cucumber.wrapper.testng.annotation.ParallelOptions;
import io.github.cucumber.wrapper.testng.model.TestNGScenario;
import org.testng.*;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.internal.TestNGMethod;
import org.testng.internal.annotations.DataProviderAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DataProviderAdapter implements IDataProviderInterceptor, IDataProviderListener {

    @Override
    public Iterator<Object[]> intercept(Iterator<Object[]> original,
                                        IDataProviderMethod dataProviderMethod,
                                        ITestNGMethod method,
                                        ITestContext iTestContext) {



        Optional.ofNullable(method.getTestClass().getRealClass().getAnnotation(ParallelOptions.class))
                .ifPresent(po -> {
                    iTestContext.getCurrentXmlTest().getSuite().setDataProviderThreadCount(po.threads());
                    try {
                        Field field = dataProviderMethod.getClass().getDeclaredField("annotation");
                        field.setAccessible(true);
                        DataProviderAnnotation dataProviderAnnotation = (DataProviderAnnotation) field.get(dataProviderMethod);
                        dataProviderAnnotation.setParallel(true);
                        field.set(dataProviderMethod, dataProviderAnnotation);
                        field.setAccessible(false);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return original;
    }
}
