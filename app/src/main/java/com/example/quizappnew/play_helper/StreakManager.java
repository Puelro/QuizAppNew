package com.example.quizappnew.play_helper;

import android.util.Log;
import android.view.animation.AnimationUtils;

import com.example.quizappnew.R;

public class StreakManager {

    private static final String TAG = "StreakManager";

    int pastStreak;
    int currentStreak;
    int maxStreak;

    double pastStreakMultiplier;
    double currentStreakMultiplier;

    long timedPointsPerQuestion;

    public StreakManager (){
        pastStreak = 0;
        currentStreak = 0;
        maxStreak = 0;
        pastStreakMultiplier = 1;
        currentStreakMultiplier = 1;
        timedPointsPerQuestion = 1000;
    }

    public long givePointsAndRaiseStreak(long _currentScore, QuestionManager _qMng, TextViewManager _tvMng){
        long pointsForThisQuestion = getPointsForCurrentQuestion(_qMng);
        _currentScore += pointsForThisQuestion;
        _tvMng.showPointsForCurrentQuestion(pointsForThisQuestion);
        _tvMng.setTvScore("Score: " +  String.valueOf(_currentScore));

        increaseStreakAndMultiplier(_tvMng);
        _tvMng.setTvStreak(String.valueOf(currentStreak));
        _tvMng.setTvMultiplier("x" + String.valueOf(currentStreakMultiplier));

        Log.d(TAG, "givePointsAndRaiseStreak: points given and Streak raised");

        return _currentScore;
    }

    private void increaseStreakAndMultiplier(TextViewManager _tvMng) {
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
            _tvMng.setTvMultiplierIsVisible(true);
            _tvMng.startMultiplierAnimation();
        }
    }

    public void decreaseStreakAndMultiplier(TextViewManager _tvMng){
        if(currentStreak > maxStreak){
            maxStreak = currentStreak;
        }
        currentStreak = 0;
        _tvMng.setTvStreak(String.valueOf(currentStreak));
        currentStreakMultiplier = 1;
        _tvMng.setTvMultiplier("x" + String.valueOf(currentStreakMultiplier));

        if(currentStreakMultiplier == 1.0){
            _tvMng.setTvMultiplierIsVisible(false);
        }
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
}
