package com.moolair.studytimer.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.Model.Timer;
import com.moolair.studytimer.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*YJ - April 4, 2020
        logo and ad page come here.
         setContentView(R.layout.logo_main); --for 2 seconds? then ad page
         */

        db = new DBHandler(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.add_timer) {
////
////            return true;
////        }
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
                //Todo: Save to db
                //Todo: Go to next screen
            if (!studySubject.getText().toString().isEmpty())
                saveItemToDB(v);
            }


        });

    }

    private void saveItemToDB(View v) {
        Timer timer = new Timer();

        String newTimer = studySubject.getText().toString();

        timer.setSubject(newTimer);

        //Save to DB
        db.addTimer(timer);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        Log.d("Item Added ID: ", String.valueOf(db.getTimersCount()));
    }
}
