package com.example.quizappnew.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.CountDownTimer;

import com.example.quizappnew.R;
import com.example.quizappnew.play_helper.Answerbutton;
import com.example.quizappnew.play_helper.AnswerbuttonManager;
import com.example.quizappnew.play_helper.ProgressbarManager;
import com.example.quizappnew.play_helper.QuestionManager;
import com.example.quizappnew.play_helper.StreakManager;
import com.example.quizappnew.play_helper.TextViewManager;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.quizappnew.database.QuestionContract.*;
// ---------------------------------------------------------------------------------------------- //
public class Play extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    TextViewManager textViewManager;
    ProgressbarManager progressbarManager;
    QuestionManager questionManager;
    StreakManager streakManager;
    AnswerbuttonManager answerbuttonManager;

    CountDownTimer questionPointsTimer;
    CountDownTimer levelTimer;

    long currentScore;

    Button buttonMenu;
    Button buttonJoker50_50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        streakManager = new StreakManager();
        // TODO Difficulty and category will be passed from previous Activity
        questionManager = new QuestionManager(1, null, this, streakManager);
        answerbuttonManager = new AnswerbuttonManager(this, questionManager);
        textViewManager = new TextViewManager(this, questionManager, answerbuttonManager);
        progressbarManager = new ProgressbarManager(this, currentScore, questionManager, textViewManager);

        //sets the default values of the variables
        initiateValues();

        questionManager.loadFilteredQuestions();

        questionManager.setRandomQuestion(answerbuttonManager);
        textViewManager.fillQuestionTextFields();

        setAllListenersFromButtonsAndTextfieldsExceptAnswers();

        progressbarManager.setProgressbar(currentScore, levelTimer);
    }

    private void initiateValues() {
        currentScore = 0;

        buttonJoker50_50 = findViewById(R.id.btnJoker50_50);
        buttonMenu = findViewById(R.id.btnMenu);

        levelTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewManager.setTvTimer("Timer: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Play.this, Score.class);
                intent.putExtra("FINAL_SCORE", currentScore);
                intent.putExtra("MAX_STREAK", streakManager.getMaxStreak());
                intent.putExtra("MAX_LEVEL", questionManager.getCurrentDifficulty());
                startActivity(intent);
            }
        }.start();

        questionPointsTimer = new CountDownTimer(5000, 1) {

            int millisInFuture = 5000;
            long basePointsPerQuestion = streakManager.getTimedPointsPerQuestion();

            public void onTick(long millisUntilFinished) {
                long currentPoints = (basePointsPerQuestion / 2) + ( ( ( basePointsPerQuestion / 2 ) / 100 ) * ( millisUntilFinished /  (millisInFuture / 100) ) );
                streakManager.setTimedPointsPerQuestion( currentPoints );
                textViewManager.setTvPoints( String.valueOf(streakManager.getPointsForCurrentQuestion(questionManager) ) );
            }
            public void onFinish() {
                streakManager.setTimedPointsPerQuestion(500);
                textViewManager.setTvPoints(String.valueOf(streakManager.getPointsForCurrentQuestion(questionManager)));
            }
        }.start();
    }


    private void joker50_50() {
        buttonJoker50_50.setEnabled(false);

        int correctAnswer = questionManager.getCurrentQuestion().getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER);

        ArrayList<Answerbutton> answerButtons = answerbuttonManager.getAnswerButtons();

        answerButtons.remove(correctAnswer - 1);

        // Removes another wrong answer at an random index between 0 and 2 (max index of the ArrayList)
        // leaves 2 wrong answers in the ArrayList
        answerButtons.remove( (int) Math.floor( Math.random() * 2 ) ) ;

        for(Answerbutton answer: answerButtons){
            answer.getUIButton().setEnabled(false);
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonJoker50_50.setEnabled(true);
            }
        }, 10000);
    }

    public void buttonWasClicked(int buttonNumber){
        answerbuttonManager.enableAllAnswerButtons(false);
        questionPointsTimer.cancel();

        if(questionManager.isRightAnswer(buttonNumber)){
            currentScore = streakManager.givePointsAndRaiseStreak(currentScore, questionManager, textViewManager);
            progressbarManager.setProgressbar(currentScore, levelTimer);
        }else{
            streakManager.decreaseStreakAndMultiplier(textViewManager);
        }

        questionManager.removeQuestionFromList(questionManager.getCurrentQuestions().indexOf(questionManager.getCurrentQuestion()));
        questionManager.setRandomQuestion(answerbuttonManager);
        textViewManager.fillQuestionTextFields();
        streakManager.setTimedPointsPerQuestion(1000);
        questionPointsTimer.start();

        answerbuttonManager.enableAllAnswerButtons(true);
    }


    private void setAllListenersFromButtonsAndTextfieldsExceptAnswers(){

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