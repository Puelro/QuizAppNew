package com.example.quizappnew.database;

import android.provider.BaseColumns;

public class HighscoreContract {

    private HighscoreContract(){

    }

    public static final class HighscoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "Highscore";

        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_POINTS = "Points";
        public static final String COLUMN_MAX_STREAK = "MaxStreak";
        public static final String COLUMN_MAX_DIFFICULTY = "MaxDifficulty";
    }
}
