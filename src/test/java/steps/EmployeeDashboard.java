    package steps;

    import drivers.DriverInstance;
    import io.cucumber.java.en.*;
    import org.openqa.selenium.By;
    import org.openqa.selenium.*;
    import org.openqa.selenium.chrome.*;
    import org.openqa.selenium.support.ui.*;

    import java.time.Duration;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;
    import java.util.*;
    import java.util.stream.Collectors;

    import org.testng.Assert;
    import org.openqa.selenium.NoSuchElementException;
    import static org.testng.AssertJUnit.*;
    //import static qa.util.ExternalFunction.clearClientAuth;
    import static qa.util.ExternalFunction.waitForLoaderToDisappear;

    public class EmployeeDashboard extends DriverInstance {

        private WebDriverWait wait;

        //TC-U016
        @Given("user on the login page")
        public void user_on_the_login_page() {
            if (driver == null) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\naqiy\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
                driver = new ChromeDriver(options);
            }
            driver.manage().window().maximize();
            driver.get("https://hrms.uat.directintegrate.com/");
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        @Given("the email {string}")
        public void the_email(String email) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r0:\"]")
            )).sendKeys(email);
        }

        @Given("the password {string}")
        public void the_password(String password) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r1:\"]")
            )).sendKeys(password);
        }

        @When("user clicked login")
        public void user_clicked_login() {
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[p[text()='Login']]")));
            loginButton.click();
        }


        @Then("user see the dashboard")
        public void user_see_the_dashboard() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[1]/div[2]/main/div[2]/header/div/h1")
            ));

            assertTrue("Employee dashboard element not visible.", dashboardElement.isDisplayed());
            System.out.println("Employee dashboard loaded successfully.");
            allureScreenshot();
        }

        //TC-U017
        @Given("user see the username and greeting")
        public void user_see_the_username_and_greeting() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement greetingElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/h1") // Adjust XPath if needed
            ));

            assertTrue("Greeting element not visible.", greetingElement.isDisplayed());

            String displayedGreeting = greetingElement.getText().trim();
            System.out.println("DEBUG: Displayed greeting = [" + displayedGreeting + "]");

            LocalTime now = LocalTime.now();
            String expectedGreeting;

            if (now.isBefore(LocalTime.NOON)) { // before 12:00
                expectedGreeting = "Good Morning";
            } else if (now.isBefore(LocalTime.of(18, 0))) { // before 18:00
                expectedGreeting = "Good Afternoon";
            } else { // before midnight
                expectedGreeting = "Good Evening";
            }

            assertTrue(
                    "Greeting does not match! Expected greeting to contain: " + expectedGreeting +
                            " but was: " + displayedGreeting,
                    displayedGreeting.contains(expectedGreeting)
            );

            System.out.println("Greeting is correct: " + displayedGreeting);
            allureScreenshot();
        }

        //TC-U018
        @Given("user see the current date")
        public void user_see_the_current_date() {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement dateElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/p") // Adjust XPath if needed
            ));

            assertTrue("Date element not visible.", dateElement.isDisplayed());

            String displayedDate = dateElement.getText().trim();
            System.out.println("DEBUG: Dashboard displayed date = [" + displayedDate + "]");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
            String todayDate = LocalDate.now().format(formatter);

            assertEquals("Displayed date does not match today's date!", todayDate, displayedDate);

            System.out.println("Employee dashboard shows correct current date: " + displayedDate);
            allureScreenshot();
        }

        @Given("user see the announcements section")
        public void user_see_the_announcements_section() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/p")
            ));
            assertTrue("Employee dashboard element not visible.", dashboardElement.isDisplayed());

            List<WebElement> noAnnouncements = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/ul/div/div")
            );

            if (!noAnnouncements.isEmpty()) {
                System.out.println("No announcements available on the dashboard.");
            } else {
                List<WebElement> announcements = driver.findElements(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/ul/li")
                );

                if (announcements.isEmpty()) {
                    System.out.println("Neither announcements nor 'no announcements' message found.");
                } else {
                    WebElement firstAnnouncementTitle = announcements.get(0).findElement(By.xpath("./div[1]/p[1]"));
                    WebElement firstAnnouncementContent = announcements.get(0).findElement(By.xpath("./div[1]/p[2]"));

                    assertTrue("Employee announcement element not visible.", firstAnnouncementTitle.isDisplayed());
                    assertTrue("Employee announcement content element not visible.", firstAnnouncementContent.isDisplayed());

                    System.out.println("Announcements section is visible with content.");
                }
            }

            allureScreenshot();
        }

        @Given("user see the holiday section")
        public void user_see_the_holiday_section() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]")
            ));
            assertTrue("Employee dashboard element not visible.", dashboardElement.isDisplayed());

            List<WebElement> noHoliday = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/div")
            );

            if (!noHoliday.isEmpty()) {
                System.out.println("No holiday available on the dashboard.");
            } else {
                List<WebElement> holiday = driver.findElements(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li")
                );

                if (holiday.isEmpty()) {
                    System.out.println("Neither holiday nor 'no holidays' message found.");
                } else {
                    WebElement firstHolidayTitle = holiday.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/div/p[1]"));
                    WebElement firstHolidayState = holiday.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/div/p[2]"));
                    WebElement firstHolidayDate = holiday.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/p"));

                    assertTrue("Employee holiday title not visible.", firstHolidayTitle.isDisplayed());
                    assertTrue("Employee holiday state not visible.", firstHolidayState.isDisplayed());
                    assertTrue("Employee holiday date not visible.", firstHolidayDate.isDisplayed());

                    System.out.println("Holiday section is visible with content.");
                }
            }
            allureScreenshot();
        }

        //TC-U021
        private int dashboardPendingCount;

        @When("user notes the Pending Claim count from Dashboard")
        public void user_notes_the_pending_claim_count_from_dashboard() {
            WebElement pendingClaimCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]/div/div[3]/div[2]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pendingClaimCountElement);
            String countText = pendingClaimCountElement.getText().trim();
            dashboardPendingCount = Integer.parseInt(countText);
            System.out.println("Dashboard Pending Claim Count: " + dashboardPendingCount);
            allureScreenshot();
        }

        @When("user navigates to Pending Claim Page")
        public void user_navigates_to_pending_claim_page() {
            WebElement pendingClaimMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]/div/div[3]/div[1]/a/div")));
            pendingClaimMenu.click();

            // wait until Pending Claim table is visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div[1]/table/tbody")));

        }

        @Then("the number of claims listed should match the Pending Claim count")
        public void the_number_of_claims_listed_should_match_the_pending_claim_count() {
            // get all rows inside the table body
            List<WebElement> claimRows = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div[1]/table/tbody/tr")
            );
            int actualClaimCount = claimRows.size();

            System.out.println("Pending Claim Page Row Count: " + actualClaimCount);
            assertEquals("Mismatch between dashboard Pending Claim count and Pending Claim page list!",
                    dashboardPendingCount, actualClaimCount);
            allureScreenshot();
        }

        //TC-U022
        @When("user notes the Pending Leave count from Dashboard")
        public void user_notes_the_pending_leave_count_from_dashboard() {
            WebElement pendingLeaveCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]/div/div[1]/div[2]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pendingLeaveCountElement);
            String countText = pendingLeaveCountElement.getText().trim();
            dashboardPendingCount = Integer.parseInt(countText);
            System.out.println("Dashboard Pending Leave Count: " + dashboardPendingCount);
            allureScreenshot();
        }

        @When("user navigates to Pending Leave Page")
        public void user_navigates_to_pending_leave_page() {
            WebElement pendingLeaveMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]/div/div[1]/div[1]/a/div")));
            pendingLeaveMenu.click();

            // wait until Pending Claim table is visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div[1]/table/tbody")));

        }

        @Then("the number of leaves listed should match the Pending Leave count")
        public void the_number_of_leaves_listed_should_match_the_pending_leave_count() {
            // get all rows inside the table body
            List<WebElement> leaveRows = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div[1]/table/tbody/tr")
            );
            int actualLeaveCount = leaveRows.size();

            System.out.println("Pending Leave Page Row Count: " + actualLeaveCount);
            assertEquals("Mismatch between dashboard Pending Leave count and Pending Leave page list!",
                    dashboardPendingCount, actualLeaveCount);
            allureScreenshot();
        }

        //TC-U023
        @Given("user see the leave balance section")
        public void user_see_the_leave_balance_section() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[3]")
            ));

            assertTrue("Employee dashboard element not visible.", dashboardElement.isDisplayed());
            System.out.println("Employee dashboard loaded successfully.");

        }

        @Given("user click view all on leave")
        public void user_click_view_all_on_leave() {
            WebElement viewAllButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[3]/div/a/div/p")));
            viewAllButton.click();
            allureScreenshot();
        }
        //TC-U024
        @Given("user see the upcoming leave section")
        public void user_see_the_upcoming_leave_section() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")
            ));

            assertTrue("Employee dashboard element not visible.", dashboardElement.isDisplayed());
            System.out.println("Employee dashboard loaded successfully.");

        }

        @Given("user click view all on upcoming")
        public void user_click_view_all_on_upcoming() {
            WebElement viewAllButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]/div/a/div/p")));
            viewAllButton.click();
            allureScreenshot();
        }

        //TC-U025
        @Given("user click logout")
        public void user_click_logout() {
            WebElement viewAllButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"side-nav\"]/div/nav/div/div")));
            viewAllButton.click();
            allureScreenshot();
        }

        //TC-U026
        @Given("user click leave management")
        public void user_click_leave_management() {
            WebElement leaveManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[2]/div/div")));
            leaveManagementButton.click();
            allureScreenshot();
        }

        //TC-U027
        @Given("user click allowance management")
        public void user_click_allowance_management() {
            WebElement allowanceManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[3]/div/div")));
            allowanceManagementButton.click();
            allureScreenshot();
        }

        //TC-U028
        @Given("user click appointment management")
        public void user_click_appointment_management() {
            WebElement appointmentManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[4]/div/div")));
            appointmentManagementButton.click();
            allureScreenshot();
        }

        //TC-U029
        @Given("user click dashboard")
        public void user_click_dashboard() {
            WebElement dashboardButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[1]/div/div")));
            dashboardButton.click();
            allureScreenshot();
        }

        //TC-U030
        @Given("user click profile icon")
        public void user_click_profile_icon() {
            WebElement profileIconButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/header/div/div/div[2]")));
            profileIconButton.click();
        }


        @Given("user see dropdown")
        public void user_see_dropdown() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dropdown = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")
            ));

            assertTrue("Employee dropdown not visible.", dropdown.isDisplayed());
            System.out.println("Employee dropdown loaded successfully.");
            allureScreenshot();
        }

        //TC-U031
        @Given("user see celebration corner")
        public void user_see_celebration_corner() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]")
            ));
            assertTrue("Employee dashboard element not visible.", dashboardElement.isDisplayed());

            List<WebElement> noCelebration = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/div/div/p[2]")
            );

            if (!noCelebration.isEmpty()) {
                System.out.println("No Celebration available on the dashboard.");
            } else {
                List<WebElement> celebration = driver.findElements(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li")
                );

                if (celebration.isEmpty()) {
                    System.out.println("Neither celebration nor 'no celebration' message found.");
                } else {
                    WebElement celebrationProfile = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li/div"));
                    WebElement celebrationName = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li/p[1]"));
                    WebElement celebrationDate = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li/p[2]"));

                    assertTrue("Employee celebration profile not visible.", celebrationProfile.isDisplayed());
                    assertTrue("Employee celebration name not visible.", celebrationName.isDisplayed());
                    assertTrue("Employee celebration date not visible.", celebrationDate.isDisplayed());

                    System.out.println("Celebration section is visible with content.");
                }
            }
            allureScreenshot();
        }

        //TC-U032
        @Given("user click bell icon")
        public void user_click_bell_icon() {
            WebElement bellIconButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/header/div/div/div[3]/img")));
            bellIconButton.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            allureScreenshot();
        }

        @Given("user see notifications")
        public void user_see_notifications() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            WebElement notifModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.notification-modal")));
            assertTrue("Notification modal not visible!", notifModal.isDisplayed());

            WebElement headerTitle = notifModal.findElement(By.cssSelector(".notification-modal__header--row--title"));
            assertEquals("Notifications", headerTitle.getText().trim());

            System.out.println("Notification list is visible in the modal");
            allureScreenshot();
        }

        //TC-U033
        @Given("user click company icon")
        public void user_click_company_icon() {
            WebElement companyIconButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"side-nav\"]/div/div/div")));
            companyIconButton.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            allureScreenshot();
        }

        //TC-U034
        @Given("user see holidays displayed in ascending order by date")
        public void user_see_holidays_displayed_in_ascending_order_by_date() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]"))
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", card);

            List<WebElement> rows = card.findElements(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li"));
            if (rows.isEmpty()) {
                rows = card.findElements(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]"));
            }

            assertFalse("No holiday rows found under 'Holiday this month' card.", rows.isEmpty());

            List<Integer> actualDates = new ArrayList<>();
            for (WebElement row : rows) {
                WebElement rightCell = row.findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/p"));
                String dateText = rightCell.getText().trim();

                String digits = dateText.replaceAll("\\D", "");
                assertFalse("No day number found in row text: '" + dateText + "'", digits.isEmpty());

                actualDates.add(Integer.parseInt(digits));
            }

            List<Integer> sortedDates = new ArrayList<>(actualDates);
            Collections.sort(sortedDates);

            assertEquals("Holidays are not sorted in ascending order by date!", sortedDates, actualDates);

            System.out.println("Holidays are correctly sorted by date in ascending order: " + actualDates);
            allureScreenshot();
        }

        //TC-U035
        @Given("dashboard responsive and elements realign")
        public void dashboard_responsive_and_elements_realign() {
            int[][] viewports = {
                    {1920, 1080},
                    {1500, 817},
                    {850, 817},
                    {600, 500}
            };

            for (int[] size : viewports) {
                int width = size[0];
                int height = size[1];
                driver.manage().window().setSize(new Dimension(width, height));
                ((JavascriptExecutor) driver).executeScript("window.dispatchEvent(new Event('resize'));");

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

                //check Announcement is visible
                WebElement announcement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]")));
                assertTrue("Announcement is not visible at " + width + "x" + height, announcement.isDisplayed());

                //check Holidays card is visible
                WebElement holidays = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]")));
                assertTrue("Holiday card is not visible at " + width + "x" + height, holidays.isDisplayed());

                //check Celebration Corner card is visible
                WebElement celebrationCorner = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]")));
                assertTrue("Celebration Corner card is not visible at " + width + "x" + height, celebrationCorner.isDisplayed());

                //check Upcoming Leave card is visible
                WebElement upcomingLeave = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")));
                assertTrue("Upcoming Leave card is not visible at " + width + "x" + height, upcomingLeave.isDisplayed());

                //check Leave Balance card is visible
                WebElement leaveBalance = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[3]")));
                assertTrue("Leave Balance card is not visible at " + width + "x" + height, leaveBalance.isDisplayed());

                //check Pending card is visible
                WebElement pending = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]")));
                assertTrue("Pending card is not visible at " + width + "x" + height, pending.isDisplayed());


                System.out.println("Dashboard responsive at " + width + "x" + height);
                allureScreenshot();
            }
        }

        //TC-U036
        @Given("user click view all pending leave")
        public void user_click_view_all_pending_leave() {
            WebElement viewAllButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]/div/div[1]/div[1]/a/div")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewAllButton);
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            viewAllButton.click();
            allureScreenshot();
        }

        //TC-U037
        @Given("user click view all pending claim")
        public void user_click_view_all_pending_claim() {
            WebElement viewAllButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[4]/div/div[3]/div[1]/a/div")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewAllButton);
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            viewAllButton.click();
            allureScreenshot();
        }

        //TC-U038
        @Given("user click profile dropdown")
        public void user_click_profile_dropdown() {
            WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[2]/div[3]/ul/li[1]")));
            profileButton.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            allureScreenshot();
        }

        //TC-U039
        @Given("user click change password dropdown")
        public void user_click_change_password_dropdown() {
            WebElement changePasswordButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[2]/div[3]/ul/li[2]")));
            changePasswordButton.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            allureScreenshot();
        }

        //TC-U040
        @Given("user wait before click dashboard")
        public void user_wait_before_click_dashboard() throws InterruptedException{
            System.out.println("Simulating session timeout...");
            //clearClientAuth(driver);

            Thread.sleep(2000000);

        }

        /*@Then("user see session timeout alert")
        public void user_see_session_timeout_alert() {
            driver.navigate().refresh();
            waitForLoaderToDisappear(driver);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement timeoutAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"app-alert\"]/div/p")
            ));
            assertTrue(timeoutAlert.isDisplayed());
            System.out.println("Session timeout alert displayed.");
            allureScreenshot();
        }*/

        @Then("user is redirected to login page")
        public void user_is_redirected_to_login_page() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement loginElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"root\"]/div[2]/div/main/div[2]/form")
            ));
            assertTrue(loginElement.isDisplayed());
            System.out.println("Redirected to login page.");
            allureScreenshot();
        }

        //TC-U041
        @Given("user see list of leave summary")
        public void user_see_list_of_leave_summary() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement leaveSummaryCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[3]")
            ));

            List<WebElement> leaveSummaryItems = leaveSummaryCard.findElements(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[3]/ul/li"));

            assertTrue("Leave summary card not visible.", leaveSummaryCard.isDisplayed());

            assertFalse("Leave summary list is empty.", leaveSummaryItems.isEmpty());

            for (int i = 0; i < leaveSummaryItems.size(); i++) {
                assertTrue("Leave summary item " + (i + 1) + " not visible.", leaveSummaryItems.get(i).isDisplayed());
            }

            System.out.println("Leave summary list loaded with " + leaveSummaryItems.size() + " items.");
            allureScreenshot();
        }

        //TC-U042
        @Given("user see list in upcoming leave section")
        public void user_see_list_in_upcoming_leave_section() {

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")
            ));
            assertTrue("List in upcoming leave not visible.", dashboardElement.isDisplayed());

            List<WebElement> noCelebration = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]/ul/div/div/p[2]")
            );

            if (!noCelebration.isEmpty()) {
                System.out.println("No List in upcoming leave available on the dashboard.");
            } else {
                List<WebElement> celebration = driver.findElements(
                        By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")
                );

                if (celebration.isEmpty()) {
                    System.out.println("Neither List in upcoming leave nor 'no List in upcoming leave' message found.");
                } else {
                    WebElement leaveName = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]/ul/li/p[1]"));
                    WebElement leaveBalance = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]/ul/li/p[2]"));

                    assertTrue("Leave name not visible.", leaveName.isDisplayed());
                    assertTrue("Leave balance not visible.", leaveBalance.isDisplayed());

                    System.out.println("List in upcoming leave is visible with content.");
                }
            }
            allureScreenshot();
        }
//changes
    }
