package com.lnebukin.gallery;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.FaceRecognition.BackgroundEmbiding;
import com.Picture.ImagePathProvider;
import com.Picture.Picture;
import com.Picture.PictureAdapter;
import com.Picture.SearchPicture;
import com.Picture.SortPicture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StartActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE = 1;
    final int Columns = 5;
    ArrayList<Picture> myPicMass = new ArrayList<>();
    PictureAdapter pictureAdapter = new PictureAdapter(this, myPicMass);
    RecyclerView gridView;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        setContentView(R.layout.activity_start);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        gridView = findViewById(R.id.recyclerview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, Columns);
            gridView.setLayoutManager(mGridLayoutManager);

            //Searching
            ImagePathProvider imagePathProvider = new ImagePathProvider();
            String[] myPaths = imagePathProvider.getAllImagesPath(this);

            //get width ceil
            DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
            int widthCeil = displaymetrics.widthPixels / Columns;

            //search pic in async
            SearchPicture searchPicture = new SearchPicture(pictureAdapter, gridView,  widthCeil);
            searchPicture.execute(myPaths);

            File internalStorageDir = getFilesDir();
            File alice = new File(internalStorageDir, "embiding.csv");
            if (!alice.isFile()) {
                BackgroundEmbiding backgroundEmbiding = new BackgroundEmbiding(getAssets(), this);
                backgroundEmbiding.execute(myPaths);
            }
        } else {
            Toast.makeText(StartActivity.this,
                    "Permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mytoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sort_by_face) {
            final long startTime = System.currentTimeMillis();

            SortPicture sortPicture = new SortPicture();
            sortPicture.onSortFaceClick(myPicMass, getAssets(), getApplicationContext());

            gridView.setAdapter(pictureAdapter);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            Toast toast = Toast.makeText(StartActivity.this, Long.toString(elapsedTime), Toast.LENGTH_LONG);
            toast.show();
        } else if (item.getItemId() == R.id.sort_by_name) {
            SortPicture sortPicture = new SortPicture();
            sortPicture.onSortNameClick(myPicMass);

            gridView.setAdapter(pictureAdapter);
        } else if (item.getItemId() == R.id.sort_by_date) {
            SortPicture sortPicture = new SortPicture();
            sortPicture.onSortDateClick(myPicMass);

            gridView.setAdapter(pictureAdapter);
        } else if (item.getItemId() == R.id.camera) {
            takePhoto();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Bitmap bit = BitmapFactory.decodeFile(mCurrentPhotoPath);
            Picture pic = new Picture (mCurrentPhotoPath, Bitmap.createScaledBitmap(bit, 216, 216, false));
            myPicMass.add(pic);
            AddPicToMediaStore();
            //compute vecEmbeeding
            BackgroundEmbiding backgroundEmbiding = new BackgroundEmbiding(getAssets(), this);
            backgroundEmbiding.execute(mCurrentPhotoPath);
            //update recyclerview
            gridView.setAdapter(pictureAdapter);
        }
    }

    private void AddPicToMediaStore() {
        File file = new File (mCurrentPhotoPath);
        ContentValues values = new ContentValues();
        values.put
                (MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put
                (MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert
                (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private File generateFile() {
        File file = null;
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Camera");
        file = new File(directory.getPath() + "/" + "IMG_"
                        + System.currentTimeMillis() + ".jpg");
        mCurrentPhotoPath = file.getAbsolutePath();
        return file;
    }

    Uri uri;
    private void takePhoto() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                      Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            uri = FileProvider.getUriForFile(StartActivity.this,  "com.lnebukin.gallery.file", generateFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);
    }
}
