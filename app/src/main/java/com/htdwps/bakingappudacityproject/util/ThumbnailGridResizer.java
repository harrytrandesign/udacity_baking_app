package com.htdwps.bakingappudacityproject.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by HTDWPS on 9/9/18.
 */
public class ThumbnailGridResizer {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 360;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 1;
        return noOfColumns;
    }

}
