package com.example.babysitterfinder.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthService {
    private FirebaseAuth firebaseAuth;

    public FirebaseAuthService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}
