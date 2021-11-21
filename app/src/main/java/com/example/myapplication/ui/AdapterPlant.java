package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;

import java.util.ArrayList;
import java.util.List;

public class AdapterPlant extends RecyclerView.Adapter<AdapterPlant.ViewHolder>
        implements Filterable {

    private ArrayList<Plant> mPlants;
    private List<Plant> displayedList;
    private Context contextAdapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eName;
        public Plant ePlant;

        public ViewHolder(View pItem) {
            super(pItem);
            eName = (TextView) pItem.findViewById(R.id.plantName);


            pItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pItem) {
                    Intent myIntent = new Intent(contextAdapter, PlantSingleActivity.class);
                    myIntent.putExtra("plant_id", String.valueOf(ePlant.getPlant_id()));
                    contextAdapter.startActivity(myIntent);
                }
            }
            );
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.ePlant = mPlants.get(position);
        holder.eName.setText(holder.ePlant.getPlant_name());
    }

    public AdapterPlant(ArrayList<Plant> pPlants, Context context) {
        //super();
        mPlants = pPlants;
        displayedList = new ArrayList<>(pPlants);
        contextAdapter = context;
    }


    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filtered = new FilterResults();
                List<Plant> results = new ArrayList<>();

                if (constraint.toString().length() > 0) {
                    if (mPlants != null && mPlants.size() > 0) {
                        for (final Plant e : mPlants) {
                            if (e.getPlant_name().toLowerCase().contains(constraint.toString())) {
                                results.add(e);
                            }
                        }
                    }
                    filtered.values = results;
                    filtered.count = results.size();
                } else {
                    filtered.values = new ArrayList<>(displayedList);
                    filtered.count = displayedList.size();
                }
                return filtered;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint.length() > 0 && results.count > 0) {
                    mPlants.clear();
                    mPlants.addAll((ArrayList<Plant>) results.values);
                    notifyDataSetChanged();
                } else {
                    mPlants = new ArrayList<>(displayedList);
                    notifyDataSetChanged();
                }
            }
        };
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mPlants.size();
    }
}
