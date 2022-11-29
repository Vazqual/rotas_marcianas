package br.unicamp.rotas_marcianas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonFileString = (getJsonFromAssets(getApplicationContext(), "cidades.json"));
        Log.i("data", jsonFileString);
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Cidade>>() { }.getType();

        List<Cidade> cidades = gson.fromJson(jsonFileString, listUserType);
        for (int i = 0; i < cidades.size(); i++) {
            Log.i("data", "> Item " + i + "\n" + cidades.get(i));
        }


        Spinner ddOrigem = findViewById(R.id.spnOrigem);
        Spinner ddDestino = findViewById(R.id.spnDestino);
        String[] items = new String[cidades.size()];
        for (int i = 0; i< cidades.size(); i++)
            items[i] = cidades.get(i).nome;

        ArrayAdapter<String> adapterDdOrigem = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapterDdDestino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ddOrigem.setAdapter(adapterDdOrigem);
        ddDestino.setAdapter(adapterDdDestino);
        ddOrigem.setSelection(0);
        ddDestino.setSelection(0);
        ddOrigem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
}