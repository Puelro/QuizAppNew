package com.example.quizappnew.play_logic;

import android.view.View;
import android.widget.Button;

import com.example.quizappnew.activities.Play;

/**
 * An Answerbutton, managed by an {@link AnswerbuttonManager} object
 * @author Kent Feldner
 */
public class Answerbutton{

    /**
     * The instance of the Play Activity which is played
     */
    Play playActivity;
    /**
     * The number of the button [1-4]
     * More info at {@link AnswerbuttonManager}
     */
    int buttonNumber;

    /**
     * The associated UI-Button in the Activity
     */
    Button uiButton;

    /**
     * Initiates the instance variables and sets the Listener for the UI button
     *
     * @param _playActivity The instance of the Play Activity which is currently played
     * @param _answerButton he associated UI-Button in the Activity
     * @param _buttonNumber The number of the button [1-4]
     */
    public Answerbutton(Play _playActivity, Button _answerButton, int _buttonNumber){
        playActivity = _playActivity;
        uiButton = _answerButton;
        buttonNumber  = _buttonNumber;
        setListener();
    }

    /**
     * Sets the Listener for the UI button
     */
    private void setListener(){
        uiButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                playActivity.answerButtonWasClicked(buttonNumber);
            }
        });
    }

    /**
     * Returns the associated UI-Button
     * @return The associated UI-Button
     */
    public Button getUIButton(){
        return uiButton;
    }

    /**
     * Returns the button number. More info at {@link AnswerbuttonManager}
     * @return The number of the button from 1-4
     */
    public int getButtonNumber(){
        return buttonNumber;
    }
}
