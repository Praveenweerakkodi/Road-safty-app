package com.sahanmw.roadsafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PoliceStationsActivity extends AppCompatActivity {


    private RecyclerView recyclerViewPoliceStations;
    private PoliceStationAdapter adapter;
    private List<PoliceStation> policeStations;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_stations);

        recyclerViewPoliceStations = findViewById(R.id.recyclerViewPoliceStations);
        recyclerViewPoliceStations.setLayoutManager(new LinearLayoutManager(this));
        policeStations = new ArrayList<>();
        adapter = new PoliceStationAdapter(policeStations);
        recyclerViewPoliceStations.setAdapter(adapter);

        // Firebase reference to the "police_stations" table
        databaseReference = FirebaseDatabase.getInstance().getReference("police_stations");

        // Load data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                policeStations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Create a PoliceStation object from the Firebase snapshot
                    PoliceStation station = snapshot.getValue(PoliceStation.class);

                    // Add the station to the list
                    policeStations.add(station);
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors when fetching data
            }
        });
    }


}