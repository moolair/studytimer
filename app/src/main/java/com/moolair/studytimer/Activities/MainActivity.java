package com.moolair.studytimer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.Model.Timer;
import com.moolair.studytimer.R;
import com.moolair.studytimer.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<Timer> listItems;
    private Timer deletedTimer = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    //timer
    int nextIntent = 0;

    //Rest Time
//    int restTime = 0;
    private TextView restSubject;
    private EditText restHour;
    private EditText restMinute;
//    private int calculateTotal;
//    private boolean timerRunning;
//    private CountDownTimer countDownTimer;
//    private long totalTime;

    //Interstitial Admob
    private InterstitialAd interstitialAd;


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
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        //Rest time
        restSubject = findViewById(R.id.restID);
        restHour = findViewById(R.id.hourRest);
        restMinute = findViewById(R.id.minuteRest);

        //interstitial Admob
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                interstitialAd.loadAd(new AdRequest.Builder().build());
                if (nextIntent != listItems.size()-1) {
                    restIntent();
                } else {
                    interstitialAds();
                    nextIntent = 0;
                    //todo: try to erase all the db timelist
                    //todo: clear buffer? memoryleak clear?
                }
            }
        });

        timerList = new ArrayList<>();
        listItems = new ArrayList<>();

        //Get items from database
        timerList = db.getAllTimers();
        for (Timer c: timerList){
            Timer timer = new Timer();
            timer.setSubject(c.getSubject());
            timer.setHour(c.getHour());
            timer.setMinute(c.getMinute());
            timer.setId(c.getId());

            listItems.add(timer);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Button start_timing = findViewById(R.id.start_timing);
        start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: takes totalTime value and run it into activity_countdown.
                //todo: if no time set on recyclerView, do nothing.
                if (listItems.size() != 0) {
                    timerIntent();
                    //todo: START button is greyed out.
                    //todo: wait for the first one to finish.

                }else {
                    Toast.makeText(MainActivity.this, "Add a timer to start", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void restIntent() {
        Intent restIntent = new Intent(MainActivity.this, CountdownActivity.class);
        restIntent.putExtra("subject", restSubject.getText().toString());
        restIntent.putExtra("hour", restHour.getText().toString());
        restIntent.putExtra("minute", restMinute.getText().toString());
//        restIntent.putExtra("id", restID.getId());

        startActivityForResult(restIntent, 1);
    }

    private void timerIntent(){
        Timer timer = listItems.get(nextIntent); //todo: take the value and do the loop of the whole process until the last subject. May 20, 2020
        Intent intent = new Intent(MainActivity.this, CountdownActivity.class);
        intent.putExtra("subject", timer.getSubject());
        intent.putExtra("hour", timer.getHour());
        intent.putExtra("minute", timer.getMinute());
        intent.putExtra("id", timer.getId());

        startActivityForResult(intent, 2);
    }

    private void interstitialAds() {
        //before going to rest, let's interstitial Admob
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //todo: drag and drop to re-order the timers.
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(listItems, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //todo: swipe to delete works, but needs to fix db deletion and recyclerview.
            final int position = viewHolder.getAdapterPosition();

            deletedTimer = listItems.get(position);
            listItems.remove(position);
            recyclerViewAdapter.deleteItem(deletedTimer.getId());

            //todo: what if I add time? in order for it to take an effect of deletion.
            recyclerViewAdapter.notifyItemRemoved(position);
            Snackbar.make(recyclerView, deletedTimer.getSubject(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listItems.add(position, deletedTimer);
                            recyclerViewAdapter.reAddItem(deletedTimer);
                            recyclerViewAdapter.notifyItemInserted(position);
                        }
                    }).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (nextIntent != listItems.size()-1) {
//            interstitialAds();
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    //todo: start admob then move onto the next activity
                    nextIntent++;
                    //todo: add rest time before starting timerList
                    //                    restTime = 0;
                    timerIntent();

                }
            } else {
                if (resultCode == RESULT_OK) {
//                    Intent restIntent = new Intent(MainActivity.this, CountdownActivity.class);
//                    restIntent.putExtra("subject", restSubject.getText().toString());
//                    restIntent.putExtra("hour", restHour.getText().toString());
//                    restIntent.putExtra("minute", restMinute.getText().toString());
//                    //                    intent.putExtra("id", timer.getId());
//
//                    startActivityForResult(restIntent, 1);
                    interstitialAds();
//                    restIntent();
                }
            }
//        }
    }

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
//            case R.id.home:
////                onUpButtonPressed();
//                Toast.makeText(getBaseContext(), "Press back again to exit",
//                        Toast.LENGTH_SHORT);
//                return true;
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

//        calculateTotal = (Integer.parseInt(newHour) * 60) + Integer.parseInt(newMinute);
//        Toast.makeText(MainActivity.this, Integer.toString(calculateTotal), Toast.LENGTH_SHORT).show();

        timer.setSubject(newTimer);
        timer.setHour(newHour);
        timer.setMinute(newMinute);

        //Save to DB
        db.addTimer(timer, false);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

//        Log.d("Item Added ID: ", String.valueOf(db.getTimersCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*todo: udpate recylcerView data to display updated cardview.
                    For now, it will start Activity in order to refresh the recyclerview.
                    However, I need to figure out how to refresh the page properly.
                    YJ: May 2, 2020
                    If back button is pressed, it goes to the previous state (no time shows)
                    YJ: May 13, 2020
                 */

                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
//                return;


            }
        }, 500); //.5 sec
//        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
//        recyclerView.setAdapter(recyclerViewAdapter);
//        recyclerViewAdapter.notifyDataSetChanged();
    }
}
