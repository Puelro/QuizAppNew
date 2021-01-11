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
import com.example.quizappnew.database.QuestionContract;

public class DatabaseEditor extends AppCompatActivity {
    private static final String TAG = "DatabaseEditor";

    private SQLiteDatabase db;
    private AppDatabase appDatabase;

    private QuestionAdapter adapter;

    Button buttonAddQuestion;
    Button buttonDeleteQuestion;
    Button buttonDropQuestion;
    Button buttonMenu;

    private TextView id;

    private EditText sqlCommand;
    private EditText questionID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_editor);

        //
        appDatabase = AppDatabase.getInstance(this);
        db = appDatabase.getReadableDatabase();

        // bind the EditText-Fields to the EditText-Objects
        sqlCommand = findViewById(R.id.etxtValuesString);
        questionID = findViewById(R.id.etxtQuestionIDDelete);

        //
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //
        adapter = new QuestionAdapter(this, appDatabase.getQuestionListContents());
        recyclerView.setAdapter(adapter);


        /*
         *
         */
        buttonAddQuestion =  findViewById(R.id.AddQuestion);
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertValues = sqlCommand.getText().toString();

                try{
                    ContentValues cv = putAllColumnValuesInAContentValuesAndReturnIt(insertValues);
                    appDatabase.addQuestion(db, cv);

                    Log.d("cv Cursor auslesen: ", "onClick(AddQuestion): " + cv.toString());

                    sqlCommand.getText().clear();

                    adapter.swapCursor(appDatabase.getQuestionListContents());
                }catch(NumberFormatException e)
                {
                    Log.d(TAG, "btnInsertValues: Text found in the inserted values, where a Number was expected");
                }
            }

            private ContentValues putAllColumnValuesInAContentValuesAndReturnIt(String _insertValues) throws NumberFormatException{
                ContentValues cv = new ContentValues();
                String[] insertValuesArr = _insertValues.split(",");

                cv.put(QuestionContract.QuestionEntry.COLUMN_CATEGORY, insertValuesArr[0]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_DIFFICULTY, Integer.parseInt(insertValuesArr[1]));
                cv.put(QuestionContract.QuestionEntry.COLUMN_QUESTIONTEXT, insertValuesArr[2]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT1, insertValuesArr[3]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT2, insertValuesArr[4]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT3, insertValuesArr[5]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_ANSWERTEXT4, insertValuesArr[6]);
                cv.put(QuestionContract.QuestionEntry.COLUMN_CORRECT_ANSWER, Integer.parseInt(insertValuesArr[7]));

                return cv;
            }

        });

        /*
         *
         */
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


        buttonDropQuestion =  findViewById(R.id.btnDropQuestion);
        buttonDropQuestion.setOnClickListener(v -> createDropDialogDropTable());

        buttonMenu =  findViewById(R.id.btnMenuDatabase);
        buttonMenu.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(DatabaseEditor.this, Menu.class);
            startActivity(intent);
        });



    } // end onCreate()


    //
    private void createDropDialogDropTable(){
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

    //
    private void createDropDialogDropRow(int _id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to drop the Question with the ID: " + _id  + " ?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appDatabase.removeQuestion(db, (long) _id);
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