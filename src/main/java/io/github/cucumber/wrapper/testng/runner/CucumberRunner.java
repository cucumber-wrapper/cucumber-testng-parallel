package io.github.cucumber.wrapper.testng.runner;

import io.cucumber.core.eventbus.EventBus;
import io.cucumber.core.exception.CucumberException;
import io.cucumber.core.feature.FeatureParser;
import io.cucumber.core.filter.Filters;
import io.cucumber.core.gherkin.Feature;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.options.*;
import io.cucumber.core.plugin.PluginFactory;
import io.cucumber.core.plugin.Plugins;
import io.cucumber.core.resource.ClassLoaders;
import io.cucumber.core.runtime.*;
import io.github.cucumber.wrapper.testng.model.TestNGFeature;
import io.github.cucumber.wrapper.testng.model.TestNGScenario;
import io.github.cucumber.wrapper.testng.service.CucumberOptionsImpl;
import io.github.cucumber.wrapper.testng.service.CucumberOptionsProvider;
import io.github.cucumber.wrapper.testng.service.TestCaseResultObserver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.IOException;
import java.time.Clock;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.cucumber.wrapper.testng.service.TestCaseResultObserver.observe;
import static java.util.stream.Collectors.toList;

public class CucumberRunner {

    private final String parallelTag;
    private final Predicate<Pickle> filters;
    private final List<Feature> features;
    private final CucumberExecutionContext context;

    public CucumberRunner(Class<?> clazz) {
        RuntimeOptions propertiesFileOptions = new CucumberPropertiesParser()
                .parse(CucumberProperties.fromPropertiesFile())
                .build();

        CucumberOptionsProvider optionsProvider = new CucumberOptionsProvider();
        CucumberOptionsImpl co = optionsProvider.getOptions(clazz);
        parallelTag = co.parallelOptions().parallelTag();

        RuntimeOptions annotationOptions = new CucumberOptionsAnnotationParser()
                .withOptionsProvider(optionsProvider)
                .parse(clazz)
                .build(propertiesFileOptions);

        RuntimeOptions environmentOptions = new CucumberPropertiesParser()
                .parse(CucumberProperties.fromEnvironment())
                .build(annotationOptions);

        RuntimeOptions runtimeOptions = new CucumberPropertiesParser()
                .parse(CucumberProperties.fromSystemProperties())
                .enablePublishPlugin()
                .build(environmentOptions);

        EventBus bus = new TimeServiceEventBus(Clock.systemUTC(), UUID::randomUUID);

        Supplier<ClassLoader> classLoader = ClassLoaders::getDefaultClassLoader;
        FeatureParser parser = new FeatureParser(bus::generateId);
        FeaturePathFeatureSupplier featureSupplier = new FeaturePathFeatureSupplier(
                classLoader,
                runtimeOptions,
                parser
        );

        Plugins plugins = new Plugins(new PluginFactory(), runtimeOptions);
        ExitStatus exitStatus = new ExitStatus(runtimeOptions);
        plugins.addPlugin(exitStatus);
        ObjectFactoryServiceLoader objectFactoryServiceLoader = new ObjectFactoryServiceLoader(runtimeOptions);
        ObjectFactorySupplier objectFactorySupplier = new ThreadLocalObjectFactorySupplier(objectFactoryServiceLoader);
        BackendServiceLoader backendSupplier = new BackendServiceLoader(clazz::getClassLoader, objectFactorySupplier);
        this.filters = new Filters(runtimeOptions);
        TypeRegistryConfigurerSupplier typeRegistryConfigurerSupplier = new ScanningTypeRegistryConfigurerSupplier(
                classLoader,
                runtimeOptions
        );
        ThreadLocalRunnerSupplier runnerSupplier = new ThreadLocalRunnerSupplier(
                runtimeOptions,
                bus,
                backendSupplier,
                objectFactorySupplier,
                typeRegistryConfigurerSupplier
        );
        this.context = new CucumberExecutionContext(bus, exitStatus, runnerSupplier);

        plugins.setSerialEventBusOnEventListenerPlugins(bus);
        features = featureSupplier.get();
        context.startTestRun();
        features.forEach(context::beforeFeature);
    }

    public void runScenario(TestNGScenario scenario) {
        context.runTestCase(runner -> {
            try (TestCaseResultObserver observer = observe(runner.getBus())) {
                Pickle cucumberPickle = scenario.retrieve();
                runner.runPickle(cucumberPickle);
                observer.assertTestCasePassed();
            }
        });
    }

    public void finish() {
        context.finishTestRun();
    }

    public Object[][] provideSequentialScenarios() {
        return prepareScenarios(p -> !p.getTags().contains(parallelTag));
    }

    public Object[][] provideParallelScenarios() {
        return prepareScenarios(p -> p.getTags().contains(parallelTag));
    }

    private Object[][] prepareScenarios(Predicate<Pickle> parallelTagPredicate) {
        return features.stream()
                .flatMap(feature -> feature.getPickles().stream()
                        .filter(filters)
                        .filter(parallelTagPredicate)
                        .map(cucumberPickle -> new Object[] {
                                new TestNGScenario(cucumberPickle),
                                new TestNGFeature(feature)
                        }))
                .collect(toList())
                .toArray(new Object[0][0]);
    }
}
