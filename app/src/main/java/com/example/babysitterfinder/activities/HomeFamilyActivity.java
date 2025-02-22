package com.example.babysitterfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.adapters.BabysitterAdapter;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.services.FirestoreService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFamilyActivity extends AppCompatActivity {

    private RecyclerView babysitterRecycleView;
    private BabysitterAdapter babysitterAdapter;
    private List<Babysitter> babysitterList;
    private List<Babysitter> originalBabysitterList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home_family);

        searchView = findViewById(R.id.searchViewFamily);
        babysitterRecycleView = findViewById(R.id.recyclerViewBabysitters);
        babysitterRecycleView.setLayoutManager(new LinearLayoutManager(this));

        babysitterList = new ArrayList<>();
        originalBabysitterList = new ArrayList<>();
        babysitterAdapter = new BabysitterAdapter(babysitterList, this, babysitterId -> {
            Intent intent = new Intent(HomeFamilyActivity.this, BabysitterViewActivity.class);
            intent.putExtra("BABYSITTER_ID", babysitterId);
            startActivity(intent);
        });
        babysitterRecycleView.setAdapter(babysitterAdapter);

        fetchBabysitters();

        setupSearchView();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    return true;
                case R.id.nav_profile:
                    Intent intent = new Intent(HomeFamilyActivity.this, FamilyViewActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void fetchBabysitters() {
        FirestoreService firestoreService = new FirestoreService();
        firestoreService.getBabysitters(new FirestoreService.BabysitterCallback() {
            @Override
            public void onSuccess(List<Babysitter> babysitters) {
                if (babysitters.isEmpty()) {
                    Toast.makeText(HomeFamilyActivity.this, "No babysitters found.", Toast.LENGTH_SHORT).show();
                } else {
                    for (Babysitter babysitter : babysitters) {
                        Log.d("BabysitterInfo", "Name: " + babysitter.getName() +
                                ", Bio: " + babysitter.getBio());
                    }
                }
                originalBabysitterList.clear();
                originalBabysitterList.addAll(babysitters);

                babysitterList.clear();
                babysitterList.addAll(originalBabysitterList);
                babysitterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("HomeFamilyActivity", "Error fetching babysitters", e);
                Toast.makeText(HomeFamilyActivity.this, "Failed to load babysitters.", Toast.LENGTH_SHORT).show();
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
                filterBabysitters(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBabysitters(newText);
                return true;
            }
        });
    }

    private void filterBabysitters(String query) {
        List<Babysitter> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(originalBabysitterList);
        } else {
            for (Babysitter babysitter : originalBabysitterList) {
                if (babysitter.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(babysitter);
                }
            }
        }

        babysitterList.clear();
        babysitterList.addAll(filteredList);
        babysitterAdapter.notifyDataSetChanged();
    }


}

