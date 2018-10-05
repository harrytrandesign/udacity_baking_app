package com.htdwps.bakingappudacityproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htdwps.bakingappudacityproject.adapter.RecipeApiService;
import com.htdwps.bakingappudacityproject.adapter.RecipeCardAdapter;
import com.htdwps.bakingappudacityproject.adapter.RetrofitClientManager;
import com.htdwps.bakingappudacityproject.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RecipeCardAdapter recipeCardAdapter;
    private List<Recipe> recipesList;
    private LinearLayoutManager layoutManager;
    private Parcelable savedRecyclerLayoutState;
    View recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;

//        recipesList = new ArrayList<>();
//
//        recipeCardAdapter = new RecipeCardAdapter(this, recipesList, new RecipeCardAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Recipe recipe) {
//
//            }
//        });

        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        try {

            RecipeApiService recipeApiService = RetrofitClientManager.getClient().create(RecipeApiService.class);

            Call<ArrayList<Recipe>> recipeCall;

            recipeCall = recipeApiService.getRecipeList();

            recipeCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                    try {

                        ArrayList<Recipe> recipes = response.body();

                        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(RecipeListActivity.this, recipes, mTwoPane));

//                        recyclerView.setAdapter(new RecipeCardAdapter(getApplicationContext(), recipes, new RecipeCardAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Recipe recipe) {
//
//                                Toast.makeText(RecipeListActivity.this, recipe.getName() + " ID: " + recipe.getId(), Toast.LENGTH_SHORT).show();
//
//                                Intent recipeIntent = new Intent(getBaseContext(), RecipeDetailActivity.class);
//
//                                recipeIntent.putExtra(StringConstantHelper.RECIPE_OBJECT_KEY, recipe);
//
//                                startActivity(recipeIntent);
//
//                            }
//                        }));

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

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe item = (Recipe) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, item.getName());
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.getName());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent, List<Recipe> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
            }
        }
    }

    private void restoreRecyclerViewPosition(Parcelable position) {
        layoutManager.onRestoreInstanceState(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        outState.putParcelable(StringConstantHelper.RECYCLERVIEW_STATE_STORE, layoutManager.onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore recyclerview to same position
        if (savedInstanceState != null) {

//            savedRecyclerLayoutState = savedInstanceState.getParcelable(StringConstantHelper.RECYCLERVIEW_STATE_STORE);

        }

    }

}
