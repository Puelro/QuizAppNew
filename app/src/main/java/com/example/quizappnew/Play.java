package com.example.quizappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Play extends AppCompatActivity {

    Button buttonMenu;
    Button buttonJoker1;
    TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        buttonMenu = findViewById(R.id.btnMenu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Play.this,Menu.class);
                startActivity(intent);
            }
        });

        timer = findViewById(R.id.tvTimer);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Timer: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                Intent intent = new Intent(Play.this,Score.class);
                startActivity(intent);
            }
        }.start();

        buttonJoker1 = findViewById(R.id.btnJoker);
        buttonJoker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}