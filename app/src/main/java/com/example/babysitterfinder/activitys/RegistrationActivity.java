package com.example.babysitterfinder.activitys;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.services.FirebaseAuthService;
import com.example.babysitterfinder.services.FirestoreService;

public class RegistrationActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup userRoleGroup;
    private RadioButton registerButton;

    private FirebaseAuthService firebaseAuthService;
    private FirestoreService firestoreService;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_registration);
    }

}
