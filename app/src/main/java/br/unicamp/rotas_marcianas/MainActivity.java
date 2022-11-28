package br.unicamp.rotas_marcianas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Cidade> cidades = new ArrayList<>();
    ArrayList<Caminhos> caminhos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            JSONObject joCidades = new JSONObject(JsonDataFromAssets("cidades.json"));
            JSONObject joCaminhos = new JSONObject(JsonDataFromAssets("caminhos.json"));
            JSONArray jaCidades = new JSONArray("caminhos");
            for (int i = 0; i<jaCidades.length(); i++){
                JSONObject dadosCidades = jaCidades.getJSONObject(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Spinner dropdown = findViewById(R.id.spnOrigem);
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    private String JsonDataFromAssets(String fileName) {
        String json = null;
        try{
            InputStream inputStream=getAssets().open(fileName);
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json=new String (bufferData, "UTF-8");
        }
        catch(IOException e){
            return null;
        }
        return json;
    }
}