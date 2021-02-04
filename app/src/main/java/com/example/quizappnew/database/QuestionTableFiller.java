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
     * Drops the current Question table, recreates it and  fills it with default set of questions.
     * The default set has to be in a .txt file which is saved under the path: app/src/main/res/raw
     * @param ctx
     * @param adb
     * @param db
     */
    public static void dropAndFillTableQuestions(Context ctx, AppDatabase adb, SQLiteDatabase db){
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.questions);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);

        adb.dropAndRecreateTableQuestion(db);

        try {

            String line = buffreader.readLine();

            while (line != null) {

                adb.addQuestion(db, line);
                line = buffreader.readLine();
            }

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
