package steps;

import drivers.DriverInstance;

import ScenarioContext.ScenarioContext;
import drivers.DriverInstance;
import hooks.Hooks;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import qa.util.ExternalFunction;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.*;
import java.util.stream.Collectors;

public class EmployeeAllowanceManagementSteps extends DriverInstance {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    String expectedClaimType;
    String expectedStatus;
    String expectedSubmitDate;
    String expectedBillAmount;

    @When("employee click on Allowance Management")
    public void employeeClickOnAllowanceManagement() {
        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement allowanceMgmt = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Allowance Management']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(allowanceMgmt));
        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allowanceMgmt));
            allowanceMgmt.click();
            System.out.println("Clicked on 'Allowance Management' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", allowanceMgmt);
            js.executeScript("arguments[0].click();", allowanceMgmt);
            System.out.println("Clicked on 'Allowance Management' via JavaScript.");
        }

        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("employee can see Allowance Summary header")
    public void employeeCanSeeAllowanceSummaryHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Allowance Summary']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Allowance Summary header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Allowance Summary' not found within timeout!");
            Assert.fail("Header 'Allowance Summary' not found!");
        }
        allureScreenshot();
    }

    @When("employee click on New Claim button")
    public void employeeClickOnNewClaimButton() {
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='New Claim']]")));
        createButton.click();
        System.out.println("New Claim button clicked");
    }

    public void setDate(String fieldName, String dateStr) throws InterruptedException {

        // Formatter used for your input "dd MMMM yyyy"
        DateTimeFormatter inputFormatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd MMMM yyyy")
                        .toFormatter(Locale.ENGLISH);

        LocalDate targetDate = LocalDate.parse(dateStr, inputFormatter);


        // --- 1. Click calendar icon ---
        String calendarIconXpath =
                "//input[@name='" + fieldName + "']/following-sibling::div//*[contains(@class, 'app-icon')]";

        WebElement calendarIcon =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(calendarIconXpath)));

        try {
            calendarIcon.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("⚠ Icon not clickable, trying JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calendarIcon);

            // Try wrapper as fallback
            try {
                WebElement wrapper = calendarIcon.findElement(
                        By.xpath("./ancestor::div[contains(@class,'app-calendar-input__wrapper')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wrapper);
            } catch (Exception ignored) {}
        }


        // --- 2. Wait for calendar popup ---
        WebElement calendarPopup = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".app-calendar .calendar")));


        // --- 3. Month/Year labels ---
        WebElement monthLabel = calendarPopup.findElement(By.cssSelector(".header__month"));
        WebElement yearLabel  = calendarPopup.findElement(By.cssSelector(".header__year"));

        // Formatter for calendar header, supports uppercase
        DateTimeFormatter headerFormatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMMM yyyy")
                        .toFormatter(Locale.ENGLISH);


        // --- 4. Navigate to correct month ---
        while (true) {
            String displayedHeader =
                    monthLabel.getText().trim().toUpperCase() + " " + yearLabel.getText().trim();

            YearMonth displayedYM = YearMonth.parse(displayedHeader, headerFormatter);
            YearMonth targetYM = YearMonth.from(targetDate);

            if (displayedYM.equals(targetYM)) break;

            if (displayedYM.isBefore(targetYM)) {
                calendarPopup.findElement(By.cssSelector(".header__next")).click();
            } else {
                calendarPopup.findElement(By.cssSelector(".header__prev")).click();
            }

            // Wait for update before next loop
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElement(monthLabel, displayedYM.getMonth().name())
            ));
        }


        // --- 5. Select the day ---
        String dayXpath =
                "//div[contains(@class,'dates__day') and normalize-space(text())='" +
                        targetDate.getDayOfMonth() + "']";

        WebElement dayElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));
        dayElement.click();

        // --- 6. Click Ok button
        WebElement okBtn = driver.findElement(
                By.xpath("//button[contains(@class, 'app-button')][.//p[text()='Ok']]")
        );

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", okBtn);
        Thread.sleep(200);

        // JS click (more reliable)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okBtn);

        Assert.assertTrue(true, "Ok button clicked.");

        System.out.println("✔ Calendar date selected successfully for " + fieldName + ": " + dateStr);
    }

    @And("employee fill all the fields {string}, {string}, {string}, {string}, {string}")
    public void employeeFillAllTheFieldsClaimTypeBillAmountVisitDateClaimDescFilePath(String claimType, String billAmount, String visitDate, String claimDesc, String filePath) throws InterruptedException {
        // -------- Claim Type --------
        WebElement claimTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-claimType")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", claimTypeDropdown);
        claimTypeDropdown.click();

        WebElement assgTitleOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + claimType + "']")
        ));
        assgTitleOption.click();
        System.out.println("Claim Type selected: " + claimType);

        ScenarioContext.setContext("CLAIM_TYPE", claimType);

        // -------- Bill Amount (RM) --------
        WebElement billAmountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='amount']")
        ));
        billAmountInput.clear();
        billAmountInput.sendKeys(billAmount);
        System.out.println("Course Title entered: " + billAmount);

        ScenarioContext.setContext("BILL", billAmount);

        // -------- Visit Date ---------
        setDate("visitDate", visitDate);

        // --------- Claim Description -------
        WebElement courseDescInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@name='description']")
        ));
        courseDescInput.clear();
        courseDescInput.sendKeys(claimDesc);
        System.out.println("Description entered: " + claimDesc);

        // --------- Attachment ------
        WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));

        fileInput.sendKeys(filePath);

        allureScreenshot();
    }

    @Then("employee click on create button")
    public void employeeClickOnCreateButton() {
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Create']]")));
        createButton.click();
        System.out.println("Create button clicked");
    }

    @And("toast message shall display")
    public void toastMessageShallDisplay() {
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        String expectedMessage = "Claim has been submitted successfully";

        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        allureScreenshot();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }

    @When("employee click on Allowance History")
    public void employeeClickOnAllowanceHistory() {

        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement allowanceHist = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Allowance History']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(allowanceHist));
        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allowanceHist));
            allowanceHist.click();
            System.out.println("Clicked on 'Allowance History' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", allowanceHist);
            js.executeScript("arguments[0].click();", allowanceHist);
            System.out.println("Clicked on 'Allowance History' via JavaScript.");
        }

        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("employee click on row action button")
    public void employeeClickOnRowActionButton() {
        ExternalFunction.waitForLoaderToDisappear(driver);

        By actionBtnLocator = By.xpath(
                "//button[contains(@class,'table__action')]"
        );

        WebElement actionBtn = wait.until(
                ExpectedConditions.elementToBeClickable(actionBtnLocator)
        );

        actionBtn.click();

        WebElement row = driver.findElement(By.xpath("//table/tbody/tr[1]"));

        expectedClaimType = row.findElement(By.xpath("./td[1]")).getText();
        expectedStatus = row.findElement(By.xpath("./td[5]")).getText();
        expectedSubmitDate = row.findElement(By.xpath("./td[2]")).getText();
        expectedBillAmount = row.findElement(By.xpath("./td[3]")).getText();
    }

    @Then("the details shall be the same as the row")
    public void theDetailsShallBeTheSameAsTheRow() {
        // Wait for panel
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("claim-modal__body")));

        String actualClaimType = driver.findElement(By.xpath("//p[text()='Claim Type']/following-sibling::p[@class='claim-modal__title']")).getText();
        String actualStatus = driver.findElement(By.xpath(
                "//div[p[normalize-space()='Status']]//p[@class='status__text']"
        )).getText();
        String actualSubmitDate = driver.findElement(By.xpath(
                "//p[text()='Submit Date']/following-sibling::p[@class='claim-modal__value']"
        )).getText();
        String actualBillAmount = driver.findElement(By.xpath(
                "//p[contains(text(),'Bill Amount')]/following-sibling::p[@class='claim-modal__value']"
        )).getText();
        Assert.assertEquals(expectedClaimType, actualClaimType, "Claim Type mismatch");
        Assert.assertEquals(expectedStatus, actualStatus, "Status mismatch");

        Date expectedDate = ExternalFunction.tryParseDate(expectedSubmitDate);
        Date actualDate = ExternalFunction.tryParseDate(actualSubmitDate);

        Assert.assertNotNull(expectedDate, "Expected date format not recognized");
        Assert.assertNotNull(actualDate, "Actual date format not recognized");

        Assert.assertEquals(expectedBillAmount, actualBillAmount, "Bill Amount mismatch");
    }

    @When("employee click on Pending Claim")
    public void employeeClickOnPendingClaim() {
        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement pendClaim = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Pending Claim']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(pendClaim));
        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(pendClaim));
            pendClaim.click();
            System.out.println("Clicked on 'Pending Claim' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", pendClaim);
            js.executeScript("arguments[0].click();", pendClaim);
            System.out.println("Clicked on 'Pending Claim' via JavaScript.");
        }

        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("employee select View")
    public void employeeSelectView() {
        By viewButtonLocator = By.xpath("//li[contains(@class,'MuiMenuItem-root') and contains(., 'View')]");

        WebElement viewButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(viewButtonLocator)
        );

        wait.until(ExpectedConditions.elementToBeClickable(viewButton)).click();
    }

    @Then("the details shall be the same as the row in pending claim table")
    public void theDetailsShallBeTheSameAsTheRowInPendingClaimTable() {
        // Wait for panel
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("claim-modal__body")));

        String actualClaimType = driver.findElement(By.xpath("//p[text()='Claim Type']/following-sibling::p[@class='claim-modal__title']")).getText();

        String actualSubmitDate = driver.findElement(By.xpath(
                "//p[text()='Submit Date']/following-sibling::p[@class='claim-modal__value']"
        )).getText();
        String actualBillAmount = driver.findElement(By.xpath(
                "//p[contains(text(),'Bill Amount')]/following-sibling::p[@class='claim-modal__value']"
        )).getText();

        // Assert all
        Assert.assertEquals(expectedClaimType, actualClaimType, "Claim Type mismatch");

        Date expectedDate = ExternalFunction.tryParseDate(expectedSubmitDate);
        Date actualDate = ExternalFunction.tryParseDate(actualSubmitDate);

        Assert.assertNotNull(expectedDate, "Expected date format not recognized");
        Assert.assertNotNull(actualDate, "Actual date format not recognized");

        Assert.assertEquals(expectedBillAmount, actualBillAmount, "Bill Amount mismatch");
    }

    @And("employee select Cancel")
    public void employeeSelectCancel() {
        By cancelButtonLocator = By.xpath("//li[contains(@class,'MuiMenuItem-root') and contains(., 'Cancel')]");

        WebElement cancelButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(cancelButtonLocator)
        );

        wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
    }

    @And("cancel claim modal shall display")
    public void cancelClaimModalShallDisplay() {
        WebElement modal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".app-cancel-claim-modal")
                )
        );

        Assert.assertTrue(modal.isDisplayed(), "Modal not displayed!");

        WebElement title = modal.findElement(
                By.cssSelector(".cancel-claim-modal__title ")
        );

        Assert.assertEquals(
                title.getText(),
                "Cancel Claim",
                "Incorrect modal title"
        );

        System.out.println("Confirmation modal displayed successfully");
    }

    @Then("employee click Confirm button")
    public void employeeClickConfirmButton() {
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Confirm']]")));
        confirmButton.click();
        System.out.println("Save button clicked");
    }

    @And("the claim should display in Allowance History with status cancelled")
    public void theClaimShouldDisplayInAllowanceHistoryWithStatusCancelled() {
        driver.navigate().refresh();

        ExternalFunction.waitForTableToLoad(driver, ".app-table", 10);

        WebElement row = driver.findElement(By.xpath("//table/tbody/tr[1]"));
        String actualClaimType = row.findElement(By.xpath("./td[1]")).getText();
        String actualSubmitDate = row.findElement(By.xpath("./td[2]")).getText();
        String actualBillAmount = row.findElement(By.xpath("./td[3]")).getText();
        String actualStatus = row.findElement(By.xpath("./td[5]")).getText();

        Assert.assertEquals(expectedClaimType, actualClaimType, "Claim Type mismatch");
        Assert.assertEquals(actualStatus, "Cancelled", "Status mismatch");

        Date expectedDate = ExternalFunction.tryParseDate(expectedSubmitDate);
        Date actualDate = ExternalFunction.tryParseDate(actualSubmitDate);

        Assert.assertNotNull(expectedDate, "Expected date format not recognized");
        Assert.assertNotNull(actualDate, "Actual date format not recognized");

        Assert.assertEquals(expectedDate, actualDate, "Submit Date mismatch");
        Assert.assertEquals(expectedBillAmount, actualBillAmount, "Bill Amount mismatch");
    }

    @And("employee view status")
    public void employeeViewStatus() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("claim-modal__body")));

        expectedStatus = driver.findElement(By.xpath("//div[p[normalize-space()='Status']]//p[@class='status__text']")).getText();

        System.out.println("Expected Status: " + expectedStatus);
    }

    @And("employee close the side panel")
    public void employeeCloseTheSidePanel() {
        WebElement closeButton = driver.findElement(By.className("claim-modal__close-button"));
        closeButton.click();
    }
}
