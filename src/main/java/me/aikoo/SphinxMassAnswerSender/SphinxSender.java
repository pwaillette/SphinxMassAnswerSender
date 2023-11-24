package me.aikoo.SphinxMassAnswerSender;

import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class SphinxSender {
    private final WebDriver driver;
    private final ExcelReader excelReader;
    private final File excelFile;
    private final String url;

    public SphinxSender(WebDriver driver, File excelFile, String url) throws IOException {
        this.driver = driver;
        this.excelFile = excelFile;
        this.url = url;

        this.excelReader = new ExcelReader(excelFile);
    }
}
