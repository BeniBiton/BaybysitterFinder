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

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Family;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class OwnFamilyProfile extends AppCompatActivity {
    private TextView viewName, viewNumOfChildren, viewLocation, viewChildrenAges, viewDescription, viewRegion;
    private FirebaseFirestore firestore;
    private RatingBar ratingBar;
    private FirebaseAuth authService;
    private BottomNavigationView bottomNavigationView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.own_family_profile);

        viewName = findViewById(R.id.familyName);
        viewNumOfChildren = findViewById(R.id.numOfChildren);
        viewLocation = findViewById(R.id.location);
        viewChildrenAges = findViewById(R.id.childrenAges);
        viewDescription = findViewById(R.id.description);
        viewRegion = findViewById(R.id.region);
        ratingBar = findViewById(R.id.ratingBar);
        ImageView logoutIcon = findViewById(R.id.logoutIcon);

        firestore = FirebaseFirestore.getInstance();
        authService = FirebaseAuth.getInstance();

        loadFamilyProfile();
        loadFamilyRating();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        logoutIcon.setOnClickListener(view -> {
            authService.signOut();

            Intent intent = new Intent(OwnFamilyProfile.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadFamilyProfile() {
        String familyId = Objects.requireNonNull(authService.getCurrentUser()).getUid();

        Log.d("FamilyViewActivity", "Family ID: " + familyId);
        firestore.collection("family").document(familyId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                Family family = document.toObject(Family.class);
                if (family != null) {
                    displayFamilyProfile(family);
                } else {
                    Toast.makeText(this, "Family profile not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to load family profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayFamilyProfile(Family family) {
        viewName.setText(family.getFamilyName());
        viewNumOfChildren.setText(String.valueOf(family.getNumOfChildren()));
        viewLocation.setText(family.getLocation());
        viewChildrenAges.setText(family.getChildrenAges());
        viewDescription.setText(family.getDescription());
        viewRegion.setText(family.getRegion());
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(OwnFamilyProfile.this, HomeFamilyActivity.class));
                finish();
                return true;
            } else return itemId == R.id.nav_profile;
        });
    }

    private void loadFamilyRating() {
        String familyId = Objects.requireNonNull(authService.getCurrentUser()).getUid();

        firestore.collection("family").document(familyId).get().addOnSuccessListener(document -> {
            if (document.exists() && document.contains("rating") && document.contains("ratingCount")) {
                double totalRating = document.getDouble("rating");
                long ratingCount = document.getLong("ratingCount");

                if (ratingCount > 0) {
                    float averageRating = (float) (totalRating / ratingCount);
                    ratingBar.setRating(averageRating);
                } else {
                    ratingBar.setRating(0);
                }
            } else {
                ratingBar.setRating(0);
            }
            ratingBar.setIsIndicator(true);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load rating", Toast.LENGTH_SHORT).show();
        });
    }


}
