package com.example.babysitterfinder.models;

import java.util.ArrayList;
import java.util.List;

public class Family {
    private String familyName;
    private int numOfChildren;
    private String location;
    private String childrenAges;
    private String description;
    private String region;
    private List<String> favorites;
    private String firestoreDocumentId;
    private float rating;
    private int ratingCount;

    public Family() {
        this.favorites = new ArrayList<>();
        this.rating = 0.0f;
        this.ratingCount = 0;
    }

    public Family(String familyName, int numOfChildren, String location, String childrenAges, String description, String region) {
        this.familyName = familyName;
        this.numOfChildren = numOfChildren;
        this.location = location;
        this.childrenAges = childrenAges;
        this.description = description;
        this.region = region;
        this.favorites = new ArrayList<>();
        this.rating = 0.0f;
        this.ratingCount = 0;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
    }

    public String getChildrenAges() {
        return childrenAges;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setChildrenAges(String childrenAges) {
        this.childrenAges = childrenAges;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFirestoreDocumentId() {
        return firestoreDocumentId;
    }

    public void setFirestoreDocumentId(String firestoreDocumentId) {
        this.firestoreDocumentId = firestoreDocumentId;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(String userId) {
        if (!favorites.contains(userId)) {
            favorites.add(userId);
        }
    }

    public void removeFavorite(String userId) {
        favorites.remove(userId);
    }

    public float getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void addRating(float newRating) {
        this.rating = ((this.rating * this.ratingCount) + newRating) / (this.ratingCount + 1);
        this.ratingCount++;
    }
}
