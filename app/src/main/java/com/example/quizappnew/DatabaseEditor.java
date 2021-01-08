package com.example.quizappnew;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.QuestionAdapter;
import com.example.quizappnew.database.QuestionContract.QuestionEntry;

public class DatabaseEditor extends AppCompatActivity {
    private SQLiteDatabase db;
    private AppDatabase appDatabase;
    private QuestionAdapter adapter;

    Button buttonAddQuestion;
    Button buttonDropQuestion;
    Button buttonDelete;
    Button buttonMenu;

    private TextView id;

    private EditText category;
    private EditText difficulty;
    private EditText questionText;

    private EditText answerText1;
    private EditText boolean1;
    private EditText answerText2;
    private EditText boolean2;
    private EditText answerText3;
    private EditText boolean3;
    private EditText answerText4;
    private EditText boolean4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_editor);

        //

        appDatabase = AppDatabase.getInstance(this);
        db = appDatabase.getReadableDatabase();

        // bind the EditText-Fields to the EditText-Objects


        bindEditTextFields();


        //

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //

        adapter = new QuestionAdapter(this, appDatabase.getQuestionListContents());
        recyclerView.setAdapter(adapter);


        /*
         *
         */
        buttonAddQuestion =  findViewById(R.id.btnAddQuestion);
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues cv = putAllEditTextValuesInAContentValuesAndReturnIt();

                appDatabase.addQuestion(db, cv);

                Log.d("cv Cursor auslesen: ", "onClick(AddQuestion): " + cv.toString());

                clearAllEditTextFields();

                adapter.swapCursor(appDatabase.getQuestionListContents());
            }

            /*
            HELPERCLASS
             */
            private ContentValues putAllEditTextValuesInAContentValuesAndReturnIt() {
                ContentValues cv = new ContentValues();

                cv.put(QuestionEntry.COLUMN_CATEGORY, category.getText().toString());
                cv.put(QuestionEntry.COLUMN_DIFFICULTY, Integer.parseInt(difficulty.getText().toString()));
                cv.put(QuestionEntry.COLUMN_QUESTIONTEXT, questionText.getText().toString());
                cv.put(QuestionEntry.COLUMN_ANSWERTEXT1, answerText1.getText().toString());
                cv.put(QuestionEntry.COLUMN_BOOLEAN1, Integer.parseInt(boolean1.getText().toString()));
                cv.put(QuestionEntry.COLUMN_ANSWERTEXT2, answerText2.getText().toString());
                cv.put(QuestionEntry.COLUMN_BOOLEAN2, Integer.parseInt(boolean2.getText().toString()));
                cv.put(QuestionEntry.COLUMN_ANSWERTEXT3, answerText3.getText().toString());
                cv.put(QuestionEntry.COLUMN_BOOLEAN3, Integer.parseInt(boolean3.getText().toString()));
                cv.put(QuestionEntry.COLUMN_ANSWERTEXT4, answerText4.getText().toString());
                cv.put(QuestionEntry.COLUMN_BOOLEAN4, Integer.parseInt(boolean4.getText().toString()));

                return cv;
            }

            /*
            HELPERCLASS
             */
            private void clearAllEditTextFields() {
                category.getText().clear();
                difficulty.getText().clear();
                questionText.getText().clear();

                answerText1.getText().clear();
                boolean1.getText().clear();
                answerText2.getText().clear();
                boolean2.getText().clear();
                answerText3.getText().clear();
                boolean3.getText().clear();
                answerText4.getText().clear();
                boolean4.getText().clear();
            }

        });

        buttonDropQuestion =  findViewById(R.id.btnDropQuestion);
        buttonDropQuestion.setOnClickListener(v -> createDropDialog());

        buttonMenu =  findViewById(R.id.btnMenuDatabase);
        buttonMenu.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(DatabaseEditor.this, Menu.class);
            startActivity(intent);
        });



    } // end onCreate()

    private void bindEditTextFields() {
        category = findViewById(R.id.etxtDatabaseCategory);
        difficulty = findViewById(R.id.etxtDatabaseDifficulty);
        questionText = findViewById(R.id.etxtDatabaseQuestiontext);

        answerText1 = findViewById(R.id.etxtDatabaseAnswerText1);
        boolean1 = findViewById(R.id.etxtDatabaseBoolean1);
        answerText2 = findViewById(R.id.etxtDatabaseAnswerText2);
        boolean2 = findViewById(R.id.etxtDatabaseBoolean2);
        answerText3 = findViewById(R.id.etxtDatabaseAnswerText3);
        boolean3 = findViewById(R.id.etxtDatabaseBoolean3);
        answerText4 = findViewById(R.id.etxtDatabaseAnswerText4);
        boolean4 = findViewById(R.id.etxtDatabaseBoolean4);
    }


    //
    private void createDropDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to Drop the Table ?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appDatabase.dropAndRecreateTableQuestion(db);
                adapter.swapCursor(appDatabase.getQuestionListContents());
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        });

        alertDialog.create().show();
    }
}