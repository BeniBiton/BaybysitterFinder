package com.example.babysitterfinder.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Family;
import com.example.babysitterfinder.services.FirestoreService;


public class FamilyProfileActivity extends AppCompatActivity {
    private EditText editTextFamilyName, editTextNumberOfChildren, editTextChildrenAges, editTextFamilyDescription, editTextRegion;

    private Button buttonSubmitFamilyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_profile);

        editTextFamilyName = findViewById(R.id.editTextFamilyName);
        editTextNumberOfChildren = findViewById(R.id.editTextNumberOfChildrens);
        editTextChildrenAges = findViewById(R.id.editTextChildrenAges);
        editTextFamilyDescription = findViewById(R.id.editTextFamilyDescription);
        editTextRegion = findViewById(R.id.editTextRegion);
        buttonSubmitFamilyProfile = findViewById(R.id.buttonSubmitFamilyProfile);

        buttonSubmitFamilyProfile.setOnClickListener(view -> submitProfile());
    }

    private void submitProfile(){
        String familyName = editTextFamilyName.getText().toString();
        String numOfChildrenStr = editTextNumberOfChildren.getText().toString();
        String childrenAges = editTextChildrenAges.getText().toString();
        String description = editTextFamilyDescription.getText().toString();
        String region = editTextRegion.getText().toString();

        // have to make validation

        int numberOfChildren = Integer.parseInt(numOfChildrenStr);

        Family family = new Family(familyName, numberOfChildren, childrenAges, description, region);
        FirestoreService.saveFamilyProfile(family, success -> {
            if (success){
                Toast.makeText(this, "profile saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
