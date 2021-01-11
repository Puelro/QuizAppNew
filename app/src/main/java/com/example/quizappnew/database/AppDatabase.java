package com.example.quizappnew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quizappnew.database.QuestionContract.QuestionEntry;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "QuizApp.db";
    public static final int DATABASE_VERSION = 1;

    // Implement AppDatabase as a Singleton
    private static AppDatabase instance = null;

    /**
     * AppDatabase-Constructor
     *
     * @param context The context
     */
    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase : constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");

        createQuestionTable(db);
        createHighscoreTable(db);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion) {
            case 1:
                //upgrade logic from version
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion" + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }

    /**
     * Get an instance of the app's singleton database helper object
     *
     * @param context the content providers context.
     * @return a SQLlite database helper object
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }

        return instance;
    }

    /**
     * Removes the Question with the passed ID
     *
     * @param db  The database
     * @param _id ID of the question
     */
    public void removeQuestion(SQLiteDatabase db, long _id) {
        int result = db.delete(QuestionEntry.TABLE_NAME, QuestionEntry._ID + "=" + _id, null);

        if (result == 0) {
            Log.d(TAG, "removeItem: ERROR! NO row with the ID: " + _id + " was found");
        } else {
            Log.d(TAG, "removeQuestion: SUCCESS! The row with the ID: " + _id + ", was deleted");
        }
    }

    /**
     * Adds the content of cv into the "Question" table
     *
     * @param db The database you want to work on
     * @param cv A ContentValues-Object which holds data-pairs, where the column-name of the table is the key and value is the value
     */
    public void addQuestion(SQLiteDatabase db, ContentValues cv) {
        long result = db.insert(QuestionContract.QuestionEntry.TABLE_NAME, null, cv);

        if (result == -1) {
            Log.d(TAG, "addQuestion: ERROR! data-Set couldn't be added!");
        } else {
            Log.d(TAG, "addQuestion: SUCCESS! The data-Set was added: " + cv.toString());
        }
    }

    /**
     * Drops the Question-table and creates it again
     *
     * @param db The database on which is worked on
     */
    public void dropAndRecreateTableQuestion(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + QuestionContract.QuestionEntry.TABLE_NAME + ";");
        } catch (android.database.SQLException e) {
            Log.d(TAG, "dropAndRecreateTableQuestion: SQL: syntax error :\n" + e.toString());
        }

        createQuestionTable(db);
    }

    /*
    public void dropTableHighscore(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS HIGHSCORE;");

        String sSQL =   "CREATE TABLE IF NOT EXISTS" + "HIGHSCORE" + " ("
                + "ID INTEGER PRIMARY KEY, "
                + "NAME TEXT NOT NULL, "
                + "SCORE NUMBER NOT NULL)";
        Log.d(TAG, "onCreate: " + sSQL);
    }*/

    /**
     * Returns all Questions of the Questions-table
     *
     * @return A cursor which holds all Questions-Items
     */
    public Cursor getQuestionListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + QuestionContract.QuestionEntry.TABLE_NAME, null);

        Log.d(TAG, "getQuestionListContents: Cursorcontent: " + DatabaseUtils.dumpCursorToString(data));
        return data;
    }

    /**
     * Returns all Questions of the Questions-table
     *
     * @return A cursor which holds all Questions-Items
     */
    public Cursor getFilteredQuestions(int difficulty, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        String categoryWhereClause = "";

        if (category != null && !category.trim().equals("")) {
            categoryWhereClause = " AND " + QuestionEntry.COLUMN_CATEGORY + "=" + category;
        }

        Cursor data = db.rawQuery("SELECT * FROM " +
                QuestionContract.QuestionEntry.TABLE_NAME +
                " WHERE " + QuestionEntry.COLUMN_DIFFICULTY + "=" + difficulty +
                categoryWhereClause, null);

        Log.d(TAG, "getFilteredQuestions: FilteredCursorcontent: " + DatabaseUtils.dumpCursorToString(data));
        return data;
    }

    //Helperclass
    private void createQuestionTable(SQLiteDatabase db) {
        String sSQL =
                "CREATE TABLE IF NOT EXISTS "
                        + QuestionContract.QuestionEntry.TABLE_NAME + " ("
                        + QuestionContract.QuestionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + QuestionContract.QuestionEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT + " TEXT NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT1 + " TEXT NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT2 + " TEXT NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT3 + " TEXT NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT4 + " TEXT NOT NULL, "
                        + QuestionContract.QuestionEntry.COLUMN_CORRECT_ANSWER + " INTEGER NOT NULL "
                        + ");";
        Log.d(TAG, "createQuestionTable: " + sSQL);

        try {
            db.execSQL(sSQL);
        } catch (android.database.SQLException e) {
            Log.d(TAG, "createQuestionTable: SQL: syntax error :\n" + e.toString());
        }
    }

    //Helperclass
    private void createHighscoreTable(SQLiteDatabase db) {
        String sSQL = "CREATE TABLE IF NOT EXISTS " + "Highscore" + " ("
                + "ID INTEGER PRIMARY KEY, "
                + "Place INTEGER NOT NULL, "
                + "NAME TEXT NOT NULL, "
                + "SCORE NUMBER NOT NULL"
                + ");";

        Log.d(TAG, "createHighscoreTable: " + sSQL);
        try {
            db.execSQL(sSQL);
        } catch (android.database.SQLException e) {
            Log.d(TAG, "createHighscoreTable: SQL: syntax error :\n" + e.toString());
        }
    }
}
