package com.lnebukin.gallery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.Picture.ImagePathProvider;
import com.Picture.Picture;
import com.Picture.PictureAdapter;
import com.Picture.PictureHelp;
import com.Picture.SortPicture;
import com.searchPicture.SearchPicture;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE = 1;
    final int Columns = 5;
    ArrayList<Picture> myPicMass = new ArrayList<>();

    View.OnClickListener oclImageOk = new View.OnClickListener() {//TODO init listener in Tablelayout
        @Override
        public void onClick(View v) {
            v.setTransitionName("TransitionName");
            int pos = v.getId();

            Intent intent = new Intent(StartActivity.this, Photo_FullSize.class);

            Pair[] pairs = new Pair[1];

            pairs[0] = new Pair<> (v, "TransitionName");

            Bundle bundle = new Bundle();
            bundle.putString("num", myPicMass.get(pos).getMyPath());
            intent.putExtra("key", bundle);

            startActivity(intent);

        }
    };

    int widthCeil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        setContentView(R.layout.activity_start);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        widthCeil = displaymetrics.widthPixels / Columns;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PictureAdapter pictureAdapter = new PictureAdapter(this, myPicMass, oclImageOk, widthCeil - 10);
            GridView gridView = (GridView) findViewById(R.id.gridView);

            //Searching
            ImagePathProvider imagePathProvider = new ImagePathProvider();
            String[] myPaths = imagePathProvider.getAllImagesPath(this);

            SearchPicture searchPicture = new SearchPicture(pictureAdapter, gridView, this);
            searchPicture.execute(myPaths);
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

            PictureAdapter pictureAdapter = new PictureAdapter(getApplicationContext(), myPicMass, oclImageOk, widthCeil - 10);
            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(pictureAdapter);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            Toast toast = Toast.makeText(StartActivity.this, Long.toString(elapsedTime), Toast.LENGTH_LONG);
            toast.show();
        } else if (item.getItemId() == R.id.sort_by_name) {
            SortPicture sortPicture = new SortPicture();
            sortPicture.onSortNameClick(myPicMass);

            PictureAdapter pictureAdapter = new PictureAdapter(getApplicationContext(), myPicMass, oclImageOk, widthCeil - 10);
            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(pictureAdapter);
        } else if (item.getItemId() == R.id.sort_by_date) {
            SortPicture sortPicture = new SortPicture();
            sortPicture.onSortDateClick(myPicMass);

            PictureAdapter pictureAdapter = new PictureAdapter(getApplicationContext(), myPicMass, oclImageOk, widthCeil - 10);
            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(pictureAdapter);
        } else if (item.getItemId() == R.id.camera) {
            takePhoto();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                } else {
                    Bundle bndl = intent.getExtras();
                    if (bndl != null) {
                        Object obj = intent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            FileOutputStream outStream;
                            File sdCard = new File("/storage/emulated/0/DCIM/Camera/");
                            File outFile = new File(sdCard, "1.png");

                            try {
                                outFile.createNewFile();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                                byte[] bitmapdata = bos.toByteArray();

                                FileOutputStream fos = new FileOutputStream(outFile);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private void takePhoto() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                      Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File directory = new File("/storage/emulated/0/DCIM/Camera/");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, new File(directory.getPath() + "photo_"
                + "1.png"));
        startActivityForResult(intent, 1);
    }
}
