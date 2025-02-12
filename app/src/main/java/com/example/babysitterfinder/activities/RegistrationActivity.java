package com.example.babysitterfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.services.FirebaseAuthService;
import com.example.babysitterfinder.services.FirestoreService;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup userTypeGroup;
    private Button registerButton;

    private FirebaseAuthService authService;
    private FirestoreService firestoreService;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_registration);

        authService = new FirebaseAuthService();
        firestoreService = new FirestoreService();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        userTypeGroup = findViewById(R.id.userRoleGroup);
        registerButton = findViewById(R.id.registrationButton);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        int selectedUserTypeId = userTypeGroup.getCheckedRadioButtonId();

//        if (!inputValidator.isValidEmail(email)){
//            emailEditText.setError("Invalid email");
//            return;
//        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password do not match");
            return;
        }

        RadioButton selectedUserTypeButton = findViewById(selectedUserTypeId);
        String userType = selectedUserTypeButton.getText().toString();

        authService.registerUser(email, password, task -> {
            if (task.isSuccessful()) {
                String userId = authService.getCurrentUser().getUid();
                Map<String, Object> profileData = new HashMap<>();
                profileData.put("email", email);
                profileData.put("userType", userType);

                firestoreService.saveUserProfile(userId, profileData, profileTask -> {
                    if (profileTask.isSuccessful()) {
                        navigateToHomeScreen(userType);
                    } else {
                        showError("Failed to save profile");
                    }
                });
            } else {
                showError("Registration failed: " + task.getException().getMessage());
            }
        });
    }

    private void navigateToHomeScreen(String userType) {
        Toast.makeText(this, "Navigating to " + userType + " screen", Toast.LENGTH_SHORT).show();
        Intent intent = userType.equals("Babysitter") ?
                new Intent(this, BabysitterProfileActivity.class) :
                new Intent(this, FamilyProfileActivity.class);

        startActivity(intent);
        finish();
    }

    private void showError(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }
}
