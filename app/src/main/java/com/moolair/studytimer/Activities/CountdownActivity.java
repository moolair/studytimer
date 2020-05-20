package com.moolair.studytimer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.R;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {
    private TextView countdownTime;
    private TextView countdownSubject;
    private String countdownHour;
    private String countdownMinute;
    private ImageButton startButton;
    private ImageButton pauseButton;

    //timer
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMillis;
    private long totalTime;
    private boolean mTimerRunning;

    //db
    private DBHandler db;
    private int calculateTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdownTime = findViewById(R.id.countdownTimer);
        countdownSubject = findViewById(R.id.countdownSubject);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);

        //reference: coding in Flow - Youtube
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
//            countdownTime.setText();
            countdownSubject.setText(bundle.getString("subject"));
            countdownHour = bundle.getString("hour");
            countdownMinute = bundle.getString("minute");

            //todo: it's able to setup a time. try to run a timer with proper hour and minute time.
            mTimeLeftInMillis = totalTime(countdownHour, countdownMinute);
////            countdownTime.setText(Integer.toString(calculation));

            startTimer();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning)
                    pauseTimer();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning)
                    startTimer();
            }
        });

        updateTimer();
    }

    private int totalTime(String countdownHour, String countdownMinute) {
        return ((Integer.parseInt(countdownHour) * 60) + Integer.parseInt(countdownMinute))*60000;
    }

    //todo: - in countdownActivity, when press start it should countdown the time.
    // time needs to be grabbed from id=1. then, once it's finished it should move onto rest time. and so and so forth until the last id.
    // YJ: May 14, 2020

//    public void startStop() {
//        if(mTimerRunning)
//            stopTimer();
//        else
//            startTimer();
//
//    }

    private void setTime(long milliSeconds){
        mTimeLeftInMillis = milliSeconds;
//        resetTimer();
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        //TODO: MAY 7, 2020 - create a detail activity edit the text
        //countDownButton.setText("START");
        mTimerRunning = false;
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                //todo: grab the value from MainActivity recyclerview hour and minute
                //YJ: May 14, 2020
                updateTimer();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //todo: for now, it goes back to MainAcitivy; however, we actually need to move onto interstitial Admob
                startActivity(new Intent(CountdownActivity.this, MainActivity.class));
            }
        }.start();

        //TODO: MAY 7, 2020 - create a detail activity edit the text
        //countDownButton.setText("START");
        mTimerRunning = true;
        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
    }


    //update timer
    protected void updateTimer(){

        int convertHour = (int) (mTimeLeftInMillis / 1000) / 3600; //120000 1hour = 3600000
        int convertMinute = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60; //120
        int convertSecond = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;

        if (convertHour > 0)
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", convertHour, convertMinute);
        else
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", convertMinute, convertSecond);

        //todo: display the time in a timeStart activity.
        //TODO: MAY 7, 2020 - create a detail activity edit the text and show it there.
        //Todo: also, once it finishes, it should  and move on to rest calculation --> then grab the next time and calculate so and so forth.
        countdownTime.setText(timeLeftFormatted);

//        return totalTime;
    }

    private void updateButtons(){
        if (mTimerRunning){
            startButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        } else {
            startButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);

//            if (mTime)
        }
    }
}
