package com.htdwps.bakingappudacityproject;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htdwps.bakingappudacityproject.models.Ingredient;
import com.htdwps.bakingappudacityproject.models.Recipe;
import com.htdwps.bakingappudacityproject.models.Step;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of RecipeSteps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipestep_list)
    RecyclerView stepsRecyclerView;
    @BindView(R.id.tv_recipe_name_holder)
    TextView tvRecipeName;
    @BindView(R.id.tv_recipe_ingredients_holder)
    TextView tvRecipeIngredients;

    private boolean mTwoPane;

    private List<Ingredient> listOfIngredients;
    private List<Step> listOfSteps;
    private Recipe chosenRecipe;
    private String ingredientsStringFillIn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTwoPane = findViewById(R.id.recipestep_detail_container) != null;

        grabExtrasFromBundle();

        // Display recent clicked recipe in widget
        if (savedInstanceState == null) {

            SharedPreferences sharedPreferencesWidget = this.getSharedPreferences(getString(R.string.shared_pref_widget), Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sharedPreferencesWidget.edit();
            editor.putString(StringConstantHelper.WIDGET_RECIPE_NAME, chosenRecipe.getName());
            editor.putString(StringConstantHelper.WIDGET_RECIPE_INGREDIENTS, ingredientsStringFillIn);
            editor.commit();

            // Update the widget to know a new recipe has been selected
            Intent updateWidgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            this.sendBroadcast(updateWidgetIntent);

        }

        assert stepsRecyclerView != null;
        setupRecyclerView(stepsRecyclerView);

    }

    public void grabExtrasFromBundle() {

        if (getIntent().getExtras() != null) {

            // Get the Recipe that was passed over from MainActivity
            chosenRecipe = getIntent().getParcelableExtra(StringConstantHelper.RECIPE_OBJECT_KEY);

            // Set up name views
            toolbar.setTitle(chosenRecipe.getName());
            tvRecipeName.setText(chosenRecipe.getName());

            listOfIngredients = chosenRecipe.getIngredients();
            listOfSteps = chosenRecipe.getSteps();

            condenseIngredientsToSingleString(listOfIngredients);
            tvRecipeIngredients.setText(ingredientsStringFillIn);

        }

    }

    private String condenseIngredientsToSingleString(List<Ingredient> data) {

        for (int i = 0; i < data.size(); i++) {
            Ingredient anIngredient = data.get(i);

            if (i != data.size()) {
                ingredientsStringFillIn += anIngredient.getQuantity() + " " + anIngredient.getMeasure() + " " + anIngredient.getIngredient() + "\n";
            } else {
                ingredientsStringFillIn += anIngredient.getQuantity() + " " + anIngredient.getMeasure() + " " + anIngredient.getIngredient();
            }

        }

        return ingredientsStringFillIn;

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, listOfSteps, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeStepListActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step item = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeStepDetailFragment.ARG_ITEM_ID, item.getDescription());
                    RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipestep_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_ID, item.getDescription());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeStepListActivity parent, List<Step> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_step_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getDescription());

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
                mIdView = (TextView) view.findViewById(R.id.tv_recipe_description);
            }
        }
    }
}
