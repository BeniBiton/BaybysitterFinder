package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.services.FirebaseStorageService;
import com.example.babysitterfinder.services.FirestoreService;
import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

public class BabysitterProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextAge, editTextRegion, editTextBio, editTextAvailability, editTextExperience, editTextPhoneNumber;
    private Button buttonUploadPicture, buttonSubmitProfile;
    private String profilePictureURL = "";
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


    private void submitProfile() {
        String name = editTextName.getText().toString();
        String ageStr = editTextAge.getText().toString();
        String region = editTextRegion.getText().toString();
        String bio = editTextBio.getText().toString();
        String availability = editTextAvailability.getText().toString();
        String experienceStr = editTextExperience.getText().toString();
        String phoneNumberStr = editTextPhoneNumber.getText().toString();


        int age = Integer.parseInt(ageStr);
        int experience = Integer.parseInt(experienceStr);
        int phoneNumber = Integer.parseInt(phoneNumberStr);

        String babysitterId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

    private String encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            byte[] bytes = IOUtils.toByteArray(inputStream);

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            Log.e("ImageEncoding", "Error encoding image to Base64", e);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profilePictureURL = encodeImageToBase64(imageUri);
            if (profilePictureURL != null) {
                Toast.makeText(this, "Image selected and encoded!", Toast.LENGTH_SHORT).show();
                Log.d("ImageEncoding", "Encoded image: " + profilePictureURL);
            } else {
                Toast.makeText(this, "Failed to encode image", Toast.LENGTH_SHORT).show();
                Log.e("ImageEncoding", "Failed to encode image");
            }
        }
    }

}
