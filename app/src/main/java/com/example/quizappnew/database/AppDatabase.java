package com.example.quizappnew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quizappnew.database.QuestionContract.QuestionEntry;

/**
 * A singleton class which creates the Database if it does not exist and provides various functionalities to operate on the Database
 * @author Kent Feldner
 */
public class AppDatabase extends SQLiteOpenHelper {
    /**
     * The Tag used in Log messages
     */
    private static final String TAG = "AppDatabase";

    /**
     * The name of the Database
     */
    public static final String DATABASE_NAME = "QuizApp.db";

    /**
     * The hardcoded version number of the Database
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * The reference to the only instance of the class
     */
    private static AppDatabase instance = null;

    /**
     * AppDatabase-Constructor
     *
     * @param context The context from which the constructor is called
     */
    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(this.getWritableDatabase());
        Log.d(TAG, "AppDatabase : constructor");
    }

    /**
     * Creates the tables of the Database if these don't exist
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        createQuestionTable(db);
        createHighscoreTable(db);
        Log.d(TAG, "onCreate: ends");
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        // no logic was needed
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
    public void removeQuestionByID(SQLiteDatabase db, long _id) {
        int result = db.delete(QuestionEntry.TABLE_NAME, QuestionEntry._ID + "=" + _id, null);

        if (result == 0) {
            Log.d(TAG, "removeItem: ERROR! NO row with the ID: " + _id + " was found");
        } else {
            Log.d(TAG, "removeQuestion: SUCCESS! The row with the ID: " + _id + ", was deleted");
        }
    }

    /**
     * Adds the content of a ContentValues-Object into the "Question" table
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
     * Adds the content of a String into the "Question" table
     *
     * @param db The database you want to work on
     * @param _insertedValues A String-Object which holds data separated by commas.
     */
    public void addQuestion(SQLiteDatabase db, String _insertedValues) {

        ContentValues cv = new ContentValues();
        String[] insertedValuesArr = _insertedValues.split(",");

        cv.put(QuestionEntry.COLUMN_CATEGORY, insertedValuesArr[0]);
        cv.put(QuestionEntry.COLUMN_DIFFICULTY, Integer.parseInt(insertedValuesArr[1]));
        cv.put(QuestionEntry.COLUMN_QUESTIONTEXT, insertedValuesArr[2]);
        cv.put(QuestionEntry.COLUMN_ANSWERTEXT1, insertedValuesArr[3]);
        cv.put(QuestionEntry.COLUMN_ANSWERTEXT2, insertedValuesArr[4]);
        cv.put(QuestionEntry.COLUMN_ANSWERTEXT3, insertedValuesArr[5]);
        cv.put(QuestionEntry.COLUMN_ANSWERTEXT4, insertedValuesArr[6]);
        cv.put(QuestionEntry.COLUMN_CORRECT_ANSWER, Integer.parseInt(insertedValuesArr[7]));

        addQuestion(db, cv);
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

    public void dropAndRecreateTableHighscore(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + HighscoreContract.HighscoreEntry.TABLE_NAME + ";");
        createHighscoreTable(db);
    }

    /**
     * Returns all Questions of the Questions-table
     *
     * @return A cursor which holds all Questions-Items
     */
    public Cursor getQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + QuestionContract.QuestionEntry.TABLE_NAME, null);

        //Log.d(TAG, "getQuestionListContents: Cursorcontent: " + DatabaseUtils.dumpCursorToString(data));
        return data;
    }

    /**
     * 
     * @return
     */
    public Cursor getHighscoresInDescendingOrder(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + HighscoreContract.HighscoreEntry.TABLE_NAME
                + " ORDER BY " + HighscoreContract.HighscoreEntry.COLUMN_POINTS + " DESC", null);

        Log.d(TAG, "getHighscores: FilteredCursorcontent: " + DatabaseUtils.dumpCursorToString(data));
        return data;
    }

    /**
     * Adds the content of a ContentValues-Object into the "Question" table
     *
     * @param db The database you want to work on
     * @param cv A ContentValues-Object which holds data-pairs, where the column-name of the table is the key and value is the value
     */
    public void addHighscore(SQLiteDatabase db, ContentValues cv) {
        long result = db.insert(HighscoreContract.HighscoreEntry.TABLE_NAME, null, cv);

        if (result == -1) {
            Log.d(TAG, "addHighscore: ERROR! data-Set couldn't be added!");
        } else {
            Log.d(TAG, "addHighscore: SUCCESS! The data-Set was added: " + cv.toString());
        }
    }

    /**
     * Returns all Questions of the Questions-table
     *
     * @param difficulty
     * @param category
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

    /**
     *
     * @param db
     */
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

    /**
     *
     * @param db
     */
    private void createHighscoreTable(SQLiteDatabase db) {
        String sSQL = "CREATE TABLE IF NOT EXISTS " + HighscoreContract.HighscoreEntry.TABLE_NAME + " ("
                + HighscoreContract.HighscoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HighscoreContract.HighscoreEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + HighscoreContract.HighscoreEntry.COLUMN_MAX_DIFFICULTY + " INTEGER NOT NULL, "
                + HighscoreContract.HighscoreEntry.COLUMN_MAX_STREAK + " INTEGER NOT NULL, "
                + HighscoreContract.HighscoreEntry.COLUMN_POINTS + " INTEGER NOT NULL"
                + ");";

        Log.d(TAG, "createHighscoreTable: " + sSQL);
        try {
            db.execSQL(sSQL);
        } catch (android.database.SQLException e) {
            Log.d(TAG, "createHighscoreTable: SQL: syntax error :\n" + e.toString());
        }
    }

    /**
     *
     * @param db
     * @param _id
     */
    public void removeHighscoreByID(SQLiteDatabase db, long _id) {
        int result = db.delete(HighscoreContract.HighscoreEntry.TABLE_NAME, HighscoreContract.HighscoreEntry._ID + "=" + _id, null);

        if (result == 0) {
            Log.d(TAG, "removeItem: ERROR! NO row with the ID: " + _id + " was found");
        } else {
            Log.d(TAG, "removeQuestion: SUCCESS! The row with the ID: " + _id + ", was deleted");
        }
    }
}
