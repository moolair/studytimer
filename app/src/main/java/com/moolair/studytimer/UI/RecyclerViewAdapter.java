package com.moolair.studytimer.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.moolair.studytimer.Activities.MainActivity;
import com.moolair.studytimer.Activities.popupActivity;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.R;
import com.moolair.studytimer.Model.Timer;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Timer> timerItems;

    private EditText updateSubject;
    private EditText updateHour;
    private EditText updateMinute;
    private Button updateItem;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Timer> timerItems) {
        this.context = context;
        this.timerItems = timerItems;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Timer timer = timerItems.get(position);

        holder.Subject.setText(timer.getSubject());
        holder.hour.setText(timer.getHour());
        holder.minute.setText(timer.getMinute());
    }

    @Override
    public int getItemCount() {
        return timerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Subject;
        public TextView hour;
        public TextView minute;
        public Button updateButton;
        public int id;

        //slide to delete or edit
        //public slide doSlideEdit
        //public slide doSlideDelete

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            //this is for list_row activity
            Subject = view.findViewById(R.id.subjectName);
            hour = view.findViewById(R.id.hourDisplay);
            minute = view.findViewById(R.id.minuteDisplay);

            //slide onclickListener

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //todo: when pressed, time and title can be fixed. Call PopupDialog to fix.
                    // Take the value from here and edit.
                    // find recyclerView + OnActivityResult --> this should be in popupActivity.java
                    //reference: https://stackoverflow.com/questions/51223752/use-onactivityresult-in-recyclerviewadapter

//                    updatePopupDialog(v);
                    //take them to popup activity - App Grocery List part 9 Udemy
                    //todo: udpate popupAcivity to accept the value and update
//--------------------------------working copy-----------------------------------------------
//                    int position = getAdapterPosition();
//
//                    Timer timer = timerItems.get(position);
//                    Intent updateIntent = new Intent(context, popupActivity.class);
//                    updateIntent.putExtra("subject", timer.getSubject());
//                    updateIntent.putExtra("hour", timer.getHour());
//                    updateIntent.putExtra("minute", timer.getMinute());
//                    updateIntent.putExtra("id", timer.getId());
//
//                    context.startActivity(updateIntent);
//--------------------------------working copy-----------------------------------------------
                    dialogBuilder = new AlertDialog.Builder(context);

                    inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.activity_popup, null);

    //                int position = getAdapterPosition();
    //                Timer timer = timerItems.get(position);
                    //This is for popupActivity
                    updateSubject = view.findViewById(R.id.subjectItem);
                    updateHour = view.findViewById(R.id.hourID);
                    updateMinute = view.findViewById(R.id.minuteID);
                    updateButton = view.findViewById(R.id.saveItem);

                    updateSubject.setText(timerItems.get(getAdapterPosition()).getSubject());
                    updateHour.setText(timerItems.get(getAdapterPosition()).getHour());
                    updateMinute.setText(timerItems.get(getAdapterPosition()).getMinute());

                    dialogBuilder.setView(view);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            Timer timer = timerItems.get(position);

                            if (!updateSubject.getText().toString().isEmpty())
                                updateItemToDB(v, timer);
                            dialog.dismiss();
                        }
                    });
                }
            });

        }

        @Override
        public void onClick(View v) {

        }


    }

    private void updateItemToDB(View view, Timer timer){
        DBHandler db = new DBHandler(context);

        //update item
        timer.setSubject(updateSubject.getText().toString());
        timer.setHour(updateHour.getText().toString());
        timer.setMinute(updateMinute.getText().toString());

        if (!updateSubject.getText().toString().isEmpty() && !updateHour.getText().toString().isEmpty()){
            db.updateTimer(timer);
            notifyDataSetChanged();
        }else
            Snackbar.make(view, "Update Subject, hour or minute", Snackbar.LENGTH_LONG).show();

//        String newTimer = updateSubject.getText().toString();
//        String newHour = updateHour.getText().toString(); //mEditTextInput
//        String newMinute = updateMinute.getText().toString();
//
//        if(newMinute.equals("00") || newMinute.equals("0")) {
//            Toast.makeText(context, "It can't be 0 minute", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
////        calculateTotal = (Integer.parseInt(newHour) * 60) + Integer.parseInt(newMinute);
////        Toast.makeText(MainActivity.this, Integer.toString(calculateTotal), Toast.LENGTH_SHORT).show();
//
//        timer.setSubject(newTimer);
//        timer.setHour(newHour);
//        timer.setMinute(newMinute);
//
//        //Save to DB
//        db.addTimer(timer, false);
//
//        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();
//
////        Log.d("Item Added ID: ", String.valueOf(db.getTimersCount()));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                /*todo: udpate recylcerView data to display updated cardview.
//                    For now, it will start Activity in order to refresh the recyclerview.
//                    However, I need to figure out how to refresh the page properly.
//                    YJ: May 2, 2020
//                    If back button is pressed, it goes to the previous state (no time shows)
//                    YJ: May 13, 2020
//                 */
//
//                dialog.dismiss();
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
////                return;
//
//
//            }
//        }, 500); //.5 sec

    }

    public void deleteItem(int id, int position) {
        DBHandler db = new DBHandler(context);
        db.deleteTimer(id);
        timerItems.remove(position);

        //todo: undo fix.
    }

    public void reAddItem(Timer timer, int position){
        DBHandler db = new DBHandler(context);
        db.addTimer(timer, true);
        timerItems.add(position, timer);
    }
}
