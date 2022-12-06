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

// ---------- Rotas Marcianas ---------
// Felipe Stolze Vazquez -------- 21233
// Guilherme Felippe de Campos -- 21236
// Júlia Lopes De Campos -------- 20140
// ------------------------------------

public class MainActivity extends AppCompatActivity {

    Canvas canvas;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Cidade[] cidades;       // vetor que guardara as cidades
        Caminho[][] caminhos;   // matriz para mapear os caminhos entre cidades
        // A linha indica o indice da cidade origem,
        // a coluna indica o indice da cidade destino


        // arquivo de cidades guardado numa string
        String jfsCidades = (getJsonFromAssets(getApplicationContext(), "cidades.json"));

        //arquivo de caminhos guardado numa string
        String jfsCaminhos = (getJsonFromAssets(getApplicationContext(), "caminhos.json"));

        Log.i("data", jfsCidades);
        Log.i("data", jfsCaminhos);

        Gson gsonCidades = new Gson();
        Gson gsonCaminhos = new Gson();

        Type tipoListaCidades = new TypeToken<List<Cidade>>() {
        }.getType();
        Type tipoListaCaminhos = new TypeToken<List<Caminho>>() {
        }.getType();

        List<Cidade> listaCidades = gsonCidades.fromJson(jfsCidades, tipoListaCidades);     // lista que guarda as cidades
        List<Caminho> listaCaminhos = gsonCaminhos.fromJson(jfsCaminhos, tipoListaCaminhos);// lista que guarda os caminhos

        cidades = new Cidade[listaCidades.size()];              // determina o tamanho do vetor cidades
        caminhos = new Caminho[listaCidades.size()][listaCidades.size()];// determina o tamanho da matriz caminhos

        for (int i = 0; i < listaCidades.size(); i++)       // armazena os dados no vetor cidades
            cidades[i] = listaCidades.get(i);               // a partir da lista de cidades

        montarMatrizCaminhos(caminhos, cidades, listaCaminhos); // metodo para montar a matriz com os caminhos

        String[] items = new String[listaCidades.size()];   // vetor tipo String com os nomes das cidades
        for (int i = 0; i < listaCidades.size(); i++)       // que vai colocar as cidades nos dropdowns
            items[i] = cidades[i].getNome();                // para se selecionar as cidades

