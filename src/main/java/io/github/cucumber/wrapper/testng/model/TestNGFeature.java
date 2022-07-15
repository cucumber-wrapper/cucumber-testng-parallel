package io.github.cucumber.wrapper.testng.model;

import io.cucumber.core.gherkin.Feature;

public class TestNGFeature implements FeatureContainer {

    private final Feature feature;

    public TestNGFeature(Feature feature) {
        this.feature = feature;
    }

    @Override
    public Feature feature() {
        return feature;
    }

    @Override
    public synchronized String toString() {
        return feature.getName().orElse("Undefined Feature");
    }
}
