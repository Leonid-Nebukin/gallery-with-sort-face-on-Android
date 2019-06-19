package com.Picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Picture {
    private String myPath;
    private Bitmap myImage;
    private int numbFaces;
    private ArrayList<Float[]> myEmbedingVec;

    public Picture (String path, int width) {
        myPath = path;
        myImage = decodeScaledFile(path, width);
    }

    public Picture (String path, Bitmap Image) {
        myPath = path;
        myImage = Image;
    }

    public Picture (String path, int width, int numbFaces, ArrayList<Float[]> EmbedingVec) {
        myPath = path;
        myImage = decodeScaledFile(path, width);
        myEmbedingVec = EmbedingVec;
        this.numbFaces = numbFaces;
    }

    public Picture (String path, Bitmap Image, int numbFaces, ArrayList<Float[]> EmbedingVec) {
        myPath = path;
        myImage = Image;
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

    private Bitmap decodeScaledFile(String f, int width) {

        Bitmap bitmap = BitmapFactory.decodeFile(f);
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        if (bitmap.getHeight() < bitmap.getWidth()) {

            bitmap = Bitmap.createBitmap(bitmap, (w - h) / 2, 0, h, h);
        } else {

            bitmap = Bitmap.createBitmap(bitmap, 0, (h - w) / 2, w, w);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, width, width, false);
        return bitmap;
    }
}
