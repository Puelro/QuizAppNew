package com.example.quizappnew;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quizappnew.database.AppDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Databasefiller {

    private static final String TAG = "DatabaseFiller";

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static void dropAndFillDatabase(Context ctx, AppDatabase adb, SQLiteDatabase db){
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
