package me.aikoo.SphinxMassAnswerSender;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SphinxSender {
    private final WebDriver driver;
    private final ExcelReader excelReader;
    private final File excelFile;
    private final String url;

    public SphinxSender(WebDriver driver, File excelFile, String url) throws IOException {
        this.driver = driver;
        this.excelFile = excelFile;
        this.url = url;

        this.excelReader = new ExcelReader(this.excelFile);
    }

    public void sendAnswers() {
        driver.get(url);
        List<WebElement> questionsElements;
        WebElement submitButton;

        do {
            try {
                submitButton = driver.findElement(By.cssSelector("button[type=submit][data-action=next]"));
            } catch (Exception e) {
                try {
                    submitButton = driver.findElement(By.cssSelector("button[type=submit][data-action=save]"));
                } catch (Exception e2) {
                    System.out.println("No submit button found.");
                    break;
                }
            }

            questionsElements = driver.findElements(By.className("question"));

            for (WebElement questionElement : questionsElements) {
                List<WebElement> choicesElements = questionElement.findElements(By.cssSelector("input[type=radio]"));
                if (choicesElements.size() == 0) {
                    choicesElements = questionElement.findElements(By.cssSelector("input[type=checkbox]"));
                }

                // If type is input
                if (choicesElements.size() == 0) {
                    choicesElements = questionElement.findElements(By.cssSelector("input[type=text]"));
                }

                System.out.println("Choices: " + choicesElements.size());

                fillQuestionPage(questionElement, choicesElements);
            }

            submitButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private void fillQuestionPage(WebElement questionElement, List<WebElement> choicesElements) {
        if (choicesElements.get(0).getAttribute("type").equals("text")) {
            if (choicesElements.get(0).getAttribute("role").equals("spinbutton")) {
                choicesElements.get(0).sendKeys("6");
            } else {
                choicesElements.get(0).sendKeys("test");
            }
        } else ((JavascriptExecutor) driver).executeScript("arguments[0].click();", choicesElements.get(0));

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
