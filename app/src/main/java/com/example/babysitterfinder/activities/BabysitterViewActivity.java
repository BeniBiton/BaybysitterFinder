package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BabysitterViewActivity extends AppCompatActivity {
    private TextView viewName, viewAge, viewRegion, viewBio, viewAvailability, viewExperience, viewPhoneNumber;
    private ImageView profileImage;
    private FloatingActionButton favoriteButton;
    private RatingBar ratingBar;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String babysitterId;
    private String currentUserId;
    private boolean isFavorite = false;


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
        favoriteButton = findViewById(R.id.favoriteButton);
        ratingBar = findViewById(R.id.ratingBar);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        babysitterId = getIntent().getStringExtra("BABYSITTER_ID");


        loadBabysitterProfile();
        checkIfFavorite(currentUserId);

        favoriteButton.setOnClickListener(view -> toggleFavorite(currentUserId));

        viewPhoneNumber.setOnClickListener(v -> {
            String phoneText = viewPhoneNumber.getText().toString().replace("📞 Phone: ", "").trim();

            String phoneNumber;
            if (phoneText.startsWith("+972")) {
                phoneNumber = phoneText;
            } else if (phoneText.startsWith("0")) {
                phoneNumber = "+972" + phoneText.substring(1);
            } else {
                phoneNumber = "+972" + phoneText;
            }

            if (!phoneNumber.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(BabysitterViewActivity.this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
        });


        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                saveRating(rating);
            }
        });
    }


    private void loadBabysitterProfile() {
        if (babysitterId == null) {
            babysitterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (babysitterId == null) {
            Log.e("BabysitterViewActivity", "No valid Babysitter ID found.");
            Toast.makeText(this, "No valid Babysitter ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        firestore.collection("babysitter").document(babysitterId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Babysitter babysitter = documentSnapshot.toObject(Babysitter.class);
                        if (babysitter != null) {
                            displayBabysitterProfile(babysitter);

                            if (documentSnapshot.contains("rating") && documentSnapshot.contains("ratingCount")) {
                                double rating = documentSnapshot.getDouble("rating");
                                long ratingCount = documentSnapshot.getLong("ratingCount");

                                if (ratingCount > 0) {
                                    float averageRating = (float) (rating / ratingCount);
                                    ratingBar.setRating(averageRating);
                                } else {
                                    ratingBar.setRating(0);
                                }
                            }
                        } else {
                            Toast.makeText(this, "Babysitter profile not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
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
            Glide.with(this).load(babysitter.getProfilePictureUrl()).placeholder(R.drawable.ic_profile_placeholder).error(R.drawable.ic_profile_placeholder).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    private void checkIfFavorite(String familyId) {
        if (familyId == null || babysitterId == null) {
            Log.e("FavoriteCheck", "familyId or babysitterId is null");
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference familyRef = firestore.collection("family").document(familyId);

        familyRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("favorites")) {
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                if (favorites != null && favorites.contains(babysitterId)) {
                    isFavorite = true;
                    favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                } else {
                    isFavorite = false;
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                }
            } else {
                isFavorite = false;
                favoriteButton.setImageResource(R.drawable.ic_favorite_border);
            }
        }).addOnFailureListener(e -> Log.e("FavoriteCheck", "Failed to check favorites", e));
    }


    private void toggleFavorite(String familyId) {
        if (familyId == null || babysitterId == null) {
            Log.e("FavoriteToggle", "familyId or babysitterId is null");
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference familyRef = firestore.collection("family").document(familyId);

        familyRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("favorites")) {
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                if (favorites != null && favorites.contains(babysitterId)) {
                    familyRef.update("favorites", FieldValue.arrayRemove(babysitterId))
                            .addOnSuccessListener(aVoid -> {
                                isFavorite = false;
                                favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                                Toast.makeText(BabysitterViewActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Log.e("FavoriteToggle", "Error removing favorite", e));
                } else {
                    familyRef.update("favorites", FieldValue.arrayUnion(babysitterId))
                            .addOnSuccessListener(aVoid -> {
                                isFavorite = true;
                                favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                                Toast.makeText(BabysitterViewActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Log.e("FavoriteToggle", "Error adding favorite", e));
                }
            } else {
                familyRef.update("favorites", FieldValue.arrayUnion(babysitterId))
                        .addOnSuccessListener(aVoid -> {
                            isFavorite = true;
                            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                            Toast.makeText(BabysitterViewActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Log.e("FavoriteToggle", "Error adding favorite", e));
            }
        }).addOnFailureListener(e -> Log.e("FavoriteToggle", "Error fetching family document", e));
    }

    private void saveRating(float newRating) {
        if (babysitterId == null) return;

        DocumentReference babysitterRef = firestore.collection("babysitter").document(babysitterId);

        firestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(babysitterRef);
            double currentTotalRating = snapshot.contains("rating") ? snapshot.getDouble("rating") : 0;
            long currentRatingCount = snapshot.contains("ratingCount") ? snapshot.getLong("ratingCount") : 0;

            double updatedTotalRating = currentTotalRating + newRating;
            long updatedRatingCount = currentRatingCount + 1;

            transaction.update(babysitterRef, "rating", updatedTotalRating);
            transaction.update(babysitterRef, "ratingCount", updatedRatingCount);

            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(BabysitterViewActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
            loadBabysitterProfile(); // Refresh rating display
        }).addOnFailureListener(e -> {
            Log.e("Rating", "Error saving rating", e);
            Toast.makeText(BabysitterViewActivity.this, "Failed to submit rating", Toast.LENGTH_SHORT).show();
        });
    }
}
