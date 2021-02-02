package com.example.quizappnew.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.quizappnew.R;
import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.HighscoreAdapter;
import com.example.quizappnew.database.HighscoreContract;
import com.example.quizappnew.database.HighscoreContract.HighscoreEntry;

public class Highscore extends AppCompatActivity {
    private static final String TAG = "Highscore";

    SQLiteDatabase db;
    AppDatabase appDatabase;

    EditText etxtName;
    HighscoreAdapter adapter;
    Button buttonAddHighscore;
    Button buttonDropTableHighscores;
    Button buttonMenu;

    /**
     * initiate values
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        appDatabase = AppDatabase.getInstance(this);
        db = appDatabase.getWritableDatabase();

        setButtons();

        RecyclerView recyclerView = findViewById(R.id.rvScoreList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HighscoreAdapter(this, appDatabase.getHighscoresInDescendingOrder());
        recyclerView.setAdapter(adapter);
    }

    /**
     * initiates buttons
     */
    private void setButtons(){

        buttonDropTableHighscores = findViewById(R.id.btnDropHighscores);
        buttonDropTableHighscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDatabase.dropAndRecreateTableHighscore(db);
                adapter.swapCursor(appDatabase.getHighscoresInDescendingOrder());
            }
        });

        buttonMenu = findViewById(R.id.btnMenuHighscore);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Highscore.this,Menu.class);
                startActivity(intent);
            }
        });

        etxtName = findViewById(R.id.etxtName);
        etxtName.setVisibility(View.INVISIBLE);
        etxtName.setEnabled(false);

        buttonAddHighscore = findViewById(R.id.btnRegister);
        buttonAddHighscore.setVisibility(View.INVISIBLE);
        buttonAddHighscore.setEnabled(false);

        // option to enter new Highscore only after a game and
        // if less than ten Highscores exist or new Highscore is higher than lowest Highscore
        if( isAfterGame() && ( hasLessThan10Entries() || pointsAreHigherThanLowestHighscore() ) ){
            etxtName.setVisibility(View.VISIBLE);
            etxtName.setEnabled(true);

            buttonAddHighscore.setVisibility(View.VISIBLE);
            buttonAddHighscore.setEnabled(true);
        }

        // option to enter a name for new Highscore
        // if no name is entered, name is "John Doe"
        buttonAddHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                if( etxtName.getText().toString().length() > 0 ){
                    name = etxtName.getText().toString();
                }else{
                    name = "John Doe";
                }

                if(!hasLessThan10Entries()){
                    removeLowestHighscore();
                }

                ContentValues cv = new ContentValues();
                cv.put( HighscoreEntry.COLUMN_NAME, name );
                cv.put( HighscoreEntry.COLUMN_POINTS, getIntent().getLongExtra("FINAL_SCORE", -1) );
                cv.put( HighscoreEntry.COLUMN_MAX_STREAK, getIntent().getIntExtra("MAX_STREAK", -1) );
                cv.put( HighscoreEntry.COLUMN_MAX_DIFFICULTY, getIntent().getIntExtra("MAX_LEVEL", -1) );
                appDatabase.addHighscore(db, cv);

                Log.d(TAG, "onClick(addHighscore): ContentValues: " + cv.toString());

                etxtName.getText().clear();

                etxtName.setVisibility(View.INVISIBLE);
                etxtName.setEnabled(false);

                buttonAddHighscore.setVisibility(View.INVISIBLE);
                buttonAddHighscore.setEnabled(false);

                adapter.swapCursor(appDatabase.getHighscoresInDescendingOrder());
            }

        });
    }

    /**
     * checks if activity_highscore was entered from after a game
     *
     * @return true if activity was entered from after a game
     * @return false if activity was entered from Menu
     */
    private boolean isAfterGame(){
        return getIntent().getBooleanExtra("AFTER_GAME", false);
    }

    /**
     * checks if Highscore list has less than 10 Entries
     *
     * @return true if it's less than 10
     * @return false if it's 10
     */
    public boolean hasLessThan10Entries() {
        Cursor data = db.rawQuery("SELECT * FROM " + HighscoreContract.HighscoreEntry.TABLE_NAME, null);
        return data.getCount() < 10;
    }

    /**
     * checks if new Score is higher than lowest Highscore
     *
     * @return true if new Score is higher
     * @return false if new Score is lower
     */
    public boolean pointsAreHigherThanLowestHighscore() {
        return getLowestHighscore() < getIntent().getLongExtra("FINAL_SCORE", -1);
    }

    /**
     * gets the lowest Highscore in the list
     *
     * @return lowest Highscore
     */
    private long getLowestHighscore() {
        long lowestHighscore;

        Cursor data = db.rawQuery("SELECT * FROM " + HighscoreContract.HighscoreEntry.TABLE_NAME
                + " ORDER BY " + HighscoreContract.HighscoreEntry.COLUMN_POINTS + " ASC LIMIT 1", null);

        if(data.getCount() > 0){
            Log.d(TAG, "newPointsLesserLowestHighscore: " + DatabaseUtils.dumpCursorToString(data));

            data.moveToPosition(0);
            ContentValues cv = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(data, cv);

            lowestHighscore = cv.getAsLong(HighscoreEntry.COLUMN_POINTS);

            return lowestHighscore;
        }else{
            return -1;
        }
    }

    /**
     * removes the lowest Highscore in the list
     */
    private void removeLowestHighscore(){
        Cursor data = db.rawQuery("SELECT * FROM " + HighscoreContract.HighscoreEntry.TABLE_NAME
                + " ORDER BY " + HighscoreContract.HighscoreEntry.COLUMN_POINTS + " ASC LIMIT 1", null);

        data.moveToPosition(0);

        ContentValues cv = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(data, cv);

        long idOflowestHighscore = cv.getAsLong(HighscoreEntry._ID);

        appDatabase.removeHighscore(db, idOflowestHighscore);
    }


}