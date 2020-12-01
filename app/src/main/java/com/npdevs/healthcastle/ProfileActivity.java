package com.npdevs.healthcastle;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import android.support.v7.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {


    private Button btnLogin;
    private Button btnProfile;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnProfile = (Button) findViewById(R.id.btnConfirmProfile);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    /**
     * login event
     * @param v
     */


    /**
     * Jump to the registration interface
     *
     * @param v
     */


    /**
     * Jump to reset password interface
     * @param v
     */

}