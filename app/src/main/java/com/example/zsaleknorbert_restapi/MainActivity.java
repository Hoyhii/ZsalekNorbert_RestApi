package com.example.zsaleknorbert_restapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.zsaleknorbert_restapi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.listaGomb.setOnClickListener(view -> {
            Intent lista = new Intent(MainActivity.this, ListResultActivity.class);
            startActivity(lista);
            finish();
        });

        binding.felvetelGomb.setOnClickListener(view -> {
            Intent felvesz = new Intent(MainActivity.this, InsertActivity.class);
            startActivity(felvesz);
            finish();
        });
}
}