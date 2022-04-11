package com.example.zsaleknorbert_restapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import com.example.zsaleknorbert_restapi.databinding.InsertActivityBinding;

public class InsertActivity extends AppCompatActivity {
    InsertActivityBinding binding;
    private String url = "http://127.0.0.1:8000/api/varos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_activity);
        binding = InsertActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.visszaGomb.setOnClickListener(view -> {
            Intent vissza = new Intent(InsertActivity.this, MainActivity.class);
            startActivity(vissza);
            finish();
        });
        binding.felvetelGomb.setOnClickListener(view -> {
            varosHozzadasa();
        });
        binding.nevTxt.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String editNev = binding.nevTxt.getText().toString().trim();
                for (int i = 0; i < ListResultActivity.cities.size(); i++) {
                    if (ListResultActivity.cities.get(i).getNev().equals(editNev)) {
                        binding.nevTxt.setTextColor(Color.RED);
                        break;
                    }
                    else {
                        binding.nevTxt.setTextColor(Color.GREEN);
                    }
                }
            }
            else {
                binding.nevTxt.setTextColor(Color.BLACK);
            }

        });
        binding.nevTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gombfeoldo();

            }
        });
        binding.orszagTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gombfeoldo();

            }
        });
        binding.lakossagTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gombfeoldo();

            }
        });
    }

    private boolean validacio(String nev, String orszag, String lakossagString) {
        if (nev.isEmpty()) {
            Toast.makeText(this, "Név megadása kötelező!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (orszag.isEmpty()) {
            Toast.makeText(this, "Ország megadása kötelező!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lakossagString.isEmpty()) {
            Toast.makeText(this, "Lakosság megadása kötelező!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void gombfeoldo() {
        if (!binding.nevTxt.getText().equals("") &&
                !binding.nevTxt.getText().equals("") &&
                !binding.lakossagTxt.getText().equals("")) {
            binding.felvetelGomb.setEnabled(true);
        }
    }
    private void alaphelyzet() {
        binding.nevTxt.setText("");
        binding.orszagTxt.setText("");
        binding.lakossagTxt.setText("");
    }


    private void varosHozzadasa() {
        String nev = binding.nevTxt.getText().toString().trim();
        String orszag = binding.orszagTxt.getText().toString().trim();
        String lakossagString = binding.lakossagTxt.getText().toString().trim();
        if (nev.isEmpty() || orszag.isEmpty() || lakossagString.isEmpty()) {
            binding.felvetelGomb.setEnabled(false);
        }
        if (!validacio(nev, orszag, lakossagString)) {
            Toast.makeText(InsertActivity.this, "Sikertelen felvétel", Toast.LENGTH_SHORT).show();
            return;
        }
        int lakossag = Integer.parseInt(lakossagString);
        Varos varos = new Varos(0, nev, orszag, lakossag);
        Toast.makeText(InsertActivity.this, "Sikeres felvétel", Toast.LENGTH_SHORT).show();
        Gson jsonConverter = new Gson();
        CityTask task = new CityTask(url, "POST", jsonConverter.toJson(varos));
        task.execute();
    }

    private class CityTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public CityTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        public CityTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.post(requestUrl, requestParams);
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(InsertActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400){
                Toast.makeText(InsertActivity.this, "Hiba történt feldolgozás közben", Toast.LENGTH_SHORT).show();
            }
            else {
                Varos varos = converter.fromJson(response.getContent(), Varos.class);
                ListResultActivity.cities.add(0, varos);
                alaphelyzet();
            }
        }
    }
}