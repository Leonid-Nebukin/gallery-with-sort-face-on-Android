package com.searchPicture;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.ImageView;

import com.Picture.ImagePathProvider;
import com.Picture.Picture;
import com.Picture.PictureAdapter;
import com.Picture.PictureHelp;

public class SearchPicture extends AsyncTask<String, Picture, Void> {
    private Context myContext;
    private PictureAdapter mypictureAdapter;
    private GridView myGridView;

    public SearchPicture (PictureAdapter pictureAdapter, GridView theGridView, Context context) {
        mypictureAdapter = pictureAdapter;
        myGridView = theGridView;
        myContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... myPaths) {
        //Adding pic
        for (String path : myPaths) {
            PictureHelp pictureHelp = new PictureHelp();
            Picture pic = new Picture(path, pictureHelp.decodeScaledFile(path, 216));
            mypictureAdapter.addNewValues(pic);
            //if (mypictureAdapter.getCount() % 20 == 0) {
                publishProgress(pic);
            //}

        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Picture... values) {
        super.onProgressUpdate(values);
        ImageView imageView = new ImageView(myContext);
        imageView.setImageBitmap(values[0].getMyImage());
        myGridView.addView(imageView);
        /*mypictureAdapter.notifyDataSetChanged();
        myGridView.invalidateViews();
        myGridView.setAdapter(mypictureAdapter);*/
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        /*mypictureAdapter.notifyDataSetChanged();
        myGridView.invalidateViews();
        myGridView.setAdapter(mypictureAdapter);*/
    }
}
