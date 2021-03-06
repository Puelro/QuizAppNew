package com.example.quizappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizappnew.database.AppDatabase;

public class Menu extends AppCompatActivity {

    Button buttonPlay;
    Button buttonHighscore;
    Button buttonOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*AppDatabase appDatabase = AppDatabase.getInstance(this);
        final SQLiteDatabase db = appDatabase.getReadableDatabase();*/

        buttonPlay = findViewById(R.id.btnPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Play.class);
                startActivity(intent);
            }
        });

        buttonOption = findViewById(R.id.btnOption);
        buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, DatabaseEditor.class);
                startActivity(intent);
            }
        });

        buttonHighscore = findViewById(R.id.btnHighscore);
        buttonHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Highscore.class);
                startActivity(intent);
            }
        });

    }
}