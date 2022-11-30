package br.unicamp.rotas_marcianas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Cidade[] cidades;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonFileString = (getJsonFromAssets(getApplicationContext(), "cidades.json"));
        Log.i("data", jsonFileString);
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Cidade>>() { }.getType();

        List<Cidade> listaCidades = gson.fromJson(jsonFileString, listUserType);



        cidades = new Cidade[listaCidades.size()];
        for (int i = 0; i< listaCidades.size(); i++)
        {
            cidades[i] = listaCidades.get(i);
            Log.i("item", cidades[i].toString());
        }

        String[] items = new String[listaCidades.size()];

        for (int i = 0; i< listaCidades.size(); i++)
            items[i] = cidades[i].nome;

        ArrayAdapter<String> adapterDdOrigem = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapterDdDestino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        Spinner ddOrigem = findViewById(R.id.spnOrigem);
        Spinner ddDestino = findViewById(R.id.spnDestino);

        if (ddOrigem != null && ddDestino != null)
        {
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
            ddDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa, myOptions);
        Paint paint = new Paint();
        paint.setTextSize(30f);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);

        desenharCidades(workingBitmap.getWidth(), workingBitmap.getHeight(), paint, canvas, cidades);



        ImageView imageView = findViewById(R.id.m);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }

    private void desenharCidades(int largura, int altura, Paint paint, Canvas canvas, Cidade[] cidade) {
        for (int i = 0; i < cidade.length; i++)
        {
            canvas.drawCircle(          //Desenha os pontos nas coordenadas
                    (float) Math.round(largura*cidade[i].x),    //das cidades
                    (float) Math.round(altura*cidade[i].y),
                    10f,
                    paint);
            canvas.drawText(            //Escreve os nomes das cidades
                    cidade[i].nome,     //nos locais apropriados
                    (float) Math.round(largura*cidade[i].x) + 10f,
                    (float) Math.round(altura*cidade[i].y),
                    paint);
        }

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