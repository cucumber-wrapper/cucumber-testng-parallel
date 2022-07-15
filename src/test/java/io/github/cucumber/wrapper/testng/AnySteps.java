package io.github.cucumber.wrapper.testng;

import io.cucumber.java.en.Given;

public class AnySteps {

    @Given("any step")
    public void anyStep() throws Exception {
        System.out.println("SUCCESS!!!");
        Thread.sleep(3000);
    }

    @Given("any step 2")
    public void anyStep2() throws Exception {
        System.out.println("AAAAAAAAAAAAAAa");
        Thread.sleep(3000);
    }
}
