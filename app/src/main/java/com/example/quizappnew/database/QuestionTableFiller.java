package com.example.quizappnew.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quizappnew.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Outsources the logic to fill the Question table from {@link AppDatabase}
 * @author Kent Feldner
 */
public class QuestionTableFiller {

    private static final String TAG = "DatabaseFiller";

    /**
     * Drops the current Question table, recreates it and  fills it with a default set of questions.
     * The default set has to be in a .txt file and needs to be placed in the directory under the path: app/src/main/res/raw
     * @param ctx A Context
     * @param adb The singleton instance of the AppDatabase class
     * @param db The Database
     */
    public static void dropAndFillTableQuestions(Context ctx, AppDatabase adb, SQLiteDatabase db){
        // Load the questions.txt file from the path %app/res/raw into an InputStream
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.questions);

        // Load the InputStream into an InputStreamReader and load this one into a BufferedReader
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);

        // Drop the Table Questions and recreate it
        adb.dropAndRecreateTableQuestion(db);

        // try to access the questions.txt file
        try {

            // read the first line
            String line = buffreader.readLine();

            // as long as there are lines
            while (line != null) {
                // add the read questions into the Questions table
                adb.addQuestion(db, line);
                // read the next line
                line = buffreader.readLine();
            }

            // close everything
            buffreader.close();
            inputreader.close();
            inputStream.close();
        }
        catch (IOException e) {
            // Handle a potential exception
            Log.d(TAG, "dropAndFillDatabase: Error with the file" );
        }
    }
}
