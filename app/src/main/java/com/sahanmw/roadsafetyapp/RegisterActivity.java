package com.sahanmw.roadsafetyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputID, inputPhoneNumber, inputEmail, inputDistrict, inputUsername, inputPassword, inputConfirmPassword;
    private Button btnRegister, btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        inputName = findViewById(R.id.inputName);
        inputID = findViewById(R.id.inputID);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        inputEmail = findViewById(R.id.inputEmail);
        inputDistrict = findViewById(R.id.inputDistrict);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        // Set Click Listener for Register Button
        btnRegister.setOnClickListener(v -> registerUser());

        // Set Click Listener for Login Button
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = inputName.getText().toString().trim();
        String id = inputID.getText().toString().trim();
        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        String email = inputEmail.getText().toString().trim(); // Use email for Firebase Authentication
        String district = inputDistrict.getText().toString().trim();
        String username = inputUsername.getText().toString().trim(); // Store username separately in Firestore
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        // Validate all inputs
        if (!validateInputs(name, id, email, district, username, password, confirmPassword)) {
            return; // Stop if any validation fails
        }

        // Show loading dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        // Proceed with registration
        createFirebaseUser(name, id, phoneNumber, email, district, username, password);
    }

    private boolean validateInputs(String name, String id, String email, String district, String username, String password, String confirmPassword) {
        // Validate Name
        if (name.isEmpty()) {
            inputName.setError("Name is required");
            inputName.requestFocus();
            return false;
        }

        // Validate ID
        if (id.isEmpty()) {
            inputID.setError("ID No. / Driving License is required");
            inputID.requestFocus();
            return false;
        }

        // Validate Email
        if (email.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter a valid email address");
            inputEmail.requestFocus();
            return false;
        }

        // Validate District
        if (district.isEmpty()) {
            inputDistrict.setError("District is required");
            inputDistrict.requestFocus();
            return false;
        }

        // Validate Username
        if (username.isEmpty()) {
            inputUsername.setError("Username is required");
            inputUsername.requestFocus();
            return false;
        } else if (username.length() < 4) {
            inputUsername.setError("Username must be at least 4 characters long");
            inputUsername.requestFocus();
            return false;
        }

        // Validate Password
        if (password.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            inputPassword.setError("Password must be at least 6 characters long");
            inputPassword.requestFocus();
            return false;
        }

        // Validate Confirm Password
        if (confirmPassword.isEmpty()) {
            inputConfirmPassword.setError("Confirm Password is required");
            inputConfirmPassword.requestFocus();
            return false;
        } else if (!confirmPassword.equals(password)) {
            inputConfirmPassword.setError("Passwords do not match");
            inputConfirmPassword.requestFocus();
            return false;
        }

        return true; // All validations passed
    }

    private void createFirebaseUser(String name, String id, String phoneNumber, String email, String district, String username, String password) {
        // Register user with email and password in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User registered successfully, save additional data in Firestore
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("id", id);
                            user.put("phoneNumber", phoneNumber);
                            user.put("email", email);  // Email used for authentication
                            user.put("district", district);
                            user.put("username", username);
                            user.put("password", password);

                            // Save user data in Firestore
                            db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss();
                                        // Show success message and navigate to login page
                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("Registration Successful")
                                                .setMessage("Your account has been created successfully.")
                                                .setPositiveButton("OK", (dialog, which) -> {
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                })
                                                .show();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Error saving user data", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
