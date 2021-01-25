package com.example.quizappnew.play_helper;

import android.widget.Button;

import com.example.quizappnew.R;
import com.example.quizappnew.activities.Play;

import java.util.ArrayList;

public class AnswerbuttonManager {

    ArrayList<Answerbutton> answerbuttons;

    public AnswerbuttonManager(Play _playActivity, QuestionManager qMng){
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

    public void enableAllAnswerButtons(boolean enabled) {
        for(Answerbutton abtn : answerbuttons){
            abtn.getUIButton().setEnabled(enabled);
        }
    }

    public ArrayList<Answerbutton> getAnswerButtons() {
        return answerbuttons;
    }
}
