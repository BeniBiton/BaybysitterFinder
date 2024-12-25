package com.example.babysitterfinder.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.services.FirebaseAuthService;
import com.example.babysitterfinder.services.FirestoreService;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuthService authService;
    private FirestoreService firestoreService;

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.acitvity_login);

        authService = new FirebaseAuthService();
        firestoreService = new FirestoreService();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        authService.loginUser(email, password, task -> {
            if (task.isSuccessful()) {
                String userId = authService.getCurrentUser().getUid();

                firestoreService.getUserRole(userId, profileTask -> {
                    if (profileTask.isSuccessful()) {
                        String userType = profileTask.getResult().getString("userType");
                        navigateToHomeScreen(userType);
                    } else {
                        showError("Failed to retrieve profile");
                    }
                });
            } else {
                showError("Login field: " + task.getException().getMessage());
            }
        });
    }

    private void navigateToHomeScreen(String userType) {
//        Intent intent = userType.equals("Babysitter") ?
//                new Intent(this, HomeBabysitterActivity.class) :
//                new Intent(this, HomeFamilyActivity.class);
//        startActivity(intent);
        finish();
    }

    private void showError(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }
}
