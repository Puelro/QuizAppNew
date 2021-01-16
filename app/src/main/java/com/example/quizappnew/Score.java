package com.example.quizappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Score extends AppCompatActivity {

    Button buttonHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        TextView tvMaxStreak = findViewById(R.id.tvMaxStreak);
        TextView tvResetTimes = findViewById(R.id.tvResetTimes);

        long finalScore = getIntent().getLongExtra("FINAL_SCORE",-1);
        int maxStreak   = getIntent().getIntExtra("MAX_STREAK",-1);
        int maxLevel    = getIntent().getIntExtra("MAX_LEVEL", -1);

        tvFinalScore.setText(String.valueOf("Finaler Score: " + finalScore));
        tvMaxStreak.setText( "Maximaler Streak: " + String.valueOf(maxStreak));
        tvResetTimes.setText("Maximaler Level: " + String.valueOf(maxLevel));

        buttonHighscore = findViewById(R.id.btnFinalScore);
        buttonHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Score.this,Highscore.class);
                startActivity(intent);
            }
        });
    }
}