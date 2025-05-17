package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Family;
import com.example.babysitterfinder.services.FirestoreService;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class FamilyCreationActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1001;

    private EditText editTextFamilyName, editTextNumberOfChildren, editTextLocation, editTextChildrenAges, editTextFamilyDescription, editTextRegion;
    private Button buttonSubmitFamilyProfile;
    private Button buttonSelectAddress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_profile);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDw1Nf31ZtANkgcZQ-oUEuon2LnOqjptF4");
        }

        editTextFamilyName = findViewById(R.id.editTextFamilyName);
        editTextNumberOfChildren = findViewById(R.id.editTextNumberOfChildrens);
        editTextChildrenAges = findViewById(R.id.editTextChildrenAges);
        editTextLocation = findViewById(R.id.editTextLocationAddress);
        editTextFamilyDescription = findViewById(R.id.editTextFamilyDescription);
        editTextRegion = findViewById(R.id.editTextRegion);
        buttonSubmitFamilyProfile = findViewById(R.id.buttonSubmitFamilyProfile);
        buttonSelectAddress = findViewById(R.id.buttonSelectAddress);

        buttonSelectAddress.setOnClickListener(view -> openPlaceAutocomplete());

        buttonSubmitFamilyProfile.setOnClickListener(view -> submitProfile());
    }

    private void submitProfile() {
        String familyName = editTextFamilyName.getText().toString();
        String numOfChildrenStr = editTextNumberOfChildren.getText().toString();
        String location = editTextLocation.getText().toString();
        String childrenAges = editTextChildrenAges.getText().toString();
        String description = editTextFamilyDescription.getText().toString();
        String region = editTextRegion.getText().toString();


        int numberOfChildren = Integer.parseInt(numOfChildrenStr);
        String familyId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Family family = new Family(familyName, numberOfChildren, location, childrenAges, description, region);
        FirestoreService.saveFamilyProfile(family, familyId, success -> {
            if (success) {
                Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openPlaceAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
        ).build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress();
                editTextLocation.setText(address); // מציג את הכתובת בשדה
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(this, "שגיאה בבחירת כתובת", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
