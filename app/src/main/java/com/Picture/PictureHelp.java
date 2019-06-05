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

        if (bitmap.getHeight() < bitmap.getWidth()) {
            double k = bitmap.getHeight() / width;
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / k), width, false);
        } else {
            double k = bitmap.getWidth() / width;
            if ( k == 0 || bitmap == null) {
                k = 1;
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, width, (int) (bitmap.getHeight() / k), false);
        }

        Bitmap bmHalf = Bitmap.createScaledBitmap(bitmap, width,
                width, false);
        return bmHalf;
    }

    public ArrayList<ArrayList<Pair<Integer, float[]>>> sorted(ArrayList<Picture> thePictures, ArrayList<float[]> embiddingMass) {
        ArrayList<ArrayList<Pair<Integer, float[]>>> Sorted = new ArrayList<>();
        Bitmap bitK = thePictures.get(0).getMyImage();
        push(embiddingMass.get(0), bitK, Sorted, 0, 0);

        for (int k = 1; k < thePictures.size(); ++k) {
            bitK = thePictures.get(k).getMyImage();
            int j = 0;
            for (j = 0; j < Sorted.size(); j++) {
                double compare = comparePicture(embiddingMass.get(k), Sorted.get(j).get(0).second);
                if (compare < 0.484) {
                    push (embiddingMass.get(k), bitK, Sorted, j, k);
                    break;
                }
            }
            if (j == Sorted.size()) {
                push (embiddingMass.get(k), bitK, Sorted, j, k);
            }
        }
        return Sorted;

    }


    private double comparePicture(float[] ff1, float[] ff2) {
        double diff = 0;
        //compare picture using L2
        for (int i = 0; i < 128; i++) {
            diff+=(ff1[i]-ff2[i])*(ff1[i]-ff2[i]);
        }
        return Math.sqrt(diff);
    }

    private void push(float[] ff, Bitmap image, ArrayList<ArrayList<Pair<Integer,float[]>>> sorted, int j, int indexPic) {
        if (j == sorted.size()) {
            sorted.add(new ArrayList<Pair<Integer, float[]>> ());
        }
        sorted.get(j).add(new Pair<>(indexPic, ff));
    }
}
