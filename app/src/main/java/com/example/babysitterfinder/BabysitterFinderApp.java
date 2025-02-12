package com.example.babysitterfinder;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class BabysitterFinderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
