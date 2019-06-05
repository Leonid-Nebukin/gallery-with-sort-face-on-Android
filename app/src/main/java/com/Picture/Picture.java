package com.Picture;

import android.graphics.Bitmap;

public class Picture {
    private String myPath;
    private Bitmap myImage;
    private int myidImageView;
    public Picture (String path, Bitmap scaledImage) {
        myPath = path;
        myImage = scaledImage;
    }

    public void setMyImage(Bitmap myImage) {
        this.myImage = myImage;
    }

    public void setMyPath(String thePath) {
        myPath = thePath;
    }

    public int getMyImageView() {
        return myidImageView;
    }

    public String getMyPath() {
        return myPath;
    }

    public Bitmap getMyImage() {
        return myImage.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setIdImageView(int idimageView) {
        this.myidImageView = idimageView;
    }
}
