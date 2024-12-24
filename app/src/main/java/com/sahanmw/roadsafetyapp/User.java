package com.sahanmw.roadsafetyapp;

public class User {
    private String name;
    private String id;
    private String phoneNumber;
    private String district;
    private String username;
    private String password;

    // Required default constructor for Firebase object mapping
    public User() {
    }

    public User(String name, String id, String phoneNumber, String district, String username, String password) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.district = district;
        this.username = username;
        this.password = password;
    }

    // Getters and setters for each field
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }



    
}
