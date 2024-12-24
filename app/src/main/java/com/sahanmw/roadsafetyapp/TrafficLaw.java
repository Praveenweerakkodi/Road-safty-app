package com.sahanmw.roadsafetyapp;

public class TrafficLaw {
    private String title;
    private String description;
    private String imageUrl;
    private String violationDescription;
    private String fine;

    public TrafficLaw() {
        // Default constructor required for calls to DataSnapshot.getValue(TrafficLaw.class)
    }

    public TrafficLaw(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.violationDescription = violationDescription;
        this.fine = fine;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}


