package com.example.quizappnew.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizappnew.R;
import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.QuestionTableFiller;

/**
 * @author Robin Püllen
 */
public class Menu extends AppCompatActivity {

    /** UI element */
    private Button buttonPlay;
    /** UI element */
    private Button buttonHighscore;
    /** UI element */
    private Button buttonOption;

    /**
     * initiate values
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        SQLiteDatabase db = appDatabase.getWritableDatabase();

        //if Database is empty fill Database with questions from question.txt
        if(appDatabase.getQuestions().getCount() == 0){
            QuestionTableFiller.dropAndFillTableQuestions(this, appDatabase, db);
        }

        buttonPlay = findViewById(R.id.btnPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Play.class);
                startActivity(intent);
                finish();
            }
        });

        buttonOption = findViewById(R.id.btnQuestionEditor);
        buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, DatabaseEditor.class);
                startActivity(intent);
                finish();
            }
        });

        buttonHighscore = findViewById(R.id.btnHighscore);
        buttonHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Highscore.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * registers if backButton was clicked after reaching a new Highscore
     * backButton leads to MainMenu
     */
    @Override
    public void onBackPressed(){
            finish();
    }
}