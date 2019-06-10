package com.FaceRecognition;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.Picture.ComparePicture;
import com.Picture.Picture;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BackgroundEmbiding extends AsyncTask<String, EmbedingVec, Void> {
    Facenet facenet;
    Context context;
    InternalFileBackground internalFileBackground;
    public BackgroundEmbiding(AssetManager assetManager, Context context) {
         facenet = new Facenet(assetManager);
         this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        internalFileBackground = new InternalFileBackground("embiding.csv", context);
    }


    @Override
    protected Void doInBackground(String... strings) {
        final FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();

        for (final String str : strings) {
            Bitmap bitmap = BitmapFactory.decodeFile(str);
            final Bitmap mbitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

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

        return null;
    }
}