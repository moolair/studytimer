package com.moolair.studytimer.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
                    //go to next screen
                }
            });

        }

        @Override
        public void onClick(View v) {

        }


    }

    public void deleteItem(int id) {
        DBHandler db = new DBHandler(context);
        db.deleteTimer(id);
    }
}
