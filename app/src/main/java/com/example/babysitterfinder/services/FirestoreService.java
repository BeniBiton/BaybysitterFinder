package com.example.babysitterfinder.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FirestoreService {
    private FirebaseFirestore firestore;

    public FirestoreService(){
        this.firestore = FirebaseFirestore.getInstance();
    }
    // this function is save the data of the userProfile on the store
    public void saveUserProfile(String userId, Map<String, Object> profileData, OnCompleteListener<Void> listener) {
        firestore.collection("users").document(userId).set(profileData).addOnCompleteListener(listener);
    }
    // this function get a single user role from db
    public void getUserRole(String userId, OnCompleteListener<DocumentSnapshot> listener){
        firestore.collection("users").document(userId).get().addOnCompleteListener(listener);
    }
}
