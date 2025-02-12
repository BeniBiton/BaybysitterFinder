package com.example.babysitterfinder.models;

public class Babysitter {
    private String Name;
    private int Age;
    private String Region;
    private String Bio;
    private String Availability;
    private int Experience;
    private int phoneNumber;
    private String profilePictureUrl;
    private String firestoreDocumentId;

    public Babysitter() {
    }

    public Babysitter(String name, int age, String region, String bio, String availability, int experience, int phoneNumber, String profilePictureUrl) {
        this.Name = name;
        this.Age = age;
        this.Region = region;
        this.Bio = bio;
        this.Availability = availability;
        this.Experience = experience;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getExperience() {
        return Experience;
    }

    public void setExperience(int experience) {
        Experience = experience;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getFirestoreDocumentId() {
        return firestoreDocumentId;
    }

    public void setFirestoreDocumentId(String firestoreDocumentId) {
        this.firestoreDocumentId = firestoreDocumentId;
    }

}
