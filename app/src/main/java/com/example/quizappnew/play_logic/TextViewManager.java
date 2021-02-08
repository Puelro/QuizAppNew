package com.example.quizappnew.play_logic;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.example.quizappnew.database.QuestionContract.QuestionEntry;

import com.example.quizappnew.R;

import java.util.ArrayList;

/**
 * Manages the interaction of the logic with the UI-Elements of Type TextView
 * @author Robin Püllen
 */
public class TextViewManager {
    private static final String TAG = "TextViewManager";

    /**
     * Timer TextView
     */
    TextView tvTimer;
    /**
     * Missing points TextView
     */
    TextView tvMissingPoints;
    /**
     * Points of the current question TextView
     * Points are counted down
     */
    TextView tvPoints;

    /**
     * Streak TextView
     */
    TextView tvStreak;
    /**
     * Score TextView
     */
    TextView tvScore;

    /**
     * Animated question points TextView
     */
    TextView tvThisQuestionPoints;
    /**
     * Multiplier TextView
     */
    TextView tvMultiplier;

    /**
     * The currently played Play activity
     */
    Activity playActivity;
    /**
     * The instance of the QuestionManager
     */
    QuestionManager questionManager;
    /**
     * The instance of the AnswerButtonManager
     */
    AnswerbuttonManager answerbuttonManager;

    /**
     *  Constructor of the TextViewManager
     * @param _activity The currently played Play activity
     * @param _qMng The instance of the QuestionManager
     * @param _abtnMng The instance of the AnswerButtonManager
     */
    public TextViewManager(Activity _activity, QuestionManager _qMng, AnswerbuttonManager _abtnMng) {
        playActivity = _activity;
        questionManager = _qMng;
        answerbuttonManager = _abtnMng;

        tvThisQuestionPoints = _activity.findViewById(R.id.tvAddPoints);
        tvThisQuestionPoints.setVisibility(View.INVISIBLE);

        tvMultiplier = _activity.findViewById(R.id.tvMultiplier);
        tvMultiplier.setVisibility(View.INVISIBLE);

        tvStreak = _activity.findViewById(R.id.tvStreak);
        tvScore = _activity.findViewById(R.id.tvScore);

        tvTimer = _activity.findViewById(R.id.tvTimer);
        tvMissingPoints = _activity.findViewById(R.id.tvMissingPoints);
        tvPoints = _activity.findViewById(R.id.tvPoints);

    }

    /**
     * Shows the points for the currently answered question
     * @param pointsForThisQuestion the points which will be displayed
     */
    public void showPointsForCurrentQuestion(long pointsForThisQuestion) {
        // update UI
        tvThisQuestionPoints.setText(String.valueOf(pointsForThisQuestion));
        // set the TExtView visible
        tvThisQuestionPoints.setVisibility(View.VISIBLE);

        // start the animation
        tvThisQuestionPoints.startAnimation(AnimationUtils.loadAnimation(playActivity, R.anim.shake));

        // show the TextView for 0.5 seconds
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvThisQuestionPoints.setVisibility(View.INVISIBLE);
            }
        }, 500);
    }

    /**
     * Fills the question text field with the question String and the answer text fields in an random order
     */
    public void fillQuestionTextFieldsRandom() {
        // find the question TextView
        TextView questionTextView = playActivity.findViewById(R.id.tvQuestion);
        // instantiate a new ArrayList
        ArrayList<Answerbutton> answerButtons = new ArrayList<>();
        // Fill the ArrayList with the AnswerButton
        for(Answerbutton answerbutton : answerbuttonManager.getAnswerButtons()){
            answerButtons.add(answerbutton);
        }

        //Question.get + set
        String questionsText = questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_QUESTIONTEXT);
        questionTextView.setText(questionsText);

        // Store the Answers of the current Question in a new ArrayList
        ArrayList<String> answertexts = new ArrayList<>();
        answertexts.add(0, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT1));
        answertexts.add(1, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT2));
        answertexts.add(2, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT3));
        answertexts.add(3, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT4));


        int randomAnswerButtonIndex;
        // store the index of the correct Answer
        int correctAnswerIndex = questionManager.getCurrentQuestion().getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER) - 1;

        // iterate through all answertexts
        for(int i = answertexts.size()-1; i >= 0; i--){
            // create a random index between 0 (inclusive) and the current size of answerButtons (exclusive)
            // the size of answerButtons changes, since filled answerButton get removed
            randomAnswerButtonIndex = (int) ( Math.random() * answerButtons.size());
            // get the AnswerButton at the random index from answerButtons
            Answerbutton randomAnswerButton = answerButtons.get(randomAnswerButtonIndex);
            // fill the TextView of the randomAnswerButton with the answertext at the ndex i
            randomAnswerButton.getUIButton().setText(answertexts.get(i));

            // if the correct answertext is the correct one
            if(i == correctAnswerIndex){
                // change the index of the correct answer to the index of the new answerButton
                questionManager.getCurrentQuestion().put(QuestionEntry.COLUMN_CORRECT_ANSWER, randomAnswerButton.getButtonNumber());
            }

            // remove the answerButton at randomIndex, which was filled in this loop circle
            answerButtons.remove(randomAnswerButtonIndex);
        }
    }

    /**
     * Sets the text for the UI element tvMissingPoints
     * @param _textViewContent The text to display
     */
    public void setTvMissingPoints(String _textViewContent) {
        tvMissingPoints.setText(_textViewContent);
    }

    /**
     * Sets the text for the UI element tvTimer
     * @param _textViewContent The text to display
     */
    public void setTvTimer(String _textViewContent) {
        tvTimer.setText(_textViewContent);
    }

    /**
     * Sets the text for the UI element tvStore
     * @param _textViewContent The text to display
     */
    public void setTvScore(String _textViewContent) {
        tvScore.setText(_textViewContent);
    }

    /**
     * Sets the text for the UI element tvPoints
     * @param _textViewContent The text to display
     */
    public void setTvPoints(String _textViewContent) {
        tvPoints.setText(_textViewContent);
    }

    /**
     * Sets the text for the UI element tvStreak
     * @param _textViewContent The text to display
     */
    public void setTvStreak(String _textViewContent) {
        tvStreak.setText(_textViewContent);
    }

    /**
     * Sets the text for the UI element tvMultiplier
     * @param _textViewContent The text to display
     */
    public void setTvMultiplier(String _textViewContent) {
        tvMultiplier.setText(_textViewContent);
    }

    /**
     * Sets the visibility of the Multiplier UI element
     * @param _visible If true the UI element is visible, by false the UI element is invisible
     */
    public void setTvMultiplierIsVisible(boolean _visible) {
        if (_visible) {
            tvMultiplier.setVisibility(View.VISIBLE);
        } else {
            tvMultiplier.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Starts the Multiplier animation
     */
    public void startMultiplierAnimation(){
        tvMultiplier.startAnimation(AnimationUtils.loadAnimation(playActivity, R.anim.shake2));
    }

}

