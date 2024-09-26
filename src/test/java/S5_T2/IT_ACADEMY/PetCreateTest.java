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

public class PetCreateTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set up the WebDriver using the locally stored ChromeDriver
        System.setProperty("webdriver.chrome.driver",
                "src/main/resources/drivers/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();

        // Navigate to the login page
        driver.get("http://localhost:3000/login");

        // Wait and interact with the login form
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("testuser");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("password123");

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Wait for the user page to be displayed
        wait.until(ExpectedConditions.urlContains("/user"));
    }

    @Test
    public void testCreatePet() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Locate and click the 'Create New Pet' button on the UserPage
        WebElement createPetButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Create New Pet']")));
        createPetButton.click();

        // Wait for the PetCreate page to load
        wait.until(ExpectedConditions.urlContains("/create-pet"));

        // Fill out the PetCreate form
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        nameField.sendKeys("Buddy");

        WebElement typeField = driver.findElement(By.name("type"));
        typeField.sendKeys("Dog");

        WebElement colorField = driver.findElement(By.name("color"));
        colorField.sendKeys("Brown");

        // Submit the form to create the pet
        WebElement createPetSubmitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        createPetSubmitButton.click();

        // Wait for the success message or redirection
        //WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("success")));

        // Verify that the pet creation was successful
       // Assert.assertTrue(successMessage.isDisplayed(), "Pet creation failed.");
    }

//    @AfterMethod
//    public void tearDown() {
//        // Close the browser
//        driver.quit();
//    }
}
