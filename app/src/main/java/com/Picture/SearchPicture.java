package com.Picture;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

public class SearchPicture extends AsyncTask<String, Picture, Void> {
    private PictureAdapter mypictureAdapter;
    private RecyclerView myGridView;
    private int widthceil;

    public SearchPicture (PictureAdapter pictureAdapter, RecyclerView theGridView, int widthCeil) {
        mypictureAdapter = pictureAdapter;
        myGridView = theGridView;
        this.widthceil = widthCeil;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... myPaths) {
        //Adding pic
        for (String path : myPaths) {
            Picture pic = new Picture(path, widthceil);
            mypictureAdapter.addNewValues(pic);
            if (mypictureAdapter.getItemCount() % 35 == 0) {
                publishProgress(pic);
            }

        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Picture... values) {
        //update grid recycler
        super.onProgressUpdate(values);
        mypictureAdapter.notifyDataSetChanged();
        myGridView.setAdapter(mypictureAdapter);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mypictureAdapter.notifyDataSetChanged();
        myGridView.setAdapter(mypictureAdapter);
    }
}
