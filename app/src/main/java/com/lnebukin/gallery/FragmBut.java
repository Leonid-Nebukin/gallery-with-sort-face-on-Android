package com.lnebukin.gallery;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
        Button delete = rootView.findViewById(R.id.delete);
        info.setOnClickListener(this);
        face_detect.setOnClickListener(this);
        delete.setOnClickListener(this);

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
            case R.id.delete:
                index = 3;
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

