package com.sahanmw.roadsafetyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.OAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText inputUsername, inputPassword;
    private Button btnLogin, btnSignUp, btnGoogleLogin, btnFacebookLogin, btnPoliceAdmin;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.bttnlogin);
        View btnForgotPassword = findViewById(R.id.forgotPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoogleLogin = findViewById(R.id.btnGoogle);
        btnFacebookLogin = findViewById(R.id.btnFacebook);
        btnPoliceAdmin = findViewById(R.id.btnPoliceAdmin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    inputUsername.setError("Username is required");
                    inputUsername.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    inputPassword.setError("Password is required");
                    inputPassword.requestFocus();
                    return;
                }

                // Query Firestore by the 'username' field
                Query query = db.collection("users").whereEqualTo("username", username);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                String storedPassword = document.getString("password");

                                if (storedPassword != null && storedPassword.equals(password)) {
                                    // Password is correct, log in the user
                                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, UserHomePageActivity.class));
                                    finish();
                                } else {
                                    // Incorrect password
                                    Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Username does not exist
                                Toast.makeText(LoginActivity.this, "Username does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error occurred while retrieving data
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                            Toast.makeText(LoginActivity.this, "Error checking username!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement Google Login functionality
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("google.com");
                signInWithProvider(provider);
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement Facebook Login functionality
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("facebook.com");
                signInWithProvider(provider);
            }
        });

        btnPoliceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Police Department Admin page
                startActivity(new Intent(LoginActivity.this, AdminLoginPageActivity.class));
            }
        });
    }

    private void signInWithProvider(OAuthProvider.Builder provider) {
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for the user.
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> Toast.makeText(LoginActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(
                            e -> Toast.makeText(LoginActivity.this, "Failed to sign in!", Toast.LENGTH_SHORT).show());
        } else {
            mAuth.startActivityForSignInWithProvider(/* activity= */ LoginActivity.this, provider.build())
                    .addOnSuccessListener(
                            authResult -> Toast.makeText(LoginActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(
                            e -> Toast.makeText(LoginActivity.this, "Failed to sign in!", Toast.LENGTH_SHORT).show());
        }
    }
}
