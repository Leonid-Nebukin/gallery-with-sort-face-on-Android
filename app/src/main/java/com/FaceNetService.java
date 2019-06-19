package com;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.FaceRecognition.BackgroundEmbiding;
import com.FaceRecognition.InternalFileBackground;
import com.Picture.Picture;
import com.Picture.SearchPicture;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class FaceNetService extends Service {
    public FaceNetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        File internalStorageDir = getFilesDir();
        File alice = new File(internalStorageDir, "embiding.csv");
        String[] myPaths = intent.getExtras().getStringArray("Picture");
        if (!alice.isFile()) {
            BackgroundEmbiding backgroundEmbiding = new BackgroundEmbiding(getAssets(), getFilesDir());

            backgroundEmbiding.execute(myPaths);
        } else {
            InternalFileBackground internalFileBackground = new InternalFileBackground("embiding.csv", getFilesDir());
            Map<String, ArrayList<Float[]>> embedingMap = internalFileBackground.loadFaces();


            ArrayList<String> srt = new ArrayList<>();
            for (String path : myPaths) {
                if (!embedingMap.containsKey(path)) {
                   srt.add(path);
                }
            }

            String[] aNewPath = new String[srt.size()];
            for (int i = 0; i < aNewPath.length; ++i) {
                aNewPath[i] = srt.get(i);
            }
            BackgroundEmbiding backgroundEmbiding = new BackgroundEmbiding(getAssets(), getFilesDir());
            backgroundEmbiding.execute(aNewPath);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
