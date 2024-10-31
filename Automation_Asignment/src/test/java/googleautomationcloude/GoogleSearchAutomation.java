package googleautomationcloude;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GoogleSearchAutomation {
    public static void main(String[] args) {
        // Set the path to your ChromeDriver executable
       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Initialize the Selenium WebDriver (Chrome in this example)
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to Google
            driver.get("https://www.google.com");

            // Locate the search bar and enter the search query
            WebElement searchBar = driver.findElement(By.name("q"));
            String searchQuery = "Selenium WebDriver";
            searchBar.sendKeys(searchQuery);
            searchBar.sendKeys(Keys.RETURN);

            // Wait for the search results to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.g")));

            // Locate the first search result
            WebElement firstResult = driver.findElement(By.cssSelector("div.g"));

            // Extract the title and URL of the first result
            String title = firstResult.findElement(By.cssSelector("h3")).getText();
            String url = firstResult.findElement(By.cssSelector("div.yuRUbf > a")).getAttribute("href");

            // Print the title and URL
            System.out.println("First search result:");
            System.out.println("Title: " + title);
            System.out.println("URL: " + url);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}