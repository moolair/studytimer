//package com.moolair.studytimer.Activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class ListActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list);
//    }
//}

package com.moolair.studytimer.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.Model.Timer;
import com.moolair.studytimer.R;
import com.moolair.studytimer.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Timer> timerList;
    private List<Timer> listItems;

    //Database
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        timerList = new ArrayList<>();
        listItems = new ArrayList<>();

        //Get items from database
        timerList = db.getAllTimers();
//todo: getHour and getMinute need to be fixed.
        for (Timer c: timerList){
            Timer timer = new Timer();
            timer.setSubject(c.getSubject());
            timer.setHour(c.getHour() + " : ");
            timer.setMinute(c.getMinute());
            timer.setId(c.getId());

            listItems.add(timer);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}

