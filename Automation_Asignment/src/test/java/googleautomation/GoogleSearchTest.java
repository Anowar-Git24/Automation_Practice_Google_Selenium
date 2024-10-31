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

public class GoogleSearchTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
       // System.setProperty("webdriver.chrome.driver", System.getProperty("user.home") + "/webdrivers/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Start the browser maximized
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testGoogleSearch() throws InterruptedException {
        driver.get("https://www.google.com");
        Thread.sleep(2000); // Wait for 2 seconds to see the Google homepage

        // Accept cookies if the dialog appears
        try {
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Accept all')]")));
            acceptButton.click();
            Thread.sleep(1000); // Wait for 1 second after clicking
        } catch (Exception e) {
            System.out.println("Cookie dialog didn't appear or couldn't be interacted with.");
        }

        WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        String searchQuery = "Selenium WebDriver";
        searchBar.sendKeys(searchQuery);
        Thread.sleep(1000); // Wait for 1 second to see the query typed
        searchBar.sendKeys(Keys.RETURN);

        // Wait for search results
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));
        Thread.sleep(2000); // Wait for 2 seconds to see the search results

        // Get all search result items
        List<WebElement> searchResults = driver.findElements(By.cssSelector("#search div.g"));

        if (!searchResults.isEmpty()) {
            WebElement firstResult = searchResults.get(0);
            
            // Extract title
            WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h3")));
            String title = titleElement.getText();

            // Extract URL
            WebElement linkElement = firstResult.findElement(By.cssSelector("a"));
            String url = linkElement.getAttribute("href");

            System.out.println("First search result:");
            System.out.println("Title: " + title);
            System.out.println("URL: " + url);

            Assert.assertTrue(title.toLowerCase().contains("selenium") || title.toLowerCase().contains("webdriver"), 
                              "First result title does not contain 'selenium' or 'webdriver'");
            Assert.assertFalse(url.isEmpty(), "URL is empty");
        } else {
            Assert.fail("No search results found");
        }

        Thread.sleep(5000); // Wait for 5 seconds to see the final state
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}