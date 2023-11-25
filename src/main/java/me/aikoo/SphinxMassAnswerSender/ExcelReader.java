package me.aikoo.SphinxMassAnswerSender;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    private final File excelFile;
    private Workbook workbook;
    private final Map<String, ArrayList<String>> possibleQuestionAndAnswers = new HashMap<>(); // <Question, [type_question, ...answers]>
    private final Map<Integer, HashMap<String, String>> answersToFill = new HashMap<>(); // <Line number, <Question, Answer>>

    public ExcelReader(File excelFile) throws IOException {
        this.excelFile = excelFile;
        this.workbook = WorkbookFactory.create(this.excelFile);
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        this.parseQuestionsFormat();
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

                String[] forbiddenQuestions = {"CLE", "DATE_SAISIE", "DATE_ENREG", "DATE_MODIF", "TEMPS_SAISIE", "ORIGINE_SAISIE", "LANG_SAISIE", "APPAREIL_SAISIE", "PROGRESSION", "DERNIERE_QUESTION_SAISIE"};

                if (Arrays.asList(forbiddenQuestions).contains(question)) {
                    continue;
                }

                if (question.matches(".*_\\d+$")) {
                    question = question.replaceAll("_\\d+$", "").replaceAll("_", "\n");
                    String s = answers.get(question);
                    answers.put(question, ((s != null && s.startsWith("MULTIPLE_ANSWERS:")) ? "" : "MULTIPLE_ANSWERS:") + (s != null ? s : "") + (answer != null && !answer.isEmpty() ? answer + ";" : ""));
                } else answers.put(question.replaceAll("_", "\n"), answer);
            }

            answersToFill.put(lineNumber, answers);
        }
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

            question = question.replaceFirst("V\\d+\\.\\s", "").trim();
            // Store the question and its details in the map
            possibleQuestionAndAnswers.put(question, answersList);

            currentRow++;
        }
    }

    private void printAll() {
        System.out.println("Possible Questions and Answers:");
        for (Map.Entry<String, ArrayList<String>> entry : possibleQuestionAndAnswers.entrySet()) {
            System.out.println("Question: " + entry.getKey());
            System.out.println("Type: " + entry.getValue().get(0));
            System.out.println("Answers: " + entry.getValue().subList(1, entry.getValue().size()));
            System.out.println("------");
        }

        System.out.println("\nAnswers to Fill:");
        for (Map.Entry<Integer, HashMap<String, String>> entry : answersToFill.entrySet()) {
            System.out.println("Line Number: " + entry.getKey());
            System.out.println("Answers: " + entry.getValue());
            System.out.println("------");
        }
    }

    public Map<String, ArrayList<String>> getPossibleQuestionAndAnswers() {
        return possibleQuestionAndAnswers;
    }

    public Map<Integer, HashMap<String, String>> getAnswersToFill() {
        return answersToFill;
    }
}
