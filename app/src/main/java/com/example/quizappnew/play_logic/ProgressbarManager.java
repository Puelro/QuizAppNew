package com.example.quizappnew.play_logic;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.quizappnew.R;

/**
 * Manages the progressbar
 * @author Robin Püllen
 */
public class ProgressbarManager {
    private static final String TAG = "QuestionManager";

    /**
     * The progressbar UI-Object
     */
    ProgressBar progressBar;

    /**
     * An Array which holds the needed points for all levels. The index + 1 equals the difficulty and the current level
     */
    final long[] basePointsPerLevel = new long[]{30000, 60000, 90000, 120000, 150000};

    /**
     *  The amount of points the player has when he started the current level
     */
    long startPoints;

    /**
     * The current goal of points to win the current level
     */
    long currentPointsGoal;

    /**
     * The instance of the {@link QuestionManager}
     */
    QuestionManager questionManager;
    /**
     * The instance of the {@link TextViewManager}
     */
    TextViewManager textViewManager;

    /**
     * The constructor of the ProgressbarManager
     * @param _playActivity The current Play activity
     * @param _currentScore The current Score
     * @param _questionManager The instance of the {@link QuestionManager}
     * @param _textViewManager The instance of the {@link TextViewManager}
     */
    public ProgressbarManager(Activity _playActivity, long _currentScore, QuestionManager _questionManager, TextViewManager _textViewManager){
        progressBar = _playActivity.findViewById(R.id.vertical_progressbar);
        // The progress is calculated in percent so the maximum of the progressbar is 100 percent
        progressBar.setMax(100);
        startPoints = _currentScore;
        currentPointsGoal = basePointsPerLevel[0];

        questionManager = _questionManager;
        textViewManager = _textViewManager;
    }

    /**
     * Updates the Progressbar
     * @param _currentScore The current Score
     * @param _levelTimer The Timer of the currently played {@link com.example.quizappnew.activities.Play} activity
     * @param _jokerStreakButton The UI-Button of the Streak Joker
     */
    public void updateProgressbar(long _currentScore, CountDownTimer _levelTimer, Button _jokerStreakButton) {

        // If the current pointsGoal is not reached
        if (_currentScore < currentPointsGoal) {
            // calculate the already collected points by subtracting points from the start of the level from the total of points
            long pointsSoFar = _currentScore - startPoints;
            // calculate the needed points for the level by subtracting the startPoints from the current points goal
            float neededPointsForLevel = (currentPointsGoal - startPoints);
            // calculate the percent of progress
            int progressInPercent = (int) Math.round(pointsSoFar / (neededPointsForLevel / 100));

            // set the progress
            progressBar.setProgress(progressInPercent);
            // change the UI_Textview of missing points
            textViewManager.setTvMissingPoints(String.valueOf((long) (neededPointsForLevel - pointsSoFar)));
        }
        // if the current pointsGoal is reached
        else {
            // and the current difficulty is under the maximum (in our case 5)
            if (questionManager.getCurrentDifficulty() < 5) {
                // cancel and restart the Leveltimer
                _levelTimer.cancel();
                _levelTimer.start();
                // raise the difficulty by one
                questionManager.raiseCurrentDifficultyByOne();
                // load the new questions filtered by the new difficulty
                questionManager.loadFilteredQuestions();

                // set the start points of the current level to the current score
                startPoints = _currentScore;

                // set the progress
                progressBar.setProgress(0);
                // set the pointsGoal to the sum of the current score and the base Points of the level at the index of the current difficulty -1
                currentPointsGoal = _currentScore + basePointsPerLevel[questionManager.getCurrentDifficulty() - 1];
            }
            // and the current difficulty is equal or higher as the maximum
            else {
                // set the start points of the current level to the current score
                startPoints = _currentScore;
                // raise the pointsgoal by 100.000
                currentPointsGoal = _currentScore + 1000000;

                // cancel and restart the Leveltimer
                _levelTimer.cancel();
                _levelTimer.start();
                // keep the progressbar at 100
                progressBar.setProgress(100);
            }

            // enable the Streak joker button if it is disabled
            _jokerStreakButton.setEnabled(true);
            // change the UI_Textview of missing points
            textViewManager.setTvMissingPoints(String.valueOf((long) (currentPointsGoal - startPoints)));

        }
    }
}
