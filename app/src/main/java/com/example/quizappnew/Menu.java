package com.example.quizappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button buttonPlay;
    Button buttonHighscore;
    Button buttonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        buttonPlay = findViewById(R.id.btnPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Play.class);
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

        buttonQuit = (Button) findViewById(R.id.btnQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

    }
}