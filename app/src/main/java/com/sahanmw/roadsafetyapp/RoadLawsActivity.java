package com.sahanmw.roadsafetyapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoadLawsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TrafficLawAdapter adapter;
    private List<TrafficLaw> trafficLaws;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_laws);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        trafficLaws = new ArrayList<>();
        adapter = new TrafficLawAdapter(trafficLaws);  // No need to pass context
        recyclerView.setAdapter(adapter);
        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        // Read data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trafficLaws.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TrafficLaw law = snapshot.getValue(TrafficLaw.class);
                    trafficLaws.add(law);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }
}

