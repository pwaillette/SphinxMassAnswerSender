package me.aikoo.SphinxMassAnswerSender;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

  private static Scanner scanner = null;

  public static void main(String[] args) throws IOException {
    System.setProperty("webdriver.chrome.silentOutput", "true");
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    WebDriver driver = new ChromeDriver();
    scanner = new Scanner(System.in);

    printHeader();
    String url = getSphinxSurveyURL();
    File excelFile = getExcelFile();

    scanner.close();

    SphinxSender sender = new SphinxSender(driver, excelFile, url);

    try {
        sender.sendAnswers();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("An error occured while sending the answers. Please open an issue on Github with the logs:");
        System.out.println("https://github.com/Aikoo-Sama/SphinxMassAnswerSender/issues/new");
    }
  }

  private static String getSphinxSurveyURL() {
    String url = "";

    while (!url.startsWith("https://www.sphinxonline.com/surveyserver/s") || !isValidURL(url)) {
      System.out.println(
          "Please enter the URL of the survey.\nThis URL should start with \"https://www.sphinxonline.com/surveyserver/s\"");
      System.out.println(
          "Example: https://www.sphinxonline.com/surveyserver/s/MySurvey/survey.htm");
      System.out.print("➜ ");
      url = scanner.next();
    }
    return url;
  }

  private static File getExcelFile() {
    File excelFile = null;

    while (excelFile == null) {
      System.out.println("Please enter the path to the Excel file.");
      System.out.print("➜ ");
      String excelFilePath = scanner.next();
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

  private static void printHeader() {
    System.out.println(
        "-------------------------------------------------------------------------------------------------------------------------------");
    System.out.println(
        " $$$$$$\\  $$$$$$$\\  $$\\   $$\\ $$$$$$\\ $$\\   $$\\ $$\\   $$\\        $$$$$$\\  $$\\   $$\\  $$$$$$\\  $$\\      $$\\ $$$$$$$$\\ $$$$$$$\\  \n"
            + "$$  __$$\\ $$  __$$\\ $$ |  $$ |\\_$$  _|$$$\\  $$ |$$ |  $$ |      $$  __$$\\ $$$\\  $$ |$$  __$$\\ $$ | $\\  $$ |$$  _____|$$  __$$\\ \n"
            + "$$ /  \\__|$$ |  $$ |$$ |  $$ |  $$ |  $$$$\\ $$ |\\$$\\ $$  |      $$ /  $$ |$$$$\\ $$ |$$ /  \\__|$$ |$$$\\ $$ |$$ |      $$ |  $$ |\n"
            + "\\$$$$$$\\  $$$$$$$  |$$$$$$$$ |  $$ |  $$ $$\\$$ | \\$$$$  /       $$$$$$$$ |$$ $$\\$$ |\\$$$$$$\\  $$ $$ $$\\$$ |$$$$$\\    $$$$$$$  |\n"
            + " \\____$$\\ $$  ____/ $$  __$$ |  $$ |  $$ \\$$$$ | $$  $$<        $$  __$$ |$$ \\$$$$ | \\____$$\\ $$$$  _$$$$ |$$  __|   $$  __$$< \n"
            + "$$\\   $$ |$$ |      $$ |  $$ |  $$ |  $$ |\\$$$ |$$  /\\$$\\       $$ |  $$ |$$ |\\$$$ |$$\\   $$ |$$$  / \\$$$ |$$ |      $$ |  $$ |\n"
            + "\\$$$$$$  |$$ |      $$ |  $$ |$$$$$$\\ $$ | \\$$ |$$ /  $$ |      $$ |  $$ |$$ | \\$$ |\\$$$$$$  |$$  /   \\$$ |$$$$$$$$\\ $$ |  $$ |\n"
            + " \\______/ \\__|      \\__|  \\__|\\______|\\__|  \\__|\\__|  \\__|      \\__|  \\__|\\__|  \\__| \\______/ \\__/     \\__|\\________|\\__|  \\__|");
    System.out.println("Author: Léa Gerard");
    System.out.println("GitHub: https://github.com/Aikoo-Sama/SphinxMassAnswerSender");
    System.out.println("Version: 1.0.0");
    System.out.println("Licence: GNU General Public License v3.0");
    System.out.println("TD 2 Supremacy.");
    System.out.println("If you have any questions or suggestions, please open an issue on GitHub:");
    System.out.println("https://github.com/Aikoo-Sama/SphinxMassAnswerSender/issues/new");
    System.out.println(
        "--------------------------------------------------------------------------------------------------------------------------------");
  }
}
