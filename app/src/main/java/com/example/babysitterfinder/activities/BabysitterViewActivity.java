package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BabysitterViewActivity extends AppCompatActivity {
    private TextView viewName, viewAge, viewRegion, viewBio, viewAvailability, viewExperience, viewPhoneNumber;

    private FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
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


        firestore = FirebaseFirestore.getInstance();

        loadBabysitterProfile();
    }

    private void loadBabysitterProfile() {
        String babysitterId = getIntent().getStringExtra("BABYSITTER_ID");
        if (babysitterId == null) {
            babysitterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
    }
}
