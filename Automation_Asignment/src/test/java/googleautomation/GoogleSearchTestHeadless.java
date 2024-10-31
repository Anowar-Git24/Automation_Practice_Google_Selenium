package googleautomation;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class GoogleSearchTestHeadless {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        // Configure ChromeDriver for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);

        // Set up explicit wait with a higher timeout (20 seconds)
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testGoogleSearch() {
        System.out.println("Navigating to Google...");
        driver.get("https://www.google.com");

        // Try accepting cookies, if present
        try {
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'I agree') or contains(., 'Accept all')]")));
            acceptButton.click();
            System.out.println("Accepted cookie consent.");
        } catch (Exception e) {
            System.out.println("No cookie consent dialog appeared.");
        }

        // Search for "Selenium WebDriver"
        System.out.println("Searching for 'Selenium WebDriver'...");
        WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        searchBar.sendKeys("Selenium WebDriver");
        searchBar.sendKeys(Keys.RETURN);

        // Wait for the search results
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));
        List<WebElement> searchResults = driver.findElements(By.cssSelector("#search div.g"));

        // Ensure there are results and process the first one
        if (!searchResults.isEmpty()) {
            WebElement firstResult = searchResults.get(0);

            // Extract the title
            WebElement titleElement = firstResult.findElement(By.cssSelector("h3"));
            String title = titleElement.getText();

            // Extract the URL
            WebElement linkElement = firstResult.findElement(By.cssSelector("a"));
            String url = linkElement.getAttribute("href");

            // Log the first search result
            System.out.println("First search result:");
            System.out.println("Title: " + title);
            System.out.println("URL: " + url);

            // Validate the title and URL
            Assert.assertTrue(title.toLowerCase().contains("selenium") || title.toLowerCase().contains("webdriver"),
                              "First result title does not contain 'selenium' or 'webdriver'");
            Assert.assertFalse(url.isEmpty(), "URL is empty");
        } else {
            System.out.println("No search results found.");
            Assert.fail("No search results found");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}
