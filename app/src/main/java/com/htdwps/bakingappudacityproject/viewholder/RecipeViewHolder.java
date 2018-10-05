package com.htdwps.bakingappudacityproject.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.htdwps.bakingappudacityproject.R;
import com.htdwps.bakingappudacityproject.adapter.RecipeCardAdapter;
import com.htdwps.bakingappudacityproject.models.Recipe;

/**
 * Created by HTDWPS on 9/11/18.
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private TextView recipeTextName;

    public RecipeViewHolder(View itemView) {
        super(itemView);

        recipeTextName = itemView.findViewById(R.id.tv_recipe_card_name);

    }

    public void bind(final Context context, final Recipe recipe, final RecipeCardAdapter.OnItemClickListener listener) {

        recipeTextName.setText(recipe.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(recipe);

            }
        });

    }

}
