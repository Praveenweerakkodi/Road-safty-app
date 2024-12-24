package com.sahanmw.roadsafetyapp;

public class PoliceStation {
    private String contact;
    private String location;
    private String name;

    // Empty constructor for Firebase
    public PoliceStation() {}

    public PoliceStation(String name, String location, String contact) {
        this.name = name;
        this.location = location;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }
}


