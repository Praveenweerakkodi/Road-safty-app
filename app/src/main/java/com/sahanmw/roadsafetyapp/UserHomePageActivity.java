package com.sahanmw.roadsafetyapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);

        // Button 1: Upload Video
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageActivity.this, VideoUploadActivity.class);
                startActivity(intent);
            }
        });

        // Button 2: Wallet
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageActivity.this, WalletActivity.class);
                startActivity(intent);
            }
        });

        // Button 3: Road Laws
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageActivity.this, RoadLawsActivity.class);
                startActivity(intent);
            }
        });

        // Button 4: Police Stations
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageActivity.this, PoliceStationsActivity.class);
                startActivity(intent);
            }
        });

        // Button 5: Notifications
        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        // Button 6: Logout
        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog for logout confirmation
                new AlertDialog.Builder(UserHomePageActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UserHomePageActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish(); // End the current activity
                            }
                        })
                        .setNegativeButton(android.R.string.no, null) // Dismiss on "No"
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
