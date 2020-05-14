package com.moolair.studytimer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.R;

public class CountdownActivity extends AppCompatActivity {
    private TextView countdownText;
    private ImageButton countdownButton;

    //timer
    private CountDownTimer countDownTimer;
    private long mStarttimeinMillis;
    private long totalTime;
    private boolean timerRunning;

    //db
    private DBHandler db;
    private int calculateTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownText = findViewById(R.id.countdownTimer);
        countdownButton = findViewById(R.id.pauseStart);

        Button start_timing = findViewById(R.id.start_timing);
        start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startStop();
                //todo: takes totalTime value and run it into activity_countdown.
                //todo: if no time set on recyclerView, do nothing.
                String inputTime = Integer.toString(calculateTotal); //todo: for now just first time to be countdown. May 14, 2020
                long millisInput = Long.parseLong(inputTime) * 60000;

                if (db.getTimersCount() != 0)
                    startActivity(new Intent(MainActivity.this, CountdownActivity.class));
                else {
                    Toast.makeText(CountdownActivity.this, "Add one time slot to start the timer.", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
            }
        });
    }

    //todo: - in ountdownActivity, when press start it should countdown the time.
    // time needs to be grabbed from id=1. then, once it's finished it should move onto rest time. and so and so forth until the last id.
    // YJ: May 14, 2020

    public void startStop() {
        if(timerRunning)
            stopTimer();
        else
            startTimer();

    }

    private void setTime(long milliSeconds){
        mStarttimeinMillis = milliSeconds;
//        resetTimer();
    }

    public void stopTimer(){
        countDownTimer.cancel();
        //TODO: MAY 7, 2020 - create a detail activity edit the text
        //countDownButton.setText("START");
        timerRunning = false;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(mStarttimeinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //todo; countdown the timer
                mStarttimeinMillis = millisUntilFinished;
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

//        totalTime = hourID + ":" + MinuteID;

        int calculateHour = (int) mStarttimeinMillis / 60;
        int calculateMinute = (int) mStarttimeinMillis / 60000;
        int calculateSecond = (int) mStarttimeinMillis % 60000 / 1000;

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
