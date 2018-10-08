package com.htdwps.bakingappudacityproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.htdwps.bakingappudacityproject.adapter.StepsCardAdapter;
import com.htdwps.bakingappudacityproject.models.Ingredient;
import com.htdwps.bakingappudacityproject.models.Recipe;
import com.htdwps.bakingappudacityproject.models.Step;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;

import java.util.List;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<Ingredient> ingredientsList;
    private List<Step> stepList;
    private Recipe recipe;
    private TextView tvIngredientsListed;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private String ingredientsString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        toolbar = (Toolbar) findViewById(R.id.step_toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        tvIngredientsListed = findViewById(R.id.tv_ingredients_list_text);
        if (findViewById(R.id.step_detail_container) != null) {

            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        } else {

            mTwoPane = false;

        }

        recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;

        grabBundledExtras();

//        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, stepList, mTwoPane));
    }

    public void grabBundledExtras() {

        if (getIntent().getExtras() != null) {

            recipe = getIntent().getParcelableExtra(StringConstantHelper.RECIPE_OBJECT_KEY);

            String recipeName = recipe.getName();

            toolbar.setTitle(recipeName);

            ingredientsList = recipe.getIngredients();
            stepList = recipe.getSteps();

            for (int i = 0; i < ingredientsList.size(); i++) {

                Ingredient mIngredient = ingredientsList.get(i);

                if (i != ingredientsList.size()) {
                    ingredientsString += mIngredient.getQuantity() + " " + mIngredient.getMeasure() + " " + mIngredient.getIngredient() + "\n";
                } else {
                    ingredientsString += mIngredient.getQuantity() + " " + mIngredient.getMeasure() + " " + mIngredient.getIngredient();
                }

                tvIngredientsListed.setText(ingredientsString);

            }

            recyclerView.setAdapter(new StepsCardAdapter(this, stepList, new StepsCardAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Step step, int whichStep) {

                    if (mTwoPane) {

                        Toast.makeText(StepListActivity.this, "Tablet " + whichStep, Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(StepListActivity.this, "Phone " + whichStep, Toast.LENGTH_SHORT).show();

                        Context context = view.getContext();

                        Intent intent = new Intent(context, StepDetailActivity.class);

                        intent.putExtra(StringConstantHelper.STEPS_LIST_ITEM_OBJECT_KEY, recipe);
                        intent.putExtra(StringConstantHelper.STEPS_OBJECT_KEY, step);
                        intent.putExtra(StringConstantHelper.STEPS_POSITION_INT_KEY, whichStep);

                        context.startActivity(intent);

                    }
                }
            }));


        }

    }


    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Step step = (Step) view.getTag();

                if (mTwoPane) {

                    Toast.makeText(mParentActivity, "Clicked Here Tablet Layout" + ((Step) view.getTag()).getDescription(), Toast.LENGTH_SHORT).show();

                    Bundle arguments = new Bundle();
                    arguments.putParcelable(StepDetailFragment.ARG_ITEM_ID, step);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();

                } else {

                    Toast.makeText(mParentActivity, "Clicked Here Phone Layout", Toast.LENGTH_SHORT).show();

                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);

                    intent.putExtra(StringConstantHelper.STEPS_OBJECT_KEY, step);

                    context.startActivity(intent);

                }

            }
        };

        SimpleItemRecyclerViewAdapter(StepListActivity parent, List<Step> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_content, parent, false);
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
                mIdView = (TextView) view.findViewById(R.id.id_text);
            }
        }
    }
}
