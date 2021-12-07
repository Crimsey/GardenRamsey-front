package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Watering;

import java.util.ArrayList;
import java.util.List;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.ViewHolder> {

    private ArrayList<Watering> mWaterings;
    private List<Watering> displayedList;
    private Context contextAdapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eName;
        public Watering eWatering;

        public ViewHolder(View wItem) {
            super(wItem);
            eName = (TextView) wItem.findViewById(R.id.noteName);

            wItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contextAdapter, WateringSingleActivity.class);
                    intent.putExtra("watering_id", String.valueOf(eWatering.getWatering_id()));
                    intent.putExtra("plant_id", String.valueOf(eWatering.getPlant_id()));
                    contextAdapter.startActivity(intent);
                }
            });
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eWatering = mWaterings.get(position);
        holder.eName.setText(holder.eWatering.getPlant());
    }

    public AdapterEvent(ArrayList<Watering> pWaterings, Context context) {
        //super();
        mWaterings = pWaterings;
        displayedList = new ArrayList<>(mWaterings);
        contextAdapter = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mWaterings.size();
    }
}
