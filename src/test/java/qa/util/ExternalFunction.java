package qa.util;

import drivers.DriverInstance;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.fail;

public class ExternalFunction extends DriverInstance {
    public static void waitForLoaderToDisappear(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(120)).until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("screen-loading")
        ));
    }

    public static void scrollDown(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            System.out.println("Successfully scrolled down to the bottom of the page.");
        } catch (Exception e) {
            System.out.println("Failed to scroll down: " + e.getMessage());
        }
    }

    public static void scrollToElement(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", element);

            System.out.println("Scrolled to element: " + locator.toString());
        } catch (TimeoutException e) {
            System.out.println("Element not found to scroll: " + locator.toString());
        } catch (Exception e) {
            System.out.println("Failed to scroll to element: " + e.getMessage());
        }
    }

    /**
     * //Previous version(void)
     * public static void waitForTableToLoad(WebDriver driver, String tableCssSelector, int timeoutInSeconds) {
     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

     try {
     // Wait for table to be visible
     wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(tableCssSelector)));

     // Wait for at least one row to appear inside the table body
     wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
     By.cssSelector(tableCssSelector + " tr"), 0));

     System.out.println("Table loaded successfully with visible rows.");
     } catch (TimeoutException e) {
     System.out.println("Table did not load within " + timeoutInSeconds + " seconds.");
     }
     } **/

    public static WebElement waitForTableToLoad(WebDriver driver, String tableCssSelector, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        // Wait for table element to appear
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(tableCssSelector)));

        // Wait until table has at least one row
        wait.until(driver1 -> {
            try {
                return table.findElements(By.cssSelector("tbody tr")).size() > 0;
            } catch (StaleElementReferenceException e) {
                return false; // recheck if DOM updated
            }
        });

        System.out.println("Table loaded and contains rows.");
        return table;
    }

    // Get column index dynamically by header name
    public static int getColumnIndexByName(WebDriver driver, String columnName) {
        List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                return i + 1; // nth-child starts from 1
            }
        }
        throw new NoSuchElementException("No header found for column: " + columnName);
    }

    public static boolean isDateOrTime(String value) {
        String[] dateTimePatterns = {
                "dd MMM yyyy, HH:mm", "dd MMM yyyy HH:mm", "dd/MM/yyyy HH:mm",
                "yyyy-MM-dd HH:mm", "MM/dd/yyyy HH:mm",
                "dd MMM yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy",
                "hh:mm a", "HH:mm" // time-only formats
        };
        for (String pattern : dateTimePatterns) {
            try {
                new SimpleDateFormat(pattern, Locale.ENGLISH).parse(value);
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    // Parse date or time safely
    public static Date parseDateOrTime(String value) {
        String[] dateTimePatterns = {
                "dd MMM yyyy, HH:mm", "dd MMM yyyy HH:mm", "dd/MM/yyyy HH:mm",
                "yyyy-MM-dd HH:mm", "MM/dd/yyyy HH:mm",
                "dd MMM yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy",
                "hh:mm a", "HH:mm"
        };
        for (String pattern : dateTimePatterns) {
            try {
                return new SimpleDateFormat(pattern, Locale.ENGLISH).parse(value);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public static Date tryParseDate(String value) {

        List<String> formats = Arrays.asList(
                "dd/MM/yyyy",
                "dd MMM yyyy",
                "MMM dd, yyyy",
                "yyyy-MM-dd"
        );

        for (String format : formats) {
            try {
                return new SimpleDateFormat(format, Locale.ENGLISH).parse(value);
            } catch (Exception ignored) {}
        }

        return null;
    }

    // Detect if string is numeric
    public static boolean isNumeric(String value) {
        return value.matches("-?\\d+(\\.\\d+)?");
    }

    // Extract and normalize column values
    public static List<Object> extractColumnValues(WebDriver driver, String columnName) {
        int columnIndex = getColumnIndexByName(driver, columnName);

        List<String> cellValues = driver.findElements(
                        By.cssSelector("table tbody tr td:nth-child(" + columnIndex + ")"))
                .stream()
                .map(e -> e.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());

        List<Object> normalized = new ArrayList<>();

        for (String value : cellValues) {

            if (isDateOrTime(value)) {

                Date parsedDate = tryParseDate(value);

                if (parsedDate != null) {
                    normalized.add(parsedDate);
                    System.out.println("Detected Date → " + value);
                    continue;
                }

            } else if (isNumeric(value)) {

                String cleaned = value.replaceAll("[^0-9.]", "");

                if (!cleaned.isEmpty()) {
                    try {
                        double number = Double.parseDouble(cleaned);
                        normalized.add(number);
                        System.out.println("Detected Number → " + value + " → " + cleaned);
                        continue;
                    } catch (Exception ignored) {}
                }

                // fallback → text
                normalized.add(value.toLowerCase());
                System.out.println("Detected Text → " + value);

            } else {

                normalized.add(value.toLowerCase());
                System.out.println("Detected Text → " + value);
            }
        }

        return normalized;
    }

    // Sort ascending (handles text, date, number, time)
    public static List<Object> sortAscending(List<Object> values) {
        List<Object> sorted = new ArrayList<>(values);
        sorted.sort((a, b) -> {
            if (a instanceof Date && b instanceof Date)
                return ((Date) a).compareTo((Date) b);
            else if (a instanceof Double && b instanceof Double)
                return ((Double) a).compareTo((Double) b);
            else
                return a.toString().compareToIgnoreCase(b.toString());
        });
        return sorted;
    }

    private static Double tryParseDouble(Object obj) {
        try {
            String cleaned = obj.toString().replaceAll("[^0-9.]", "");
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return null;
        }
    }

    // Sort descending (handles text, date, number, time)
    public static List<Object> sortDescending(List<Object> values) {
        List<Object> sorted = new ArrayList<>(values);

        sorted.sort((a, b) -> {

            Double numA = tryParseDouble(a);
            Double numB = tryParseDouble(b);

            if (numA != null && numB != null) {
                return numB.compareTo(numA);
            }

            if (a instanceof Date && b instanceof Date) {
                return ((Date) b).compareTo((Date) a);
            }

            return b.toString().compareToIgnoreCase(a.toString());
        });

        return sorted;
    }

    // Click sorting icon for a given column
    public static void clickSortingIcon(WebDriver driver, String columnName) {
        waitForTableToLoad(driver, ".app-table", 15);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
            WebElement targetHeader = headers.stream()
                    .filter(h -> h.getText().trim().equalsIgnoreCase(columnName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No header found for column: " + columnName));

            WebElement sortIcon = targetHeader.findElement(By.cssSelector("img.table__sortby"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortIcon);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortIcon);

            System.out.println("Clicked sorting icon for column: " + columnName);
            waitForTableToLoad(driver, ".app-table", 15);

        } catch (Exception e) {
            System.out.println("Error clicking sorting icon for " + columnName + ": " + e.getMessage());
        }
    }

    public static void verifyEmptyRemarksListedFirst(int columnIndex) {
        List<WebElement> remarkCells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(" + columnIndex + ")"));

        List<String> remarks = remarkCells.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());

        System.out.println("=== Remarks Column Content ===");
        for (int i = 0; i < remarks.size(); i++) {
            System.out.println("Row " + (i + 1) + ": [" + remarks.get(i) + "]");
        }

        boolean emptyEnded = false;
        boolean orderCorrect = true;

        for (String remark : remarks) {
            if (remark.isEmpty()) {
                if (emptyEnded) {
                    orderCorrect = false;
                    break;
                }
            } else {
                emptyEnded = true;
            }
        }

        System.out.println("=== Verification Result ===");
        if (orderCorrect) {
            System.out.println("Test Passed: Empty remarks are listed first.");
        } else {
            System.out.println("Test Failed: Found empty remark after non-empty remark.");
            System.out.println("Proof (list order): " + remarks);
            fail("Empty remark found after non-empty remark: " + remarks);
        }
    }

    public static void verifyColumnHasValidData(WebDriver driver, int columnIndex, String columnName) {
        List<WebElement> cells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(" + columnIndex + ")"));

        List<String> values = cells.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());

        System.out.println("=== Verifying Column: " + columnName + " ===");
        System.out.println("Extracted " + values.size() + " rows.");

        boolean allValid = true;
        int emptyCount = 0;

        for (int i = 0; i < values.size(); i++) {
            String val = values.get(i);
            System.out.println("Row " + (i + 1) + ": [" + val + "]");

            // Basic validation: empty
            if (val.isEmpty() && !columnName.equalsIgnoreCase("Remark")) {
                System.out.println("Invalid data at row " + (i + 1) + ": " + val);
                allValid = false;
                emptyCount++;
            }
        }

        System.out.println("=== Verification Result for " + columnName + " ===");
        if (allValid) {
            System.out.println("All " + columnName + " values are valid and non-empty.");
        } else {
            System.out.println("Found " + emptyCount + " empty entries in " + columnName + " column.");
            fail("Column [" + columnName + "] contains empty values: " + values);
        }
    }

    public static int getColumnIndexByHeader(WebDriver driver, String headerName) {
        List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(headerName)) {
                return i + 1; // nth-child starts from 1
            }
        }
        fail("Column header not found: " + headerName);
        return -1;
    }

    /**
     * Detect the sorting state of a column based on its icon attributes.
     * @param sortClass - The CSS class of the sort icon
     * @param sortAlt - The alt attribute of the sort icon
     * @return "ascending", "descending", "inactive", or "unknown"
     */
    public static String detectSortState(String sortClass, String sortAlt) {
        if (sortClass != null) {
            sortClass = sortClass.toLowerCase();
            if (sortClass.contains("asc")) return "ascending";
            if (sortClass.contains("desc")) return "descending";
            if (sortClass.contains("inactive")) return "inactive";
        }

        if (sortAlt != null) {
            sortAlt = sortAlt.toLowerCase();
            if (sortAlt.contains("asc")) return "ascending";
            if (sortAlt.contains("desc")) return "descending";
            if (sortAlt.contains("inactive")) return "inactive";
        }

        return "unknown";
    }

    public static boolean isHeaderDisplayed(WebDriver driver, String headerText) {
        try {
            WebElement header = driver.findElement(By.xpath("//h1[@class='header__title' and normalize-space(text())='" + headerText + "']"));
            boolean visible = header.isDisplayed();
            System.out.println("Header '" + headerText + "' is displayed: " + visible);
            return visible;
        } catch (NoSuchElementException e) {
            System.out.println("Header '" + headerText + "' is NOT displayed.");
            return false;
        }
    }

    public static WebElement findRowByStatus(WebDriver driver, String targetStatus) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        while (true) {
            List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));

            for (WebElement row : rows) {
                try {
                    // find the status column (adjust the index if needed)
                    WebElement statusCell = row.findElement(By.cssSelector("td:nth-child(5)"));
                    String status = statusCell.getText().trim();

                    if (status.equalsIgnoreCase(targetStatus)) {
                        System.out.println("✅ Found a row with status: " + status);
                        return row;
                    }
                } catch (Exception ignored) {}
            }

            // check if there’s a next page
            List<WebElement> nextButtons = driver.findElements(By.cssSelector("button[aria-label='Go to next page']"));
            if (!nextButtons.isEmpty() && nextButtons.get(0).isEnabled()) {
                System.out.println("🔄 Moving to next page...");
                nextButtons.get(0).click();

                // wait until table refreshes
                wait.until(ExpectedConditions.stalenessOf(rows.get(0)));
            } else {
                break;
            }
        }

        fail("No row found with status: " + targetStatus);
        return null;
    }
}
