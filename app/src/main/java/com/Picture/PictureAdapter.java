package com.Picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lnebukin.gallery.Photo_FullSize;
import com.lnebukin.gallery.R;
import com.lnebukin.gallery.StartActivity;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private Context myContext;

    ArrayList<Picture> myPic;

    public PictureAdapter(Context context, ArrayList<Picture> Pic) {
        myContext = context;
        myPic = Pic;

    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_layout,
                parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, int position) {
        holder.mPlace.setImageBitmap(myPic.get(position).getMyImage());
        holder.mPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(myContext, Photo_FullSize.class);
                mIntent.putExtra("Image", myPic.get(holder.getAdapterPosition()).getMyPath());
                myContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPic.size();
    }

    public void addNewValues(Picture pic) {
        myPic.add(pic);
    }
}

class PlaceViewHolder extends RecyclerView.ViewHolder {

    ImageView mPlace;

    public PlaceViewHolder(View itemView) {
        super(itemView);

        mPlace = itemView.findViewById(R.id.ivPlace);
    }
}

