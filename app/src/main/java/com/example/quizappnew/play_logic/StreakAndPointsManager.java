package com.example.quizappnew.play_logic;

import android.util.Log;

public class StreakAndPointsManager {

    private static final String TAG = "StreakManager";

    /**
     * The previous Streak
     */
    int previousStreak;

    /**
     * The current Streak
     */
    int currentStreak;

    /**
     * The maximal Streak in the current Playthrough
     */
    int maxStreak;

    /**
     * The previous points multiplier
     */
    double previousPointsMultiplier;

    /**
     * The current points multiplier
     */
    double currentPointsMultiplier;

    /**
     * The base points for a question
     */
    long basePointsPerQuestion;

    /**
     * The constructor of the StreakAndPointsManager
     */
    public StreakAndPointsManager(){
        previousStreak = 0;
        currentStreak = 0;
        maxStreak = 0;
        previousPointsMultiplier = 1;
        currentPointsMultiplier = 1;
        basePointsPerQuestion = 1000;
    }

    /**
     * Returns the number of points the current question was worth and displays it in the UI
     * @param _currentScore The current score
     * @param _qMng The instance of the QuestionManager
     * @param _tvMng The instance of the TextViewManager
     * @return The number of points the current question was worth, when it was answered
     */
    public long getCurrentPointsAndShowThem(long _currentScore, QuestionManager _qMng, TextViewManager _tvMng){
        // store the points for this question in a variable
        long pointsForThisQuestion = getPointsForCurrentQuestion(_qMng);

        // calculate currentPoints
        _currentScore += pointsForThisQuestion;
        //display the changed score
        _tvMng.showPointsForCurrentQuestion(pointsForThisQuestion);
        _tvMng.setTvScore("Score: " +  String.valueOf(_currentScore));

        // return the updated current score
        return _currentScore;
    }

    /**
     * Increases the Streak and the Multiplier
     * @param _tvMng The instance of the TextViewManager
     */
    public void increaseStreakAndMultiplier(TextViewManager _tvMng) {
        // raise the current streak by one
        currentStreak++;

        // update maxStreak
        if(maxStreak < currentStreak){
            maxStreak = currentStreak;
        }

        // update previous streak
        previousStreak = currentStreak;

        // if the currentPointsMultiplier is smaller than 5, raise it by 0.5
        if(currentPointsMultiplier < 5) {
            currentPointsMultiplier += 0.5;
            // update previousPointsMultiplier
            previousPointsMultiplier = currentPointsMultiplier;
        }

        // if the currentPointsMultiplier is bigger than 1 show it in the UI
        if(currentPointsMultiplier > 1.0){
            _tvMng.setTvMultiplierIsVisible(true);
            _tvMng.startMultiplierAnimation();
        }

        // fill the UI Elements
        _tvMng.setTvStreak(String.valueOf(currentStreak));
        _tvMng.setTvMultiplier(String.valueOf(currentPointsMultiplier + "x"));

        Log.d(TAG, "givePointsAndRaiseStreak: points given and Streak raised");
    }

    /**
     * Decreases the streak and multiplier
     * @param _tvMng
     */
    public void decreaseStreakAndMultiplier(TextViewManager _tvMng){
        // update maxStreak
        if(currentStreak > maxStreak){
            maxStreak = currentStreak;
        }
        // set currentStreak to 0
        currentStreak = 0;
        // update UI
        _tvMng.setTvStreak(String.valueOf(currentStreak));
        // set currentPointsMultiplier to 1
        currentPointsMultiplier = 1;
        // update UI
        _tvMng.setTvMultiplier(String.valueOf(currentPointsMultiplier));
        _tvMng.setTvMultiplierIsVisible(false);
    }

    /**
     * Get the points for the current question
     * @param _qMng The instance of the questionManager
     * @return The number of points the current question was worth
     */
    public long getPointsForCurrentQuestion(QuestionManager _qMng){
        int difficultyMultiplier = _qMng.getCurrentDifficulty();
        long points = Math.round(difficultyMultiplier * basePointsPerQuestion * currentPointsMultiplier);

        return points;
    }

    /**
     * Get the maximum streak of correct answers for this round
     * @return The highest streak of correct answers of this round
     */
    public int getMaxStreak(){
        return maxStreak;
    }

    /**
     * Get the base points for a question
     * @return the base points for a question
     */
    public long getBasePointsPerQuestion() {
        return basePointsPerQuestion;
    }

    /**
     * Sets the points per Question
     * @param _timedPointsPerQuestion The points the current questions is worth
     */
    public void setPointsPerQuestion(long _timedPointsPerQuestion) {
        basePointsPerQuestion = _timedPointsPerQuestion;
    }

    /**
     * Resets the current streak to its previous value
     * @param _tvMng The instance of the TextViewManager
     */
    public void previousStreakToCurrentStreak(TextViewManager _tvMng) {
        currentStreak = previousStreak;
        _tvMng.setTvStreak(String.valueOf(currentStreak));
    }

    /**
     * Resets the current multiplier to its previous value
     * @param _tvMng The instance of the TextViewManager
     */
    public void previousMultiplierToCurrentMultiplier(TextViewManager _tvMng) {
        currentPointsMultiplier = previousPointsMultiplier;
        // update UI
        _tvMng.setTvMultiplier(String.valueOf(currentPointsMultiplier + "x"));
        if(currentPointsMultiplier > 1){
            _tvMng.setTvMultiplierIsVisible(true);
        }
    }
}
