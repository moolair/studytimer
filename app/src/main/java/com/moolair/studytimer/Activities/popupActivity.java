package com.moolair.studytimer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moolair.studytimer.R;

public class popupActivity extends AppCompatActivity {

    //    popup syntax

    private TextView hour;
    private TextView minute;
//    float minHr = 0, maxHr = 24, currentHr = 0;
    int   minMin = 1, maxMin = 60, currentMin = 45;

    //    private Slider scrollHour;
    private SeekBar scrollMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

//    //popup syntax create
    hour = (TextView) findViewById(R.id.hour);
    minute = (TextView) findViewById(R.id.minute);
//        scrollHour = (Slider) findViewById(R.id.scrollHour);
    scrollMinute = (SeekBar) findViewById(R.id.scrollMinute);

        scrollMinute.setMax(maxMin - minMin);
        minute.setText(""+ currentMin);

        scrollMinute.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentMin = progress + minMin;
            minute.setText("" + currentMin);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    }


}
