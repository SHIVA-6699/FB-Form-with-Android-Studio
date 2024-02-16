package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class githubAuthPage extends AppCompatActivity {
    ImageButton githubAuthButton;
    TextInputEditText githubEmail;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_auth_page);
        githubEmail = findViewById(R.id.username);
        githubAuthButton = findViewById(R.id.githubsingup);
        githubAuthButton.setOnClickListener(v -> {
            String email = String.valueOf(githubEmail.getText());
            if (email.isEmpty()) {
                Toast.makeText(githubAuthPage.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                githubEmail.requestFocus();
                return;
            } else {
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                provider.addCustomParameter("login", email);
                List<String> scopes =
                        new ArrayList<String>() {
                            {
                                add("user:email");
                            }
                        };
                provider.setScopes(scopes);


                Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
                if (pendingResultTask != null) {
                    pendingResultTask
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {

                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(githubAuthPage.this, "Failedr to authenticate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                } else {

                    firebaseAuth
                            .startActivityForSignInWithProvider(this, provider.build())
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(githubAuthPage.this, "Success", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(githubAuthPage.this, "Faileds to authenticate", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                }
            }
        });

    }
}