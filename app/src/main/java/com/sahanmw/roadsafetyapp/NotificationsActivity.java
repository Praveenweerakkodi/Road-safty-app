package com.sahanmw.roadsafetyapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private List<Notification> notificationsList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Set up adapter
        notificationsAdapter = new NotificationsAdapter(notificationsList);
        recyclerView.setAdapter(notificationsAdapter);

        // Load notifications from Firebase Firestore
        loadNotifications();
    }

    private void loadNotifications() {
        db.collection("notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            notificationsList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Notification notification = document.toObject(Notification.class);
                                notificationsList.add(notification);
                            }
                            notificationsAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("NotificationsActivity", "Error fetching notifications", task.getException());
                            Toast.makeText(NotificationsActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
