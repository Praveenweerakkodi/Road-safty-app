package com.sahanmw.roadsafetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PoliceStationAdapter extends RecyclerView.Adapter<PoliceStationAdapter.ViewHolder> {
    private List<PoliceStation> policeStations;

    public PoliceStationAdapter(List<PoliceStation> policeStations) {
        this.policeStations = policeStations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_police_station, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PoliceStation station = policeStations.get(position);
        holder.stationName.setText(station.getName());
        holder.stationLocation.setText(station.getLocation());
        holder.stationContact.setText(station.getContact());
    }

    @Override
    public int getItemCount() {
        return policeStations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stationName, stationLocation, stationContact;

        public ViewHolder(View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.stationName);
            stationLocation = itemView.findViewById(R.id.stationLocation);
            stationContact = itemView.findViewById(R.id.stationContact);
        }
    }
}
