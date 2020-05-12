package com.moolair.studytimer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moolair.studytimer.R;

public class CountdownActivity extends AppCompatActivity {
    private TextView countdownText;
    private ImageButton countdownButton;

    //timer
    private CountDownTimer countDownTimer;
    private long totalTime;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdownText = findViewById(R.id.countdownTimer);
        countdownButton = findViewById(R.id.pauseStart);
    }

    public void startStop() {
        if(timerRunning)
            stopTimer();
        else
            startTimer();

    }

    public void stopTimer(){
        countDownTimer.cancel();
        //TODO: MAY 7, 2020 - create a detail activity edit the text
        //countDownButton.setText("START");
        timerRunning = false;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //todo; countdown the timer
                totalTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {

            }
        }.start();

        //TODO: MAY 7, 2020 - create a detail activity edit the text
        //countDownButton.setText("START");
        timerRunning = true;
    }

    //update timer
    protected long updateTimer(String hour, String minute){
        //todo: calculate timer with hour and minute
        //todo: update: hink about remove the time so it needs to subtract the time)

        totalTime = hourID + ":" + MinuteID;

        int calculateHour = (int) totalTime / 60;
        int calculateMinute = (int) totalTime / 60000;
        int calculateSecond = (int) totalTime % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + calculateHour;
        timeLeftText+=":";
        if (calculateMinute < 10) timeLeftText+="0";
        timeLeftText += calculateMinute;

        //todo: display the time in a timeStart activity.
        //TODO: MAY 7, 2020 - create a detail activity edit the text and show it there.
        //Todo: also, once it finishes, it should  and move on to rest calculation --> then grab the next time and calculate so and so forth.
        countdownText.setText(timeLeftText);

        return totalTime;
    }
}
