package com.example.garbagedisposal;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.garbagedisposal.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PredictionService {
    int imageSize = 32;
    Context appContext;
    String[] wasteCategories = {"cardboard", "glass", "metal", "paper", "plastic", "trash"};
    String returnResult = "";
    Model model;

    public PredictionService(Context appContext) {
        this.appContext = appContext;
        createInstance(appContext);
    }

    private void createInstance(Context appContext) {
        try {
            model = Model.newInstance(appContext);

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    public String predict(Bitmap image) {
        if (model == null){
            createInstance(appContext);
        }

        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[imageSize * imageSize];
        image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        int pixel = 0;
        //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        for (int i = 0; i < imageSize; i++) {
            for (int j = 0; j < imageSize; j++) {
                int val = intValues[pixel++]; // RGB
                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
            }
        }

        inputFeature0.loadBuffer(byteBuffer);

        Model.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

        float[] confidences = outputFeature0.getFloatArray();

        // find the index of the class with the biggest confidence.


        returnResult = wasteCategories[Utils.getMaxPos(confidences)];



        return returnResult;
    }

    public void disposeModel(){
        model.close();
    }


}
