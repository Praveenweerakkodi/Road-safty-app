package com.sahanmw.roadsafetyapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class LawDetailActivity extends AppCompatActivity {

    private TextView lawTitleDetail, lawDescriptionDetail;
    private ImageView lawImageDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_deatil);

        // Initialize UI components
        lawTitleDetail = findViewById(R.id.lawTitleDetail);
        lawDescriptionDetail = findViewById(R.id.lawDescriptionDetail);
        lawImageDetail = findViewById(R.id.lawImageDetail);

        // Retrieve data from Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Set data to views
        lawTitleDetail.setText(title);
        lawDescriptionDetail.setText(description);

        // Load the image using Glide
        Glide.with(this)
                .load(imageUrl)
                .into(lawImageDetail);
    }
}
