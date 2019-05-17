package com.searchPicture;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class Facenet {
    private static final String MODEL_FILE  = "file:///android_asset/20180402-114759.pb";
    private static final String INPUT_NAME  = "input:0";
    private static final String OUTPUT_NAME = "embeddings:0";
    private static final String PHASE_NAME  = "phase_train:0";
    private static final String[] outputNames = new String[] {OUTPUT_NAME};
    private static final int INPUT_SIZE=160;
    private static final int SIZE = 512;
    private float[] floatValues;
    private int[] intValues;
    private TensorFlowInferenceInterface tensorFlowInferenceInterface;

    public Facenet(AssetManager assetManager){
        loadModel(assetManager);
        floatValues=new float[INPUT_SIZE*INPUT_SIZE*3];
        intValues = new int[INPUT_SIZE * INPUT_SIZE];
    }

    private void loadModel(AssetManager assetManager){
        try {
            tensorFlowInferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        }catch(Exception e){
            Log.e("Facenetd", "Facenet load error\n");
        }
    }

    //Bitmap to floatValues
    private int normalizeImage(Bitmap bitmap){

        float scale_width=((float)INPUT_SIZE)/bitmap.getWidth();
        float scale_height=((float)INPUT_SIZE)/bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale_width,scale_height);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        float imageMean=127.5f;
        float imageStd=128;
        bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        for (int i=0;i<intValues.length;i++){
            final int val=intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
        }

        return 0;
    }
    public float[] recognizeImage(final Bitmap bitmap){
        normalizeImage(bitmap);

        try {
            tensorFlowInferenceInterface.feed(INPUT_NAME, floatValues, 1, INPUT_SIZE, INPUT_SIZE, 3);
            boolean[] phase=new boolean[1];
            phase[0] = false;
            tensorFlowInferenceInterface.feed(PHASE_NAME,phase);
        }catch (Exception e){
            Log.e("Facenet","Facenet feed Error\n"+e);
            return null;
        }

        try {
            tensorFlowInferenceInterface.run(outputNames, false);
        }catch (Exception e){
            Log.e("Facenet","Facenet run error\n"+e);
            return null;
        }

        float[] faceFeature=new float[SIZE];
        try {
            tensorFlowInferenceInterface.fetch(OUTPUT_NAME, faceFeature);
        }catch (Exception e){
            Log.e("Facenet","Facenet fetch error\n"+e);
            return null;
        }
        return faceFeature;
    }
}
