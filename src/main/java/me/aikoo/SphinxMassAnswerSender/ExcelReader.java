package me.aikoo.SphinxMassAnswerSender;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {
  private final File excelFile;
  private final Map<Integer, HashMap<String, String>> answersToFill =
      new HashMap<>(); // <Line number, <Question, Answer>>
  private final Workbook workbook;

  public ExcelReader(File excelFile) throws IOException {
    this.excelFile = excelFile;
    this.workbook = WorkbookFactory.create(this.excelFile);
    this.loadAnswersToFill();
    this.workbook.close();
  }

  private void loadAnswersToFill() {
    Sheet sheet = workbook.getSheetAt(0); // The first one

    DataFormatter dataFormatter = new DataFormatter();

    int startingRow = 1; // Assuming headers are in the first row

    Iterator<Row> rowIterator = sheet.rowIterator();
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();

      if (row.getRowNum() < startingRow) {
        continue;
      }

      // Check if the first cell contains a numeric value
      Cell cell = row.getCell(0);
      if (cell.getCellType() != CellType.NUMERIC) {
        // Handle the case where the cell contains a non-numeric value
        System.err.println("Skipping row: " + row.getRowNum() + " - Invalid line number format");
        continue;
      }

      int lineNumber = (int) cell.getNumericCellValue();

      HashMap<String, String> answers = new HashMap<>();

      for (int cellIndex = 1; cellIndex < row.getLastCellNum(); cellIndex++) {
        Cell answerCell = row.getCell(cellIndex);
        String question = sheet.getRow(1).getCell(cellIndex).getStringCellValue();
        String answer = dataFormatter.formatCellValue(answerCell);

        String[] forbiddenQuestions = {
          "CLE",
          "DATE_SAISIE",
          "DATE_ENREG",
          "DATE_MODIF",
          "TEMPS_SAISIE",
          "ORIGINE_SAISIE",
          "LANG_SAISIE",
          "APPAREIL_SAISIE",
          "PROGRESSION",
          "DERNIERE_QUESTION_SAISIE"
        };

        if (Arrays.asList(forbiddenQuestions).contains(question)) {
          continue;
        }

        if (question.matches(".*_\\d+$")) {
          question = question.replaceAll("_\\d+$", "").replaceAll("_", "\n");
          String s = answers.get(question);
          answers.put(
              question,
              ((s != null && s.startsWith("MULTIPLE_ANSWERS:")) ? "" : "MULTIPLE_ANSWERS:")
                  + (s != null ? s : "")
                  + (answer != null && !answer.isEmpty() ? answer + ";" : ""));
        } else answers.put(question.replaceAll("_", "\n"), answer);
      }

      answersToFill.put(lineNumber, answers);
    }
  }

  private void printAll() {
    System.out.println("\nAnswers to Fill:");
    for (Map.Entry<Integer, HashMap<String, String>> entry : answersToFill.entrySet()) {
      System.out.println("Line Number: " + entry.getKey());
      System.out.println("Answers: " + entry.getValue());
      System.out.println("------");
    }
  }

  public Map<Integer, HashMap<String, String>> getAnswersToFill() {
    return answersToFill;
  }
}
