package com.lnebukin.gallery;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;

import java.util.List;


public class FragmBut extends Fragment implements View.OnClickListener {
    View rootView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_button, container, false);

        Button face_detect = rootView.findViewById(R.id.face_detect);
        Button info = rootView.findViewById(R.id.infomation);
        info.setOnClickListener(this);
        face_detect.setOnClickListener(this);

        return rootView;
    }

    int translateIdToIndex(int id) {
        int index = -1;
        switch (id) {
            case R.id.face_detect:
                index = 1;
                break;
            case R.id.infomation:
                index = 2;

                break;
        }
        return index;
    }

    @Override
    public void onClick(View view) {
        int buttonIndex = translateIdToIndex(view.getId());


        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(buttonIndex);
    }

    public interface OnSelectedButtonListener {
        void onButtonSelected(int buttonIndex);
    }
}

