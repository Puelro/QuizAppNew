package com.example.quizappnew.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizappnew.R;

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
                Intent intent = new Intent(Score.this,Highscore.class);
                intent.putExtra("FINAL_SCORE", getIntent().getLongExtra("FINAL_SCORE",-1));
                intent.putExtra("MAX_STREAK", getIntent().getIntExtra("MAX_STREAK",-1));
                intent.putExtra("MAX_LEVEL", getIntent().getIntExtra("MAX_LEVEL", -1));
                intent.putExtra("AFTER_GAME", true);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed(){
        createDropDialoGoBackToMainMenu();
    }

    private void createDropDialoGoBackToMainMenu(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Willst du wirklich den Highscore-Bildschirm überspringen und zurück zum Hauptmenü?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("JA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Score.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        });

        alertDialog.create().show();
    }
}