package com.htdwps.bakingappudacityproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.htdwps.bakingappudacityproject.models.Recipe;
import com.htdwps.bakingappudacityproject.models.Step;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    Recipe recipe;
    Step step;
    int position;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        toolbar = (Toolbar) findViewById(R.id.step_toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (getIntent().getExtras() != null) {

            recipe = getIntent().getParcelableExtra(StringConstantHelper.STEPS_LIST_ITEM_OBJECT_KEY);
            step = getIntent().getParcelableExtra(StringConstantHelper.STEPS_OBJECT_KEY);
            position = getIntent().getIntExtra(StringConstantHelper.STEPS_POSITION_INT_KEY, 0);

        }
            if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(StringConstantHelper.RECIPE_OBJECT_KEY, recipe);
            arguments.putParcelable(StringConstantHelper.STEPS_OBJECT_KEY, step);
            arguments.putInt(StringConstantHelper.STEPS_POSITION_INT_KEY, position);

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, StepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
