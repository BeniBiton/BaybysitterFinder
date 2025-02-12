package com.example.babysitterfinder.models;

public class Family {
    private String familyName;
    private int numOfChildren;
    private String location;
    private String childrenAges;
    private String description;
    private String region;

    private String firestoreDocumentId;

    public Family() {
    }

    public Family(String familyName, int numOfChildren,String location, String childrenAges, String description, String region) {
        this.familyName = familyName;
        this.numOfChildren = numOfChildren;
        this.location = location;
        this.childrenAges = childrenAges;
        this.description = description;
        this.region = region;
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
}