        BitmapFactory.Options myOptions = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa, myOptions);
        Paint paint = new Paint();
        paint.setTextSize(60f);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;


        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        ImageView imageView = findViewById(R.id.m);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);


        canvas = new Canvas(mutableBitmap);


        ArrayAdapter<String> adapterDdOrigem = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items);
        ArrayAdapter<String> adapterDdDestino = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items);

        Spinner ddOrigem = findViewById(R.id.spnOrigem);        // usuario vai escolher as cidades
        Spinner ddDestino = findViewById(R.id.spnDestino);      // atravez destes spinners

        Button btnBuscar = findViewById(R.id.btnBuscar);        // button que realiza as buscas ao ser clicado

        TextView tvCaminhos = findViewById(R.id.tvCaminhos);    // textView dentro de scrollview que vai guardar os caminhos

        Switch swtDijkstra = findViewById(R.id.swtDijkstra);    // switches para escolher qual
        Switch swtRecursao = findViewById(R.id.swtRecursao);    // metodo de busca deve ser usado

        //desenha cidades
        desenharCidades(workingBitmap.getWidth(), workingBitmap.getHeight(), paint, cidades);

        //muda a width pra desenhar os caminhos mais finos
        paint.setStrokeWidth(5f);
        desenharCaminhos(workingBitmap.getWidth(), workingBitmap.getHeight(), paint, caminhos, cidades);

        if (ddOrigem != null && ddDestino != null) {
            ddOrigem.setAdapter(adapterDdOrigem);       // adiciona ao spinner os ArrayAdapters com as
            ddDestino.setAdapter(adapterDdDestino);     // os nomes das cidades

            ddOrigem.setSelection(0);       //Inicia o programa com a primeira cidade selecionada
            ddDestino.setSelection(0);      //"                                                 "

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

        montarMatrizCaminhos(caminhos, cidades, listaCaminhos);

        //Somente um Switch pode ficar ativo por vez
        swtDijkstra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swtRecursao.setChecked(false);
            }
        });
        swtRecursao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swtDijkstra.setChecked(false);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Para evitar que o usuario escolha duas cidades iguais
                if (ddOrigem.getSelectedItemPosition() == ddDestino.getSelectedItemPosition())
                    Toast.makeText(MainActivity.this, "Origem é igual ao destino! altere um dos dois.", Toast.LENGTH_SHORT).show();

                else if (swtDijkstra.isChecked())
                {
                    Grafo grafo = new Grafo(cidades.length, caminhos);

                    for (Cidade cidade : cidades) // Monta as vertices
                        grafo.novoVertice(cidade);


                    for (int i = 0; i < caminhos.length; i++)
                        if (caminhos[i] != null)
                            for (int j = 0; j < caminhos.length; j++) // Monta as Arestas
                                if (caminhos[i][j] != null)
                                    grafo.novaAresta(i, j, caminhos[i][j].getDistancia());

                    tvCaminhos.setText(grafo.caminho(ddOrigem.getSelectedItemPosition(), ddDestino.getSelectedItemPosition()));
                } else if (swtRecursao.isChecked())
                {
                    Recursao recursao = new Recursao(caminhos, cidades);


                    //Recebe a pilha de movimentos com menor soma de distancia
                    Stack<Movimento> sMovimentos = recursao.procurarCaminhos(ddOrigem.getSelectedItemPosition(),
                                                                             ddDestino.getSelectedItemPosition());

                    //necessario desenhar caminhos novamente para evitar que os
                    // caminhos destacados anteriormente permaneçam asssim
                    desenharCaminhos(workingBitmap.getWidth(), workingBitmap.getHeight(), paint, caminhos, cidades);

                    //desenha o menor caminho atravez do stack
                    desenharMenorCaminho(workingBitmap.getHeight(), workingBitmap.getWidth(), sMovimentos, cidades);

                    // escreve os movimentos numa string s que vai ser passada pro textview
                    String s = "\n --> " + sMovimentos.get(sMovimentos.size() - 1).getDados().getCidadeDestino();
                    while (sMovimentos.size() > 0)
                        s = "\n --> " + sMovimentos.pop().getDados().getCidadeOrigem() + s;

                    tvCaminhos.setText(s);

                } else
                    Toast.makeText(MainActivity.this, "Nenhum método foi selecionado! Escolha Recursão ou Dijkstra.", Toast.LENGTH_SHORT).show();
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


    private void desenharCidades(int largura, int altura, Paint paint, Cidade[] cidade) {
        for (int i = 0; i < cidade.length; i++) {
            canvas.drawCircle(          //Desenha os pontos nas coordenadas
                    (float) Math.round(largura * cidade[i].getX()),    //das cidades
                    (float) Math.round(altura * cidade[i].getY()),
                    10f,
                    paint);
            canvas.drawText(                //Escreve os nomes das cidades
                    cidade[i].getNome(),    //nos locais apropriados
                    (float) Math.round(largura * cidade[i].getX()) + 10f,   // adiciona 10 ao x pra nao sobrepor ao ponto
                    (float) Math.round(altura * cidade[i].getY()),          // na posição da cidade
                    paint);
        }
    }

    private void desenharCaminhos(int largura, int altura, Paint paint, Caminho[][] caminhos, Cidade[] cidades) {
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

    //lê o arquivo json a partir dos Assets
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



    //metodo que desenha o caminho indicado pelo stack de movimentos
    private void desenharMenorCaminho(int altura, int largura, Stack<Movimento> sMovimento, Cidade[] cidades)
    {
        Paint paint = new Paint();
        paint.setColor(Color.RED); //O menor caminho das cidades vai estar em vermelho
        paint.setStrokeWidth(8f);

        for(int i = 0; i< sMovimento.size(); i++)
        {
            canvas.drawLine(
                    (float) Math.round(largura * cidades[sMovimento.get(i).getOrigem()].getX()),
                    (float) Math.round(altura * cidades[sMovimento.get(i).getOrigem()].getY()),
                    (float) Math.round(largura * cidades[sMovimento.get(i).getDestino()].getX()),
                    (float) Math.round(altura * cidades[sMovimento.get(i).getDestino()].getY()),
                    paint);
        }
    }
}