package com.moolair.studytimer.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moolair.studytimer.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;

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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Timer timer = timerItems.get(position);
    }

    @Override
    public int getItemCount() {
        return timerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView timerItemName;
        public TextView hour;
        public TextView minute;

        public ViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
