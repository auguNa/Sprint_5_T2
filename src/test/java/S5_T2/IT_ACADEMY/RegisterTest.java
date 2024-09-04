package S5_T2.IT_ACADEMY;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class RegisterTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set the path to the local ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\augus\\Desktop\\BCN ACTIVA\\ITAcademy\\SPRINT_5_T2\\src\\main\\resources\\drivers\\chromedriver-win64\\chromedriver.exe");

        // Initialize the ChromeDriver
        driver = new ChromeDriver();

        // Maximize the browser window
        driver.manage().window().maximize();

        // Navigate to the registration page
        driver.get("http://localhost:3000/register");
    }

    @Test
    public void testUserRegistration() {
        // Find the username field and enter a username
        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("testuser");

        // Find the password field and enter a password
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("password123");

        // Find the confirm password field and enter the same password
        WebElement confirmPasswordField = driver.findElement(By.name("confirmPassword"));
        confirmPasswordField.sendKeys("password123");

        // Find the register button and click it
        WebElement registerButton = driver.findElement(By.xpath("//button[@type='submit']"));
        registerButton.click();

        // Explicit wait for the success message or redirect
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("success")));

        // Verify that the registration was successful
        Assert.assertTrue(successMessage.isDisplayed(), "Registration failed.");
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
