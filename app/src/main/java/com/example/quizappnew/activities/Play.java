package com.example.quizappnew.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.os.CountDownTimer;
import android.widget.Toast;

import com.example.quizappnew.R;
import com.example.quizappnew.database.QuestionContract;
import com.example.quizappnew.play_logic.AnswerbuttonManager;
import com.example.quizappnew.play_logic.ProgressbarManager;
import com.example.quizappnew.play_logic.QuestionManager;
import com.example.quizappnew.play_logic.StreakAndPointsManager;
import com.example.quizappnew.play_logic.TextViewManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Kent Feldner / Robin Püllen
 */
public class Play extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    /**
     * The time one level lasts
     * If the needed points are reached the levelTimer should be reset to this value
     */
    int levelTimeSeconds = 30;

    /** The instance of the {@link TextViewManager} */
    TextViewManager textViewManager;
    /** The instance of the {@link ProgressbarManager} */
    ProgressbarManager progressbarManager;
    /** The instance of the {@link QuestionManager} */
    QuestionManager questionManager;
    /** The instance of the {@link StreakAndPointsManager} */
    StreakAndPointsManager streakAndPointsManager;
    /** The instance of the {@link AnswerbuttonManager} */
    AnswerbuttonManager answerbuttonManager;

    /** The timer which controls the reduction of points for the current question */
    CountDownTimer questionPointsTimer;
    /** The timer which ends the game if it reaches 0 */
    CountDownTimer levelTimer;

    /** the currentScore of the Player */
    long currentScore;

    /** The UI Element for the menu button*/
    Button buttonMenu;
    /** The UI Element for the 50:50 Joker button*/
    Button buttonJoker50_50;
    /** The UI Element for the streak Joker button*/
    Button buttonJokerStreak;

    /**
     * initiate values
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //sets the default values of the variables
        initiateValues();
        initiateTimer();

        questionManager.loadFilteredQuestions();
        questionManager.setRandomQuestion(answerbuttonManager);
        textViewManager.fillQuestionTextFieldsRandom();

        setButtonListeners();
        progressbarManager.updateProgressbar(currentScore, levelTimer, buttonJokerStreak);
    }

    /**
     * initiates Values for buttons and current score
     */
    private void initiateValues() {
        streakAndPointsManager = new StreakAndPointsManager();
        // TODO Difficulty and category will be passed from previous Activity
        questionManager = new QuestionManager(1, null, this, streakAndPointsManager);
        answerbuttonManager = new AnswerbuttonManager(this);
        textViewManager = new TextViewManager(this, questionManager, answerbuttonManager);
        progressbarManager = new ProgressbarManager(this, currentScore, questionManager, textViewManager);

        currentScore = 0;

        buttonJoker50_50 = findViewById(R.id.btnJoker50_50);
        buttonJokerStreak = findViewById(R.id.btnJokerStreak);
        buttonMenu = findViewById(R.id.btnMenu);
    }

    /**
     * initiates Timer for level and Timer for each question
     */
    private void initiateTimer(){
        levelTimer = new CountDownTimer(levelTimeSeconds*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewManager.setTvTimer("Timer: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Play.this, Score.class);
                // append data for Score activity
                intent.putExtra("FINAL_SCORE", currentScore);
                intent.putExtra("MAX_STREAK", streakAndPointsManager.getMaxStreak());
                intent.putExtra("MAX_LEVEL", questionManager.getCurrentDifficulty());
                startActivity(intent);
                finish();
            }
        }.start();

        // centralised millis in future for questionTimer
        int millisInFutureQuestionTimer = 5000;

        questionPointsTimer = new CountDownTimer(millisInFutureQuestionTimer, 1) {
            // get the basepoints per question
            long basePointsPerQuestion = streakAndPointsManager.getBasePointsPerQuestion();

            public void onTick(long millisUntilFinished) {
                // calculate halt the base points for the question
                long halfTheBasePoints = basePointsPerQuestion / 2;
                // calculate one percent of the other half of the base points
                long onePercentOfHalfTheBasePoints = ( basePointsPerQuestion / 2 ) / 100;
                // calculate the percentage of time passed of millisInTheFuture
                long percentOfPassedMilliseconds = millisUntilFinished /  (millisInFutureQuestionTimer / 100);

                // add to the first half of basepoints the percentage of points of the other half of the basepoints equal to the percentage of passed time
                long currentPoints = ( halfTheBasePoints ) + ( onePercentOfHalfTheBasePoints * percentOfPassedMilliseconds );
                // update the points you get for the question
                streakAndPointsManager.setPointsPerQuestion( currentPoints );
                // update the score UI element
                textViewManager.setTvPoints( String.valueOf(streakAndPointsManager.getPointsForCurrentQuestion(questionManager) ) );
            }
            public void onFinish() {
                // if timer finishes set points to half of the basepoints
                streakAndPointsManager.setPointsPerQuestion(basePointsPerQuestion / 2);
                // update UI element
                textViewManager.setTvPoints(String.valueOf(streakAndPointsManager.getPointsForCurrentQuestion(questionManager)));
            }
        }.start();
    }

    /**
     * inittiates 50/50 Joker
     */
    private void joker50_50() {
        buttonJoker50_50.setEnabled(false);

        questionManager.joker50_50(answerbuttonManager);

        String questionAtButtonpressText = questionManager.getCurrentQuestion().getAsString(QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT);

        // enable 50/50 joker after 10 seconds
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // if the current question isn't the old question
                if(questionAtButtonpressText != questionManager.getCurrentQuestion().getAsString(QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT)){
                    // enable the 50_50 button
                    buttonJoker50_50.setEnabled(true);
                }else{
                    // create a new timer and check every 100ms if the question has changed
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        // counts the calls
                        int count = 0;
                        @Override
                        public void run() {
                            // if the question has changed
                            if(questionAtButtonpressText != questionManager.getCurrentQuestion().getAsString(QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT)){
                                // run in UI thread because of UI change in new thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // enable 50_50 joker
                                        buttonJoker50_50.setEnabled(true);
                                    }
                                });
                                // end timer
                                timer.cancel();
                            } // or if the timer is longer alive than the level can exist without a new question
                            else if( count > ( levelTimeSeconds * (1000 / 100) ) ){
                                timer.cancel();
                            }
                            // raise the counter
                            count ++;
                        } // end run()
                    }, 0, 100); // end inner timer.shedule
                }  // end outer else
            } // end outer if
        }, 5000); // end handler.postDelayed
    } // end joker50_50

    /**
     * initiates Joker to return to previous Streak
     */
    private void jokerStreak(){
        buttonJokerStreak.setEnabled(false);
        streakAndPointsManager.previousStreakToCurrentStreak(textViewManager);
        streakAndPointsManager.previousMultiplierToCurrentMultiplier(textViewManager);
        // will be enabled again, in the progressbar class, every time the bar is filled
        // / the level changes
    }

    /**
     * when AnswerButton was clicked:
     * resets pointsTimer
     * increases or decreases points, streak and progressbar
     * load new Question
     *
     * @param buttonNumber ButtonNumber of clicked AnswerButton
     */
    public void answerButtonWasClicked(int buttonNumber){
        answerbuttonManager.enableAllAnswerButtons(false);
        questionPointsTimer.cancel();
        colorAnswerButton(buttonNumber);

        if(questionManager.isRightAnswer(buttonNumber)){
            currentScore = streakAndPointsManager.getCurrentPointsAndShowThem(currentScore, questionManager, textViewManager);
            streakAndPointsManager.increaseStreakAndMultiplier(textViewManager);
            progressbarManager.updateProgressbar(currentScore, levelTimer, buttonJokerStreak);
        }else{
            streakAndPointsManager.decreaseStreakAndMultiplier(textViewManager);
        }

        questionManager.removeQuestionFromList(questionManager.getCurrentQuestions().indexOf(questionManager.getCurrentQuestion()));
        questionManager.setRandomQuestion(answerbuttonManager);
        textViewManager.fillQuestionTextFieldsRandom();
        streakAndPointsManager.setPointsPerQuestion(1000);
        questionPointsTimer.start();
        answerbuttonManager.enableAllAnswerButtons(true);
    }


    /**
     * sets onClickListener for JokerButtons ans MenuButton
     */
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
                createDropDialogGoBackToMainMenu();
            }
        });
    }

    /**
     * colors background of clicked AnswerButton green if answer was true and red if answer was wrong
     * !!!!!!!!! doesn't work yet !!!!!!!!!
     * @TODO Make method work as intended
     * @param buttonNumber pressed answer button
     */
    public void colorAnswerButton(int buttonNumber){
        if(questionManager.isRightAnswer(buttonNumber)){
            answerbuttonManager.getAnswerButtons().get(buttonNumber -1).getUIButton().setBackgroundColor(getResources().getColor(R.color.button_background_color_correct));
        }
        else{
            answerbuttonManager.getAnswerButtons().get(buttonNumber -1).getUIButton().setBackgroundColor(getResources().getColor(R.color.button_background_color_wrong));
        }
        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                answerbuttonManager.getAnswerButtons().get(buttonNumber -1).getUIButton().setBackgroundColor(getResources().getColor(R.color.button_background_color_default));
            }
        },100);
    }

    /**
     * backButton leads to MainMenu
     */
    @Override
    public void onBackPressed(){
        createDropDialogGoBackToMainMenu();
    }

    /**
     * creates a Dialog to confirm to go back to mainMenu
     */
    private void createDropDialogGoBackToMainMenu(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Willst du wirklich zurück zum Hauptmenü?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                levelTimer.cancel();
                questionPointsTimer.cancel();
                Intent intent = new Intent(Play.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        });

        alertDialog.create().show();
    }
}