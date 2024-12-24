package com.sahanmw.roadsafetyapp;

public class Video {
    private String date;
    private String description;
    private String place;
    private String time;
    private String videoDuration;
    private String violationCategory;
    private String username;
    private String videoUrl;

    // Constructor
    public Video(String date, String description, String place, String time, String videoDuration, String violationCategory, String username, String videoUrl) {
        this.date = date;
        this.description = description;
        this.place = place;
        this.time = time;
        this.videoDuration = videoDuration;
        this.violationCategory = violationCategory;
        this.username = username;
        this.videoUrl = videoUrl;
    }

    // Overloaded constructor
    public Video(String date, String description, String place, String time, String videoDuration, String violationCategory, String username) {
        this.date = date;
        this.description = description;
        this.place = place;
        this.time = time;
        this.videoDuration = videoDuration;
        this.violationCategory = violationCategory;
        this.username = username;
    }

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getVideoDuration() { return videoDuration; }
    public void setVideoDuration(String videoDuration) { this.videoDuration = videoDuration; }

    public String getViolationCategory() { return violationCategory; }
    public void setViolationCategory(String violationCategory) { this.violationCategory = violationCategory; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    @Override
    public String toString() {
        return "Video{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", videoDuration='" + videoDuration + '\'' +
                ", violationCategory='" + violationCategory + '\'' +
                ", username='" + username + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
