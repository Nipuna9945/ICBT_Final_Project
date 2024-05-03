package com.example.garbagedisposal;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;

public class UtilsTest {
    PredictionService predictionService;

    @Mock
    Context appContext;


    @Test
    public void getMaxPosTest_returns_max_when_max_is_at_last() {
        float[] confidences = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f};

        assertThat(Utils.getMaxPos(confidences)).isEqualTo(4);
    }
    @Test
    public void getMaxPosTest_returns_max_when_max_is_at_start() {
        float[] confidences = {5.0f, 2.0f, 3.0f, 4.0f, 1.0f};

        assertThat(Utils.getMaxPos(confidences)).isEqualTo(0);
    }
    @Test
    public void getMaxPosTest_returns_max_when_all_negative() {
        float[] confidences = {-5.0f, -2.0f, -3.0f, -4.0f, -1.0f};

        assertThat(Utils.getMaxPos(confidences)).isEqualTo(4);
    }
    @Test
    public void getMaxPosTest_returns_max_when_random_float_values() {
        float[] confidences = {3.5f, 1.2f, 5.7f, 4.1f, 2.9f};

        assertThat(Utils.getMaxPos(confidences)).isEqualTo(2);
    }
    @Test
    public void getMaxPosTest_returns_max_when_some_are_negative_and_max_at_middle() {
        float[] confidences = {3.0f, -2.0f, 5.0f, -4.0f, 6.0f};

        assertThat(Utils.getMaxPos(confidences)).isEqualTo(4);
    }
}