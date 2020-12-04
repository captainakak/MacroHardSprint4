package com.npdevs.healthcastle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

public class SugarMeasure extends AppCompatActivity {
    public static String BaseUrl = "https://api.spoonacular.com/mealplanner/generate?apiKey=97a22756a8984567a6878b69d68bb191&timeFrame=day&targetCalories=1200";
    public static String AppId = "1e5fe32ece3a902a42b6fa8319712ddc";

    private TextView recipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_measure);
        recipeData = findViewById(R.id.textView18);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService service = retrofit.create(RecipeService.class);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Call<RecipeResponse> call = service.getRecipeData();
        System.out.println(call);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeResponse> call, @NonNull Response<RecipeResponse> response) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                if (response.code() == 200) {
                    RecipeResponse weatherResponse = response.body();
                    assert weatherResponse != null;

//					String stringBuilder =
//							"Recipe: \n" +
//									weatherResponse.meals.toString();
                    StringBuilder sb = new StringBuilder();
//					for (Meals m :weatherResponse.meals){
//						sb.append("============ \n");
//						sb.append(m.toString());
//					}

                    sb.append("BREAKFAST\n");
                    sb.append(weatherResponse.meals.get(0).toString());
                    sb.append("======================== \n  \n");
                    sb.append("LUNCH\n");
                    sb.append(weatherResponse.meals.get(1).toString());
                    sb.append("========================\n \n");
                    sb.append("DINNER\n");
                    sb.append(weatherResponse.meals.get(2).toString());


                    String stringBuilder = sb.toString();
                    recipeData.setText(stringBuilder);
                    System.out.println(stringBuilder);
                    System.out.println("***************************************");
                    Log.i("recipeData", stringBuilder);
                }

            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                recipeData.setText(t.getMessage());
            }
        });
    }
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_sugar_measure);
//		recipeData = findViewById(R.id.textView18);
//
//		Retrofit retrofit = new Retrofit.Builder()
//				.baseUrl("https://api.spoonacular.com/")
//				.addConverterFactory(GsonConverterFactory.create())
//				.build();
//
//		RecipeService service = retrofit.create(RecipeService.class);
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//		Call<RecipeResponse> call = service.getRecipeData();
//		System.out.println(call);
//
//		call.enqueue(new Callback<BNResponse>() {
//			@Override
//			public void onResponse(@NonNull Call<BNResponse> call, @NonNull Response<BNResponse> response) {
//				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//				if (response.code() == 200) {
//					BNResponse weatherResponse = response.body();
//					assert weatherResponse != null;
//
//					String stringBuilder =
//							"Recipe: " +
//									weatherResponse.meals.toString();
//
//					recipeData.setText(stringBuilder);
//					System.out.println(stringBuilder);
//					System.out.println("***************************************");
//					Log.i("recipeData", stringBuilder);
//				}
//
//			}
//
//			@Override
//			public void onFailure(Call<BNResponse> call, Throwable t) {
//				recipeData.setText(t.getMessage());
//			}
//		});
//	}
}