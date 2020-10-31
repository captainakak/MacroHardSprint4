package com.npdevs.healthcastle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mushtaq on 05-11-2018.
 */

//public class RecipeResponse {
//
////    @SerializedName("nutrients")
////    @Expose
////    public String nutrients;
//
//    @SerializedName("meals")
//    @Expose
//    public ArrayList<Meals> meals = new ArrayList<Meals>();
//
////    public String getNutrients() {
////        return nutrients;
////    }
////
////    public void setNutrients(String nutrients) {
////        this.nutrients = nutrients;
////    }
//
//    public ArrayList<Meals> getMeals() {
//        return meals;
//    }
//
//    public void setMeals(ArrayList<Meals> meals) {
//        this.meals = meals;
//    }
//}
//
//class Meals {
//    @SerializedName("id")
//    public int id;
//    @SerializedName("title")
//    public String title;
//    @SerializedName("imageType")
//    public String imageType;
//    @SerializedName("readyInMinutes")
//    public String readyInMinutes;
//    @SerializedName("sourceUrl")
//    public String sourceUrl;
//
//    @Override
//    public String toString() {
//        return "Meals:" + '\n' +
//                "Meal name/title=" + title + '\n' +
//                "readyInMinutes='" + readyInMinutes + '\n' +
//                "sourceUrl='" + sourceUrl + '\n';
//    }
//}
public class BNResponse {

//    @SerializedName("nutrients")
//    @Expose
//    public String nutrients;

    @SerializedName("meals")
    @Expose
    public ArrayList<Meals> meals = new ArrayList<Meals>();

//    public String getNutrients() {
//        return nutrients;
//    }
//
//    public void setNutrients(String nutrients) {
//        this.nutrients = nutrients;
//    }

    public ArrayList<Meals> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meals> meals) {
        this.meals = meals;
    }
}

//class Meals {
//    @SerializedName("id")
//    public int id;
//    @SerializedName("title")
//    public String title;
//    @SerializedName("imageType")
//    public String imageType;
//    @SerializedName("readyInMinutes")
//    public String readyInMinutes;
//    @SerializedName("sourceUrl")
//    public String sourceUrl;
//
//    @Override
//    public String toString() {
//        return  title ;
//    }
//}