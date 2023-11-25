package me.aikoo.SphinxMassAnswerSender;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = null;
    public static void main(String[] args) throws IOException {
        WebDriver driver = new ChromeDriver();
        scanner = new Scanner(System.in);
        String url = getSphinxSurveyURL();
        File excelFile = getExcelFile();
        scanner.close();
        SphinxSender sender = new SphinxSender(driver, excelFile, url);
        sender.sendAnswers();
    }

    private static String getSphinxSurveyURL() {
        String url = "";

        while (!url.startsWith("https://www.sphinxonline.com/surveyserver/s") || !isValidURL(url)) {
            System.out.println("Please enter the URL of the survey.\nThis URL should start with \"https://www.sphinxonline.com/surveyserver/s\"");
            url = scanner.nextLine();
        }
        return url;
    }

    private static File getExcelFile() {
        File excelFile = null;

        while (excelFile == null) {
            System.out.println("Please enter the path to the Excel file.");
            String excelFilePath = scanner.nextLine();
            excelFile = new File(excelFilePath);
            if (!excelFile.exists()) {
                System.out.println("The file does not exist. Please enter a valid file path.");
                excelFile = null;
            }
        }
        return excelFile;
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}