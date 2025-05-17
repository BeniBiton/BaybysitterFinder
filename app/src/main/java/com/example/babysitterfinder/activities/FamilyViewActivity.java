package com.example.babysitterfinder.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.API.RetrofitClientWeather;
import com.example.babysitterfinder.API.RetrofitTranslateClient;
import com.example.babysitterfinder.API.TranslationApiService;
import com.example.babysitterfinder.API.WeatherApiService;
import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Family;
import com.example.babysitterfinder.models.TranslationRequest;
import com.example.babysitterfinder.models.TranslationResponse;
import com.example.babysitterfinder.models.WeatherResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


public class FamilyViewActivity extends AppCompatActivity {
    private TextView viewName, viewNumOfChildren, viewLocation, viewChildrenAges, viewDescription, viewRegion;
    private TextView weatherDescription;
    private MaterialButton translateButton;

    private FirebaseFirestore firestore;
    private FirebaseAuth authService;
    private RatingBar ratingBar;
    private FloatingActionButton favoriteButton;
    private String familyId;
    private String currentUserId;
    private boolean isFavorite = false;

    private final String WEATHER_API_KEY = "e6aae810fa67a3b8cb473db07a8c7d87";
    private final String TRANSLATE_API_KEY = "AIzaSyDv2kfsGkKJ3sA0SuTgjAzbMvUhi11AjMI";


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
        ratingBar = findViewById(R.id.ratingBar);
        favoriteButton = findViewById(R.id.favoriteButton);
        weatherDescription = findViewById(R.id.weatherDescription);
        translateButton = findViewById(R.id.buttonTranslateDescription);

        firestore = FirebaseFirestore.getInstance();
        authService = FirebaseAuth.getInstance();
        currentUserId = authService.getCurrentUser().getUid();
        familyId = getIntent().getStringExtra("FAMILY_ID");

        loadFamilyProfile();
        checkIfFavorite();

        favoriteButton.setOnClickListener(view -> toggleFavorite(currentUserId));

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                saveRating(rating);
            }
        });

        translateButton.setOnClickListener(v -> {
            String text = viewDescription.getText().toString();
            translateDescription(text);
        });
    }

    private void loadFamilyProfile() {
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

                            if (document.contains("rating") && document.contains("ratingCount")) {
                                double rating = document.getDouble("rating");
                                long ratingCount = document.getLong("ratingCount");

                                if (ratingCount > 0) {
                                    float averageRating = (float) (rating / ratingCount);
                                    ratingBar.setRating(averageRating);
                                } else {
                                    ratingBar.setRating(0);
                                }
                            }
                        } else {
                            Toast.makeText(this, "Family profile not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to load family profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void translateDescription(String originalText) {
        TranslationRequest request = new TranslationRequest(originalText, "en", "he");
        TranslationApiService apiService = RetrofitTranslateClient.getTranslationService();

        apiService.translateText(request, TRANSLATE_API_KEY)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String translated = response.body().data.translations.get(0).translatedText;
                            viewDescription.setText(translated);
                        } else {
                            Toast.makeText(FamilyViewActivity.this, "Translation failed", Toast.LENGTH_SHORT).show();
                            Log.e("Translation", "Response error: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<TranslationResponse> call, Throwable t) {
                        Toast.makeText(FamilyViewActivity.this, "Translation error", Toast.LENGTH_SHORT).show();
                        Log.e("Translation", "Error", t);
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

        fetchWeatherByCity(family.getLocation());

    }

    private void fetchWeatherByCity(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            weatherDescription.setText("No city info for weather");
            return;
        }

        WeatherApiService apiService = RetrofitClientWeather.getWeatherApiService();
        apiService.getCurrentWeatherByCity(cityName, "metric", WEATHER_API_KEY)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            WeatherResponse weather = response.body();
                            String desc = weather.getWeather().get(0).getDescription();
                            double temp = weather.getMain().getTemp();

                            weatherDescription.setText(String.format("%s, %.1f°C", desc, temp));
                        } else {
                            weatherDescription.setText("Weather data unavailable");
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        weatherDescription.setText("Error loading weather");
                        Log.e("WeatherAPI", "Failed to fetch weather", t);
                    }
                });
    }
    private void saveRating(float newRating) {
        if (familyId == null) return;

        DocumentReference familyRef = firestore.collection("family").document(familyId);

        firestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(familyRef);
            double currentTotalRating = snapshot.contains("rating") ? snapshot.getDouble("rating") : 0;
            long currentRatingCount = snapshot.contains("ratingCount") ? snapshot.getLong("ratingCount") : 0;

            // Update rating and count
            double updatedTotalRating = currentTotalRating + newRating;
            long updatedRatingCount = currentRatingCount + 1;

            transaction.update(familyRef, "rating", updatedTotalRating);
            transaction.update(familyRef, "ratingCount", updatedRatingCount);

            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(FamilyViewActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
            loadFamilyProfile(); // Refresh rating display
        }).addOnFailureListener(e -> {
            Log.e("Rating", "Error saving rating", e);
            Toast.makeText(FamilyViewActivity.this, "Failed to submit rating", Toast.LENGTH_SHORT).show();
        });
    }


    private void checkIfFavorite() {
        if (familyId == null || currentUserId == null) return;

        firestore.collection("family").document(familyId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("favorites")) {
                        List<String> favorites = (List<String>) documentSnapshot.get("favorites");
                        if (favorites != null && favorites.contains(currentUserId)) {
                            isFavorite = true;
                            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                        } else {
                            isFavorite = false;
                            favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("FavoriteCheck", "Failed to check favorites", e));
    }

    private void toggleFavorite(String babysitterId) {
        if (babysitterId == null || familyId == null) {
            Log.e("FavoriteToggle", "familyId or currentUserId is null");
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference familyRef = firestore.collection("babysitter").document(babysitterId);

        familyRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("favorites")) {
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                if (favorites != null && favorites.contains(familyId)) {
                    familyRef.update("favorites", FieldValue.arrayRemove(familyId))
                            .addOnSuccessListener(aVoid -> {
                                isFavorite = false;
                                favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                                Toast.makeText(FamilyViewActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Log.e("FavoriteToggle", "Error removing favorite", e));
                } else {
                    familyRef.update("favorites", FieldValue.arrayUnion(familyId))
                            .addOnSuccessListener(aVoid -> {
                                isFavorite = true;
                                favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                                Toast.makeText(FamilyViewActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Log.e("FavoriteToggle", "Error adding favorite", e));
                }
            } else {
                familyRef.update("favorites", FieldValue.arrayUnion(familyId))
                        .addOnSuccessListener(aVoid -> {
                            isFavorite = true;
                            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                            Toast.makeText(FamilyViewActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Log.e("FavoriteToggle", "Error adding favorite", e));
            }
        }).addOnFailureListener(e -> Log.e("FavoriteToggle", "Error fetching family document", e));
    }

}
