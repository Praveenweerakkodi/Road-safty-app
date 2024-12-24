package com.sahanmw.roadsafetyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity {
    private LinearLayout walletContainer;
    private DatabaseReference walletDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Initialize the wallet container and Firebase reference
        walletContainer = findViewById(R.id.walletContainer);
        walletDatabaseRef = FirebaseDatabase.getInstance().getReference("wallet");

        // Load wallet data from Firebase
        loadWalletData();
    }

    private void loadWalletData() {
        // Listen for changes in the "wallet" node in Firebase
        walletDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the previous views to avoid duplication
                walletContainer.removeAllViews();

                // Iterate over each child (user) in the "wallet" node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract user data as a HashMap
                    HashMap<String, Object> walletData = (HashMap<String, Object>) snapshot.getValue();

                    if (walletData != null) {
                        // Get the username and points from the database
                        String userName = (String) walletData.get("userName");
                        String points = String.valueOf(walletData.get("points"));

                        // Create a TextView for each user's wallet information
                        TextView userWalletView = new TextView(WalletActivity.this);
                        userWalletView.setText("User: " + userName + "\nPoints: " + points);
                        userWalletView.setPadding(10, 20, 10, 20);
                        userWalletView.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

                        // Add the TextView to the wallet container
                        walletContainer.addView(userWalletView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log an error message if data retrieval is cancelled
                Log.e("WalletActivity", "Error loading wallet data: " + databaseError.getMessage());
            }
        });
    }
}
