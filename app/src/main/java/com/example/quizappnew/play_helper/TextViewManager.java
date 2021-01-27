package com.example.quizappnew.play_helper;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.example.quizappnew.database.QuestionContract.QuestionEntry;

import com.example.quizappnew.R;

import java.util.ArrayList;

public class TextViewManager {
    private static final String TAG = "TextViewManager";

    TextView tvTimer;
    TextView tvMissing;
    TextView tvPoints;

    TextView tvStreak;
    TextView tvScore;

    TextView tvThisQuestionPoints;
    TextView tvMultiplier;

    Activity playActivity;
    QuestionManager questionManager;
    AnswerbuttonManager answerbuttonManager;

    public TextViewManager(Activity _activity, QuestionManager _qMng, AnswerbuttonManager _abtnMng) {
        playActivity = _activity;
        questionManager = _qMng;
        answerbuttonManager = _abtnMng;

        // Quelle für Activity Übergabe-Lösungsansatz:
        // https://stackoverflow.com/questions/8323777/using-findviewbyid-in-a-class-that-does-not-extend-activity-in-android

        tvThisQuestionPoints = _activity.findViewById(R.id.tvAddPoints);
        tvThisQuestionPoints.setVisibility(View.INVISIBLE);

        tvMultiplier = _activity.findViewById(R.id.tvMultiplier);
        tvMultiplier.setVisibility(View.INVISIBLE);

        tvStreak = _activity.findViewById(R.id.tvStreak);
        tvScore = _activity.findViewById(R.id.tvScore);

        tvTimer = _activity.findViewById(R.id.tvTimer);
        tvMissing = _activity.findViewById(R.id.tvMissing);
        tvPoints = _activity.findViewById(R.id.tvPoints);

    }

    public void showPointsForCurrentQuestion(long pointsForThisQuestion) {
        tvThisQuestionPoints.setText(String.valueOf(pointsForThisQuestion));
        tvThisQuestionPoints.setVisibility(View.VISIBLE);

        tvThisQuestionPoints.startAnimation(AnimationUtils.loadAnimation(playActivity, R.anim.shake));

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvThisQuestionPoints.setVisibility(View.INVISIBLE);
            }
        }, 500);
    }

    public void fillQuestionTextFieldsRandom() {
        TextView questionTextView = playActivity.findViewById(R.id.tvQuestion);
        ArrayList<Answerbutton> answerButtons = new ArrayList<>();
        for(Answerbutton answerbutton : answerbuttonManager.getAnswerButtons()){
            answerButtons.add(answerbutton);
        }

        //Question.get + set
        String questionsText = questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_QUESTIONTEXT);
        questionTextView.setText(questionsText);

        ArrayList<String> answertexts = new ArrayList<>();
        answertexts.add(0, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT1));
        answertexts.add(1, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT2));
        answertexts.add(2, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT3));
        answertexts.add(3, questionManager.getCurrentQuestion().getAsString(QuestionEntry.COLUMN_ANSWERTEXT4));

        int randomAnswerButtonIndex;
        int correctAnswerIndex = questionManager.getCurrentQuestion().getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER) - 1;
        //allAnswers.get + set
        for(int i = 3; i >= 0; i--){
            randomAnswerButtonIndex = (int) ( Math.random() * answerButtons.size());
            Answerbutton randomAnswerButton = answerButtons.get(randomAnswerButtonIndex);
            randomAnswerButton.getUIButton().setText(answertexts.get(i));

            if(i == correctAnswerIndex){
                questionManager.getCurrentQuestion().put(QuestionEntry.COLUMN_CORRECT_ANSWER, randomAnswerButton.getButtonNumber());
            }

            answerButtons.remove(randomAnswerButtonIndex);
        }
    }


    public void setTvMissing(String _textViewContent) {
        tvMissing.setText(_textViewContent);
    }

    public void setTvTimer(String _textViewContent) {
        tvTimer.setText(_textViewContent);
    }

    public void setTvScore(String _textViewContent) {
        tvScore.setText(_textViewContent);
    }

    public void setTvPoints(String _textViewContent) {
        tvPoints.setText(_textViewContent);
    }

    public void setTvStreak(String _textViewContent) {
        tvStreak.setText(_textViewContent);
    }

    public void setTvMultiplier(String _textViewContent) {
        tvMultiplier.setText(_textViewContent);
    }

    public void setTvMultiplierIsVisible(boolean _visible) {
        if (_visible) {
            tvMultiplier.setVisibility(View.VISIBLE);
        } else {
            tvMultiplier.setVisibility(View.INVISIBLE);
        }
    }

    public void startMultiplierAnimation(){
        tvMultiplier.startAnimation(AnimationUtils.loadAnimation(playActivity, R.anim.shake2));
    }

}

