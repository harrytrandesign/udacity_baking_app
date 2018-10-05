package com.htdwps.bakingappudacityproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htdwps.bakingappudacityproject.R;
import com.htdwps.bakingappudacityproject.models.Step;
import com.htdwps.bakingappudacityproject.viewholder.RecipeStepsViewHolder;

import java.util.List;

/**
 * Created by HTDWPS on 9/14/18.
 */
public class StepsCardAdapter extends RecyclerView.Adapter<RecipeStepsViewHolder> {

    private Context mContext;
    private List<Step> stepList;
    private LayoutInflater mInflater;
    private final OnItemClickListener listener;

    public StepsCardAdapter(Context mContext, List<Step> stepList, OnItemClickListener listener) {
        this.mContext = mContext;
        this.stepList = stepList;
        this.mInflater = LayoutInflater.from(mContext);
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Step step);
    }

    @Override
    public RecipeStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_recipe_step_card, parent, false);

        return new RecipeStepsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeStepsViewHolder holder, int position) {

        RecipeStepsViewHolder recipeStepsViewHolder = holder;
        recipeStepsViewHolder.bind(mContext, stepList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        if (stepList != null) {
            return stepList.size();
        } else {
            return 0;
        }
    }

}
