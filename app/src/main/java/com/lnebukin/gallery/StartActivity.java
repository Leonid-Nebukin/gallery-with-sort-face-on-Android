package com.lnebukin.gallery;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.Picture.ComparePicture;
import com.Picture.Picture;
import com.Picture.PictureAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    final int Columns = 5;
    ArrayList<Picture> myPicMass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();


        setContentView(R.layout.activity_start);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        SearchPicture searchPicture = new SearchPicture();
        myPicMass = searchPicture.SearcPic();

        DetectTableLayoutDim detectTableLayoutDim = new DetectTableLayoutDim();

        final int widthCeil = detectTableLayoutDim.getWidthCeil() / Columns;

        View.OnClickListener oclImageOk = new View.OnClickListener() {//TODO init listener in Tablelayout
            @Override
            public void onClick(View v) {
                v.setTransitionName("TransitionName");
                int pos = v.getId();
                ImageView imageView = findViewById(pos);
                //imageView.setTransitionName("TransitionName");
                //imageView.setImageBitmap(BitmapFactory.decodeFile(myPicMass.get(pos).getMyPath()));


                Intent intent = new Intent(StartActivity.this, Photo_FullSize.class);

                Pair[] pairs = new Pair[1];

                pairs[0] = new Pair<View, String> (v, "TransitionName");

                Bundle bundle = new Bundle();
                bundle.putString("num", myPicMass.get(pos).getMyPath());
                intent.putExtra("key", bundle);

                //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this, pairs);

                startActivity(intent/*, options.toBundle()*/);

            }
        };

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new PictureAdapter(this, myPicMass, oclImageOk, widthCeil - 10));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mytoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sort_by_face) {
            onSortFaceClick(myPicMass);
        } else if (item.getItemId() == R.id.sort_by_name) {
            onSortNameClick(myPicMass);
        } else if (item.getItemId() == R.id.sort_by_date) {
            onSortDateClick(myPicMass);
        }
        return true;
    }

    private void onSortNameClick(ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                return o1.getMyPath().compareTo(o2.getMyPath());
            }
        });
        for (int i = 0; i < aPicMass.size(); i ++) {
            ImageView imageView = (ImageView) findViewById(i);
            imageView.setImageBitmap(aPicMass.get(i).getMyImage());

            aPicMass.get(i).setIdImageView(i);
        }
    }

    private void onSortDateClick (ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                File file1 = new File(o1.getMyPath());
                File file2 = new File(o2.getMyPath());

                return Long.compare(file1.lastModified(), file2.lastModified());
            }
        });
        for (int i = 0; i < aPicMass.size(); i ++) {
            ImageView imageView = (ImageView) findViewById(i);
            imageView.setImageBitmap(aPicMass.get(i).getMyImage());

            aPicMass.get(i).setIdImageView(i);
        }
    }

    private void onSortFaceClick(final ArrayList<Picture> aPicMass) {
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

                                for (Picture x : aPicMass) {
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

    private void checkPermissions() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(StartActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, permissionStatus);
        }
    }

}
