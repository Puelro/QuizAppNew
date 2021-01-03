package com.example.quizappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.QuestionContract;

import org.w3c.dom.Text;

public class Play extends AppCompatActivity {

    Button buttonRefreshQuestion;
    Button buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        loadRandomQuestionAndSetTextViews();

        buttonMenu = findViewById(R.id.btnMenu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Play.this,Menu.class);
                startActivity(intent);
            }
        });

        buttonRefreshQuestion = findViewById(R.id.btnRefreshQuestion);
        buttonRefreshQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomQuestionAndSetTextViews();
            }
        });

    }

    private void loadRandomQuestionAndSetTextViews(){

        TextView questionTextTextView = findViewById(R.id.tvQuestion);
        Button answerText1Button = findViewById(R.id.btnA);
        Button answerText2Button = findViewById(R.id.btnB);
        Button answerText3Button = findViewById(R.id.btnC);
        Button answerText4Button = findViewById(R.id.btnD);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        Cursor allQuestions = appDatabase.getQuestionListContents();

        int randomPosition = (int) ( Math.random() * allQuestions.getCount());

        allQuestions.moveToPosition(randomPosition);

        Log.d("Play", "loadRandomQuestionAndSetTextViews: randomPosition: " + randomPosition + " \n" + allQuestions.getPosition());


        int questionTextColumnIndex = allQuestions.getColumnIndex(QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT);
        String questionsText = allQuestions.getString(questionTextColumnIndex);
        questionTextTextView.setText(questionsText);

        int answerText1ColumnIndex = allQuestions.getColumnIndex(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT1);
        String answerText1 = allQuestions.getString(answerText1ColumnIndex);
        answerText1Button.setText(answerText1);

        int answerText2ColumnIndex = allQuestions.getColumnIndex(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT2);
        String answerText2 = allQuestions.getString(answerText2ColumnIndex);
        answerText2Button.setText(answerText2);

        int answerText3ColumnIndex = allQuestions.getColumnIndex(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT3);
        String answerText3 = allQuestions.getString(answerText3ColumnIndex);
        answerText3Button.setText(answerText3);

        int answerText4ColumnIndex = allQuestions.getColumnIndex(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT4);
        String answerText4 = allQuestions.getString(answerText4ColumnIndex);
        answerText4Button.setText(answerText4);

    }
}