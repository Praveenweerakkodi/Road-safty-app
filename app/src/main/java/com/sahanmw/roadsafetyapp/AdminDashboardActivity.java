package com.sahanmw.roadsafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // View Videos Button
        Button viewVideosButton = findViewById(R.id.viewVideosButton);
        viewVideosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to View Videos Page
                Intent intent = new Intent(AdminDashboardActivity.this, AdminVideoActivity.class);
                startActivity(intent);
            }
        });

        // Manage Users Button
        Button manageUsersButton = findViewById(R.id.manageUsersButton);
        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Manage Users Page
                Intent intent = new Intent(AdminDashboardActivity.this, ManageUserActivity.class);
                startActivity(intent);
            }
        });

        // Security Options Button


        // Logout Button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Admin Login Page
                Intent intent = new Intent(AdminDashboardActivity.this, AdminLoginPageActivity.class);
                startActivity(intent);
                finish(); // End current activity to prevent returning to it
            }
        });
    }
}
