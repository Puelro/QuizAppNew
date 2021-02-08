package com.example.quizappnew.database;

import android.provider.BaseColumns;

/**
 * The naming contract for the Highscore table in the Database
 */
public class HighscoreContract {
    /**
     *  Overrides the default constructor with a private constructor so HighscoreContract can't be instantiated
     */
    private HighscoreContract(){
    }

    /**
     * The information holder of the HighscoreContract
     * Centralises the naming of the table/columns and makes it easy to maintain
     */
    public static final class HighscoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "Highscore";

        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_POINTS = "Points";
        public static final String COLUMN_MAX_STREAK = "MaxStreak";
        public static final String COLUMN_MAX_DIFFICULTY = "MaxDifficulty";
    }
}
