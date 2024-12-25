package com.example.babysitterfinder.models;

public class Family {
    private String familyName;
    private int numOfChildren;
    private String childrenAges;
    private String description;
    private String region;

    public Family(String familyName, int numOfChildren, String childrenAges, String description, String region) {
        this.familyName = familyName;
        this.numOfChildren = numOfChildren;
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
}
