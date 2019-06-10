package com.Picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.media.session.IMediaControllerCallback;
import android.util.Pair;
import android.widget.ImageView;

import com.lnebukin.gallery.R;
import com.lnebukin.gallery.StartActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PictureHelp {

    public PictureHelp () {
    }

    public Bitmap decodeScaledFile(String f, int width) {

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

    public Pair<ArrayList<ArrayList<Pair<Integer, Integer>>>, ArrayList<Integer>>  sorted (ArrayList<Picture> thePictures) {
        ArrayList<ArrayList<Pair<Integer, Integer>>> withFaces = new ArrayList<>();
        ArrayList<Integer> withoutFaces= new ArrayList<>();

        for (int indexPicture = 0; indexPicture < thePictures.size(); ++indexPicture) {
            if (thePictures.get(indexPicture).getNumbFaces() != 0) {
                for (int indexFace = 0; indexFace < thePictures.get(indexPicture).getNumbFaces(); ++indexFace) {
                    int indexGroup = 0;
                    for (; indexGroup < withFaces.size(); indexGroup++) {
                        double compare = comparePicture(thePictures.get(indexPicture).getEmbedingVec().get(indexFace),
                                                        thePictures.get(withFaces.get(indexGroup).get(0).first).getEmbedingVec().get(withFaces.get(indexGroup).get(0).second));
                        if (compare < 0.5) {
                            push(withFaces, indexGroup, indexPicture, indexFace);
                            break;
                        }
                    }
                    if (indexGroup == withFaces.size()) {
                        push(withFaces, indexGroup, indexPicture, indexFace);
                    }
                }
            } else {
                withoutFaces.add(indexPicture);
            }
        }
        return new Pair<>(withFaces, withoutFaces);
    }


    private double comparePicture(Float[] ff1, Float[] ff2) {
        double diff = 0;
        //compare picture using L2
        for (int i = 0; i < 128; i++) {
            diff+=(ff1[i]-ff2[i])*(ff1[i]-ff2[i]);
        }
        return Math.sqrt(diff);
    }

    private void push( ArrayList<ArrayList<Pair<Integer, Integer>>> sorted, int indexGroup, int indexPic, int indexFaces) {

        if (indexGroup == sorted.size()) {
            sorted.add( new ArrayList<Pair<Integer, Integer>>());
        }
        sorted.get(indexGroup).add(new Pair<>(indexPic, indexFaces));
    }
}
