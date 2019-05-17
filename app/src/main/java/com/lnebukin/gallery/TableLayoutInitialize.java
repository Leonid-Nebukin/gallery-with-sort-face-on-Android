package com.lnebukin.gallery;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.Picture.Picture;
import com.Picture.PictureHelp;

import java.io.File;
import java.util.ArrayList;

class TableLayoutInitialize {
    private TableLayout myTableLayout;
    private int myColumns;
    private int myRows;
    private int mywidthCeil;

    TableLayoutInitialize (TableLayout tableLayout, int Columns, int Rows, int widthCeil) {
        myTableLayout = tableLayout;
        myColumns = Columns;
        myRows = Rows;
        mywidthCeil = widthCeil;
    }

    public void setTableImages(ArrayList<Picture> PictureMass, View.OnClickListener oclImageOk) {


        for (int i = 0; i < myRows; i++) {

            TableRow tableRow = new TableRow(myTableLayout.getContext());
            tableRow.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < myColumns && i * myColumns + j < PictureMass.size(); j++) {
                ImageView imageView= new ImageView(tableRow.getContext());
                imageView.setImageBitmap(PictureMass.get(i * myColumns + j).getMyImage());
                imageView.setId(i * myColumns + j);
                imageView.setOnClickListener(oclImageOk);
                tableRow.addView(imageView, j);
                PictureMass.get(i * myColumns + j).setIdImageView(i * myColumns + j);
            }

            myTableLayout.addView(tableRow, i);
        }
    }
}
