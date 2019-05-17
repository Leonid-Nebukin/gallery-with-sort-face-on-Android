package com.searchPicture;

import android.provider.ContactsContract;

import com.Picture.Picture;
import com.Picture.PictureHelp;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class SearchPicture {
    private int Image_ScaledSizeDefault = 216;
    public SearchPicture () {

    }
    public ArrayList<Picture> SearcPic () {
        //String DCIMdir = Environment.DIRECTORY_DCIM;
        File f = null;
        ArrayList<Picture> aPictures = new ArrayList<>();
        //Searching
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            f = new File(sdDir, "/DCIM/Camera/");
        }
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("jpg");
            }
        });
        //Adding pic
        for (File path : matchingFiles) {
            PictureHelp pictureHelp = new PictureHelp();
            aPictures.add(new Picture(path.getPath(), pictureHelp.decodeScaledFile(path, Image_ScaledSizeDefault)));
        }

        return aPictures;
    }
}
