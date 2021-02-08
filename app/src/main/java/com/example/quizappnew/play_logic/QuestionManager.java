package com.example.quizappnew.play_logic;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.quizappnew.activities.Play;
import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.QuestionContract.QuestionEntry;

import java.util.ArrayList;

/**
 * Manages the extraction of the Questions from the database
 * @author Kent Feldner
 */
public class QuestionManager {
    private static final String TAG = "QuestionManager";

    /**
     * The current loaded questions
     */
    ArrayList<ContentValues> currentQuestions;
    /**
     * The current displayed question
     */
    ContentValues currentQuestion;

    /**
     * The current difficulty of the questions
     */
    int currentDifficulty;
    /**
     * The current category of the questions
     */
    String currentCategory;

    /**
     * The currently played play Activity
     */
    Play playActivity;

    /**
     * The instance of the streakAndPointsManager
     */
    StreakAndPointsManager streakAndPointsManager;

    /**
     * Constructor of the QuestionManager
     * @param _currentDifficulty The current difficulty of the questions, as choosen by the player (SELECTION WAS NOT IMPLEMENTED YET)
     * @param _category The category of the questions
     * @param _playActivity The currently Played activity
     * @param _sMng The instance of the streakAndPointsManager
     */
    public QuestionManager(int _currentDifficulty, String _category, Play _playActivity, StreakAndPointsManager _sMng){
        currentQuestions = new ArrayList<ContentValues>();
        currentDifficulty = _currentDifficulty;
        currentCategory = _category;


        playActivity = _playActivity;
        streakAndPointsManager =  _sMng;
    }

    /**
     * Loads the filtered questions from the database and fills them into the ArrayList currentQuestions.
     * The questions are filtered by difficulty and category
     */
    public void loadFilteredQuestions() {
        // get the instance of AppDatabase
        AppDatabase appDatabase = AppDatabase.getInstance(playActivity);
        // call getFilteredQuestions and store the cursor in a variable
        Cursor cursor = appDatabase.getFilteredQuestions(currentDifficulty, currentCategory);

        currentQuestions = new ArrayList<ContentValues>();

        // As long as there are questions in the cursor
        for(int i = 0; i < cursor.getCount(); i++){
            // create a new ContenValues variable cv
            ContentValues cv = new ContentValues();
            // move the cursor to the current index
            cursor.moveToPosition(i);
            // store the data at the current position of the cursor into cv
            DatabaseUtils.cursorRowToContentValues(cursor, cv);
            // add the contentValues object at index i in currentQuestions
            currentQuestions.add(i, cv);
        }
    }

    /**
     * Removes the currentQuestion from the currentQuestions
     * @param index The index of the current question in currentQuestions
     */
    public void removeQuestionFromList(int index) {

        // if currentQuestions has more than one question and the index is not -1
        if(currentQuestions.size() > 1 && index != -1){
                // remove the question at the passed index
                currentQuestions.remove(index);
        }
    }

    /**
     * Tests if the buttonNumber is the Number of the correct Answer
     * @param buttonNumber the number of the answer which is to test
     * @return Returns true if the buttonNumber is equl to the number of the correct answer else it reurns false
     */
    public boolean isRightAnswer(int buttonNumber) {
        int  correctAnswerNumber = currentQuestion.getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER);

        return buttonNumber == correctAnswerNumber;
    }

    /**
     * Sets a random Question as the current Question
     * @param _answerbuttonManager
     */
    public void setRandomQuestion(AnswerbuttonManager _answerbuttonManager){
        // create a random index between 0 and the size of currentQuestions
        int randomIndex = (int) ( Math.random() * currentQuestions.size());

        // get a contentValues object from currentQuestions at randomIndex and store it in currentQuestion
        currentQuestion = currentQuestions.get(randomIndex);

        // enable all Answer-Buttons, if 2 Buttons are disabled because of the 50/50 Joker
        _answerbuttonManager.enableAllAnswerButtons(true);
    }

    /**
     * Disables 2 random wrong answer button
     * @param _abtnMng The instance of the AnswerbuttonManager
     */
    public void joker50_50(AnswerbuttonManager _abtnMng){
        // get the number of the current correct answer
        int correctAnswer = getCurrentQuestion().getAsInteger(QuestionEntry.COLUMN_CORRECT_ANSWER);

        // create a local ArrayList of the AnswerButtons
        ArrayList<Answerbutton> answerButtons = new ArrayList<>();

        // and store them into List
        for(Answerbutton answer : _abtnMng.getAnswerButtons()){
            answerButtons.add(answer);
        }

        // remove the correct answer from the list, subtract 1 because of the index
        answerButtons.remove(correctAnswer - 1);

        // Remove a wrong answer at an random index between 0 and 2 (max index of the ArrayList at the moment)
        // leaves 2 wrong answers in the ArrayList
        answerButtons.remove( (int) Math.floor( Math.random() * 2 ) ) ;

        // disable the 2 remaining answer buttons
        for(Answerbutton answer: answerButtons){
            answer.getUIButton().setEnabled(false);
        }
    }

    /**
     * Returns the current difficulty
     * @return The current difficulty
     */
    public int getCurrentDifficulty() {
        return currentDifficulty;
    }

    /**
     * Raises the current difficulty by one
     */
    public void raiseCurrentDifficultyByOne() {
        currentDifficulty++;
    }

    /**
     * Returns the current question
     * @return the current Question
     */
    public ContentValues getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Returns the current Questins
     * @return The current Questions
     */
    public ArrayList<ContentValues> getCurrentQuestions() {
        return currentQuestions;
    }
}
