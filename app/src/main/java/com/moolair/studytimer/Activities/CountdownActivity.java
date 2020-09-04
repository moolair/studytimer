package com.moolair.studytimer.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.R;

import java.util.Locale;

//import com.facebook.ads.*;

public class CountdownActivity extends AppCompatActivity {
    private TextView countdownTime;
    private TextView countdownSubject;
    private String countdownHour;
    private String countdownMinute;
    private ImageButton startButton;
    private ImageButton pauseButton;
    private long backPressedTime;
    private Toast backToast;

    //timer
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMillis;
//    private long totalTime;
    private boolean mTimerRunning;
    private long mEndTime;
    int restTime = 0;

    //db
    private DBHandler db;
    private int calculateTotal;

    //AdView
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        //AdMob implementation
//        adView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        //-------------Facebook Adview Banner START--------------------------
        adView = new AdView(this, "IMG_16_9_APP_INSTALL#323685912162312_323718938825676", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
        //-------------Facebook Adview Banner END--------------------------

        ActionBar actionBar = getSupportActionBar();

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
            actionBar.setTitle(countdownSubject.getText());
//            actionBar.setDisplayHomeAsUpEnabled(true); --set onClickListener??


            //todo: it's able to setup a time. try to run a timer with proper hour and minute time.
            //todo: ********************for now, May 27, 2020. Change this back to the bottom mTimeLestInMillis.*********************
            mTimeLeftInMillis = Integer.parseInt(countdownMinute) * 1000;
            //mTimeLeftInMillis = totalTime(countdownHour, countdownMinute);

            startTimer();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTimerRunning)
                    startTimer();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning)
                    pauseTimer();
            }
        });

        //updateTimer();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();

            //todo: bundle, ActivityResult, requestCode to update textView on MainPage?
            //todo: MainActivity Timer Intent 참조
            //todo: 일단, 올릴까? click to edit the time 하고?

            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to go Main page",
                    Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private int totalTime(String countdownHour, String countdownMinute) {
        return ((Integer.parseInt(countdownHour) * 60) + Integer.parseInt(countdownMinute))*60000;
    }

//    public void startStop() {
//        if(mTimerRunning)
//            stopTimer();
//        else
//            startTimer();
//
//    }

//    private void setTime(long milliSeconds){
//        mTimeLeftInMillis = milliSeconds;
////        resetTimer();
//    }

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
//                if (restTime == 0){
//                    restTime = 1;
//                    Intent restIntent = new Intent();
//                    setResult(RESULT_CANCELED, restIntent);
//                }else {
//                    restTime = 0;
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
//                }
                finish();
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

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putLong("millisLeft", mTimeLeftInMillis);
//        editor.putBoolean("timerRunning", mTimerRunning);
//        editor.putLong("endTime", mEndTime);
//
////        pauseTimer();
//        editor.apply();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//
//        mTimeLeftInMillis = prefs.getLong("millisLeft", 600000);
//        mTimerRunning = prefs.getBoolean("timerRunning", false);
//
//        updateTimer();
//        updateButtons();
//
//        if (mTimerRunning){
//            mEndTime = prefs.getLong("endTime", 0);
//            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
//
//            if (mTimeLeftInMillis < 0){
//                mTimeLeftInMillis = 0;
//                mTimerRunning = false;
//                updateTimer();
//                updateButtons();
//            } else {
//                startTimer();
//            }
//        }
//    }
//
//    private void updateButtons(){
//        if (mTimerRunning){
//            startButton.setVisibility(View.INVISIBLE);
//            pauseButton.setVisibility(View.VISIBLE);
//        } else {
//            startButton.setVisibility(View.VISIBLE);
//            pauseButton.setVisibility(View.INVISIBLE);
//        }
//    }
}
