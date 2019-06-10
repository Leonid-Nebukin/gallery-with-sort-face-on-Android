package com.FaceRecognition;

import android.content.Context;

import com.Picture.Picture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InternalFileBackground {
    private File file;
    Context context;
    public InternalFileBackground (String fileName, Context context) {
        this.context = context;
        File internalStorageDir = context.getFilesDir();
        file = new File(internalStorageDir, fileName);
    }


    public Map<String, ArrayList<Float[]>> loadFaces() {
        Map <String, ArrayList<Float[]>> embeding = new HashMap<>();
        try {
            FileInputStream fos = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fos));
            String strline;
            while ((strline = br.readLine()) != null) {
                EmbedingVec embedingVec = new EmbedingVec(strline);
                embeding.put(embedingVec.getPath(), embedingVec.getEmbedingVec());
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return embeding;
    }

    public void WritePicture (String path, int numbFace, ArrayList<Float[]> EmbeedingVec) {
        try {
            if (!file.isFile()) {
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            writer.print(path + " ");
            writer.print(numbFace + " ");
            for (Float[] res : EmbeedingVec) {
                for (Float f : res) {
                    writer.print(f.toString() + " ");
                }
            }
            writer.println();
            writer.close();
        } catch (Exception e) {
            e  .printStackTrace();
        }
    }

    public void DeletePicture (String path) {
        try {
            FileInputStream fos = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fos));

            File internalStorageDir = context.getFilesDir();
            File tempFile = new File(internalStorageDir, "myTempFile.txt");
            tempFile.createNewFile();

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));

            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String[] str = currentLine.split(" ");
                if (str[0].equals(path)) {
                    continue;
                }
                writer.println(currentLine);
            }
            writer.close();
            br.close();
            tempFile.renameTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
