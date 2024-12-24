package com.sahanmw.roadsafetyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar seekBar;
    private Button playButton, pauseButton, acceptButton;
    private TextView videoDetailsTextView;
    private Handler handler = new Handler();
    private Runnable updateSeekBarRunnable;

    private static final String NOTIFICATION_PREF = "NotificationPref";
    private static final String USER_NOTIFICATION = "UserNotification";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Initialize UI components
        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        videoDetailsTextView = findViewById(R.id.videoDetailsTextView);
        acceptButton = findViewById(R.id.acceptButton);

        // Get video details from intent
        String videoUrl = getIntent().getStringExtra("videoUrl");
        String username = getIntent().getStringExtra("username");

        if (videoUrl == null || videoUrl.isEmpty()) {
            Log.e("VideoPlayerActivity", "Video URL is null or empty");
            return;
        }

        // Set video details
        setVideoDetails();

        // Set video URI and prepare video playback
        videoView.setVideoURI(Uri.parse(videoUrl));

        // Play button logic
        playButton.setOnClickListener(v -> {
            videoView.start();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
        });

        // Pause button logic
        pauseButton.setOnClickListener(v -> {
            videoView.pause();
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
        });

        // Video completion listener
        videoView.setOnCompletionListener(mp -> {
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
        });

        // Video prepared listener
        videoView.setOnPreparedListener(mp -> {
            seekBar.setMax(videoView.getDuration());
            updateSeekBar();
            playButton.setEnabled(true);
        });

        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Can handle if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Can handle if needed
            }
        });

        // Accept button logic
        acceptButton.setOnClickListener(v -> {
            Toast.makeText(VideoPlayerActivity.this, "Video accepted", Toast.LENGTH_SHORT).show();
            saveNotificationForUser(username, "Your video has been accepted");
        });
    }

    // Function to update the SeekBar based on video progress
    private void updateSeekBar() {
        updateSeekBarRunnable = () -> {
            seekBar.setProgress(videoView.getCurrentPosition());
            if (videoView.isPlaying()) {
                handler.postDelayed(updateSeekBarRunnable, 1000);
            }
        };
        handler.post(updateSeekBarRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBarRunnable);
    }

    // Set video details in the TextView
    private void setVideoDetails() {
        String videoDate = getIntent().getStringExtra("videoDate");
        String videoDescription = getIntent().getStringExtra("videoDescription");
        String videoPlace = getIntent().getStringExtra("videoPlace");
        String videoTime = getIntent().getStringExtra("videoTime");
        String videoCategory = getIntent().getStringExtra("videoCategory");
        String username = getIntent().getStringExtra("username");

        String videoDetails = "Date: " + videoDate + "\n" +
                "Description: " + videoDescription + "\n" +
                "Place: " + videoPlace + "\n" +
                "Time: " + videoTime + "\n" +
                "Category: " + videoCategory + "\n" +
                "Uploaded by (User ID): " + username;

        videoDetailsTextView.setText(videoDetails);
    }

    // Function to save a notification for the user
    private void saveNotificationForUser(String username, String message) {
        SharedPreferences sharedPreferences = getSharedPreferences(NOTIFICATION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NOTIFICATION + "_" + username, message);
        editor.apply();
    }
}
