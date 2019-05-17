package com.lnebukin.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Photo_FullSize extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__full_size);
        String path = this.getIntent().getBundleExtra("key").getString("num");
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        imageView.setId(1);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.myLinearLayout);
        linearLayout.addView(imageView);
    }

    public void onDetectClick(View view)
    {
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();
        LinearLayout imageView = findViewById(R.id.myLinearLayout);
        final ImageView im = imageView.findViewById(1);
        BitmapDrawable drawable = (BitmapDrawable) im.getDrawable();
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
                    im.setImageBitmap(markedBitMAp);
                }
            }
        });
    }

}
