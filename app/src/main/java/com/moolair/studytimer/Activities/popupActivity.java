package com.moolair.studytimer.Activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moolair.studytimer.R;


public class popupActivity extends AppCompatActivity {

    //    popup syntax
    private TextView subject;
    private TextView hour;
    private TextView minute;
    private Button saveItem;
    private int timerID;
//    float minHr = 0, maxHr = 24, currentHr = 0;
    //int   minMin = 1, maxMin = 60, currentMin = 45;

    //    private Slider scrollHour;
    private SeekBar scrollMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        //Taking the value from RecyclerViewAdapter - line 100
        subject = findViewById(R.id.subjectItem);
        hour = findViewById(R.id.hourID);
        minute = findViewById(R.id.minuteID);
        saveItem = findViewById(R.id.saveItem);
    //        scrollHour = (Slider) findViewById(R.id.scrollHour);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            subject.setText(bundle.getString("subject"));
            hour.setText(bundle.getString("hour"));
            minute.setText(bundle.getString("minute"));
            timerID = bundle.getInt("id");
        }

    }


}
