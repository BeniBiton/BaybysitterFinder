package com.example.babysitterfinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.adapters.FamilyAdapter;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.models.Family;
import com.example.babysitterfinder.services.FirestoreService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeBabysitterActivity extends AppCompatActivity {
    private RecyclerView familyRecyclerView;
    private FamilyAdapter familyAdapter;
    private List<Family> familyList;
    private List<Family> originalFamilyList;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home_babysitters);

        searchView = findViewById(R.id.searchViewBabysitter);
        familyRecyclerView = findViewById(R.id.recyclerViewFamilies);
        familyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        familyList = new ArrayList<>();
        originalFamilyList = new ArrayList<>();
        familyAdapter = new FamilyAdapter(familyList, this, familyId -> {
            Intent intent = new Intent(HomeBabysitterActivity.this, FamilyViewActivity.class);
            intent.putExtra("FAMILY_ID", familyId);
            startActivity(intent);
        });
        familyRecyclerView.setAdapter(familyAdapter);

        fetchFamilies();
        setupSearchView();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    return true;
                case R.id.nav_profile:
                    Intent intent = new Intent(HomeBabysitterActivity.this, BabysitterViewActivity.class);
                    ;
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void fetchFamilies() {
        FirestoreService firestoreService = new FirestoreService();
        firestoreService.getFamilies(new FirestoreService.FamilyCallback() {
            @Override
            public void onSuccess(List<Family> families) {
                Log.d("HomeBabysitterActivity", "Families fetched: " + families.size());
                if (families.size() > 1) {
                    for (Family family : families) {
                        Log.d("HomeBabysitterActivity", "Family: " + family.getFamilyName());
                    }
                } else {
                    Log.d("HomeBabysitterActivity", "Only one family fetched.");
                }
                familyList.clear();
                originalFamilyList.clear();

                familyList.addAll(families);
                originalFamilyList.addAll(families);

                familyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("HomeBabysitterActivity", "Error fetching families", e);
                Toast.makeText(HomeBabysitterActivity.this, "Failed to load families: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchView() {
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterFamilies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFamilies(newText);
                return true;
            }
        });
    }

    private void filterFamilies(String query) {
        List<Family> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(originalFamilyList);
        } else {
            for (Family family : originalFamilyList) {
                if (family.getFamilyName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(family);
                }
            }
        }

        familyList.clear();
        familyList.addAll(filteredList);
        familyAdapter.notifyDataSetChanged();
    }

}
