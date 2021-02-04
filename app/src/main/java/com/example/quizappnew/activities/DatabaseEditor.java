package com.example.quizappnew.activities;

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

import com.example.quizappnew.R;
import com.example.quizappnew.database.AppDatabase;
import com.example.quizappnew.database.QuestionAdapter;
import com.example.quizappnew.database.QuestionContract;

public class DatabaseEditor extends AppCompatActivity {
    private static final String TAG = "DatabaseEditor";

    private SQLiteDatabase db;
    private AppDatabase appDatabase;

    private QuestionAdapter adapter;

    Button buttonAddQuestion;
    Button buttonDeleteQuestion;
    Button buttonDropQuestionTable;
    Button buttonMenu;

    private EditText sqlCommand;
    private EditText questionID;


    /**
     * initiate values
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_editor);

        appDatabase = AppDatabase.getInstance(this);
        db = appDatabase.getReadableDatabase();

        // bind the EditText-Fields to the EditText-Objects
        sqlCommand = findViewById(R.id.etxtValuesString);
        questionID = findViewById(R.id.etxtQuestionIDDelete);

        // initiate RecyclerView for questions
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initiate QuestionAdapter
        adapter = new QuestionAdapter(this, appDatabase.getQuestions());
        recyclerView.setAdapter(adapter);

        setButtons();
    } // end onCreate()



    /**
     * Remove all Questions from Database
     */
    private void createDropDialogDropQuestionTable(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Willst du wirklich die Datentabelle ''Question'' löschen?");
        alertDialog.setCancelable(false);

        //confirm choice to remove questions
        alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appDatabase.dropAndRecreateTableQuestion(db);
                adapter.swapCursor(appDatabase.getQuestions());
            }
        });

        //don't remove questions
        alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        });

        alertDialog.create().show();
    }


    /**
     * Remove a Question from Database by id
     *
     * @param _id id of the question that should be removed
     */
    private void createDropDialogDropRow(int _id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Willst du wirklich die Frage mit der ID : " + _id  + "löschen?");
        alertDialog.setCancelable(false);

        //confirm choice to remove question
        alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appDatabase.removeQuestionByID(db, (long) _id);
                adapter.swapCursor(appDatabase.getQuestions());
            }
        });

        //don't remove question
        alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        });

        alertDialog.create().show();
    }

    /**
     * initiate buttons
     */
    private void setButtons(){

        buttonAddQuestion =  findViewById(R.id.AddQuestion);
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertValues = sqlCommand.getText().toString();

                try{
                    //throws exception
                    ContentValues cv = putAllColumnValuesInAContentValuesAndReturnIt(insertValues);
                    appDatabase.addQuestion(db, cv);

                    Log.d("cv Cursor auslesen: ", "onClick(AddQuestion): " + cv.toString());

                    sqlCommand.getText().clear();

                    adapter.swapCursor(appDatabase.getQuestions());
                }catch(NumberFormatException e)
                {
                    Log.d(TAG, "btnInsertValues: Text found in the inserted values, where a number was expected");
                }
            }

            private ContentValues putAllColumnValuesInAContentValuesAndReturnIt(String _insertedValues) throws NumberFormatException{
                ContentValues cv = new ContentValues();
                String[] insertedValuesArr = _insertedValues.split(",");

                cv.put(QuestionContract.QuestionEntry.COLUMN_CATEGORY, insertedValuesArr[0]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_DIFFICULTY, Integer.parseInt(insertedValuesArr[1]));
                cv.put(QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT, insertedValuesArr[2]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT1, insertedValuesArr[3]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT2, insertedValuesArr[4]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT3, insertedValuesArr[5]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT4, insertedValuesArr[6]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_CORRECT_ANSWER, Integer.parseInt(insertedValuesArr[7]));

                return cv;
            }

        });

        buttonDeleteQuestion =  findViewById(R.id.btnDeleteQuestion);
        buttonDeleteQuestion.setOnClickListener(v -> {
            String _questionID = questionID.getText().toString();

            if( android.text.TextUtils.isDigitsOnly(_questionID) ){
                createDropDialogDropRow( Integer.parseInt(_questionID) );
                questionID.getText().clear();
            }else{
                Log.d(TAG, "DeleteTableRow: ID was not a number");
            }

        });

        buttonDropQuestionTable =  findViewById(R.id.btnDropQuestionTable);
        buttonDropQuestionTable.setOnClickListener(v -> createDropDialogDropQuestionTable());

        buttonMenu =  findViewById(R.id.btnMenuHighscore);
        buttonMenu.setOnClickListener(v -> {
            Intent intent = new Intent(DatabaseEditor.this, Menu.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(DatabaseEditor.this, Menu.class);
        startActivity(intent);
        finish();
    }
}