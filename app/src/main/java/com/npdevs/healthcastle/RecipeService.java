package com.npdevs.healthcastle;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mushtaq on 05-11-2018.
 */

public interface RecipeService {
    @GET("mealplanner/generate?apiKey=97a22756a8984567a6878b69d68bb191&timeFrame=day&targetCalories=1200")
    Call<RecipeResponse> getRecipeData();
}