package com.npdevs.healthcastle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
	private Button about;
	private Button login;
	private EditText mobNumber;
	private EditText password;
	private Button signup;
	private TextInputLayout textInputLayout1;
	private TextInputLayout textInputLayout2;
	private ProgressDialog progressDialog;
	private String mobNo;
	private String pswd;
	private String loggedIn;
	private Button btnLogin;
	private Button btnProfile;
	private FirebaseAuth mAuth;

	protected void onCreate(Bundle savedInstanceState) {
		FirebaseApp.initializeApp(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);

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
	public void OnMyLoginClick(View v) {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		//intent.putExtra("info", "No66778899");
		MainActivity.this.startActivity(intent);
	}
	public void OnMyProfileClick(View v) {
		Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
		//intent.putExtra("info", "No66778899");
		MainActivity.this.startActivity(intent);
	}

	/**
	 * Jump to reset password interface
	 * @param v
	 */

}


