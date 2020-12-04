package com.npdevs.healthcastle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class FrontActivity2 extends AppCompatActivity {
	private static boolean FIRST_TIME = true;
	boolean running = false;
	String[] activites = new String[]{"Weight Lifting: general", "Weight Lifting: vigorous", "Bicycling, Stationary: moderate", "Rowing, Stationary: moderate", "Bicycling, Stationary: vigorous", "Dancing: slow, waltz, foxtrot", "Volleyball: non-competitive, general play", "Walking: 3.5 mph", "Dancing: disco, ballroom, square", "Soccer: general", "Tennis: general", "Swimming: backstroke", "Running: 5.2 mph", "Bicycling: 14-15.9 mph", "Digging", "Chopping & splitting wood", "Sleeping", "Cooking", "Auto Repair", "Paint house: outside", "Computer Work", "Welding", "Coaching Sports", "Sitting in Class"};
	int[] calories1 = new int[]{112, 223, 260, 260, 391, 112, 112, 149, 205, 260, 260, 298, 335, 372, 186, 223, 23, 93, 112, 186, 51, 112, 149, 65};
	ImageView iv_image;
	SurfaceView sv;
	SurfaceHolder sHolder;
	private TextView maxCalorie, consumedCalorie, burntCalorie, allowedCalorie, steps;
	private Button checkSafe, addFood, addExercise, takePhoto;
	private DatabaseHelper databaseHelper;
	private DatabaseHelper2 databaseHelper2;
	private TextToSpeech textToSpeech;
	private String MOB_NUMBER;
	private String[] categorties = new String[]{"Whole Milk", "Paneer (Whole Milk)", "Butter", "Ghee", "Apple", "Banana", "Grapes", "Mango", "Musambi", "Orange", "Cooked Cereal", "Rice Cooked", "Chapatti", "Potato", "Dal", "Mixed Vegetables", "Fish", "Mutton", "Egg", "Biscuit (Sweet)", "Cake (Plain)", "Cake (Rich Chocolate)", "Dosa (Plain)", "Dosa (Masala)", "Pakoras", "Puri", "Samosa", "Vada (Medu)", "Biryani (Mutton)", "Biryani (Veg.)", "Curry (Chicken)", "Curry (Veg.)", "Fried Fish", "Pulav (Veg.)", "Carrot Halwa", "Jalebi", "Kheer", "Rasgulla"};
	private int[] measure = new int[]{230, 60, 14, 15, 150, 60, 75, 100, 130, 130, 100, 25, 60, 150, 100, 150, 50, 30, 40, 15, 50, 50, 100, 100, 50, 40, 35, 40, 200, 200, 100, 100, 85, 100, 45, 20, 100, 50};
	private int[] calories = new int[]{150, 150, 45, 45, 55, 55, 55, 55, 55, 55, 80, 80, 80, 80, 80, 80, 55, 75, 75, 70, 135, 225, 135, 250, 175, 85, 140, 70, 225, 200, 225, 130, 140, 130, 165, 100, 180, 140};
	private DrawerLayout dl;
	private ActionBarDrawerToggle t;
	private NavigationView nv;
	private int age, weight, height, sex;


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (t.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);
		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");

		//checkFamilyHealth();
		FIRST_TIME = true;
		loadUserData();

		int index = getFrontCameraId();
		if (index == -1) {
			Toast.makeText(getApplicationContext(), "No front camera", Toast.LENGTH_LONG).show();
		} else {
			iv_image = (ImageView) findViewById(R.id.imageView);
			sv = (SurfaceView) findViewById(R.id.surfaceView);
			sHolder = sv.getHolder();
			sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		TextView heart = findViewById(R.id.textView12);
		heart.setText(readHeartbeat());

		dl = findViewById(R.id.activity_front);
		t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

		dl.addDrawerListener(t);
		t.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		nv = findViewById(R.id.nv);
		View headerView = nv.getHeaderView(0);
		Button editProfile = headerView.findViewById(R.id.button8);
		editProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FrontActivity2.this, ProfileActivity.class);
				intent.putExtra("MOB_NUMBER", MOB_NUMBER);
				startActivity(intent);
			}
		});
		nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				int id = item.getItemId();
				switch (id) {
//					case R.id.heartbeat:
//						//Toast.makeText(FrontActivity.this, "Click finish when satisfied!",Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(FrontActivity.this, HeartMeter.class);
//						startActivity(intent);
//						return true;
					case R.id.addfood:
						//Toast.makeText(FrontActivity.this, "Settings... who got time for that?",Toast.LENGTH_SHORT).show();
						Intent intent1 = new Intent(FrontActivity2.this, AddNewFood.class);
						startActivity(intent1);
						return true;
					case R.id.addexercise:
						//Toast.makeText(FrontActivity.this, "I won't give help!",Toast.LENGTH_SHORT).show();
						Intent intent2 = new Intent(FrontActivity2.this, AddNewExercise.class);
						startActivity(intent2);
						return true;


					case R.id.logout:
						Toast.makeText(FrontActivity2.this, "Logged out", Toast.LENGTH_SHORT).show();
						clearTable();
						//saveTable();
						Intent intent = new Intent(FrontActivity2.this, MainActivity.class);
						startActivity(intent);
						finish();
						return true;
					case R.id.contact:
						intent = new Intent(FrontActivity2.this, About.class);
						startActivity(intent);
						return true;
					case R.id.feedback:
						intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/captainakak/MacroHardSprint4"));
						startActivity(intent);
						return true;
//					case R.id.sugar:
//						intent = new Intent(FrontActivity2.this, SugarMeasure.class);
//						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
//						startActivity(intent);
//						return true;
//					case R.id.bloodpressure:
//						intent = new Intent(FrontActivity.this, BloodpressureMeasure.class);
//						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
//						startActivity(intent);
//						return true;
					default:
						return true;
				}
			}

		});



		maxCalorie = findViewById(R.id.textView30);
		consumedCalorie = findViewById(R.id.textView4);
		burntCalorie = findViewById(R.id.textView6);
		allowedCalorie = findViewById(R.id.textView8);
		steps = findViewById(R.id.textView10);
		checkSafe = findViewById(R.id.button1);
		addFood = findViewById(R.id.button2);
		addExercise = findViewById(R.id.button4);
		takePhoto = findViewById(R.id.button10);
		databaseHelper = new DatabaseHelper(this);
		Cursor res = databaseHelper.getAllData();
		consumedCalorie.setText(loadPreferences("consumed"));
		burntCalorie.setText(loadPreferences("burnt"));
		if (sex == 1) {
			double bmr = 0;
			int bmr1 = (int) bmr;
			maxCalorie.setText(bmr1 + "");
			int x = Integer.parseInt(burntCalorie.getText().toString());
			int y = Integer.parseInt(consumedCalorie.getText().toString());
			allowedCalorie.setText(bmr1 + x - y + "");
			saveTable2(bmr1 + x - y + "");
		} else {
			double bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
			bmr = bmr * 1.2;
			int bmr1 = (int) bmr;
			maxCalorie.setText(bmr1 + "");
			int x = Integer.parseInt(burntCalorie.getText().toString());
			int y = Integer.parseInt(consumedCalorie.getText().toString());
			allowedCalorie.setText(bmr1 + x - y + "");
			saveTable2(bmr1 + x - y + "");
		}
		if (res.getCount() == 0) {
			int size = categorties.length;
			for (int i = 0; i < size; i++) {
				databaseHelper.insertData(categorties[i], measure[i], calories[i]);
			}
		}
		databaseHelper2 = new DatabaseHelper2(this);
		Cursor res2 = databaseHelper2.getAllData();
		if (res2.getCount() == 0) {
			int size = activites.length;
			for (int i = 0; i < size; i++) {
				databaseHelper2.insertData(activites[i], 30, calories1[i]);
			}
			Cursor ashu = databaseHelper2.getAllData();
		}
		checkSafe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openCheckSafeActivity();
			}
		});
		addFood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAddFoodActivity();
			}
		});
		addExercise.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAddExerciseActivity();
			}
		});
		addFood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAddFoodActivity();
			}
		});
		takePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openTakePhotoActivity();
			}
		});
	}

	private void openAddExerciseActivity() {
		Intent intent = new Intent(FrontActivity2.this, AddExerciseSearch.class);
		startActivity(intent);
	}

	private void openAddFoodActivity() {
		Intent intent = new Intent(this, AddFoodSearch.class);
		startActivity(intent);
	}

	private void clearTable() {
		SharedPreferences preferences = getSharedPreferences("usersave", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private void saveTable() {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("User", "no");
		editor.apply();
	}
	private void saveTable(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("burnt", ans);
		editor.apply();
	}

	private void saveTable1(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("steps", ans);
		editor.apply();
	}

	private void saveTable2(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("allowed", ans);
		editor.apply();
	}
	private void openTakePhotoActivity() {
		Intent intent = new Intent(FrontActivity2.this, TakePhoto.class);
		startActivity(intent);
	}


	private String readHeartbeat() {
		SharedPreferences sharedPreferences = getSharedPreferences("heartbeats", MODE_PRIVATE);
		String beats = sharedPreferences.getString("beats", "no");
		if (beats.equals("") || beats.isEmpty() || beats.equals("no"))
			beats = "NaN";
		return beats;
	}

	private void openCheckSafeActivity() {
		Intent intent = new Intent(this, CheckSafeSearch.class);
		startActivity(intent);
	}

	private String loadPreferences(String whom) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		return sharedPreferences.getString(whom, "0");
	}

	private void loadUserData() {
//		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
//		String temp = sharedPreferences.getString("Age", "0");
//		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
//			temp = "0";
//		age = Integer.parseInt(temp);
//		temp = sharedPreferences.getString("Height", "0");
//		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
//			temp = "0";
//		height = Integer.parseInt(temp);
//		temp = sharedPreferences.getString("Weight", "0");
//		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
//			temp = "0";
//		weight = Integer.parseInt(temp);
//		temp = sharedPreferences.getString("Sex", "0");
//		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
//			temp = "0";
//		sex = Integer.parseInt(temp);

		age = Integer.parseInt("0");
		height = Integer.parseInt("0");
		weight = Integer.parseInt("0");
		sex = Integer.parseInt("1");
	}

	@Override
	public void onResume() {
		super.onResume();
		running = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		running = false;
	}




	@SuppressWarnings( "deprecation" )
	int getFrontCameraId() {
		Camera.CameraInfo ci = new Camera.CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, ci);
			if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
		}
		return -1; // No front-facing camera found
	}

}