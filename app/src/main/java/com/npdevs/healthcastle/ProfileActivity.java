package com.npdevs.healthcastle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import android.support.v7.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {


    private Button btnLogin;
    private Button btnProfile;
    private Button skipfornow;
    private FirebaseAuth mAuth;

    private String age, weight, height, sex;

    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnProfile = (Button) findViewById(R.id.btnConfirmProfile);
        skipfornow = (Button) findViewById(R.id.skipfornow);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        final TextView ageTemp = (TextView)findViewById(R.id.editAge);
        final TextView heightTemp = (TextView)findViewById(R.id.editHeight);
        final TextView weightTemp = (TextView)findViewById(R.id.editWeight);
        final TextView genderTemp = (TextView)findViewById(R.id.editGender);

        btnProfile.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        age = ageTemp.getText().toString();
                        height = heightTemp.getText().toString();
                        weight = weightTemp.getText().toString();
                        String genderT = genderTemp.getText().toString();
                        sex = (genderT == "F") ? "1" : "0";

                        Intent myIntent = new Intent(ProfileActivity.this, FrontActivity.class);
                        myIntent.putExtra("age", age);
                        myIntent.putExtra("weight", weight);
                        myIntent.putExtra("height", height);
                        myIntent.putExtra("sex", sex);
                        startActivity(myIntent);
                    }
                });

    }

    public void ProfileJump(View v) {
        Intent intent = new Intent(ProfileActivity.this, FrontActivity.class);
        //intent.putExtra("info", "No66778899");
        ProfileActivity.this.startActivity(intent);
    }

    public void Skipfornow(View v) {
        Intent intent = new Intent(ProfileActivity.this, FrontActivity2.class);
        //intent.putExtra("info", "No66778899");
        ProfileActivity.this.startActivity(intent);
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