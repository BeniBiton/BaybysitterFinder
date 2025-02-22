package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

public class BabysitterViewActivity extends AppCompatActivity {
    private TextView viewName, viewAge, viewRegion, viewBio, viewAvailability, viewExperience, viewPhoneNumber;
    private ImageView profileImage;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_profile_view);

        viewName = findViewById(R.id.babysitterName);
        viewAge = findViewById(R.id.babysitterAge);
        viewRegion = findViewById(R.id.babysitterRegion);
        viewBio = findViewById(R.id.babysitterBio);
        viewAvailability = findViewById(R.id.babysitterAvailability);
        viewExperience = findViewById(R.id.babysitterExperience);
        viewPhoneNumber = findViewById(R.id.babysitterPhoneNumber);
        profileImage = findViewById(R.id.babysitterImage);

        firestore = FirebaseFirestore.getInstance();

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        loadBabysitterProfile();
    }

    private void loadBabysitterProfile() {
        String babysitterId = getIntent().getStringExtra("BABYSITTER_ID");
        if (babysitterId == null) {
            babysitterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (babysitterId == null) {
            Log.e("BabysitterViewActivity", "No valid Babysitter ID found.");
            Toast.makeText(this, "No valid Babysitter ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("BabysitterViewActivity", "Babysitter ID: " + babysitterId);

        firestore.collection("babysitter").document(babysitterId)
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
                Intent intent = new Intent(BabysitterViewActivity.this, HomeBabysitterActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}
