package com.htdwps.bakingappudacityproject.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.htdwps.bakingappudacityproject.R;
import com.htdwps.bakingappudacityproject.adapter.StepsCardAdapter;
import com.htdwps.bakingappudacityproject.models.Step;

/**
 * Created by HTDWPS on 9/14/18.
 */
public class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

    TextView description;

    public RecipeStepsViewHolder(View itemView) {
        super(itemView);

        description = itemView.findViewById(R.id.tv_recipe_description);

    }

    public void bind(final Context context, final Step step, final StepsCardAdapter.OnItemClickListener listener) {

        description.setText(step.getShortDescription());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(step);

            }
        });

    }

}
