package com.example.babysitterfinder.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.services.FirebaseStorageService;
import com.example.babysitterfinder.services.FirestoreService;

public class BabysitterProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextAge, editTextRegion, editTextBio, editTextAvailability, editTextExperience;
    private Button buttonUploadPicture, buttonSubmitProfile;
    private String profilePictureURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babysitter_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextRegion = findViewById(R.id.editTextRegion);
        editTextBio = findViewById(R.id.editTextBio);
        editTextAvailability = findViewById(R.id.editTextAvailability);
        editTextExperience = findViewById(R.id.editTextExperience);
        buttonUploadPicture = findViewById(R.id.buttonUploadPicture);
        buttonSubmitProfile = findViewById(R.id.buttonSumbitProfile);

        buttonUploadPicture.setOnClickListener(v -> FirebaseStorageService.pickImage(this));
        buttonSubmitProfile.setOnClickListener(v -> submitProfile());
    }

    private void submitProfile() {
        String name = editTextName.getText().toString();
        String ageStr = editTextAge.getText().toString();
        String region = editTextRegion.getText().toString();
        String bio = editTextBio.getText().toString();
        String availability = editTextAvailability.getText().toString();
        String experienceStr = editTextExperience.getText().toString();

        // have to make validate for the request

        int age = Integer.parseInt(ageStr);
        int experience = Integer.parseInt(experienceStr);

        Babysitter babysitter = new Babysitter(name, age, region, bio, availability, experience, profilePictureURL);
        FirestoreService.saveBabysitterProfile(babysitter, success -> {
            if (success) {
                Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
