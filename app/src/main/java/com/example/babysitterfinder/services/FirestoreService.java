package com.example.babysitterfinder.services;

import android.util.Log;

import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.models.Family;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreService {
    private FirebaseFirestore firestore;


    public FirestoreService() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    // this function is save the data of the userProfile on the store
    public void saveUserProfile(String userId, Map<String, Object> profileData, OnCompleteListener<Void> listener) {
        firestore.collection("users").document(userId).set(profileData).addOnCompleteListener(listener);
    }

    // this function get a single user role from db
    public void getUserRole(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        firestore.collection("users").document(userId).get().addOnCompleteListener(listener);
    }

    public static void saveBabysitterProfile(Babysitter babysitter, FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("babysitter")
                .add(babysitter)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirestoreService", "Babysitter profile saved with ID: " + documentReference.getId());
                    callback.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreService", "Error saving babysitter profile", e);
                    callback.onComplete(false);
                });
    }

    public static void saveFamilyProfile(Family family, FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("family")
                .add(family)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirestoreService", "Family profile saved with ID: " + documentReference.getId());
                    callback.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreService", "Error saving family profile", e);
                    callback.onComplete(false);
                });
    }

    public void getBabysitters(BabysitterCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("babysitter")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Babysitter> babysitters = new ArrayList<>();
                    for(DocumentSnapshot doc: queryDocumentSnapshots){
                        Babysitter babysitter = doc.toObject(Babysitter.class);
                        babysitters.add(babysitter);
                    }
                    callback.onSuccess(babysitters);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public interface FirestoreCallback {
        void onComplete(boolean success);
    }

    public interface BabysitterCallback {
        void onSuccess(List<Babysitter> babysitters);
        void onFailure(Exception e);
    }
}
