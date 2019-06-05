package com.Picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lnebukin.gallery.Photo_FullSize;
import com.lnebukin.gallery.StartActivity;

import java.util.ArrayList;

public class PictureAdapter extends BaseAdapter {
    private Context myContext;
    View.OnClickListener myImageButton;

    ArrayList<Picture> myPic;
    int width;

    public PictureAdapter (Context context, ArrayList<Picture> Pic, View.OnClickListener myImageButton, int width) {
        myContext = context;
        myPic = Pic;
        this.myImageButton = myImageButton;
        this.width = width;
    }



    public ArrayList<Picture> getMyPic() {
        return myPic;
    }

    public void addNewValues(Picture thePicture){
        this.myPic.add(thePicture);
    }

    @Override
    public int getCount() {
        return myPic.size();
    }

    @Override
    public Object getItem(int i) {
        return myPic.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(myContext);
        imageView.setImageBitmap(myPic.get(i).getMyImage());
        imageView.setMaxHeight(width);
        imageView.setMaxWidth(width);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(width, width));
        imageView.setOnClickListener(myImageButton);
        imageView.setId(i);

        return imageView;
    }
}
