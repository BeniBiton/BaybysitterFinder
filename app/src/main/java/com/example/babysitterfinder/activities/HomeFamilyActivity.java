package com.example.babysitterfinder.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.adapters.BabysitterAdapter;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.services.FirestoreService;

import java.util.ArrayList;
import java.util.List;

public class HomeFamilyActivity extends AppCompatActivity {

    private RecyclerView babysitterRecycleView;
    private BabysitterAdapter babysitterAdapter;
    private List<Babysitter> babysitterList;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home_family);

        babysitterRecycleView = findViewById(R.id.recyclerViewBabysitters);
        babysitterRecycleView.setLayoutManager(new LinearLayoutManager(this));

        babysitterList = new ArrayList<>();
        babysitterAdapter = new BabysitterAdapter(babysitterList, this);
        babysitterRecycleView.setAdapter(babysitterAdapter);

        fetchBabysitters();
    }

    private void fetchBabysitters(){
        FirestoreService firestoreService = new FirestoreService();

    }
}
