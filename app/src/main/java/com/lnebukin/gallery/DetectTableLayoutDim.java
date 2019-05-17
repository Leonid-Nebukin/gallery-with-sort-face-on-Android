package com.lnebukin.gallery;

import android.util.DisplayMetrics;
import android.view.WindowManager;

class DetectTableLayoutDim {
    private DisplayMetrics myMetrics;
    public DetectTableLayoutDim () {
        myMetrics = new DisplayMetrics();
    }

    public int getRows(int length, int columns, WindowManager windowManager) {
        windowManager.getDefaultDisplay().getMetrics(myMetrics);

        return length / columns + 1;
    }

    public int getWidthCeil() {
        return myMetrics.widthPixels;
    }
}
