package S5_T2.IT_ACADEMY;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set up the WebDriver using the locally stored ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\augus\\Desktop\\BCN ACTIVA\\ITAcademy\\SPRINT_5_T2\\src\\main\\resources\\drivers\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();

        // Navigate to the login page
        driver.get("http://localhost:3000/login");
    }

    @Test
    public void testUserLogin() {
        // Find the username field and enter a username
        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("testuser");

        // Find the password field and enter a password
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("password123");

        // Find the login button and click it
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Wait for redirect or error message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Wait for the page to load after successful login, checking for either the admin or user page
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/admin"),
                    ExpectedConditions.urlContains("/user")
            ));

            // Check if the URL contains "/admin" or "/user" to verify correct redirection
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/admin") || currentUrl.contains("/user"), "Login failed: User not redirected properly.");
        } catch (Exception e) {
            // If the redirect does not occur, look for an error message
            WebElement errorMessage = driver.findElement(By.className("error"));
            Assert.assertTrue(errorMessage.isDisplayed(), "Login failed: No error message displayed.");
        }
    }

//    @AfterMethod
//    public void tearDown() {
//        // Close the browser
//        driver.quit();
//    }
}
