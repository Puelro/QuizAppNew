package com.example.quizappnew.play_helper;

import android.util.Log;

public class StreakAndPointsManager {

    private static final String TAG = "StreakManager";

    int previousStreak;
    int currentStreak;
    int maxStreak;

    double previousStreakMultiplier;
    double currentStreakMultiplier;

    long timedPointsPerQuestion;

    public StreakAndPointsManager(){
        previousStreak = 0;
        currentStreak = 0;
        maxStreak = 0;
        previousStreakMultiplier = 1;
        currentStreakMultiplier = 1;
        timedPointsPerQuestion = 1000;
    }

    public long getPointsAndShowThem(long _currentScore, QuestionManager _qMng, TextViewManager _tvMng){
        long pointsForThisQuestion = getPointsForCurrentQuestion(_qMng);

        _currentScore += pointsForThisQuestion;
        _tvMng.showPointsForCurrentQuestion(pointsForThisQuestion);
        _tvMng.setTvScore("Score: " +  String.valueOf(_currentScore));

        return _currentScore;
    }

    public void increaseStreakAndMultiplier(long _currentScore, TextViewManager _tvMng) {
        currentStreak++;

        if(maxStreak < currentStreak){
            maxStreak = currentStreak;
        }

        previousStreak = currentStreak;

        if(currentStreakMultiplier < 5) {
            currentStreakMultiplier += 0.5;
            previousStreakMultiplier = currentStreakMultiplier;
        }

        if(currentStreakMultiplier > 1.0){
            _tvMng.setTvMultiplierIsVisible(true);
            _tvMng.startMultiplierAnimation();
        }

        _tvMng.setTvStreak(String.valueOf(currentStreak));
        _tvMng.setTvMultiplier(String.valueOf(currentStreakMultiplier + "x"));

        Log.d(TAG, "givePointsAndRaiseStreak: points given and Streak raised");
    }

    public void decreaseStreakAndMultiplier(TextViewManager _tvMng){
        if(currentStreak > maxStreak){
            maxStreak = currentStreak;
        }
        currentStreak = 0;
        _tvMng.setTvStreak(String.valueOf(currentStreak));
        currentStreakMultiplier = 1;
        _tvMng.setTvMultiplier(String.valueOf(currentStreakMultiplier));
        _tvMng.setTvMultiplierIsVisible(false);
    }

    public long getPointsForCurrentQuestion(QuestionManager _qMng){
        int difficultyMultiplier = _qMng.getCurrentDifficulty();
        long points = Math.round(difficultyMultiplier * timedPointsPerQuestion * getCurrentStreakMultiplier());

        return points;
    }

    public double getCurrentStreakMultiplier() {
        return currentStreakMultiplier;
    }

    public int getMaxStreak(){
        return maxStreak;
    }

    public long getTimedPointsPerQuestion() {
        return timedPointsPerQuestion;
    }

    public void setTimedPointsPerQuestion(long _timedPointsPerQuestion) {
        timedPointsPerQuestion = _timedPointsPerQuestion;
    }

    public void previousStreakToCurrentStreak(TextViewManager _tvMng) {
        currentStreak = previousStreak;
        _tvMng.setTvStreak(String.valueOf(currentStreak));
    }

    public void previousMultiplierToCurrentMultiplier(TextViewManager _tvMng) {
        currentStreakMultiplier = previousStreakMultiplier;
        _tvMng.setTvMultiplier(String.valueOf(currentStreakMultiplier + "x"));
        if(currentStreakMultiplier > 1){
            _tvMng.setTvMultiplierIsVisible(true);
        }
    }
}
