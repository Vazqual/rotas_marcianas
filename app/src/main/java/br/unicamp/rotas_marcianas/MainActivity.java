package br.unicamp.rotas_marcianas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cidade[] cidades;
        Caminho[][] caminhos;

        String jfsCidades = (getJsonFromAssets(getApplicationContext(), "cidades.json"));
        String jfsCaminhos = (getJsonFromAssets(getApplicationContext(), "caminhos.json"));

        Log.i("data", jfsCidades);
        Log.i("data", jfsCaminhos);

        Gson gsonCidades = new Gson();
        Gson gsonCaminhos = new Gson();

        Type tipoListaCidades = new TypeToken<List<Cidade>>() {
        }.getType();
        Type tipoListaCaminhos = new TypeToken<List<Caminho>>() {
        }.getType();

        List<Cidade> listaCidades = gsonCidades.fromJson(jfsCidades, tipoListaCidades);
        List<Caminho> listaCaminhos = gsonCaminhos.fromJson(jfsCaminhos, tipoListaCaminhos);

        cidades = new Cidade[listaCidades.size()];
        caminhos = new Caminho[listaCidades.size()][listaCidades.size()];

        for (int i = 0; i < listaCidades.size(); i++) {
            cidades[i] = listaCidades.get(i);
            Log.i("item", cidades[i].toString());
        }
        montarMatrizCaminhos(caminhos, cidades, listaCaminhos);

        String[] items = new String[listaCidades.size()];
        for (int i = 0; i < listaCidades.size(); i++)
            items[i] = cidades[i].getNome();

        ArrayAdapter<String> adapterDdOrigem = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapterDdDestino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        Spinner ddOrigem = findViewById(R.id.spnOrigem);
        Spinner ddDestino = findViewById(R.id.spnDestino);

        Button btnBuscar = findViewById(R.id.btnBuscar);

        TextView tvCaminhos = findViewById(R.id.tvCaminhos);
        Switch swtDijkstra = findViewById(R.id.swtDijkstra);
        Switch swtRecursao = findViewById(R.id.swtRecursao);

        if (ddOrigem != null && ddDestino != null) {
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
        desenharCaminhos(workingBitmap.getWidth(), workingBitmap.getHeight(), paint, canvas, caminhos, cidades);

        ImageView imageView = findViewById(R.id.m);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);

        montarMatrizCaminhos(caminhos, cidades, listaCaminhos);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (swtDijkstra.isChecked())
                {
                    Grafo grafo = new Grafo(cidades.length, caminhos);
                    for (Cidade cidade : cidades)
                        grafo.novoVertice(cidade);


                    for (int i = 0; i < caminhos.length; i++)
                        if (caminhos[i] != null)
                            for (int j = 0; j < caminhos.length; j++)
                                if (caminhos[i][j] != null)
                                    grafo.novaAresta(i, j, caminhos[i][j].getDistancia());

                    tvCaminhos.setText(grafo.caminho(ddOrigem.getSelectedItemPosition(), ddDestino.getSelectedItemPosition()));
                }
                else if(swtRecursao.isChecked())
                {
                    Recursao recursao = new Recursao(caminhos, cidades);

                    Stack<Stack<Caminho>> ssCaminhos = recursao.procurarCaminhos(ddOrigem.getSelectedItemPosition(), ddDestino.getSelectedItemPosition());

                }
                else Toast.makeText(MainActivity.this, "Nenhum método foi selecionado! Escolha Recursão ou Dijkstra.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void montarMatrizCaminhos(Caminho[][] caminhos, Cidade[] cidades, List<Caminho> listCaminhos) {
        for (int i = 0; i < listCaminhos.size(); i++)
            for (int j = 0; j < cidades.length; j++)
                if (listCaminhos.get(i).getCidadeOrigem().compareTo(cidades[j].getNome()) == 0)
                    for (int k = 0; k < cidades.length; k++)
                        if (listCaminhos.get(i).getCidadeDestino().compareTo(cidades[k].getNome()) == 0) {
                            caminhos[j][k] = listCaminhos.get(i);
                            Log.i("item", "Cidade Origem " + caminhos[j][k].getCidadeOrigem()
                                    + " conectada com a Cidade Destino " + caminhos[j][k].getCidadeDestino());
                        }
    }


    private void desenharCidades(int largura, int altura, Paint paint, Canvas canvas, Cidade[] cidade) {
        for (int i = 0; i < cidade.length; i++) {
            canvas.drawCircle(          //Desenha os pontos nas coordenadas
                    (float) Math.round(largura * cidade[i].getX()),    //das cidades
                    (float) Math.round(altura * cidade[i].getY()),
                    10f,
                    paint);
            canvas.drawText(            //Escreve os nomes das cidades
                    cidade[i].getNome(),     //nos locais apropriados
                    (float) Math.round(largura * cidade[i].getX()) + 10f,
                    (float) Math.round(altura * cidade[i].getY()),
                    paint);
        }
    }

    private void desenharCaminhos(int largura, int altura, Paint paint, Canvas canvas, Caminho[][] caminhos, Cidade[] cidades) {
        for (int i = 0; i < caminhos.length; i++)
            if (caminhos[i] != null)
                for (int j = 0; j < caminhos.length; j++)
                    if (caminhos[i][j] != null)
                        canvas.drawLine(
                                (float) Math.round(largura * cidades[i].getX()),
                                (float) Math.round(altura * cidades[i].getY()),
                                (float) Math.round(largura * cidades[j].getX()),
                                (float) Math.round(altura * cidades[j].getY()),
                                paint);
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