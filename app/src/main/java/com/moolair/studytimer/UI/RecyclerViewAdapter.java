package com.moolair.studytimer.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.moolair.studytimer.Activities.MainActivity;
import com.moolair.studytimer.Data.DBHandler;
import com.moolair.studytimer.R;
import com.moolair.studytimer.Model.Timer;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Timer> timerItems;

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

            timerItemName = (TextView) view.findViewById(R.id.subjectName);
            hour = (TextView) view.findViewById(R.id.hourDisplay);
            minute = (TextView) view.findViewById(R.id.minuteDisplay);

            //slide onclickListener

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //todo: when pressed, time and title can be fixed. Call PopupDialog to fix.
                    // Take the value from here and edit.

                    updatePopupDialog();
                }
            });

        }

        @Override
        public void onClick(View v) {

        }


    }

    private void updatePopupDialog(){
        //todo: click to popup the dialog and edit then save.
        //todo: total time show on start?
        //todo: start an interstitial ad before starting the study.
        Toast.makeText(context, "this is pressed", Toast.LENGTH_SHORT).show();
//        dialogBuilder = new AlertDialog.Builder(this);
//        View v = getLayoutInflater().inflate(R.layout.activity_popup, null);
//        studySubject = v.findViewById(R.id.subjectItem);
//        hour = v.findViewById(R.id.hourID);
//        minute = v.findViewById(R.id.minuteID);
//
//        saveItem = (Button) v.findViewById(R.id.saveItem);
//
//        dialogBuilder.setView(v);
//        dialog = dialogBuilder.create();
//
//        dialog.show();
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
