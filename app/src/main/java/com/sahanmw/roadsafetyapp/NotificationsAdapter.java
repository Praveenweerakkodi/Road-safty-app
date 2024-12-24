package com.sahanmw.roadsafetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Notification> notificationsList;

    public NotificationsAdapter(List<Notification> notificationsList) {
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationsList.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.dateTextView.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, messageTextView, dateTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notificationTitle);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            dateTextView = itemView.findViewById(R.id.notificationDate);
        }
    }
}

