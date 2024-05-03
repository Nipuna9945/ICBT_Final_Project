package com.example.garbagedisposal;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dispose extends AppCompatActivity {

    TextView disposalWaysTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispose);

        disposalWaysTextView = findViewById(R.id.textView21);

        Intent intent = getIntent();
        String disposalMethods = intent.getStringExtra("WAYS_OF_DISPOSAL");

        disposalWaysTextView.setText(disposalMethods);
    }
}
