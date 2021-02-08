package com.example.quizappnew.play_logic;

import android.widget.Button;

import com.example.quizappnew.R;
import com.example.quizappnew.activities.Play;

import java.util.ArrayList;

/**
 * Manages the 4 Answerbuttons in the Play Activity
 * @author Kent Feldner
 */
public class AnswerbuttonManager {

    /**
     * The 4 {@link Answerbutton} objects of the Play Activity
     */
    ArrayList<Answerbutton> answerbuttons;

    /**
     * Constructor of the AnswerbuttonManager
     * @param _playActivity The instance of the Play Activity which is currently played
     */
    public AnswerbuttonManager(Play _playActivity){
        answerbuttons = new ArrayList<>();

        Button answerButton = _playActivity.findViewById(R.id.btnAnswer1);
        answerbuttons.add(new Answerbutton(_playActivity, answerButton, 1));
        answerButton = _playActivity.findViewById(R.id.btnAnswer2);
        answerbuttons.add(new Answerbutton(_playActivity, answerButton, 2));
        answerButton = _playActivity.findViewById(R.id.btnAnswer3);
        answerbuttons.add(new Answerbutton(_playActivity, answerButton, 3));
        answerButton = _playActivity.findViewById(R.id.btnAnswer4);
        answerbuttons.add(new Answerbutton(_playActivity, answerButton, 4));
    }

    /**
     * Enables or disables all Answerbutton in the Play Activity
     * @param enabled When true all buttons will be enabled, when false all buttons will be disabled
     */
    public void enableAllAnswerButtons(boolean enabled) {
        for(Answerbutton abtn : answerbuttons){
            abtn.getUIButton().setEnabled(enabled);
        }
    }

    /**
     * Returns an ArrayList which contains all Answerbutton
     * @return An ArrayList which contains all Answerbutton
     */
    public ArrayList<Answerbutton> getAnswerButtons() {
        return answerbuttons;
    }
}
