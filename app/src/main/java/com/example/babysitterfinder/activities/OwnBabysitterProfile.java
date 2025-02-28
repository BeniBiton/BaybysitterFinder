package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class OwnBabysitterProfile extends AppCompatActivity {
    private TextView viewName, viewAge, viewRegion, viewBio, viewAvailability, viewExperience, viewPhoneNumber;
    private ImageView profileImage, logoutIcon;
    private BottomNavigationView bottomNavigationView;
    private RatingBar ratingBar;
    private FirebaseFirestore firestore;
    private FirebaseAuth authService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.own_babysitter_profile);

        viewName = findViewById(R.id.babysitterName);
        viewAge = findViewById(R.id.babysitterAge);
        viewRegion = findViewById(R.id.babysitterRegion);
        viewBio = findViewById(R.id.babysitterBio);
        viewAvailability = findViewById(R.id.babysitterAvailability);
        viewExperience = findViewById(R.id.babysitterExperience);
        viewPhoneNumber = findViewById(R.id.babysitterPhoneNumber);
        profileImage = findViewById(R.id.babysitterImage);
        logoutIcon = findViewById(R.id.logoutIcon);
        ratingBar = findViewById(R.id.ratingBar);

        firestore = FirebaseFirestore.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        loadBabysitterProfile();
        loadBabysitterRating();

        logoutIcon.setOnClickListener(view -> {
            authService.signOut();

            Intent intent = new Intent(OwnBabysitterProfile.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadBabysitterProfile() {
        String ownId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Log.d("BabysitterViewActivity", "Babysitter ID: " + ownId);

        firestore.collection("babysitter").document(ownId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Babysitter babysitter = documentSnapshot.toObject(Babysitter.class);
                        if (babysitter != null) {
                            displayBabysitterProfile(babysitter);
                        } else {
                            Toast.makeText(this, "Babysitter profile not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("BabysitterViewActivity", "Error fetching babysitter data", task.getException());
                        Toast.makeText(this, "Error fetching babysitter data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayBabysitterProfile(Babysitter babysitter) {
        viewName.setText(babysitter.getName());
        viewAge.setText(String.valueOf(babysitter.getAge()));
        viewRegion.setText(babysitter.getRegion());
        viewBio.setText(babysitter.getBio());
        viewAvailability.setText(babysitter.getAvailability());
        viewExperience.setText(String.valueOf(babysitter.getExperience()));
        viewPhoneNumber.setText(String.valueOf(babysitter.getPhoneNumber()));

        if (babysitter.getProfilePictureUrl() != null && !babysitter.getProfilePictureUrl().isEmpty()) {
            Glide.with(this)
                    .load(babysitter.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Intent intent = new Intent(OwnBabysitterProfile.this, HomeBabysitterActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else return item.getItemId() == R.id.nav_profile;
        });
    }

    private void loadBabysitterRating() {
        authService = FirebaseAuth.getInstance();
        String babysitterId = Objects.requireNonNull(authService.getCurrentUser()).getUid();

        firestore.collection("babysitter").document(babysitterId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double totalRating = documentSnapshot.getDouble("rating");
                        Long ratingCount = documentSnapshot.getLong("ratingCount");

                        Log.d("BabysitterProfile", "Raw Data: " + documentSnapshot.getData());


                        Log.d("BabysitterProfile", "Total Rating: " + totalRating);
                        Log.d("BabysitterProfile", "Rating Count: " + ratingCount);

                        if (totalRating != null && ratingCount != null && ratingCount > 0) {
                            float averageRating = (float) (totalRating / ratingCount);
                            ratingBar.setRating(averageRating);
                            Log.d("BabysitterProfile", "Average Rating Set: " + averageRating);
                        } else {
                            ratingBar.setRating(0);
                            Log.d("BabysitterProfile", "Default rating (0) set due to no ratings.");
                        }
                    } else {
                        ratingBar.setRating(0);
                        Log.d("BabysitterProfile", "No rating data found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load rating", Toast.LENGTH_SHORT).show();
                    Log.e("BabysitterProfile", "Error fetching rating", e);
                });
    }

}
