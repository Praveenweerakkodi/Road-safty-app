package com.sahanmw.roadsafetyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminVideoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;
    private FirebaseFirestore firestore;
    private Spinner categorySpinner;
    private EditText filterDateEditText;
    private Button clearDateButton;
    private Button loadAllVideosButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_video);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        categorySpinner = findViewById(R.id.filterCategorySpinner);
        filterDateEditText = findViewById(R.id.filterDateEditText);
        clearDateButton = findViewById(R.id.clearDateButton);
        loadAllVideosButton = findViewById(R.id.loadAllVideosButton);

        // Initialize Firestore and video list
        firestore = FirebaseFirestore.getInstance();
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this, videoList); // Pass context

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(videoAdapter);

        // Load all videos initially
        loadVideos(null, null);

        // Set up date picker for filter
        filterDateEditText.setOnClickListener(v -> openDatePicker());

        // Set item selected listener for the category spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String selectedDate = filterDateEditText.getText().toString();
                loadVideos(selectedCategory, selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // Set click listener for the Clear button
        clearDateButton.setOnClickListener(v -> {
            filterDateEditText.setText(""); // Clear the date field
            loadVideos(categorySpinner.getSelectedItem().toString(), null); // Reload videos without date filter
        });

        // Set click listener for Load All Videos button
        loadAllVideosButton.setOnClickListener(v -> {
            filterDateEditText.setText(""); // Clear the date field
            categorySpinner.setSelection(0); // Reset to default category
            loadVideos(null, null); // Load all videos
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AdminVideoActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    calendar.set(year1, month1, dayOfMonth);
                    filterDateEditText.setText(sdf.format(calendar.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }

    // Method to load videos with optional filters
    private void loadVideos(String category, String date) {
        videoList.clear(); // Clear the current video list

        firestore.collection("videos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String videoCategory = document.getString("violationCategory");
                            String videoDate = document.getString("date");

                            // Apply filters safely
                            if (category != null && !category.equals("All") &&
                                    (videoCategory == null || !videoCategory.equals(category))) {
                                continue; // Skip if category does not match
                            }
                            if (date != null && !TextUtils.isEmpty(date) &&
                                    (videoDate == null || !videoDate.equals(date))) {
                                continue; // Skip if date does not match
                            }

                            // Retrieve video URL
                            String videoUrl = document.getString("video_url"); // Assuming 'video_url' is the field name in Firestore

                            // Create Video object and add to the list
                            Video video = new Video(
                                    videoDate != null ? videoDate : "Unknown Date",
                                    document.getString("description"),
                                    document.getString("place"),
                                    document.getString("time"),
                                    document.getString("video_duration"),
                                    videoCategory != null ? videoCategory : "Unknown Category",
                                    document.getString("username"),
                                    videoUrl != null ? videoUrl : "Unknown URL" // Set video URL
                            );
                            videoList.add(video);
                        }

                        // Notify adapter of data change
                        videoAdapter.notifyDataSetChanged();
                    } else {
                        // Handle error - Show a toast message or log the error
                        Log.e("Firestore Error", "Error getting documents: ", task.getException());
                    }
                });
    }


    // Group videos by month
    private void groupVideosByMonth() {
        // Logic to group videos by month
        Map<String, List<Video>> groupedVideos = new HashMap<>();
        for (Video video : videoList) {
            String monthYear = getMonthYear(video.getDate());
            if (!groupedVideos.containsKey(monthYear)) {
                groupedVideos.put(monthYear, new ArrayList<>());
            }
            groupedVideos.get(monthYear).add(video);
        }

        // Convert the grouped map back to a list for display
        videoList.clear();
        for (Map.Entry<String, List<Video>> entry : groupedVideos.entrySet()) {
            String monthYear = entry.getKey();
            videoList.addAll(entry.getValue());
        }

        videoAdapter.notifyDataSetChanged(); // Notify adapter for data change
    }

    private String getMonthYear(String date) {
        // Convert date string to month-year format
        // Implement your logic to convert date to the desired format
        // Example: "dd/MM/yyyy" to "MM/yyyy"
        // You might need to parse the date string first

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inputFormat.parse(date)); // Parse the date string
            return outputFormat.format(calendar.getTime()); // Format to "MM/yyyy"
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Month"; // Fallback if parsing fails
        }
    }
}
