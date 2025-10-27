package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        //features = {"src/test/java/features/EmployeeLogin.feature"},
        features = {"src/test/java/features/EmployeeDashboard.feature"},
        //dryRun = true,
        glue = { "steps", "hooks" },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true, // TO REMOVE JUNK CHARACTERS
        //tags = "@IHP-61-003",
        plugin = { "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" })

public class TestRunner extends AbstractTestNGCucumberTests {
}
