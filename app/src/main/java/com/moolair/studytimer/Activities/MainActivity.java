package com.moolair.studytimer.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.Model.Timer;
import com.moolair.studytimer.R;
import com.moolair.studytimer.UI.RecyclerViewAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

//    private MaterialButton add_timer;
    private Button saveItem;
    private EditText hour;
    private EditText minute;
    private EditText studySubject;

    //Database
    private DBHandler db;

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Timer> timerList;
    private List<Timer> listItems;

    //timer
    public int calculateTotal;
//    private boolean timerRunning;
//    private CountDownTimer countDownTimer;
//    private long totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*YJ - April 4, 2020
        logo and ad page come here.
         setContentView(R.layout.logo_main); --for 2 seconds? then ad page
         */

        db = new DBHandler(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        timerList = new ArrayList<>();
        listItems = new ArrayList<>();

        //Get items from database
        timerList = db.getAllTimers();
        for (Timer c: timerList){
            Timer timer = new Timer();
            timer.setSubject(c.getSubject());
            timer.setHour(c.getHour() + " :");
            timer.setMinute(c.getMinute());
            timer.setId(c.getId());

            listItems.add(timer);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        Button start_timing = findViewById(R.id.start_timing);
        start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startStop();
                int position = 0;
                //todo: takes totalTime value and run it into activity_countdown.
                //todo: if no time set on recyclerView, do nothing.
                String inputTime = Integer.toString(calculateTotal); //todo: for now just first time to be countdown. May 14, 2020
                long millisInput = Long.parseLong(inputTime) * 60000;
                if (db.getTimersCount() != 0) {
                    Timer timer = timerList.get(position);
                    Intent intent = new Intent(MainActivity.this, CountdownActivity.class);
                    intent.putExtra("subject", timer.getSubject());
                    intent.putExtra("hour", timer.getHour());
                    intent.putExtra("minute", timer.getMinute());
                    intent.putExtra("id", timer.getId());

                    startActivity(intent);

                }else {
                    Toast.makeText(MainActivity.this, "Add one time slot to start the timer.", Toast.LENGTH_SHORT).show();
                    return;
                }
//                setTime(millisInput);
            }
        });

    }

//    public void startStop() {
//        if(timerRunning)
//            stopTimer();
//        else
//            startTimer();
//
//    }
//
//    public void stopTimer(){
//        countDownTimer.cancel();
//        //TODO: MAY 7, 2020 - create a detail activity edit the text
//        //countDownButton.setText("START");
//        timerRunning = false;
//    }
//
//    public void startTimer(){
//        countDownTimer = new CountDownTimer(totalTime, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //todo; countdown the timer
//                totalTime = millisUntilFinished;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
//
//        //TODO: MAY 7, 2020 - create a detail activity edit the text
//        //countDownButton.setText("START");
//        timerRunning = true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Remove title in toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //todo: menu case handler
        switch (item.getItemId()){
            case R.id.add_timer:

                createPopupDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createPopupDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.activity_popup, null);
        studySubject = v.findViewById(R.id.subjectItem);
        hour = v.findViewById(R.id.hourID);
        minute = v.findViewById(R.id.minuteID);

        saveItem = (Button) v.findViewById(R.id.saveItem);

        dialogBuilder.setView(v);
        dialog = dialogBuilder.create();

        dialog.show();


        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!studySubject.getText().toString().isEmpty())
                saveItemToDB(v);
            }


        });

    }

    private void saveItemToDB(View v) {
        Timer timer = new Timer();

        String newTimer = studySubject.getText().toString();
//        if (newTimer.isEmpty()){
//            Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
//            return;
//        } //importantForAutoFill = no on EditText
        String newHour = hour.getText().toString(); //mEditTextInput
        String newMinute = minute.getText().toString();
        if(newMinute.equals("00") || newMinute.equals("0")) {
            Toast.makeText(MainActivity.this, "It can't be 0 minute", Toast.LENGTH_SHORT).show();
            return;
        }

        calculateTotal = (Integer.parseInt(newHour) * 60) + Integer.parseInt(newMinute);
//        Toast.makeText(MainActivity.this, Integer.toString(calculateTotal), Toast.LENGTH_SHORT).show();

        timer.setSubject(newTimer);
        timer.setHour(newHour);
        timer.setMinute(newMinute);

        //Save to DB
        db.addTimer(timer);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

//        Log.d("Item Added ID: ", String.valueOf(db.getTimersCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                /*todo: udpate recylcerView data to display updated cardview.
                    For now, it will start Activity in order to refresh the recyclerview.
                    However, I need to figure out how to refresh the page properly.
                    YJ: May 2, 2020
                    If back button is pressed, it goes to the previous state (no time shows)
                    YJ: May 13, 2020
                 */
//                recyclerView.setAdapter(recyclerViewAdapter);
//                recyclerViewAdapter.notifyDataSetChanged();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
//                return;
            }
        }, 500); //.5 sec
    }
}
