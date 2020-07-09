package com.moolair.studytimer.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

    private TextView updateSubject;
    private EditText updateHour;
    private EditText updateMinute;
    private Button updateItem;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

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

        holder.timerItemName.setText(timer.getSubject());
        holder.hour.setText(timer.getHour());
        holder.minute.setText(timer.getMinute());
    }

    @Override
    public int getItemCount() {
        return timerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView timerItemName;
        public TextView hour;
        public TextView minute;
        public int id;

        //slide to delete or edit
        //public slide doSlideEdit
        //public slide doSlideDelete

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            timerItemName = view.findViewById(R.id.subjectName);
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
                    //todo: udpate popupAcivity to accept the value and update.
                    int position = getAdapterPosition();

                    Timer timer = timerItems.get(position);
                    Intent updateIntent = new Intent(context, popupActivity.class);
                    updateIntent.putExtra("subject", timer.getSubject());
                    updateIntent.putExtra("hour", timer.getHour());
                    updateIntent.putExtra("minute", timer.getMinute());
                    updateIntent.putExtra("id", timer.getId());

                    context.startActivity(updateIntent);

                }
            });

        }

        @Override
        public void onClick(View v) {

        }


    }

    private void updatePopupDialog(View view){
        //todo: click to popup the dialog and edit then save.
        //todo: total time show on start?
        //todo: start an interstitial ad before starting the study.
        Toast.makeText(context, "this is pressed", Toast.LENGTH_SHORT).show();

        //todo: inflate을 써서, popup을 이용.

//        View v = getLayoutInflater().inflate(R.layout.activity_popup, null);

//        Intent updateIntent = new Intent(context, popupActivity.class);
//        updateIntent.putExtra("subject", updateSubject.getText().toString());
//        updateIntent.putExtra("hour", updateHour.getText().toString());
//        updateIntent.putExtra("minute", updateMinute.getText().toString());

//        startActivityResult

        dialogBuilder = new AlertDialog.Builder(context);
//        view = LayoutInflater.from().getLayoutInflater().inflate(R.layout.activity_popup, null);


        updateItem = view.findViewById(R.id.saveItem);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();

        dialog.show();
//
//
//        saveItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!studySubject.getText().toString().isEmpty())
//                    saveItemToDB(v);
//            }
//
//
//        });

    }

    public void deleteItem(int id) {
        DBHandler db = new DBHandler(context);
        db.deleteTimer(id);

        //todo: undo fix.
    }

    public void reAddItem(Timer timer){
        DBHandler db = new DBHandler(context);
        db.addTimer(timer, true);
    }
}
