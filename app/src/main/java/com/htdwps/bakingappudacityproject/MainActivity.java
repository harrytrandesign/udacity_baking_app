package com.htdwps.bakingappudacityproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.htdwps.bakingappudacityproject.adapter.RecipeApiService;
import com.htdwps.bakingappudacityproject.adapter.RecipeCardAdapter;
import com.htdwps.bakingappudacityproject.adapter.RetrofitClientManager;
import com.htdwps.bakingappudacityproject.models.Recipe;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;
import com.htdwps.bakingappudacityproject.util.ThumbnailGridResizer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvRecipeList;

    private RecipeCardAdapter recipeCardAdapter;
    private List<Recipe> recipesList;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private Parcelable savedRecyclerLayoutState;
    @Nullable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRecipeList = findViewById(R.id.rv_recipe_list);

        recipesList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, ThumbnailGridResizer.calculateNoOfColumns(this));

        rvRecipeList.setLayoutManager(gridLayoutManager);

        try {

            RecipeApiService recipeApiService = RetrofitClientManager.getClient().create(RecipeApiService.class);

            Call<ArrayList<Recipe>> recipeCall;

            recipeCall = recipeApiService.getRecipeList();

            recipeCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                    try {

                        ArrayList<Recipe> recipes = response.body();

                        rvRecipeList.setAdapter(new RecipeCardAdapter(getApplicationContext(), recipes, new RecipeCardAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Recipe recipe) {

                                Intent recipeIntent = new Intent(getBaseContext(), StepListActivity.class);

                                recipeIntent.putExtra(StringConstantHelper.RECIPE_OBJECT_KEY, recipe);

                                startActivity(recipeIntent);

                            }
                        }));

                        if (savedRecyclerLayoutState != null) {
                            restoreRecyclerViewPosition(savedRecyclerLayoutState);
                        }

                        Log.i("server", "Success: List size " + String.valueOf(recipes.size()));

                    } catch (NullPointerException e) {

                        Log.i("server", "error retrieving data");

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

                    Log.i("server", "Failure");
                    Log.i("server", t.getMessage());

                }
            });

        } catch (Exception e) {

            Log.i("server", e.getMessage());

        }

    }

    private void restoreRecyclerViewPosition(Parcelable position) {
        gridLayoutManager.onRestoreInstanceState(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(StringConstantHelper.RECYCLERVIEW_STATE_STORE, gridLayoutManager.onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore recyclerview to same position
        if (savedInstanceState != null) {

            savedRecyclerLayoutState = savedInstanceState.getParcelable(StringConstantHelper.RECYCLERVIEW_STATE_STORE);

        }

    }

}
