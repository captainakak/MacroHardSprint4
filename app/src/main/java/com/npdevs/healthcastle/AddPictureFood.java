package com.npdevs.healthcastle;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.npdevs.healthcastle.DatabaseHelper;
import com.npdevs.healthcastle.R;

public class AddPictureFood extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    private EditText amount;
    private Button add;
    private TextView food;
    private TextView calorieDensity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture_food);
        calorieDensity = findViewById(R.id.editText16);
        amount = findViewById(R.id.editText10);
        add = findViewById(R.id.button6);
        food = findViewById(R.id.editText9);

        final String string = getIntent().getStringExtra("Food");
        food.setText(string);
        //
        final String CD = getIntent().getStringExtra("CD");
        calorieDensity.setText(CD);

        //databaseHelper = new DatabaseHelper(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        int x = Integer.parseInt(amount.getText().toString());
                        int y = Integer.parseInt(CD);

                        int ans = (x * y) / 100;
                        String test = loadPreferences("consumed");
                        int prev = 0;
                        prev = Integer.parseInt(test);
                        ans = ans + prev;
                        clearTable();
                        saveTable(ans + "");
                        Toast.makeText(getApplicationContext(), ans + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private String loadPreferences(String whom) {
        SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
        return sharedPreferences.getString(whom, "0");
    }

    private void clearTable() {
        SharedPreferences preferences = getSharedPreferences("food", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    private void saveTable(String ans) {
        SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("consumed", ans);
        editor.apply();
    }
}