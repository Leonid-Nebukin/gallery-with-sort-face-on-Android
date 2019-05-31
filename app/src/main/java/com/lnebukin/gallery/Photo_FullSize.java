package com.lnebukin.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Picture.Picture;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class Photo_FullSize extends AppCompatActivity implements FragmBut.OnSelectedButtonListener {
    @SuppressLint("ClickableViewAccessibility")
    Picture picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__full_size);

        String path = this.getIntent().getBundleExtra("key").getString("num");
        PhotoView img = findViewById(R.id.myImage);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        img.setImageBitmap(bitmap);
        picture = new Picture (path, bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_full_size, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.face_detect) {
            onDetectClick();
        } else if (item.getItemId() == R.id.infomation) {
            info();
        }
        return true;
    }

    private void info() {
        File file = new File(picture.getMyPath());

        Date date = new Date(file.lastModified());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        String formattedDate = sdf.format(date);

        long size = file.length();
        double size1 = size / 1024;
        size1 = size1 / 1024;

        Toast toast = Toast.makeText(Photo_FullSize.this,
                                "Path:" + file.getAbsolutePath() + "\n" +
                                        picture.getMyImage().getWidth() + "*" + picture.getMyImage().getHeight() + " " + String.valueOf(size1).substring(0, 4) + "Mb" + "\n" +
                                        "Last Modified: " + formattedDate.substring(0, formattedDate.length() - 3), Toast.LENGTH_LONG);
        toast.show();
    }

    public void onDetectClick()
    {

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();
        final ImageView imageView = findViewById(R.id.myImage);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        final Bitmap bitmap = drawable.getBitmap();

        detector.detectInImage(FirebaseVisionImage.fromBitmap(bitmap)).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionFace>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionFace>> task) {
                if (task.isSuccessful()) {
                    Bitmap markedBitMAp = (bitmap).copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(markedBitMAp);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.parseColor("#99003399"));
                    List<FirebaseVisionFace> face = task.getResult();
                    for (FirebaseVisionFace it : face) {
                        canvas.drawRect(it.getBoundingBox(), paint);
                    }
                    imageView.setImageBitmap(markedBitMAp);
                    if (face.size() == 0) {
                        Toast toast = Toast.makeText(Photo_FullSize.this, "Face not found", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    @Override
    public void onButtonSelected(int buttonIndex) {
        if (buttonIndex == 1) {
            onDetectClick();
        } else if (buttonIndex == 2) {
            info();
        }
    }
}
