//package S5_T2.IT_ACADEMY;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.time.Duration;
//
//public class PetActionsTest {
//    private WebDriver driver;
//    private WebDriverWait wait;
//
//    @BeforeMethod
//    public void setUp() {
//        // Set the path to the local ChromeDriver
//        System.setProperty("webdriver.chrome.driver",
//                "src/main/resources/drivers/chromedriver-win64/chromedriver.exe");
//        // Initialize the ChromeDriver
//        driver = new ChromeDriver();
//        // Initialize WebDriverWait
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        // Maximize the browser window
//        driver.manage().window().maximize();
//
//        // Navigate to the home page or pet list page
//        driver.get("http://localhost:3000/pets");
//    }
//
//    @Test
//    public void testUpdatePet() {
//        // Assume we have a pet with ID 1
//        driver.findElement(By.id("edit-pet-1")).click(); // Click edit button for pet ID 1
//
//        // Fill in the pet update form
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pet-name"))).sendKeys("Buddy Updated");
//        driver.findElement(By.id("pet-color")).sendKeys("Black");
//        driver.findElement(By.id("pet-mood")).sendKeys("Excited");
//        driver.findElement(By.id("pet-energy")).sendKeys("80");
//
//        // Submit the update form
//        driver.findElement(By.id("update-pet-button")).click();
//
//        // Verify that the pet was updated successfully
//        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message"))).getText();
//        Assert.assertEquals(successMessage, "Pet updated successfully!");
//    }
//
//    @Test
//    public void testPerformAction() {
//        // Assume we have a pet with ID 1
//        driver.findElement(By.id("perform-action-pet-1")).click(); // Click action button for pet ID 1
//
//        // Select an action (e.g., "feed")
//        driver.findElement(By.id("action-feed")).click();
//
//        // Verify that the action was performed successfully
//        String actionMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("action-message"))).getText();
//        Assert.assertEquals(actionMessage, "Pet fed successfully!");
//    }
//
//    @Test
//    public void testDeletePet() {
//        // Assume we have a pet with ID 1
//        driver.findElement(By.id("delete-pet-1")).click(); // Click delete button for pet ID 1
//
//        // Confirm the deletion in the alert dialog
//        driver.switchTo().alert().accept();
//
//        // Verify that the pet was deleted successfully
//        String deleteMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-message"))).getText();
//        Assert.assertEquals(deleteMessage, "Pet deleted successfully!");
//    }
//
////    @AfterMethod
////    public void tearDown() {
////        // Close the browser
////        if (driver != null) {
////            driver.quit();
////        }
////    }
//
//}
