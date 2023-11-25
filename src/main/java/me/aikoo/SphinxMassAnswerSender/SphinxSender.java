package me.aikoo.SphinxMassAnswerSender;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        for (Integer observationNumber : excelReader.getAnswersToFill().keySet()) {
            System.out.println("Fill survey for observation n°" + observationNumber);
            answerQuestion(observationNumber);
        }
    }

    private void answerQuestion(Integer answerNumber) {
        driver.get(url);
        this.logObservation(answerNumber, "Opening survey page...");
        List<WebElement> questionsElements;
        WebElement submitButton;
        do {
            try {
                submitButton = driver.findElement(By.cssSelector("button[type=submit][data-action=next]"));
                this.logObservation(answerNumber, "Next button found.");
            } catch (Exception e) {
                try {
                    submitButton = driver.findElement(By.cssSelector("button[type=submit][data-action=save]"));
                    this.logObservation(answerNumber, "Finish survey button found.");
                } catch (Exception e2) {
                    this.logObservation(answerNumber, "No submit button found. Finishing observation...");
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

                fillQuestion(answerNumber, questionElement, choicesElements);
            }

            submitButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillQuestion(Integer observationNumber, WebElement questionElement, List<WebElement> choicesElements) {
        WebElement questionTitleElement = questionElement.findElement(By.tagName("h3"));
        String questionTitle = questionTitleElement.getText().replaceAll("_", "\n");

        this.logObservation(observationNumber, "--------------------------------------------------");
        this.logObservation(observationNumber, "Filling question: " + questionTitle);
        this.logObservation(observationNumber, "This question have " + choicesElements.size() + " choices.");
        this.logObservation(observationNumber, "--------------------------------------------------");

        String answer = this.excelReader.getAnswersToFill().get(observationNumber).get(questionTitle);

        this.logObservation(observationNumber, "--------------------------------------------------");
        for (WebElement choiceElement : choicesElements) {
            if (answer == null) {
                continue;
            }

            if (choiceElement.getAttribute("type").equals("text")) {
                if (choiceElement.getAttribute("role").equals("spinbutton")) {
                    if (!answer.matches("\\d+")) {
                        this.logObservation(observationNumber, "Answer " + answer + " is not a number for question " + questionTitle + ".");
                        System.exit(0);
                    }

                    Integer answerInt = Integer.parseInt(answer);
                    WebElement spanUpElement = questionElement.findElement(By.cssSelector("span[aria-label='Augmenter valeur']"));

                    for (int i = 0; i < answerInt; i++) {
                        if (!choiceElement.getAttribute("title").equals("") && Integer.parseInt(choiceElement.getAttribute("title")) == answerInt) {
                            break;
                        }

                        spanUpElement.click();

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    choiceElement.sendKeys(answer.split("(?!^)"));
                }
            } else if (choiceElement.getAttribute("type").equals("radio") || choiceElement.getAttribute("type").equals("checkbox")) {
                WebElement labelElement = questionElement.findElement(By.cssSelector("label[for='" + choiceElement.getAttribute("id") + "']"));
                WebElement spanElement = labelElement.findElement(By.tagName("span"));

                answer = answer.replace("Item", "");
                if (spanElement.getText().equals(answer) || isMultipleAnswer(answer, spanElement.getText())) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", choiceElement);
                }
            }
        }

        try {
            this.logObservation(observationNumber, "Question filled. Now waiting 700ms...");
            this.logObservation(observationNumber, "Question : \"" + questionTitle + "\" filled with answer : \"" + answer + "\"");
            this.logObservation(observationNumber, "--------------------------------------------------");
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isMultipleAnswer(String answer, String text) {
        if (answer.startsWith("MULTIPLE_ANSWERS:")) {
            String regex = "MULTIPLE_ANSWERS:(.*?);";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(answer);

            if (matcher.find()) {
                answer = answer.replaceAll("MULTIPLE_ANSWERS:", "");
                String[] answers = answer.split(";");
                for (String answerToCheck : answers) {
                    if (answerToCheck.equals(text)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void logObservation(Integer observationNumber, String message) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("[" + timestamp + " / Obervation n°" + observationNumber + "] " + message);
    }
}
