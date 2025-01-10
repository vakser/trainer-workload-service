package com.epam.learn.trainerworkloadservice.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.epam.learn.trainerworkloadservice.cucumber",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class RunCucumberTests {
}

