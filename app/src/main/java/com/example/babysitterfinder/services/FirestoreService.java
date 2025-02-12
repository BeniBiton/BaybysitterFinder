package com.example.babysitterfinder.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.models.Family;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static void saveBabysitterProfile(Babysitter babysitter,String babysitterId,  FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        babysitter.setFirestoreDocumentId(babysitterId);
        db.collection("babysitter")
                .document(babysitterId)
                .set(babysitter)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirestoreService", "Babysitter profile saved with ID: " + babysitterId);
                    callback.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreService", "Error saving babysitter profile", e);
                    callback.onComplete(false);
                });
    }

    public static void saveFamilyProfile(Family family, String familyId, FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        family.setFirestoreDocumentId(familyId);

        db.collection("family")
                .document(familyId)
                .set(family)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreService", "Family profile saved with ID: " + familyId);
                    callback.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreService", "Error saving family profile", e);
                    callback.onComplete(false);
                });
    }


    public void getBabysitters(BabysitterCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("babysitter")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Babysitter> babysitters = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Babysitter babysitter = doc.toObject(Babysitter.class);
                        babysitter.setFirestoreDocumentId(doc.getId());
                        babysitters.add(babysitter);
                    }
                    callback.onSuccess(babysitters);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getFamilies(FamilyCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("family")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Family> families = new ArrayList<>();
                    for (var doc : queryDocumentSnapshots) {
                        Family family = doc.toObject(Family.class);
                        family.setFirestoreDocumentId(doc.getId());
                        families.add(family);
                    }
                    Log.d("FirestoreService", "Families fetched: " + families.size());
                    callback.onSuccess(families);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreService", "Error fetching families", e);
                    callback.onFailure(e);
                });

    }


    public interface FamilyCallback {
        void onSuccess(List<Family> families);

        void onFailure(Exception e);
    }

    public interface FirestoreCallback {
        void onComplete(boolean success);
    }

    public interface BabysitterCallback {
        void onSuccess(List<Babysitter> babysitters);

        void onFailure(Exception e);
    }
}
