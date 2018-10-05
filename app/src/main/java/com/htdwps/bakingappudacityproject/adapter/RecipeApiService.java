package com.htdwps.bakingappudacityproject.adapter;

import com.htdwps.bakingappudacityproject.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by HTDWPS on 9/9/18.
 */
public interface RecipeApiService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipeList();

}
