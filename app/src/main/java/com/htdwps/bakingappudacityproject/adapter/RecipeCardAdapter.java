package com.htdwps.bakingappudacityproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htdwps.bakingappudacityproject.R;
import com.htdwps.bakingappudacityproject.models.Recipe;
import com.htdwps.bakingappudacityproject.viewholder.RecipeViewHolder;

import java.util.List;

/**
 * Created by HTDWPS on 9/11/18.
 */
public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList;
    private LayoutInflater mInflater;
    private final OnItemClickListener listener;

    public RecipeCardAdapter(Context mContext, List<Recipe> recipeList, OnItemClickListener listener) {
        this.mContext = mContext;
        this.recipeList = recipeList;
        this.mInflater = LayoutInflater.from(mContext);
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_recipe_name_card, parent, false);

        return new RecipeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
        recipeViewHolder.bind(mContext, recipeList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        if (recipeList != null) {
            return recipeList.size();
        } else {
            return 0;
        }
    }
}
