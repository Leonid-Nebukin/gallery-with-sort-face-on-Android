package com.lnebukin.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class Photo_FullSize extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__full_size);
        String path = this.getIntent().getBundleExtra("key").getString("num");
        PhotoView img = findViewById(R.id.myImage);
        img.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void onDetectClick(View view)
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

}
