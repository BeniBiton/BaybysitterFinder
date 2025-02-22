package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.services.FirebaseStorageService;
import com.example.babysitterfinder.services.FirestoreService;
import com.google.firebase.auth.FirebaseAuth;

public class BabysitterProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextAge, editTextRegion, editTextBio, editTextAvailability, editTextExperience, editTextPhoneNumber;
    private Button buttonUploadPicture, buttonSubmitProfile;
    private Uri selectedImageUri = null; // Store selected image URI
    public static final int IMAGE_PICKER_REQUEST = 1000;

    @SuppressLint("MissingInflatedId")
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
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonUploadPicture = findViewById(R.id.buttonUploadPicture);
        buttonSubmitProfile = findViewById(R.id.buttonSumbitProfile);

        buttonUploadPicture.setOnClickListener(v -> FirebaseStorageService.pickImage(this));
        buttonSubmitProfile.setOnClickListener(v -> submitProfile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitProfile() {
        String name = editTextName.getText().toString();
        String ageStr = editTextAge.getText().toString();
        String region = editTextRegion.getText().toString();
        String bio = editTextBio.getText().toString();
        String availability = editTextAvailability.getText().toString();
        String experienceStr = editTextExperience.getText().toString();
        String phoneNumberStr = editTextPhoneNumber.getText().toString();

        if (name.isEmpty() || ageStr.isEmpty() || region.isEmpty() || bio.isEmpty() || availability.isEmpty() || experienceStr.isEmpty() || phoneNumberStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        int experience = Integer.parseInt(experienceStr);
        int phoneNumber = Integer.parseInt(phoneNumberStr);
        String babysitterId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (selectedImageUri != null) {
            FirebaseStorageService.uploadProfilePicture(this, new Intent().setData(selectedImageUri), uri -> {
                String profilePictureURL = uri.toString();
                saveProfile(babysitterId, name, age, region, bio, availability, experience, phoneNumber, profilePictureURL);
            });
        } else {
            saveProfile(babysitterId, name, age, region, bio, availability, experience, phoneNumber, "");
        }
    }

    private void saveProfile(String babysitterId, String name, int age, String region, String bio, String availability, int experience, int phoneNumber, String profilePictureURL) {
        Babysitter babysitter = new Babysitter(name, age, region, bio, availability, experience, phoneNumber, profilePictureURL);
        FirestoreService.saveBabysitterProfile(babysitter, babysitterId, success -> {
            if (success) {
                Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
