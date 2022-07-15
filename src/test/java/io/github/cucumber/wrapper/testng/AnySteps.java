package io.github.cucumber.wrapper.testng;

import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

@Slf4j
public class AnySteps {

    @Given("any step")
    public void anyStep() throws Exception {
        log.info("SUCCESS!!!");
        Thread.sleep(3000);
    }

    @Given("any step 2")
    public void anyStep2() throws Exception {
        Assert.fail();
        log.info("AAAAAAAAAAAAAAa");
        Thread.sleep(5000);
    }
}
