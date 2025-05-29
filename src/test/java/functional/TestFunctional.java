package functional;

import jakarta.persistence.EntityManager;
import org.example.ejb.PointService;
import org.example.ejb.UserService;
import org.example.entity.Point;
import org.example.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class TestFunctional {

    WebDriver driver;
    private PointService pointService;
    private EntityManager em;
    private UserService userService;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void testSuccessfulRegistration() {
        driver.get("http://127.0.0.1:8080/web-lab4/");
        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();

        driver.findElement(By.xpath("//button[contains(text(), \"Don't have an account\")]")).click();

        driver.findElement(By.id("register_login_input")).sendKeys("newuser3");
        driver.findElement(By.id("register_password_input")).sendKeys("password12345");
        driver.findElement(By.id("register_password_second_input")).sendKeys("password12345");

        driver.findElement(By.xpath("//button[contains(text(), 'Register')]")).click();

        driver.get("http://127.0.0.1:8080/web-lab4/#/graph");

        assertEquals("http://127.0.0.1:8080/web-lab4/#/graph", driver.getCurrentUrl());
    }

    @Test
    public void testRegistrationWithShortPassword() {
        driver.get("http://127.0.0.1:8080/web-lab4/");
        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();
        driver.findElement(By.xpath("//button[contains(text(), \"Don't have an account\")]")).click();

        driver.findElement(By.id("register_login_input")).sendKeys("Gosha");
        driver.findElement(By.id("register_password_input")).sendKeys("123");
        driver.findElement(By.id("register_password_second_input")).sendKeys("123");

        driver.findElement(By.xpath("//button[contains(text(), 'Register')]")).click();

        WebElement errorMessage = driver.findElement(By.id("error-message"));
        String actualErrorText = errorMessage.getText().toLowerCase(Locale.ROOT);
        assertEquals("password must be at least 6 characters long", actualErrorText);
    }

    @Test
    public void testRegistrationExistingUser() {
        driver.get("http://127.0.0.1:8080/web-lab4/");
        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();
        driver.findElement(By.xpath("//button[contains(text(), \"Don't have an account\")]")).click();

        driver.findElement(By.id("register_login_input")).sendKeys("123");
        driver.findElement(By.id("register_password_input")).sendKeys("123456");
        driver.findElement(By.id("register_password_second_input")).sendKeys("123456");

        driver.findElement(By.xpath("//button[contains(text(), 'Register')]")).click();

        // Добавьте ожидание
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("error-message")
        ));

        String actualErrorText = errorMessage.getText().toLowerCase(Locale.ROOT);
        assertEquals("username already exists", actualErrorText);
    }

    @Test
    public void testClearButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get("http://127.0.0.1:8080/web-lab4/");
        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();

        driver.findElement(By.xpath("//label[contains(text(), 'Username')]/following::input[1]")).sendKeys("123");

        driver.findElement(By.xpath("//label[contains(text(), 'Password')]/following::input[1]")).sendKeys("123456");

        driver.findElement(By.id("enter_button")).click();

        driver.get("http://127.0.0.1:8080/web-lab4/#/graph");

        WebElement clearBtn = driver.findElement(By.id("clr.button"));
        clearBtn.click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("check")
        ));
        String actualErrorText = errorMessage.getText().toLowerCase(Locale.ROOT);

        assertEquals(actualErrorText, "No points added yet".toLowerCase(Locale.ROOT));
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        driver.get("http://127.0.0.1:8080/web-lab4/");

        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();

        driver.findElement(By.xpath("//label[contains(text(), 'Username')]/following::input[1]")).sendKeys("1");
        driver.findElement(By.xpath("//label[contains(text(), 'Password')]/following::input[1]")).sendKeys("1298319823");

        driver.findElement(By.id("enter_button")).click();

        // Ожидаем появления сообщения об ошибке
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("error-message")
        ));

        String actualErrorText = errorMessage.getText().toLowerCase(Locale.ROOT);
        assertEquals("Invalid credentials".toLowerCase(Locale.ROOT), actualErrorText);
    }


//    @Test
//    void testAddPoint_Hit() {
//        driver.get("http://127.0.0.1:8080/web-lab4/");
//        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();
//
//        driver.findElement(By.xpath("//label[contains(text(), 'Username')]/following::input[1]")).sendKeys("123");
//
//        driver.findElement(By.xpath("//label[contains(text(), 'Password')]/following::input[1]")).sendKeys("123456");
//
//        driver.findElement(By.id("enter_button")).click();
//
//        driver.get("http://127.0.0.1:8080/web-lab4/#/graph");
//
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        Point point = pointService.addPoint("0", "0", "2", userId);
//
//        assertTrue(point.isHit(), "Point should be within the area");
//    }
//    @Test
//    void testAddPoint_Miss() {
//        driver.get("http://127.0.0.1:8080/web-lab4/");
//        driver.findElement(By.xpath("//button[contains(text(), 'Login In')]")).click();
//
//        driver.findElement(By.xpath("//label[contains(text(), 'Username')]/following::input[1]")).sendKeys("123");
//
//        driver.findElement(By.xpath("//label[contains(text(), 'Password')]/following::input[1]")).sendKeys("123456");
//
//        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
//
//        driver.get("http://127.0.0.1:8080/web-lab4/#/graph");
//
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        Point point = pointService.addPoint("5", "5", "1", userId);
//
//        assertFalse(point.isHit(), "Point should be outside the area");
//    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
