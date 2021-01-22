package com.example.quizappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.CountDownTimer;

import com.example.quizappnew.database.AppDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.quizappnew.database.QuestionContract.*;
// ---------------------------------------------------------------------------------------------- //
public class Play extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    ProgressBar progressBar;
    final long[] basePointsPerLevel = new long[]{30000, 60000, 90000, 120000, 150000};
    long startPoints;
    long currentPointsGoal;

    CountDownTimer questionPointsTimer;
    CountDownTimer levelTimer;

    Cursor currentQuestions;
    ContentValues currentQuestion;

    int currentDifficulty;
    String currentCategory;

    int pastStreak;
    int currentStreak;
    int maxStreak;

    double pastStreakMultiplier;
    double currentStreakMultiplier;

    long currentScore;
    long[] timedPointsPerQuestion;

    // -----------------------------------------

    TextView tvTimer;
    TextView tvStreak;
    TextView tvScore;
    TextView tvThisQuestionPoints;
    TextView tvMissing;
    TextView tvMultiplier;
    TextView tvPoints;

    Button buttonAnswer1;
    Button buttonAnswer2;
    Button buttonAnswer3;
    Button buttonAnswer4;

    Button buttonMenu;
    Button buttonJoker50_50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //sets the default values of the variables
        initiateValues();

        loadFilteredQuestions();

        setRandomQuestion();
        fillQuestionTextFields();

        setAllListenersAnswerButtons();
        setAllListenersFromButtonsAndTextfieldsExceptAnswers();

        setProgressBar();

    }

    private void initiateValues() {
        tvThisQuestionPoints = findViewById(R.id.tvAddPoints);
        tvThisQuestionPoints.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.vertical_progressbar);
        progressBar.setMax(100);

        currentDifficulty = 1;
        currentCategory= null;

        pastStreak = 0;
        currentStreak = 0;
        maxStreak = 0;

        pastStreakMultiplier = 1;
        currentStreakMultiplier = 1;

        currentScore = 0;
        timedPointsPerQuestion = new long[] {1000};

        startPoints = currentScore;
        currentPointsGoal = basePointsPerLevel[0];

        tvStreak = findViewById(R.id.tvStreak);
        tvScore = findViewById(R.id.tvScore);
        tvMultiplier = findViewById(R.id.tvMultiplier);
        tvMultiplier.setVisibility(View.INVISIBLE);
        tvThisQuestionPoints = findViewById(R.id.tvAddPoints);
        tvTimer = findViewById(R.id.tvTimer);
        tvMissing = findViewById(R.id.tvMissing);
        tvPoints = findViewById(R.id.tvPoints);

        buttonAnswer1 = findViewById(R.id.btnAnswer1);
        buttonAnswer2 = findViewById(R.id.btnAnswer2);
        buttonAnswer3 = findViewById(R.id.btnAnswer3);
        buttonAnswer4 = findViewById(R.id.btnAnswer4);

        buttonJoker50_50 = findViewById(R.id.btnJoker50_50);
        buttonMenu = findViewById(R.id.btnMenu);

        levelTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Timer: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Play.this, Score.class);
                intent.putExtra("FINAL_SCORE", currentScore);
                intent.putExtra("MAX_STREAK", maxStreak);
                intent.putExtra("MAX_LEVEL", currentDifficulty);
                startActivity(intent);
            }
        }.start();

        questionPointsTimer = new CountDownTimer(5000, 1) {

            int millisInFuture = 5000;
            long basePointsPerQuestion = timedPointsPerQuestion[0];

            public void onTick(long millisUntilFinished) {
                timedPointsPerQuestion[0] = (basePointsPerQuestion / 2) + ( ( ( basePointsPerQuestion / 2 ) / 100 ) * ( millisUntilFinished /  (millisInFuture / 100) ) );
                tvPoints.setText(String.valueOf(pointsForThisQuestion()));
                //Log.d(TAG, "onTick: basePointsPerQuestion = " + basePointsPerQuestion);
                //Log.d(TAG, "onTick: millisUntilFinished = " + millisUntilFinished);
            }
            public void onFinish() {
                timedPointsPerQuestion[0] = 500;
                tvPoints.setText(String.valueOf(pointsForThisQuestion()));
            }
        }.start();

    }

    private void setProgressBar() {
        if(currentScore < currentPointsGoal){
            long pointsSoFar = currentScore - startPoints;
            float neededPointsForLevel = ( currentPointsGoal - startPoints );
            int progressInPercent = (int) Math.round( pointsSoFar / ( neededPointsForLevel / 100 ) );
            progressBar.setProgress( progressInPercent );
            tvMissing.setText(String.valueOf( (long) (neededPointsForLevel - pointsSoFar) ) );
        }else{
            if(currentDifficulty < 5){
                levelTimer.cancel();
                levelTimer.start();
                currentDifficulty++;
                loadFilteredQuestions();

                startPoints = currentScore;
                currentPointsGoal = currentScore + basePointsPerLevel[currentDifficulty - 1];
            }else{
                startPoints = currentScore;
                currentPointsGoal = currentScore + 300000;
                levelTimer.cancel();
                levelTimer.start();
            }
            tvMissing.setText( String.valueOf( (long) ( currentPointsGoal - startPoints ) ) );
            progressBar.setProgress(0);
        }
    }

    private void givePointsAndRaiseStreak() {

        long pointsForThisQuestion = pointsForThisQuestion();
        currentScore += pointsForThisQuestion;
        showPointsForThisQuestion(pointsForThisQuestion);
        tvScore.setText("Score: " +  String.valueOf(currentScore));

        increaseStreakAndMultiplier();
        tvStreak.setText(String.valueOf(currentStreak));
        tvMultiplier.setText("x" + String.valueOf(currentStreakMultiplier));

        Log.d(TAG, "givePointsAndRaiseStreak: points given and Streak raised");
    }

    private void showPointsForThisQuestion(long pointsForThisQuestion) {
        tvThisQuestionPoints.setText(String.valueOf(pointsForThisQuestion));
        tvThisQuestionPoints.setVisibility(View.VISIBLE);

        tvThisQuestionPoints.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvThisQuestionPoints.setVisibility(View.INVISIBLE);
            }
        }, 500);
    }

    private long pointsForThisQuestion() {
        int difficultyMultiplier = currentQuestion.getAsInteger(QuestionEntry.COLUMN_DIFFICULTY);
        long points = Math.round(difficultyMultiplier * timedPointsPerQuestion[0] * currentStreakMultiplier);

        return points;
    }

    private void increaseStreakAndMultiplier() {
        currentStreak++;

        if(maxStreak < currentStreak){
            maxStreak = currentStreak;
        }

        pastStreak = currentStreak;

        if(currentStreakMultiplier < 5) {
            currentStreakMultiplier += 0.5;
            pastStreakMultiplier = currentStreakMultiplier;
        }

        if(currentStreakMultiplier > 1.0){
            tvMultiplier.setVisibility(View.VISIBLE);
            tvMultiplier.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake2));
        }
    }

    private void decreaseStreakAndMultiplier(){
        if(currentStreak > maxStreak){
            maxStreak = currentStreak;
        }
        currentStreak = 0;
        tvStreak.setText(String.valueOf(currentStreak));
        currentStreakMultiplier = 1;
        tvMultiplier.setText("x" + String.valueOf(currentStreakMultiplier));

        if(currentStreakMultiplier == 1.0){
            tvMultiplier.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isRightAnswer(int buttonNumber) {
        int  correctAnswerNumber = currentQuestion.getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER);

        return buttonNumber == correctAnswerNumber;
    }

    private void enableAllAnswerButtons(boolean enabled) {
        buttonAnswer1.setEnabled(enabled);
        buttonAnswer2.setEnabled(enabled);
        buttonAnswer3.setEnabled(enabled);
        buttonAnswer4.setEnabled(enabled);
    }

    private void loadFilteredQuestions(){
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        currentQuestions = appDatabase.getFilteredQuestions(currentDifficulty, currentCategory);
    }

    private void setRandomQuestion(){
        int randomPosition = (int) ( Math.random() * currentQuestions.getCount());

        currentQuestions.moveToPosition(randomPosition);

        Log.d("Play", "loadRandomQuestionAndSetTextViews: randomPosition: " + randomPosition + " \n" + currentQuestions.getPosition());

        ContentValues question = new ContentValues();

        DatabaseUtils.cursorRowToContentValues(currentQuestions, question);

        currentQuestion = question;

        // enable all Answer-Buttons, if 2 Buttons are disabled because of the 50/50 Joker
        enableAllAnswerButtons(true);
    }

    private void fillQuestionTextFields(){

        TextView questionTextView = findViewById(R.id.tvQuestion);
        Button answerText1Button = findViewById(R.id.btnAnswer1);
        Button answerText2Button = findViewById(R.id.btnAnswer2);
        Button answerText3Button = findViewById(R.id.btnAnswer3);
        Button answerText4Button = findViewById(R.id.btnAnswer4);

        //Question.get + set

        String questionsText = currentQuestion.getAsString(QuestionEntry.COLUMN_QUESTIONTEXT);
        questionTextView.setText(questionsText);

        //allAnswers.get + set
        String answerText1 = currentQuestion.getAsString(QuestionEntry.COLUMN_ANSWERTEXT1);
        answerText1Button.setText(answerText1);

        String answerText2 = currentQuestion.getAsString(QuestionEntry.COLUMN_ANSWERTEXT2);
        answerText2Button.setText(answerText2);

        String answerText3 = currentQuestion.getAsString(QuestionEntry.COLUMN_ANSWERTEXT3);
        answerText3Button.setText(answerText3);

        String answerText4 = currentQuestion.getAsString(QuestionEntry.COLUMN_ANSWERTEXT4);
        answerText4Button.setText(answerText4);
    }

    private void joker50_50() {
        buttonJoker50_50.setEnabled(false);

        int correctAnswer = currentQuestion.getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER);

        Button[] answerButtons = {buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4};
        ArrayList<Button> answers = new ArrayList<>(Arrays.asList(answerButtons));

        // REMOVES THE CORRECT ANSWER AND SHOWS IT IN THE LOG
        Log.d(TAG, "joker50_50: correct Answer: " + correctAnswer +
                " / removed number: " + answers.remove(correctAnswer - 1));

        // Removes another wrong answer at an random index between 0 and 2 (max index of the ArrayList)
        // leaves 2 wrong answers in the ArrayList
        Log.d(TAG, "joker50_50: Array answers: Before removal" + answers.toString());
        Log.d(TAG, "joker50_50: 2.Removed Answer: " + answers.remove( (int) Math.floor( Math.random() * 2 ) ) );
        Log.d(TAG, "joker50_50: Array answers: After removal " + answers.toString());

        for(Button answer: answers){
            answer.setEnabled(false);
            Log.d(TAG, "joker50_50: button" + answer + "was disabled");
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonJoker50_50.setEnabled(true);
            }
        }, 10000);
    }

    private void setAllListenersAnswerButtons(){

        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonNumber = 1;
                enableAllAnswerButtons(false);
                questionPointsTimer.cancel();

                if(isRightAnswer(buttonNumber)){
                    givePointsAndRaiseStreak();
                    setProgressBar();
                }else{
                    decreaseStreakAndMultiplier();
                }

                setRandomQuestion();
                fillQuestionTextFields();
                timedPointsPerQuestion[0] = 1000;
                questionPointsTimer.start();

                enableAllAnswerButtons(true);
            }
        });

        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonNumber = 2;
                enableAllAnswerButtons(false);
                questionPointsTimer.cancel();

                if(isRightAnswer(buttonNumber)){
                    givePointsAndRaiseStreak();
                    pastStreak = currentStreak;
                    setProgressBar();
                }else{
                    decreaseStreakAndMultiplier();
                }

                setRandomQuestion();
                fillQuestionTextFields();
                timedPointsPerQuestion[0] = 1000;
                questionPointsTimer.start();

                enableAllAnswerButtons(true);
            }
        });

        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonNumber = 3;
                enableAllAnswerButtons(false);
                questionPointsTimer.cancel();

                if(isRightAnswer(buttonNumber)){
                    givePointsAndRaiseStreak();
                    pastStreak = currentStreak;
                    setProgressBar();
                }else{
                    decreaseStreakAndMultiplier();
                }

                setRandomQuestion();
                fillQuestionTextFields();
                timedPointsPerQuestion[0] = 1000;
                questionPointsTimer.start();

                enableAllAnswerButtons(true);
            }
        });

        buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonNumber = 4;
                enableAllAnswerButtons(false);
                questionPointsTimer.cancel();

                if(isRightAnswer(buttonNumber)){
                    givePointsAndRaiseStreak();
                    pastStreak = currentStreak;
                    setProgressBar();
                }else{
                    decreaseStreakAndMultiplier();
                }

                setRandomQuestion();
                fillQuestionTextFields();
                timedPointsPerQuestion[0] = 1000;
                questionPointsTimer.start();

                enableAllAnswerButtons(true);
            }
        });
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