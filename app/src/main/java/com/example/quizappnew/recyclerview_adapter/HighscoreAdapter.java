package com.example.quizappnew.recyclerview_adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizappnew.R;
import com.example.quizappnew.database.HighscoreContract.HighscoreEntry;

/**
 * @author Kent Feldner
 */
public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {
    /** The context for the layout inflater */
    private Context context;
    /** The cursor which will hold the rows of the highscore table */
    private Cursor cursor;

    /**
     * initiates values
     * @param _context The given context
     * @param _cursor The cursor which hold the highscore data
     */
    public HighscoreAdapter(Context _context, Cursor _cursor) {
        this.context = _context;
        this.cursor = _cursor;
    }

    /**
     * Returns a HighscoreViewHolder
     * @param parent default
     * @param viewType default
     * @return the onCreate Viewholder
     */
    @NonNull
    @Override
    public HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.highscore_item, parent, false);
        return new HighscoreViewHolder(view);
    }

    /**
     * Moves the cursor to the given position and fills the HighscoreViewHolder with the new data
     * @param holder the HighscoreViewHolder to fill
     * @param position the postion the cursor will point at
     */
    @Override
    public void onBindViewHolder(@NonNull HighscoreViewHolder holder, int position) {
        if (this.cursor.moveToPosition(position)) {
            fillTextViews(holder);
        }
    }

    /**
     * Fills the textview of the HighscoreViewHolder
     * @param holder The HighscoreViewholder
     */
    private void fillTextViews(@NonNull HighscoreViewHolder holder) {

        String name = this.cursor.getString(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_NAME));
        int points = this.cursor.getInt(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_POINTS));
        int maxDifficulty = this.cursor.getInt(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_MAX_DIFFICULTY));
        int maxStreak = this.cursor.getInt(this.cursor.getColumnIndex(HighscoreEntry.COLUMN_MAX_STREAK));

        holder.nameText.setText(String.valueOf(name));
        holder.pointsText.setText(String.valueOf(points));
        holder.maxDifficultyText.setText(String.valueOf(maxDifficulty));
        holder.maxStreakText.setText(String.valueOf(maxStreak));
    }

    /**
     * Get the count of items
     * @return The count of items
     */
    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    /**
     * Swaps the cursor and notifies Observers about the change
     * @param newCursor the new cursor which holds the new data
     */
    public void swapCursor(Cursor newCursor) {
        if (this.cursor != null) {
            this.cursor.close();
        }

        this.cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    /**
     * Inner class which extends Viewholder
     * */
    class HighscoreViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView pointsText;
        public TextView maxDifficultyText;
        public TextView maxStreakText;

        HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.tvName_item);
            pointsText = itemView.findViewById(R.id.tvPoints_item);
            maxDifficultyText = itemView.findViewById(R.id.tvMaxDifficulty_item);
            maxStreakText = itemView.findViewById(R.id.tvMaxStreak_item);
        }

    }
}
