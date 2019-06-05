package com.Picture;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.lnebukin.gallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortPicture {
    public void onSortNameClick(ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                return o1.getMyPath().compareTo(o2.getMyPath());
            }
        });
    }

    public void onSortFaceClick(final ArrayList<Picture> aPicMass, final AssetManager assetManager, final Context context) {
        final FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();
        final ArrayList<Bitmap> faces = new ArrayList<>();
        for (int i = 0; i < aPicMass.size(); ++i) {
            final Bitmap bit = aPicMass.get(i).getMyImage();

            Task<List<FirebaseVisionFace>> task1 = detector.detectInImage(FirebaseVisionImage.fromBitmap(bit)).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionFace>>() {
                @Override
                public void onComplete(@NonNull Task<List<FirebaseVisionFace>> task) {
                    Bitmap markedBitMAp = (bit).copy(Bitmap.Config.RGB_565, true);
                    List<FirebaseVisionFace> face = task.getResult();
                    if (face.size() > 0) {
                        Rect rect = new Rect(face.get(0).getBoundingBox());
                        Bitmap bitmap = (Bitmap.createBitmap(markedBitMAp, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
                        faces.add((bitmap).copy(Bitmap.Config.RGB_565, true));
                        if (faces.size() == aPicMass.size()) {//TODO Fix DETECTOR
                            ComparePicture comparePicture = new ComparePicture(faces, aPicMass, assetManager);
                            ArrayList<ArrayList<Pair<Integer, float[]>>> compare = comparePicture.getCompare();

                            ArrayList<Picture> aTempPic = new ArrayList<>();

                            for (Picture x : aPicMass) {
                                aTempPic.add(new Picture(x.getMyPath(), x.getMyImage()));
                            }

                            int j = 0;
                            for (int i = 0; i < compare.size(); ++i) {
                                for (int k = 0; k < compare.get(i).size(); ++k) {
                                    aPicMass.get(j).setMyImage(aTempPic.get(compare.get(i).get(k).first).getMyImage());
                                    aPicMass.get(j).setMyPath(aTempPic.get(compare.get(i).get(k).first).getMyPath());
                                    j++;
                                }
                            }

                            //PictureAdapter pictureAdapter = new PictureAdapter(context, aPicMass, oclImageOk, widthCeil - 10);
                            //GridView gridView = (GridView) findViewById(R.id.gridView);
                            //gridView.setAdapter(pictureAdapter);
                        }
                    } else {
                        faces.add((markedBitMAp).copy(Bitmap.Config.ARGB_8888, true));
                    }
                }
            });
        }
    }

    public void onSortDateClick(ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                File file1 = new File(o1.getMyPath());
                File file2 = new File(o2.getMyPath());

                return Long.compare(file1.lastModified(), file2.lastModified());
            }
        });
    }
}
