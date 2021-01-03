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

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase : constructor");
    }

    /**
     * Get an instance of the app's singleton database helper object
     * @param context the content providers context.
     * @return a SQLlite database helper object
     */
    public static AppDatabase getInstance(Context context){
        if(instance == null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }

        return instance;
    }

    public void removeItem(SQLiteDatabase db, long _id){
        db.delete(QuestionEntry.TABLE_NAME, QuestionEntry._ID + "=" + _id, null);
    }

    public void addItem(SQLiteDatabase db, ContentValues cv){

        long result = db.insert(QuestionContract.QuestionEntry.TABLE_NAME, null, cv);

        if(result == -1){
            Log.d(TAG, "addQuestion: Error, Data-Set couldn't be added!");
        }
    }

    public void dropTableHighscore(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS HIGHSCORE;");

        String sSQL =   "CREATE TABLE IF NOT EXISTS" + "HIGHSCORE" + " ("
                + "ID INTEGER PRIMARY KEY, "
                + "NAME TEXT NOT NULL, "
                + "SCORE NUMBER NOT NULL)";
        Log.d(TAG, "onCreate: " + sSQL);
    }

    public Cursor getQuestionListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data =  db.rawQuery("SELECT * FROM " + QuestionContract.QuestionEntry.TABLE_NAME, null);

        Log.d(TAG, "getQuestionListContents: Cursorcontent: " + DatabaseUtils.dumpCursorToString(data) );
        return data;
    }

    public void dropTableQuestion(SQLiteDatabase db){
        db.execSQL( "DROP TABLE IF EXISTS " + QuestionContract.QuestionEntry.TABLE_NAME + ";");

        String sSQL =   "CREATE TABLE IF NOT EXISTS "
                + QuestionContract.QuestionEntry.TABLE_NAME + " ("
                + QuestionContract.QuestionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuestionContract.QuestionEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT+ " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT1 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN1 + " BIT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT2 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN2 + " BIT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT3 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN3 + " BIT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT4 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN4 + " BIT NOT NULL"
                + ");";
        db.execSQL(sSQL);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        String sSQL = "";
        sSQL =   "CREATE TABLE IF NOT EXISTS "
                + QuestionContract.QuestionEntry.TABLE_NAME + " ("
                + QuestionContract.QuestionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuestionContract.QuestionEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT+ " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT1 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN1 + " BIT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT2 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN2 + " BIT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT3 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN3 + " BIT NOT NULL, "
                + QuestionContract.QuestionEntry .COLUMN_ANSWERTEXT4 + " TEXT NOT NULL, "
                + QuestionContract.QuestionEntry.COLUMN_BOOLEAN4 + " BIT NOT NULL"
                + ");";
        Log.d(TAG, "onCreate: " + sSQL);
        db.execSQL(sSQL);

        sSQL =   "CREATE TABLE IF NOT EXISTS " + "HIGHSCORE" + " ("
                + "ID INTEGER PRIMARY KEY, "
                + "NAME TEXT NOT NULL, "
                + "SCORE NUMBER NOT NULL)";
        Log.d(TAG, "onCreate: " + sSQL);
        db.execSQL(sSQL);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch(oldVersion) {
            case 1:
                //upgrade logic from version
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion" + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }
}
