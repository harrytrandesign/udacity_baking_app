package com.htdwps.bakingappudacityproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.htdwps.bakingappudacityproject.RecipeListActivity;
import com.htdwps.bakingappudacityproject.R;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        SharedPreferences sharedPreferencesForWidget = context.getSharedPreferences(context.getString(R.string.shared_pref_widget), Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        String recipeNameStored = sharedPreferencesForWidget.getString(StringConstantHelper.WIDGET_RECIPE_NAME, "");
        String recipeIngredientStored = sharedPreferencesForWidget.getString(StringConstantHelper.WIDGET_RECIPE_INGREDIENTS, "");

        views.setTextViewText(R.id.tv_widget_recipe_name, recipeNameStored);
        views.setTextViewText(R.id.tv_widget_ingredient_list, recipeIngredientStored);

        // Click Handler for clicking on the Widget
        Intent widgetIntent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetIntent, 0);
        views.setOnClickPendingIntent(R.id.ll_widget_view, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Make sure widget has the most current recipe ingredients listed based on user's recipe selection
        ComponentName componentName = new ComponentName(context.getPackageName(), IngredientWidget.class.getName());
        int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
        onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);

        super.onReceive(context, intent);
    }
}

