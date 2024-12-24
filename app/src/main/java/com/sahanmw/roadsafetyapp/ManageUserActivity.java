package com.sahanmw.roadsafetyapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private List<LogUser> userList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        loadUsersFromFirestore();
    }

    private void loadUsersFromFirestore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                // Map Firestore document to LogUser object (excluding password)
                                LogUser user = document.toObject(LogUser.class);
                                if (user != null) {
                                    userList.add(user);
                                }
                            }
                            userAdapter = new UserAdapter(userList);
                            usersRecyclerView.setAdapter(userAdapter);
                        }
                    } else {
                        Log.e("Firestore Error", "Error getting users", task.getException());
                        Toast.makeText(ManageUserActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

