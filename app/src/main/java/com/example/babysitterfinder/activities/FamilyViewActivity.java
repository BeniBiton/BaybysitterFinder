package com.example.babysitterfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Family;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class FamilyViewActivity extends AppCompatActivity {
    private TextView viewName, viewNumOfChildren, viewLocation, viewChildrenAges, viewDescription, viewRegion;
    private FirebaseFirestore firestore;
    private FirebaseAuth authService;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_profile_view);

        viewName = findViewById(R.id.familyName);
        viewNumOfChildren = findViewById(R.id.numOfChildren);
        viewLocation = findViewById(R.id.location);
        viewChildrenAges = findViewById(R.id.childrenAges);
        viewDescription = findViewById(R.id.description);
        viewRegion = findViewById(R.id.region);

        firestore = FirebaseFirestore.getInstance();
        authService = FirebaseAuth.getInstance();

        loadFamilyProfile();

        String currentId = getIntent().getStringExtra("FAMILY_ID");
        if (currentId == null) {
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            setupBottomNavigation();
        }
    }

    private void loadFamilyProfile() {
        String familyId = getIntent().getStringExtra("FAMILY_ID");
        if (familyId == null) {
            familyId = authService.getCurrentUser().getUid();
        }
        Log.d("FamilyViewActivity", "Family ID: " + familyId);
        firestore.collection("family").document(familyId)
                .get()
                .addOnCompleteListener(task -> {
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
                startActivity(new Intent(FamilyViewActivity.this, HomeFamilyActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}
