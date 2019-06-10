package com.FaceRecognition;

import java.util.ArrayList;

public class EmbedingVec {
    ArrayList<Float[]> myEmbedingVec  = new ArrayList<>();
    String myPathPic;
    private int NumbFaces;

    public String getPath () {
        return myPathPic;
    }

    public ArrayList<Float[]> getEmbedingVec() {
        return myEmbedingVec;
    }

    public EmbedingVec (String string) {
        String[] strings = string.split(" ");
        myPathPic = strings[0];
        NumbFaces = Integer.parseInt(strings[1]);
        for (int indexFace = 0; indexFace < NumbFaces; ++indexFace) {
            Float[] embiding = new Float[128];
            for (int i = 0; i < 128; ++i) {
                embiding[i] = Float.parseFloat(strings[i + 2]);
            }
            myEmbedingVec.add(embiding);
        }
    }

    public int getNumbFaces() {
        return NumbFaces;
    }
}
