package com.webchat_agc.webchat_agc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebchatAgcApplicationTests {

    private WebDriver chromeDriver_1;
    private WebDriver chromeDriver_2;

    @BeforeEach
    public void setup() {
        // Configurar ChromeOptions para ignorar errores de certificados
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");

        // Inicializar las instancias de ChromeDriver
        chromeDriver_1 = new ChromeDriver(options);
        chromeDriver_2 = new ChromeDriver(options);
    }

    @AfterEach
    public void teardown() {
        // Cerrar ambos navegadores
        if (chromeDriver_1 != null) {
            chromeDriver_1.quit();
        }
        if (chromeDriver_2 != null) {
            chromeDriver_2.quit();
        }
    }

    @Test
    public void testChatFunctionality() throws InterruptedException {

        // Usuario 1: Inicio de sesión
        chromeDriver_1.get("https://localhost");
        Thread.sleep(5000);
        chromeDriver_1.findElement(By.id("username")).sendKeys("TestUser1");
        chromeDriver_1.findElement(By.id("password")).sendKeys("TestPass1");
        chromeDriver_1.findElement(By.id("login-submit")).click();
        Thread.sleep(5000);
        chromeDriver_1.switchTo().alert().accept();
        Thread.sleep(5000);
        chromeDriver_1.findElement(By.id("list-button")).click();
        Thread.sleep(5000);
        chromeDriver_1.findElement(By.id("room-button")).click();
        Thread.sleep(5000);

        // Usuario 2: Inicio de sesión
        chromeDriver_2.get("https://localhost");
        Thread.sleep(5000);
        chromeDriver_2.findElement(By.id("username")).sendKeys("TestUser2");
        chromeDriver_2.findElement(By.id("password")).sendKeys("TestPass2");
        chromeDriver_2.findElement(By.id("login-submit")).click();
        Thread.sleep(5000);
        chromeDriver_2.switchTo().alert().accept();
        Thread.sleep(5000);
        chromeDriver_2.findElement(By.id("list-button")).click();
        Thread.sleep(5000);
        chromeDriver_2.findElement(By.id("room-button")).click();
        Thread.sleep(5000);

        // Usuario 1 envía un mensaje
        String messageUser1 = "Mensaje de prueba de User1";
        chromeDriver_1.findElement(By.id("input-message")).sendKeys(messageUser1);
        chromeDriver_1.findElement(By.id("input-message")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);

        // Usuario 2 envía un mensaje
        String messageUser2 = "Mensaje de prueba de User2";
        chromeDriver_2.findElement(By.id("input-message")).sendKeys(messageUser2);
        chromeDriver_2.findElement(By.id("input-message")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);

        // Verificar que Usuario 2 ve el mensaje de Usuario 1
        assertNotNull(chromeDriver_2.findElement(By.xpath("//*[contains(text(), '" + messageUser1 + "')]")),
            "El mensaje de User1 no fue recibido por User2");
  		assertTrue(
            chromeDriver_2.findElements(By.xpath("//*[contains(text(), '" + messageUser1 + "')]")).size() > 0,
            "El mensaje de User1 no es visible para User2");

        // Verificar que Usuario 1 ve el mensaje de Usuario 2
        assertNotNull(chromeDriver_1.findElement(By.xpath("//*[contains(text(), '" + messageUser2 + "')]")),
            "El mensaje de User2 no fue recibido por User1");
        assertTrue(
            chromeDriver_1.findElements(By.xpath("//*[contains(text(), '" + messageUser2 + "')]")).size() > 0,
            "El mensaje de User2 no es visible para User1");
    }
}