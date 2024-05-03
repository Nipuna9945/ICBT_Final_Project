package com.example.garbagedisposal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.graphics.ColorMatrix;

import com.example.garbagedisposal.databinding.ActivityGarbageScanBinding;
import com.example.garbagedisposal.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GarbageScan extends AppCompatActivity {
    String predictionResult;
    DbService dbService;
    int imageSize = 32;
    int confidentPos;
    ActivityGarbageScanBinding binding;
    String path;
    TextView typeText, resultText;
    Button howToDisposeButton;
    Button scanButton;
    Button predictButton;
    ImageView imageView;

    PredictionService predictionService;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbService.destroy();
        predictionService.disposeModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        predictionService = new PredictionService(getApplicationContext());
        super.onCreate(savedInstanceState);
        binding = ActivityGarbageScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_garbage_scan);
        howToDisposeButton = findViewById(R.id.button4);
        scanButton = findViewById(R.id.button11);
        predictButton = findViewById(R.id.button6);
        imageView = this.findViewById(R.id.imageView2);
        resultText = findViewById(R.id.textView3);
        dbService = new DbService();
        scanButton.setOnClickListener(view -> {

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 10);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });

        predictButton.setOnClickListener(view -> {
        });

        howToDisposeButton.setOnClickListener(v -> {
            @Nullable String disposalWays = dbService.getReUseMethodFromDb(predictionResult);

            Intent intent = new Intent(GarbageScan.this, Dispose.class);

            // Navigate
            intent.putExtra("WAYS_OF_DISPOSAL", disposalWays);
            startActivity(intent);
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

//                Bitmap image = toGrayscale(image);

                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                predictionResult = predictionService.predict(image);
                resultText.setText(predictionResult);

                if (!Objects.equals(predictionResult, "")){
                    howToDisposeButton.setEnabled(true);
                } else{
                    howToDisposeButton.setEnabled(false);
                }
            }
        }
    }


    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        float oneThird = 1 / 3f;
        float[] mat = new float[]{
                oneThird, oneThird, oneThird, 0, 0,
                oneThird, oneThird, oneThird, 0, 0,
                oneThird, oneThird, oneThird, 0, 0,
                0, 0, 0, 1, 0,};
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(mat);
        paint.setColorFilter(filter);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public void addGarbage() {

//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://75.119.143.175:8080/ErpNext/")
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://127.0.0.1:5000/api/predict")
                .addConverterFactory(GsonConverterFactory.create()).build();

        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<AddGarbageRes> call = apiService.addGarbage(body);
        call.enqueue(new Callback<AddGarbageRes>() {
            @Override
            public void onResponse(Call<AddGarbageRes> call, Response<AddGarbageRes> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().toString().equals("200")) {
                        Toast.makeText(getApplicationContext(), "Garbage Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "not Added", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddGarbageRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}

