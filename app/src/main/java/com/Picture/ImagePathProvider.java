package com.Picture;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


public class ImagePathProvider {
    public ImagePathProvider(){}

    public String[] getAllImagesPath(Context context) {
        String[] listOfAllImages = getExternalImagesPath(context);
        return listOfAllImages;
    }


    private String[] getExternalImagesPath(Context context) {
        return getImagesPathFromUri(context, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private String[] getImagesPathFromUri(Context context, Uri uri) {
        Cursor cursor;
        int column_index_data;

        String absolutePathOfImage;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media._ID};

        cursor = context.getContentResolver().query(uri, projection, null, null, null);
        String[] listOfAllImages = new String[cursor.getCount()];
        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int i = 0;
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages[i] = absolutePathOfImage;
                i++;
            }
            cursor.close();
        }
        return listOfAllImages;
    }

}
