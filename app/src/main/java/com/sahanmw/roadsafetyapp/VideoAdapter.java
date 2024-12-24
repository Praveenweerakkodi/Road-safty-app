package com.sahanmw.roadsafetyapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Video> videoList;
    private Context context; // Added context variable

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context; // Initialize context
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.videoDate.setText(video.getDate());
        holder.violationCategory.setText(video.getViolationCategory());

        // Set click listener for the item
        holder.videoIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("videoUrl", video.getVideoUrl());
            intent.putExtra("videoDate", video.getDate());
            intent.putExtra("videoDescription", video.getDescription());
            intent.putExtra("videoPlace", video.getPlace());
            intent.putExtra("videoTime", video.getTime());
            intent.putExtra("videoDuration", video.getVideoDuration());
            intent.putExtra("videoCategory", video.getViolationCategory());
            intent.putExtra("username", video.getUsername());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoDate;
        TextView violationCategory;
        ImageButton videoIconButton; // Add this line

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoDate = itemView.findViewById(R.id.videoDateTextView);
            violationCategory = itemView.findViewById(R.id.violationCategoryTextView);
            videoIconButton = itemView.findViewById(R.id.videoIconButton); // Initialize here
        }
    }

}
