package io.github.cucumber.wrapper.testng;

import io.cucumber.java.en.Given;
import org.testng.Assert;

public class AnySteps {

    @Given("any step")
    public void anyStep() throws Exception {
        Thread.sleep(3000);
    }

    @Given("any step 2")
    public void anyStep2() throws Exception {
        Thread.sleep(5000);
    }
}
