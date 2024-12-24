package com.sahanmw.roadsafetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<LogUser> userList;

    public UserAdapter(List<LogUser> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        LogUser user = userList.get(position);

        // Set the user's information in the views
        holder.userName.setText("Name: " + user.getName());
        holder.userId.setText("ID No. / Driving License: " + user.getId()); // Display user ID below the name
        holder.userUsername.setText("Username: " + user.getUsername());
        holder.userEmail.setText("Email: " + user.getEmail());
        holder.userPhoneNumber.setText("Phone: " + user.getPhoneNumber());
        holder.userDistrict.setText("District: " + user.getDistrict());




    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userUsername, userEmail, userPhoneNumber, userDistrict, userId;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userId = itemView.findViewById(R.id.userUserId); // Make sure the ID TextView is in the layout
            userUsername = itemView.findViewById(R.id.userUsername);
            userEmail = itemView.findViewById(R.id.userEmail);
            userPhoneNumber = itemView.findViewById(R.id.userPhoneNumber);
            userDistrict = itemView.findViewById(R.id.userDistrict);

        }
    }
}
