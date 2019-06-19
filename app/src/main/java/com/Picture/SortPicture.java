package com.Picture;

import android.content.res.AssetManager;
import android.util.Pair;

import com.FaceRecognition.InternalFileBackground;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class SortPicture {
    public void onSortNameClick(ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                return o1.getMyPath().compareTo(o2.getMyPath());
            }
        });
    }

    public void onSortFaceClick(final ArrayList<Picture> aPicMass, final AssetManager assetManager, final File internalDir) {
        InternalFileBackground internalFileBackground = new InternalFileBackground("embiding.csv", internalDir);
        Map <String, ArrayList<Float[]>> embedingMap = internalFileBackground.loadFaces();
        Pair<ArrayList<ArrayList<Pair<Integer, Integer>>>, ArrayList<Integer>> compare;

        ComparePicture comparePicture = new ComparePicture();
        for (Picture pic : aPicMass) {
            if (embedingMap.containsKey(pic.getMyPath())) {
                pic.setEmbedingVec(embedingMap.get(pic.getMyPath()));
                pic.setNumbFaces(embedingMap.get(pic.getMyPath()).size());
            } else {
            }
        }

        compare = comparePicture.sorted(aPicMass);

        ArrayList<Picture> aTempPic = new ArrayList<>();

        for (Picture x : aPicMass) {
            aTempPic.add(new Picture(x.getMyPath(), x.getMyImage(), x.getNumbFaces(), x.getEmbedingVec()));
        }

        int j = 0;
        for (int i = 0; i < compare.first.size(); ++i) {
            for (int k = 0; k < compare.first.get(i).size(); ++k) {
                if (aPicMass.size() > j) {
                    aPicMass.get(j).setMyImage(aTempPic.get(compare.first.get(i).get(k).first).getMyImage());
                    aPicMass.get(j).setMyPath(aTempPic.get(compare.first.get(i).get(k).first).getMyPath());
                    aPicMass.get(j).setEmbedingVec(aTempPic.get(compare.first.get(i).get(k).first).getEmbedingVec());
                    aPicMass.get(j).setNumbFaces(aTempPic.get(compare.first.get(i).get(k).first).getNumbFaces());
                    j++;
                } else {
                    aPicMass.add(new Picture(aTempPic.get(compare.first.get(i).get(k).first).getMyPath(),
                                             aTempPic.get(compare.first.get(i).get(k).first).getMyImage(),
                                             aTempPic.get(compare.first.get(i).get(k).first).getNumbFaces(),
                                             aTempPic.get(compare.first.get(i).get(k).first).getEmbedingVec()));
                }
            }
        }
        for (int i = 0; i < compare.second.size(); ++i) {
            if (aPicMass.size() > j) {
                aPicMass.get(j).setMyImage(aTempPic.get(compare.second.get(i)).getMyImage());
                aPicMass.get(j).setMyPath(aTempPic.get(compare.second.get(i)).getMyPath());
                aPicMass.get(j).setEmbedingVec(aTempPic.get(compare.second.get(i)).getEmbedingVec());
                aPicMass.get(j).setNumbFaces(aTempPic.get(compare.second.get(i)).getNumbFaces());
                j++;
            } else {
                aPicMass.add(new Picture(aTempPic.get(compare.second.get(i)).getMyPath(),
                                         aTempPic.get(compare.second.get(i)).getMyImage(),
                                         aTempPic.get(compare.second.get(i)).getNumbFaces(),
                                         aTempPic.get(compare.second.get(i)).getEmbedingVec()));
            }
        }
    }


    public void onSortDateClick(ArrayList<Picture> aPicMass) {
        Collections.sort(aPicMass, new Comparator<Picture>() {
            public int compare(Picture o1, Picture o2) {
                File file1 = new File(o1.getMyPath());
                File file2 = new File(o2.getMyPath());

                return Long.compare(file1.lastModified(), file2.lastModified());
            }
        });
    }
}
