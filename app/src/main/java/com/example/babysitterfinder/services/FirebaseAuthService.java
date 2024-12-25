package com.example.babysitterfinder.services;

import android.media.MediaPlayer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthService {
    private FirebaseAuth firebaseAuth;

    public FirebaseAuthService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    // this function make register to user
    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }
    // this function make sign in to app if we dosnt have already user
    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }
    // get the current user after get in
    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }
    // this function make sign out by user
    public void logoutUser(){
        firebaseAuth.signOut();
    }
}
