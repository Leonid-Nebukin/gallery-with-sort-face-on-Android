package com.Picture;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.widget.GridView;

import com.FaceRecognition.EmbedingVec;
import com.FaceRecognition.InternalFileBackground;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.lnebukin.gallery.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortPicture {
    public void onSortNameClick(ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                return o1.getMyPath().compareTo(o2.getMyPath());
            }
        });
    }

    public void onSortFaceClick(final ArrayList<Picture> aPicMass, final AssetManager assetManager, final Context context) {
        InternalFileBackground internalFileBackground = new InternalFileBackground("embiding.csv", context);
        Map <String, ArrayList<Float[]>> embedingMap = internalFileBackground.loadFaces();
        Pair<ArrayList<ArrayList<Pair<Integer, Integer>>>, ArrayList<Integer>> compare;

        PictureHelp pictureHelp = new PictureHelp();
        for (Picture pic : aPicMass) {
            if (embedingMap.containsKey(pic.getMyPath())) {
                pic.setEmbedingVec(embedingMap.get(pic.getMyPath()));
                pic.setNumbFaces(embedingMap.get(pic.getMyPath()).size());
            } else {
                /*runOnUiThread {
                    detector.detectInImage(FirebaseVisionImage.fromBitmap(mbitmap)).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionFace>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<FirebaseVisionFace>> task) {
                            List<FirebaseVisionFace> faces = task.getResult();
                            ArrayList<Float[]> VecEmbeed = new ArrayList<Float[]>();

                            if (faces.size() > 0) {
                                for (FirebaseVisionFace face : faces) {
                                    Rect rect = new Rect(face.getBoundingBox());
                                    Bitmap markedBitMAp = (Bitmap.createBitmap(mbitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
                                    markedBitMAp = Bitmap.createScaledBitmap(markedBitMAp, 160, 160, false);
                                    VecEmbeed.add(facenet.recognizeImage(markedBitMAp));

                                }
                            }
                            internalFileBackground.WritePicture(str, faces.size(), VecEmbeed);
                        }
                    });
                }


                Bitmap bitmap = BitmapFactory.decodeFile(pic.getMyPath());
                final Bitmap mbitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
*/

                //get EMBEDING
            }
        }

        compare = pictureHelp.sorted(aPicMass);

        ArrayList<Picture> aTempPic = new ArrayList<>();

        for (Picture x : aPicMass) {
            aTempPic.add(new Picture(x.getMyPath(), x.getMyImage(), x.getNumbFaces(), x.getEmbedingVec()));
        }

        int j = 0;
        for (int i = 0; i < compare.first.size(); ++i) {
            for (int k = 0; k < compare.first.get(i).size(); ++k) {
                if (aPicMass.size() > j) {
                    aPicMass.get(j).setMyImage(aTempPic.get(compare.first.get(i).get(k).first).getMyImage());
                    aPicMass.get(j).setMyPath(aTempPic.get(compare.first.get(i).get(k).first).getMyPath());
                    aPicMass.get(j).setEmbedingVec(aTempPic.get(compare.first.get(i).get(k).first).getEmbedingVec());
                    aPicMass.get(j).setNumbFaces(aTempPic.get(compare.first.get(i).get(k).first).getNumbFaces());
                    j++;
                } else {
                    aPicMass.add(new Picture(aTempPic.get(compare.first.get(i).get(k).first).getMyPath(),
                                             aTempPic.get(compare.first.get(i).get(k).first).getMyImage(),
                                             aTempPic.get(compare.first.get(i).get(k).first).getNumbFaces(),
                                             aTempPic.get(compare.first.get(i).get(k).first).getEmbedingVec()));
                }
            }
        }
        for (int i = 0; i < compare.second.size(); ++i) {
            if (aPicMass.size() > j) {
                aPicMass.get(j).setMyImage(aTempPic.get(compare.second.get(i)).getMyImage());
                aPicMass.get(j).setMyPath(aTempPic.get(compare.second.get(i)).getMyPath());
                aPicMass.get(j).setEmbedingVec(aTempPic.get(compare.second.get(i)).getEmbedingVec());
                aPicMass.get(j).setNumbFaces(aTempPic.get(compare.second.get(i)).getNumbFaces());
                j++;
            } else {
                aPicMass.add(new Picture(aTempPic.get(compare.second.get(i)).getMyPath(),
                                         aTempPic.get(compare.second.get(i)).getMyImage(),
                                         aTempPic.get(compare.second.get(i)).getNumbFaces(),
                                         aTempPic.get(compare.second.get(i)).getEmbedingVec()));
            }
        }


/*
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
                            ArrayList<ArrayList<Pair<Integer, Float[]>>> compare = comparePicture.getCompare();


                            //PictureAdapter pictureAdapter = new PictureAdapter(context, aPicMass, oclImageOk, widthCeil - 10);
                            //GridView gridView = (GridView) findViewById(R.id.gridView);
                            //gridView.setAdapter(pictureAdapter);
                        }
                    } else {
                        faces.add((markedBitMAp).copy(Bitmap.Config.ARGB_8888, true));
                    }
                }
            });
        }*/
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
