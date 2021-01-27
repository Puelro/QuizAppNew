package com.example.quizappnew.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import android.os.CountDownTimer;

import com.example.quizappnew.R;
import com.example.quizappnew.play_helper.AnswerbuttonManager;
import com.example.quizappnew.play_helper.ProgressbarManager;
import com.example.quizappnew.play_helper.QuestionManager;
import com.example.quizappnew.play_helper.StreakAndPointsManager;
import com.example.quizappnew.play_helper.TextViewManager;

// ---------------------------------------------------------------------------------------------- //
public class Play extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    int levelTimeSeconds = 10;

    TextViewManager textViewManager;
    ProgressbarManager progressbarManager;
    QuestionManager questionManager;
    StreakAndPointsManager streakAndPointsManager;
    AnswerbuttonManager answerbuttonManager;

    CountDownTimer questionPointsTimer;
    CountDownTimer levelTimer;

    long currentScore;

    Button buttonMenu;
    Button buttonJoker50_50;
    Button buttonJokerStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        streakAndPointsManager = new StreakAndPointsManager();
        // TODO Difficulty and category will be passed from previous Activity
        questionManager = new QuestionManager(1, null, this, streakAndPointsManager);
        answerbuttonManager = new AnswerbuttonManager(this, questionManager);
        textViewManager = new TextViewManager(this, questionManager, answerbuttonManager);
        progressbarManager = new ProgressbarManager(this, currentScore, questionManager, textViewManager);

        //sets the default values of the variables
        initiateValues();
        initiateTimer();

        questionManager.loadFilteredQuestions();
        questionManager.setRandomQuestion(answerbuttonManager);
        textViewManager.fillQuestionTextFields();

        setButtonListeners();
        progressbarManager.setProgressbar(currentScore, levelTimer, buttonJokerStreak);
    }

    private void initiateValues() {
        currentScore = 0;

        buttonJoker50_50 = findViewById(R.id.btnJoker50_50);
        buttonJokerStreak = findViewById(R.id.btnJokerStreak);
        buttonMenu = findViewById(R.id.btnMenu);
    }

    private void initiateTimer(){
        levelTimer = new CountDownTimer(levelTimeSeconds*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewManager.setTvTimer("Timer: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Play.this, Score.class);
                intent.putExtra("FINAL_SCORE", currentScore);
                intent.putExtra("MAX_STREAK", streakAndPointsManager.getMaxStreak());
                intent.putExtra("MAX_LEVEL", questionManager.getCurrentDifficulty());
                startActivity(intent);
            }
        }.start();

        questionPointsTimer = new CountDownTimer(5000, 1) {

            int millisInFuture = 5000;
            long basePointsPerQuestion = streakAndPointsManager.getTimedPointsPerQuestion();

            public void onTick(long millisUntilFinished) {
                long currentPoints = (basePointsPerQuestion / 2) + ( ( ( basePointsPerQuestion / 2 ) / 100 ) * ( millisUntilFinished /  (millisInFuture / 100) ) );
                streakAndPointsManager.setTimedPointsPerQuestion( currentPoints );
                textViewManager.setTvPoints( String.valueOf(streakAndPointsManager.getPointsForCurrentQuestion(questionManager) ) );
            }
            public void onFinish() {
                streakAndPointsManager.setTimedPointsPerQuestion(500);
                textViewManager.setTvPoints(String.valueOf(streakAndPointsManager.getPointsForCurrentQuestion(questionManager)));
            }
        }.start();
    }

    private void joker50_50() {
        buttonJoker50_50.setEnabled(false);

        questionManager.jocker50_50(answerbuttonManager);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonJoker50_50.setEnabled(true);
            }
        }, 10000);
    }

    private void jokerStreak(){
        buttonJokerStreak.setEnabled(false);
        streakAndPointsManager.previousStreakToCurrentStreak(textViewManager);
        streakAndPointsManager.previousMultiplierToCurrentMultiplier(textViewManager);
    }

    public void answerButtonWasClicked(int buttonNumber){
        answerbuttonManager.enableAllAnswerButtons(false);
        questionPointsTimer.cancel();

        if(questionManager.isRightAnswer(buttonNumber)){
            currentScore = streakAndPointsManager.getPointsAndShowThem(currentScore, questionManager, textViewManager);
            streakAndPointsManager.increaseStreakAndMultiplier(currentScore, textViewManager);
            progressbarManager.setProgressbar(currentScore, levelTimer, buttonJokerStreak);
        }else{
            streakAndPointsManager.decreaseStreakAndMultiplier(textViewManager);
        }

        questionManager.removeQuestionFromList(questionManager.getCurrentQuestions().indexOf(questionManager.getCurrentQuestion()));
        questionManager.setRandomQuestion(answerbuttonManager);
        textViewManager.fillQuestionTextFields();
        streakAndPointsManager.setTimedPointsPerQuestion(1000);
        questionPointsTimer.start();
        answerbuttonManager.enableAllAnswerButtons(true);
    }


    private void setButtonListeners(){

        buttonJokerStreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jokerStreak();
            }
        });

        buttonJoker50_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joker50_50();
            }
        });

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelTimer.cancel();
                questionPointsTimer.cancel();
                finish();
                Intent intent = new Intent(Play.this,Menu.class);
                startActivity(intent);
            }
        });
    }

}