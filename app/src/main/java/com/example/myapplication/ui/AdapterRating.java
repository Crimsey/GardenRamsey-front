package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Rating;
import com.example.myapplication.ui.models.Watering;

import java.util.ArrayList;
import java.util.List;

public class AdapterRating extends RecyclerView.Adapter<AdapterRating.ViewHolder> {

    private ArrayList<Rating> ratings;
    private List<Rating> displayedList;
    private Context contextAdapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rateName;
        public TextView rateComment;
        public RatingBar rateRating;
        public Rating rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rateName = itemView.findViewById(R.id.rateName);
            rateComment = itemView.findViewById(R.id.rateComment);
            rateRating = itemView.findViewById(R.id.rateRating);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rate, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rating = ratings.get(position);
        holder.rateName.setText(holder.rating.getUser_name_rating());
        holder.rateComment.setText(holder.rating.getComment());
        holder.rateRating.setRating(holder.rating.getRate());
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public AdapterRating(ArrayList<Rating> pRatings, Context context) {
        //super();
        ratings = pRatings;
        displayedList = new ArrayList<>(pRatings);
        contextAdapter = context;
    }
}
