package com.example.quizappnew.database;
import android.provider.BaseColumns;

/**
 *
 */
public class QuestionContract {

    /**
     *
     */
    private QuestionContract(){
    }

    /**
     *
     */
    public static final class QuestionEntry implements BaseColumns {
        public static final String TABLE_NAME = "Question";

        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_DIFFICULTY = "Difficulty";
        public static final String COLUMN_QUESTIONTEXT = "Questiontext";

        public static final String COLUMN_ANSWERTEXT1 = "Answertext1";
        public static final String COLUMN_ANSWERTEXT2 = "Answertext2";
        public static final String COLUMN_ANSWERTEXT3 = "Answertext3";
        public static final String COLUMN_ANSWERTEXT4 = "Answertext4";
        public static final String COLUMN_CORRECT_ANSWER = "Correct_Answer";
    }
}
