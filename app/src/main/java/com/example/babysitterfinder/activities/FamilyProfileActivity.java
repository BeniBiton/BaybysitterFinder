package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Family;
import com.example.babysitterfinder.services.FirestoreService;
import com.google.firebase.auth.FirebaseAuth;


public class FamilyProfileActivity extends AppCompatActivity {
    private EditText editTextFamilyName, editTextNumberOfChildren, editTextLocation, editTextChildrenAges, editTextFamilyDescription, editTextRegion;

    private Button buttonSubmitFamilyProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_profile);

        editTextFamilyName = findViewById(R.id.editTextFamilyName);
        editTextNumberOfChildren = findViewById(R.id.editTextNumberOfChildrens);
        editTextChildrenAges = findViewById(R.id.editTextChildrenAges);
        editTextLocation = findViewById(R.id.editTextLocationAddress);
        editTextFamilyDescription = findViewById(R.id.editTextFamilyDescription);
        editTextRegion = findViewById(R.id.editTextRegion);
        buttonSubmitFamilyProfile = findViewById(R.id.buttonSubmitFamilyProfile);

        buttonSubmitFamilyProfile.setOnClickListener(view -> submitProfile());
    }

    private void submitProfile() {
        String familyName = editTextFamilyName.getText().toString();
        String numOfChildrenStr = editTextNumberOfChildren.getText().toString();
        String Location = editTextLocation.getText().toString();
        String childrenAges = editTextChildrenAges.getText().toString();
        String description = editTextFamilyDescription.getText().toString();
        String region = editTextRegion.getText().toString();

        // have to make validation

        int numberOfChildren = Integer.parseInt(numOfChildrenStr);
        String familyId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Family family = new Family(familyName, numberOfChildren, Location, childrenAges, description, region);
        FirestoreService.saveFamilyProfile(family, familyId, success -> {
            if (success) {
                Toast.makeText(this, "profile saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
