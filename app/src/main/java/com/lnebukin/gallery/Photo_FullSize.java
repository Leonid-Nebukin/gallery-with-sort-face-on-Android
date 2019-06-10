package com.lnebukin.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.FaceRecognition.InternalFileBackground;
import com.Picture.Picture;
import com.Picture.PictureAdapter;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import uk.co.senab.photoview.PhotoView;

public class Photo_FullSize extends AppCompatActivity implements FragmBut.OnSelectedButtonListener {
    Picture picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__full_size);

        String path = this.getIntent().getExtras().getString("Image");
        PhotoView img = findViewById(R.id.myImage);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        img.setImageBitmap(bitmap);
        picture = new Picture (path, bitmap);

    }

    private void info() {
        File file = new File(picture.getMyPath());

        Date date = new Date(file.lastModified());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        String formattedDate = sdf.format(date);
        //convert to Mb
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
        final ImageView imageView = findViewById(R.id.myImage);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        final Bitmap bitmap = drawable.getBitmap();

        FirebaseVisionFaceDetector detector1 = FirebaseVision.getInstance().getVisionFaceDetector();
        detector1.detectInImage(FirebaseVisionImage.fromBitmap(bitmap)).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionFace>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionFace>> task) {
                Bitmap markedBitMAp = (bitmap).copy(Bitmap.Config.RGB_565, true);
                Canvas canvas = new Canvas(markedBitMAp);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
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
        });
    }

    private void delete (final Context context) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage("Are you sure?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                ActivityCompat.requestPermissions(Photo_FullSize.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                String[] projection = { MediaStore.Images.Media._ID };
                File file = new File(picture.getMyPath());

                String selection = MediaStore.Images.Media.DATA + " = ?";
                String[] selectionArgs = new String[] { file.getAbsolutePath() };

                Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = getContentResolver();
                Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
                if (c.moveToFirst()) {
                    long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    contentResolver.delete(deleteUri, null, null);
                }
                c.close();

                InternalFileBackground internalFileBackground = new InternalFileBackground("embiding.csv", context);
                internalFileBackground.DeletePicture(picture.getMyPath());

                Intent intent = new Intent(Photo_FullSize.this, StartActivity.class);
                startActivity(intent);
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        ad.show();

    }

    @Override
    public void onButtonSelected(int buttonIndex) {
        if (buttonIndex == 1) {
            onDetectClick();
        } else if (buttonIndex == 2) {
            info();
        } else if (buttonIndex == 3) {
            delete(this);
        }
    }
}
