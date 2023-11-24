package me.aikoo.SphinxMassAnswerSender;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        System.out.println(driver.getTitle());
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    }
}