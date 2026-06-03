package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CallbackTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
// опции для управления режимами работы с памятью, будут полезны при запуске тестов в CI
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
// опция для включения headless-режима, обязателен при запуске тестов в CI
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }


    @Test
    void emptyNameField() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void emptyPhoneField() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void emptyCheckboxField() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid"));
        assertTrue(result.isDisplayed());
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", result.getText().trim());
    }

    @Test
    void nameFieldWithSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("!!! @@@");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void nameFieldWithEnglish() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("David Duhovni");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void lowerLimitValuesPhoneField() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7927000000");
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void higherLimitValuesPhoneField() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+792700000000");
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }
}

