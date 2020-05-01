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

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
//    private EditText timing;
    private MaterialButton add_timer;
    private MaterialButton saveItem;
    private EditText hour;
    private EditText minute;
    private EditText studySubject;

    //Database
    private DBHandler db;

//    //RecyclerView
//    private RecyclerView recyclerView;
//    private RecyclerViewAdapter recyclerViewAdapter;
//    private List<Timer> timerList;
//    private List<Timer> listItems;

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

        //todo:add recyclerView when main page starts

        MaterialButton start_timing = findViewById(R.id.start_timing);
        start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //todo: menu case handler
        switch (item.getItemId()){
            case R.id.add_timer:

                /*
                YJ - April 4, 2020
                created popupDialog
                todo: this needs to store values
                */
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

        saveItem = (MaterialButton) v.findViewById(R.id.saveItem);

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
        String newHour = hour.getText().toString();
        String newMinute = minute.getText().toString();

        timer.setSubject(newTimer);
        timer.setHour(newHour);
        timer.setMinute(newMinute);

        //Save to DB
        db.addTimer(timer);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        Log.d("Item Added ID: ", String.valueOf(db.getTimersCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 500); //.5 sec
    }
}
