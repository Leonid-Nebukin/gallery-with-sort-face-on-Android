package com.Picture;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Picture {
    private String myPath;
    private Bitmap myImage;
    private int numbFaces;
    private ArrayList<Float[]> myEmbedingVec;
    public Picture (String path, Bitmap scaledImage) {
        myPath = path;
        myImage = scaledImage;
    }

    public Picture (String path, Bitmap scaledImage, int numbFaces, ArrayList<Float[]> EmbedingVec) {
        myPath = path;
        myImage = scaledImage;
        myEmbedingVec = EmbedingVec;
        this.numbFaces = numbFaces;
    }

    public void setMyImage(Bitmap myImage) {
        this.myImage = myImage;
    }

    public void setMyPath(String thePath) {
        myPath = thePath;
    }

    public String getMyPath() {
        return myPath;
    }

    public Bitmap getMyImage() {
        return myImage.copy(Bitmap.Config.RGB_565, true);
    }

    public void setEmbedingVec(ArrayList<Float[]> myEmbedingVec) {
        this.myEmbedingVec = myEmbedingVec;
    }

    public ArrayList<Float[]> getEmbedingVec() {
        return myEmbedingVec;
    }

    public int getNumbFaces() {
        return numbFaces;
    }

    public void setNumbFaces(int numbFaces) {
        this.numbFaces = numbFaces;
    }
}
