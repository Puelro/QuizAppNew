package com.example.quizappnew.play_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.quizappnew.activities.Play;
import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.QuestionContract.QuestionEntry;

import java.util.ArrayList;

public class QuestionManager {
    private static final String TAG = "QuestionManager";

    ArrayList<ContentValues> currentQuestions;
    ContentValues currentQuestion;

    int currentDifficulty;
    String currentCategory;

    Play playActivity;
    StreakManager streakManager;


    public QuestionManager(int _difficulty, String _category, Play _playActivity, StreakManager _sMng){
        currentQuestions = new ArrayList<ContentValues>();
        currentDifficulty = _difficulty;
        currentCategory = _category;


        playActivity = _playActivity;
        streakManager =  _sMng;
    }

    public void loadFilteredQuestions() {
        AppDatabase appDatabase = AppDatabase.getInstance(playActivity);
        Cursor cursor = appDatabase.getFilteredQuestions(currentDifficulty, currentCategory);

        currentQuestions = new ArrayList<ContentValues>();

        //Log.d(TAG, "loadFilteredQuestions: " + DatabaseUtils.dumpCursorToString(cursor));

        for(int i = 0; i < cursor.getCount(); i++){
            ContentValues cv = new ContentValues();
            cursor.moveToPosition(i);
            Log.d(TAG, "loadFilteredQuestions: currentPosition: " + cursor.getPosition());
            DatabaseUtils.cursorRowToContentValues(cursor, cv);
            currentQuestions.add(i, cv);
            Log.e(TAG, "loadFilteredQuestions: ArrayList: " + currentQuestions);
        }
    }

    public void removeQuestionFromList(int index) {

        if(currentQuestions.size() > 1){
            if(index != -1) {
                Log.d(TAG, "removeQuestionFromCursor: removed this Question: " + currentQuestions.get(index));
                currentQuestions.remove(index);
                Log.d(TAG, "removeQuestionFromCursor: ArrayList: " + currentQuestions);
            }
        }
    }

    public boolean isRightAnswer(int buttonNumber) {
        int  correctAnswerNumber = currentQuestion.getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER);

        return buttonNumber == correctAnswerNumber;
    }

    public void setRandomQuestion(AnswerbuttonManager _answerbuttonManager){
        int randomIndex = (int) ( Math.random() * currentQuestions.size());

        currentQuestion = currentQuestions.get(randomIndex);

        Log.d(TAG, "setRandomQuestion: ArayListContents: " + currentQuestions.toString());
        Log.d("Play", "setRandomQuestion: randomIndex: " + randomIndex + " \n" + "Question: " + currentQuestions.get(randomIndex));

        // enable all Answer-Buttons, if 2 Buttons are disabled because of the 50/50 Joker
        _answerbuttonManager.enableAllAnswerButtons(true);
    }

    public int getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void raiseCurrentDifficultyByOne() {
        currentDifficulty++;
    }

    public ContentValues getCurrentQuestion() {
        return currentQuestion;
    }

    public ArrayList<ContentValues> getCurrentQuestions() {
        return currentQuestions;
    }
}
