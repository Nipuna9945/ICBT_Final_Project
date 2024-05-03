package com.example.garbagedisposal;

public class Utils {
    public static int getMaxPos(float[] confidences) {
        int maxPos = 0;
        float maxConfidence = confidences[maxPos];
        for (int i = 0; i < confidences.length; i++) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i];
                maxPos = i;
            }
        }
        return maxPos;
    }
}
