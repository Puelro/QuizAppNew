package com.example.quizappnew.play_helper;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.example.quizappnew.R;
import com.example.quizappnew.activities.Play;

public class ProgressbarManager {
    private static final String TAG = "QuestionManager";

    ProgressBar progressBar;
    final long[] basePointsPerLevel = new long[]{30000, 60000, 90000, 120000, 150000, 180000, 210000};
    long startPoints;
    long currentPointsGoal;

    QuestionManager questionManager;
    TextViewManager textViewManager;

    public ProgressbarManager(Activity _activity, long _currentScore, QuestionManager _questionManager, TextViewManager _textViewManager){
        progressBar = _activity.findViewById(R.id.vertical_progressbar);
        progressBar.setMax(100);
        startPoints = _currentScore;
        currentPointsGoal = basePointsPerLevel[0];

        questionManager = _questionManager;
        textViewManager = _textViewManager;
    }


    public void setProgressbar(long _currentScore, CountDownTimer _levelTimer) {

        if (_currentScore < currentPointsGoal) {
            long pointsSoFar = _currentScore - startPoints;
            float neededPointsForLevel = (currentPointsGoal - startPoints);
            int progressInPercent = (int) Math.round(pointsSoFar / (neededPointsForLevel / 100));
            progressBar.setProgress(progressInPercent);
            textViewManager.setTvMissing(String.valueOf((long) (neededPointsForLevel - pointsSoFar)));
        } else {
            if (questionManager.getCurrentDifficulty() < 5) {
                _levelTimer.cancel();
                _levelTimer.start();
                questionManager.raiseCurrentDifficultyByOne();
                questionManager.loadFilteredQuestions();

                startPoints = _currentScore;
                currentPointsGoal = _currentScore + basePointsPerLevel[questionManager.getCurrentDifficulty() - 1];
            } else {
                startPoints = _currentScore;
                currentPointsGoal = _currentScore + 300000;
                _levelTimer.cancel();
                _levelTimer.start();
            }
            textViewManager.setTvMissing(String.valueOf((long) (currentPointsGoal - startPoints)));
            progressBar.setProgress(0);
        }
    }
}
