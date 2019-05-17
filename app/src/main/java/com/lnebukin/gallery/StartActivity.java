package com.lnebukin.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.Picture.ComparePicture;
import com.Picture.Picture;
import com.Picture.PictureHelp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.searchPicture.Facenet;
import com.searchPicture.SearchPicture;

import org.tensorflow.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        checkPermissions();

        SearchPicture searchPicture = new SearchPicture();
        final ArrayList<Picture> aPicMass = searchPicture.SearcPic();

        DetectTableLayoutDim detectTableLayoutDim = new DetectTableLayoutDim();
        final int Columns = 5;
        final int Rows = detectTableLayoutDim.getRows(aPicMass.size(), Columns, getWindowManager());

        final int widthCeil = detectTableLayoutDim.getWidthCeil();

        View.OnClickListener oclImageOk = new View.OnClickListener() {//TODO init listener in Tablelayout
            @Override
            public void onClick(View v) {
                int pos = v.getId();
                Intent intent = new Intent(StartActivity.this, Photo_FullSize.class);
                Bundle bundle = new Bundle();
                bundle.putString("num", aPicMass.get(pos).getMyPath());
                intent.putExtra("key", bundle);
                startActivity(intent);
            }
        };

        View.OnClickListener onSortFaceClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();
                final ArrayList<Bitmap> faces = new ArrayList<>();

                for (int i = 0; i < aPicMass.size(); ++i) {
                    final Bitmap bit = aPicMass.get(i).getMyImage();

                    detector.detectInImage(FirebaseVisionImage.fromBitmap(bit)).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionFace>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<FirebaseVisionFace>> task) {
                            if (task.isSuccessful()) {
                                Bitmap markedBitMAp = (bit).copy(Bitmap.Config.ARGB_8888, true);
                                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                paint.setColor(Color.parseColor("#99003399"));
                                List<FirebaseVisionFace> face = task.getResult();
                                if (face.size() > 0) {
                                    Rect rect = new Rect(face.get(0).getBoundingBox());
                                    Bitmap bitmap = (Bitmap.createBitmap(markedBitMAp, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
                                    faces.add((bitmap).copy(Bitmap.Config.ARGB_8888, true));
                                    if (faces.size() == aPicMass.size()) {//TODO Fix DETECTOR
                                        ComparePicture comparePicture = new ComparePicture(faces, aPicMass, getAssets());
                                        ArrayList<ArrayList<Pair<Integer, float[]>>> compare = comparePicture.getCompare();

                                        ArrayList<Picture> aTempPic = new ArrayList<>();

                                        for (Picture x: aPicMass) {
                                            aTempPic.add(new Picture(x.getMyPath(), x.getMyImage()));
                                        }

                                        int j = 0;
                                        for (int i = 0; i < compare.size(); ++i) {
                                            for (int k = 0; k < compare.get(i).size(); ++k) {
                                                ImageView imageView = (ImageView) findViewById(j);
                                                imageView.setImageBitmap(aTempPic.get(compare.get(i).get(k).first).getMyImage());

                                                aPicMass.get(j).setMyImage(aTempPic.get(compare.get(i).get(k).first).getMyImage());
                                                aPicMass.get(j).setMyPath(aTempPic.get(compare.get(i).get(k).first).getMyPath());

                                                j++;
                                            }
                                        }
                                    }
                                } else {
                                    faces.add((markedBitMAp).copy(Bitmap.Config.ARGB_8888, true));
                                }
                            }
                        }
                    });
                }
            }
        };

        TableLayoutInitialize tableLayoutInitialize = new TableLayoutInitialize((TableLayout) findViewById(R.id.myTableLayout), Columns, Rows, (int) (widthCeil / Columns));
        tableLayoutInitialize.setTableImages(aPicMass, oclImageOk);

        Button button = (Button) findViewById(R.id.SortFace);
        button.setOnClickListener(onSortFaceClick);


    }

    private void checkPermissions() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(StartActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, permissionStatus);
        }
    }

}
