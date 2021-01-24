package com.example.quizappnew.play_helper;

import android.view.View;
import android.widget.Button;

import com.example.quizappnew.activities.Play;

public class Answerbutton{

    Play playActivity;
    int buttonNumber;
    Button uiButton;

    public Answerbutton(Play _playActivity, Button _answerButton, int _buttonNumber){
        playActivity = _playActivity;
        uiButton = _answerButton;
        buttonNumber  = _buttonNumber;
        setListiner();
    }

    private void setListiner(){
        uiButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                playActivity.buttonWasClicked(buttonNumber);
            }
        });
    }

    public Button getUIButton(){
        return uiButton;
    }
}
