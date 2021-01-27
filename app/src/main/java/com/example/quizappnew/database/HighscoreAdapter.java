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
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizappnew.R;

import com.example.quizappnew.database.HighscoreContract.HighscoreEntry;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {
    private Context context;
    private Cursor cursor;


    public HighscoreAdapter(Context _context, Cursor _cursor) {
        this.context = _context;
        this.cursor = _cursor;
    }

    @NonNull
    @Override
    public HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.highscore_item, parent, false);
        return new HighscoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighscoreViewHolder holder, int position) {
        if (!this.cursor.moveToPosition(position)) {
            return;
        }

        saveValuesInVariablesAndSetTextViews(holder);
    }

    private void saveValuesInVariablesAndSetTextViews(@NonNull HighscoreViewHolder holder) {

        String name = this.cursor.getString(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_NAME));
        int points = this.cursor.getInt(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_POINTS));
        int maxDifficulty = this.cursor.getInt(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_MAX_DIFFICULTY));
        int maxStreak = this.cursor.getInt(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_MAX_STREAK));

        holder.nameText.setText(String.valueOf(name));
        holder.pointsText.setText(String.valueOf(points));
        holder.maxDifficultyText.setText(String.valueOf(maxDifficulty));
        holder.maxStreakText.setText(String.valueOf(maxStreak));
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (this.cursor != null) {
            Log.d("HighscoreAdapter: ", "swapCursor: Cursor contents: " + DatabaseUtils.dumpCursorToString(cursor));
            this.cursor.close();
        }

        this.cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    class HighscoreViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView pointsText;
        public TextView maxDifficultyText;
        public TextView maxStreakText;


        public HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.tvName_item);
            pointsText = itemView.findViewById(R.id.tvPoints_item);
            maxDifficultyText = itemView.findViewById(R.id.tvMaxDifficulty_item);
            maxStreakText = itemView.findViewById(R.id.tvMaxStreak_item);
        }

    }
}
