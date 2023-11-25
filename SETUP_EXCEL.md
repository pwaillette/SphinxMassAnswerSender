## Excel File Setup 📑

The Excel file must be formatted in a specific way for the program to work properly. The file must contain the following columns:

- **N°Obs:** The observation number. This is used to identify the survey response. This column must be the A column in the file and the must value must be in A3.

All other columns will be used to the survey questions:

- The question name must be in second row, and must start at column B. Son the first question must be in B2.

There is no limit to the number of questions that can be added to the file.
The program will automatically detect the number of questions and will answer them accordingly.

## Questions setup 🛠️

All questions types have different ways of interpretation in Excel. Here are the different ways to setup the questions in the Excel file:

### Multiple Choice Questions 📝

If the question have only one of the choices, the answer must be written in the cell. For example, if the question is:
B2: What is your favorite color?
B3: Red

If the question have multiple choices, the answer must be written in the cell. Even if the choice number 4 isn't selected, it should be present but the cell must be blank. For example, if the question is:
B2: What is your favorites colors?_1
B3: Red
C3: What is your favorites colors?_2
D3: Blue
C4: What is your favorites colors?_3
D4:

### Open Questions 📝

If the question is an open question, the answer must be written in the cell. For example, if the question is:
B2: What is your favorite color?
B3: izinoflen

The program will automatically fill the answer in the open question.

### Ranking Questions 📝

The question must be written in the cell, and the answer must be written in the cell. The program will automatically fill the answer in the ranking question. All back to line must be replaced by an underscore.
Example:
B2: On a scale of 1 to 5, how complete do you think the tourist information currently available in Greater Annecy is?_ bearing in mind that 1 corresponds to "Not at all complete"_and 5 to "Very complete".
B3: 3

**I think that's all, if you have any questions, feel free to contact me.**
** You can use the [survey template](survey_template.xlsx) and the [Excel file example](exp_QuestionnairevilledAnnecy.xlsx) to help you.**

## Warning ⚠️

The program is not perfect, and it is possible that it will not work properly. If you have any problems, please open an issue.
**THIS PROGRAM DOESN'T SUPPORT NON-FILLED ANSWERS ON QUESTION THAT REQUIRE AN ANSWER.**
**YOU MUST TO WRITE YOUR DATASET IN THE EXCEL FILE PERFECTLY MATCHING TO YOUR SURVEY, OTHERWISE THE PROGRAM WON'T WORK!!!!.**
