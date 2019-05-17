package com.Picture;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnebukin.gallery.R;
import com.searchPicture.Facenet;

import java.util.ArrayList;

public class ComparePicture {
    private ArrayList<ArrayList<Pair<Integer, float[]>>> compare;

    public ComparePicture (final ArrayList<Bitmap> Faces, ArrayList<Picture> thePictures, AssetManager assetManager) {
        ArrayList<float[]> embidingVecMass = getEmbidingVecMass(Faces, assetManager);

        PictureHelp pictureHelp = new PictureHelp();
        compare = pictureHelp.sorted(thePictures, embidingVecMass);

    }
    public ArrayList<float[]> getEmbidingVecMass (final ArrayList<Bitmap> Photos, final AssetManager assetManager) {
        final ArrayList<float[]> ff2 = new ArrayList<>();
        final ArrayList<float[]> ff1 = new ArrayList<>();
        int j = 0;

        Thread t1 = new Thread() {
            @Override
            public void run() {
                super.run();
                Facenet facenet = new Facenet(assetManager);
                for (int k = 0; k < Photos.size(); k+=2) {
                    ff1.add(facenet.recognizeImage(Photos.get(k)));
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                super.run();
                Facenet facenet = new Facenet(assetManager);
                for (int k = 1; k < Photos.size(); k+=2) {
                    ff2.add(facenet.recognizeImage(Photos.get(k)));
                }
            }
        };

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (Exception ex) {

        }

        ArrayList<float[]> ff = new ArrayList<>();

        for (int i = 0; i < ff1.size() || i < ff2.size(); i ++) {
            if (i < ff1.size()) {
                ff.add(ff1.get(i));
            }
            if (i < ff2.size()) {
                ff.add(ff2.get(i));
            }
        }
        return ff;
    }

    public ArrayList<ArrayList<Pair<Integer, float[]>>> getCompare () {
        return compare;
    }
}
