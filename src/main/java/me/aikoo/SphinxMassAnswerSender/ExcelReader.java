package me.aikoo.SphinxMassAnswerSender;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    private final File excelFile;
    private Workbook workbook;
    private final Map<String, ArrayList<String>> possibleQuestionAndAnswers = new HashMap<>(); // <Question, [type_question, ...answers]>

    public ExcelReader(File excelFile) throws IOException {
        this.excelFile = excelFile;
        this.workbook = WorkbookFactory.create(this.excelFile);
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        this.parseQuestionsFormat();
        this.workbook.close();
    }

    private void parseQuestionsFormat() {
        Sheet sheet = workbook.getSheetAt(1); // The second one

        // The questions start from B15
        int startingRow = 14;
        int currentRow = startingRow;

        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext() && currentRow <= sheet.getLastRowNum()) {
            Row row = rowIterator.next();

            if (currentRow < startingRow) {
                currentRow++;
                continue;
            }

            // Get the question from column B
            Cell questionCell = row.getCell(1); // B column
            String question = dataFormatter.formatCellValue(questionCell);

            if (question.isEmpty() || question.isBlank() || question.equals(" ") || !question.startsWith("V") || question.matches("V\\d+\\.\\s[A-Z_]+")) {
                continue;
            }

            // Get the type of question from column C
            Cell typeCell = row.getCell(2); // C column
            String type = dataFormatter.formatCellValue(typeCell);

            // Get the answers starting from column D
            ArrayList<String> answers = new ArrayList<>();
            for (int cellIndex = 3; cellIndex < row.getLastCellNum(); cellIndex++) {
                Cell answerCell = row.getCell(cellIndex);
                String answer = dataFormatter.formatCellValue(answerCell);
                if (!answer.isEmpty()) {
                    answers.add(answer);
                }
            }

            ArrayList<String> answersList = new ArrayList<>(List.of(answers.toArray(new String[0])));
            answersList.add(0, type);

            // Store the question and its details in the map
            possibleQuestionAndAnswers.put(question, answersList);

            currentRow++;
        }
    }

    public Map<String, ArrayList<String>> getPossibleQuestionAndAnswers() {
        return possibleQuestionAndAnswers;
    }
}
