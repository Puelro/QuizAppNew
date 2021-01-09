package com.example.quizappnew.database;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;

import com.example.quizappnew.R;

import com.example.quizappnew.database.QuestionContract.QuestionEntry;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{
    private Context context;
    private Cursor cursor;


    public QuestionAdapter(Context _context, Cursor _cursor){
        this.context = _context;
        this.cursor = _cursor;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        if(!this.cursor.moveToPosition(position)){
            return;
        }

        saveValuesInVariablesAndSetTextViews(holder);
    }

    private void saveValuesInVariablesAndSetTextViews(@NonNull QuestionViewHolder holder) {
        int id = this.cursor.getInt(this.cursor.getColumnIndex(QuestionEntry._ID));
        String category = this.cursor.getString(this.cursor.getColumnIndex(QuestionEntry.COLUMN_CATEGORY));
        int difficulty = this.cursor.getInt(this.cursor.getColumnIndex(QuestionEntry.COLUMN_DIFFICULTY));
        String questionText = this.cursor.getString(this.cursor.getColumnIndex(QuestionEntry.COLUMN_QUESTIONTEXT));

        String answerText1 = this.cursor.getString(this.cursor.getColumnIndex(QuestionEntry.COLUMN_ANSWERTEXT1));
        String answerText2 = this.cursor.getString(this.cursor.getColumnIndex(QuestionEntry.COLUMN_ANSWERTEXT2));
        String answerText3 = this.cursor.getString(this.cursor.getColumnIndex(QuestionEntry.COLUMN_ANSWERTEXT3));
        String answerText4 = this.cursor.getString(this.cursor.getColumnIndex(QuestionEntry.COLUMN_ANSWERTEXT4));

        int correctAnswer = this.cursor.getInt(this.cursor.getColumnIndex(QuestionEntry.COLUMN_CORRECT_ANSWER));

        holder.idText.setText(String.valueOf(id));
        holder.categoryText.setText(category);
        holder.difficultyText.setText(String.valueOf(difficulty));
        holder.questiontextText.setText(questionText);

        holder.answertext1Text.setText(answerText1);
        holder.answertext2Text.setText(answerText2);
        holder.answertext3Text.setText(answerText3);
        holder.answertext4Text.setText(answerText4);

        holder.correctAnswer.setText(String.valueOf(correctAnswer));
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(this.cursor != null){
            Log.d("QuestionAdapter: ", "swapCursor: Cursor contents: " + DatabaseUtils.dumpCursorToString(cursor));
            this.cursor.close();
        }

        this.cursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }

    public interface onItemClickListener{
        void onItemDelete(int position);
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder{

        public TextView idText;
        public TextView categoryText;
        public TextView difficultyText;
        public TextView questiontextText;
        public TextView answertext1Text;
        public TextView answertext2Text;
        public TextView answertext3Text;
        public TextView answertext4Text;
        public TextView correctAnswer;


        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            idText = itemView.findViewById(R.id.tvID_item);
            categoryText= itemView.findViewById(R.id.tvCategory_item);
            difficultyText= itemView.findViewById(R.id.tvDifficulty_item);
            questiontextText= itemView.findViewById(R.id.tvQuestion_item);

            answertext1Text= itemView.findViewById(R.id.tvAnswertext1_item);
            answertext2Text= itemView.findViewById(R.id.tvAnswertext2_item);
            answertext3Text= itemView.findViewById(R.id.tvAnswertext3_item);
            answertext4Text= itemView.findViewById(R.id.tvAnswertext4_item);

            correctAnswer= itemView.findViewById(R.id.tvCorrectAnswer_item);
        }

    }

}
