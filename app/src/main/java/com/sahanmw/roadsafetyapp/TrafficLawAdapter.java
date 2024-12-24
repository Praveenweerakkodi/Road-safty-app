package com.sahanmw.roadsafetyapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sahanmw.roadsafetyapp.LawDetailActivity;
import com.sahanmw.roadsafetyapp.R;
import com.sahanmw.roadsafetyapp.TrafficLaw;

import java.util.List;

public class TrafficLawAdapter extends RecyclerView.Adapter<TrafficLawAdapter.ViewHolder> {
    private List<TrafficLaw> trafficLaws;

    public TrafficLawAdapter(List<TrafficLaw> trafficLaws) {
        this.trafficLaws = trafficLaws;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_traffic_law, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrafficLaw law = trafficLaws.get(position);

        holder.lawDescription.setText(law.getDescription());

        // Load image from Firebase Storage or URL using Glide
        Glide.with(holder.lawImage.getContext())
                .load(law.getImageUrl())
                .into(holder.lawImage);

        // Set click listener to open LawDetailActivity with selected law details
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            Intent intent = new Intent(context, LawDetailActivity.class);
            intent.putExtra("title", law.getTitle());
            intent.putExtra("description", law.getDescription());
            intent.putExtra("imageUrl", law.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trafficLaws.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lawTitle, lawDescription;
        public ImageView lawImage;

        public ViewHolder(View itemView) {
            super(itemView);

            lawDescription = itemView.findViewById(R.id.lawDescription);
            lawImage = itemView.findViewById(R.id.lawImage);
        }
    }
}

