package com.example.zsaleknorbert_restapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.zsaleknorbert_restapi.databinding.ListResultActivityBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListResultActivity extends AppCompatActivity {
    ListResultActivityBinding binding;
    public static List<Varos> cities = new ArrayList<>();
    private String url = "http://127.0.0.1:8000/api/varos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_result_activity);
        binding = ListResultActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.visszaGomb.setOnClickListener(view -> {
            Intent vissza = new Intent(ListResultActivity.this, MainActivity.class);
            startActivity(vissza);
            finish();
        });
        CityTask cityTask = new CityTask(url, "GET");
        cityTask.execute();
    }

    private class CityTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;



        public CityTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.get(requestUrl);

            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(ListResultActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null | response.getResponseCode() >= 400) {
                Toast.makeText(ListResultActivity.this, "Hiba történt a kérésnek feldolgozása során!", Toast.LENGTH_SHORT).show();

            }
            else {
                Varos[] places =  converter.fromJson(response.getContent(), Varos[].class);
                cities.clear();
                cities.addAll(Arrays.asList(places));
                ArrayAdapter<Varos> cityArrayAdapter = new ArrayAdapter<>(ListResultActivity.this, R.layout.list_item, R.id.textItems, cities);
                binding.list.setAdapter(cityArrayAdapter);
            }
        }


    }
}