package com.sahanmw.roadsafetyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VideoUploadActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final String TAG = "VideoUploadActivity";

    private VideoView videoView;
    private ImageButton btnPlayPause, btnResizeVideo;
    private TextView videoDuration, videoSize;
    private Button btnSelectVideo, btnUpload, btnSelectDate, btnSelectTime;
    private EditText etPlace, etUsername, etDescription;
    private Spinner spinnerViolationCategory;
    private Uri videoUri;

    private boolean isFullScreen = false;

    private Calendar selectedDateTime = Calendar.getInstance();
    private FirebaseFirestore firestore;
    private StorageReference storageReference;  // Firebase Storage reference

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);

        // Initialize Firestore and Storage
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // UI Elements
        videoView = findViewById(R.id.videoView);
        btnPlayPause = findViewById(R.id.btnPlayVideoInside);
        btnResizeVideo = findViewById(R.id.btnResizeVideo);
        videoDuration = findViewById(R.id.videoDuration);
        videoSize = findViewById(R.id.videoSize);
        btnSelectVideo = findViewById(R.id.btnSelectVideo);
        btnUpload = findViewById(R.id.btnUpload);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        etPlace = findViewById(R.id.etPlace);
        etUsername = findViewById(R.id.etUsername);
        etDescription = findViewById(R.id.etDescription);  // Description text field
        spinnerViolationCategory = findViewById(R.id.spinnerViolationCategory);

        // Set up spinner for violation categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.violation_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerViolationCategory.setAdapter(adapter);

        // Load video on button click
        btnSelectVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, PICK_VIDEO_REQUEST);
        });

        // Play/Pause Video
        btnPlayPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            } else {
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            }
        });

        // Resize Video (Minimize/Maximize)
        btnResizeVideo.setOnClickListener(v -> {
            if (isFullScreen) {
                videoView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                btnResizeVideo.setImageResource(R.drawable.ic_fullscreen);
                isFullScreen = false;
            } else {
                videoView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                btnResizeVideo.setImageResource(R.drawable.ic_fullscreen_exit);
                isFullScreen = true;
            }
        });

        // Select Date
        btnSelectDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(VideoUploadActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDateTime.set(year, month, dayOfMonth);
                        btnSelectDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateTime.getTime()));
                    },
                    selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Select Time
        btnSelectTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(VideoUploadActivity.this,
                    (view, hourOfDay, minute) -> {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        btnSelectTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedDateTime.getTime()));
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        // Upload video data to Firestore and Storage
        btnUpload.setOnClickListener(v -> uploadVideoDetails());

    }

    // Handle video selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.setOnPreparedListener(mp -> {
                // Show video duration
                int duration = videoView.getDuration() / 1000; // duration in seconds
                videoDuration.setText("Duration: " + (duration / 60) + ":" + (duration % 60));

                // Show video size
                File file = new File(videoUri.getPath());
                videoSize.setText("Size: " + file.length() / (1024 * 1024) + " MB");

                // Auto start video when prepared
                videoView.start();
            });
        }
    }

    // Upload video details to Firestore and Storage
    private void uploadVideoDetails() {
        String username = etUsername.getText().toString();  // Capture username
        String place = etPlace.getText().toString();
        String description = etDescription.getText().toString();  // Capture description
        String category = spinnerViolationCategory.getSelectedItem() != null ? spinnerViolationCategory.getSelectedItem().toString() : "";
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateTime.getTime());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedDateTime.getTime());

        // Check for null or empty inputs
        if (username.isEmpty() || place.isEmpty() || description.isEmpty() || category.isEmpty() || videoUri == null) {
            Toast.makeText(this, "Please fill all required fields and select a video.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload video to Firebase Storage
        String videoFileName = "videos/" + System.currentTimeMillis() + ".mp4";
        StorageReference videoRef = storageReference.child(videoFileName);

        // Upload the video
        UploadTask uploadTask = videoRef.putFile(videoUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL after upload
            videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String videoDownloadUrl = uri.toString();

                // Prepare video details to save to Firestore
                Map<String, Object> videoDetails = new HashMap<>();
                videoDetails.put("username", username);  // Save username
                videoDetails.put("place", place);
                videoDetails.put("violationCategory", category);
                videoDetails.put("description", description);  // Save description
                videoDetails.put("date", date);
                videoDetails.put("time", time);
                videoDetails.put("videoUrl", videoDownloadUrl);  // Store download URL
                videoDetails.put("videoDuration", videoDuration.getText().toString()); // Store video duration
                // Save to Firestore
                firestore.collection("videos")
                        .add(videoDetails)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Video details uploaded successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error uploading video details", e);
                            Toast.makeText(this, "Failed to upload video details", Toast.LENGTH_SHORT).show();
                        });
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to get download URL", e);
                Toast.makeText(this, "Failed to upload video", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Video upload failed", e);
            Toast.makeText(this, "Video upload failed", Toast.LENGTH_SHORT).show();
        });
    }
}
