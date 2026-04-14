package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        //features = {"src/test/java/features/EmployeeLogin.feature"},
        //features = {"src/test/java/features/EmployeeDashboard.feature"},
        //features = {"src/test/java/features/EmployeeLeaveManagement.feature"},
        features = {"src/test/java/features/EmployeeAllowanceManagement.feature"},
        //dryRun = true,
        glue = { "steps", "hooks" },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true, // TO REMOVE JUNK CHARACTERS
        //tags = "@Test",
        plugin = { "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" })

public class TestRunner extends AbstractTestNGCucumberTests {
}
