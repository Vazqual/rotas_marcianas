package br.unicamp.rotas_marcianas;

import android.util.Log;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

// ---------- Rotas Marcianas ---------
// Felipe Stolze Vazquez -------- 21233
// Guilherme Felippe de Campos -- 21236
// Júlia Lopes De Campos -------- 20140

public class Grafo extends AppCompatActivity {
    ;
    private Vertice[] vertices;
    private int[][] adjMatrix;
    int numVerts;

    /// DIJKSTRA
    DistOriginal[] percurso;
    int infinity = Integer.MAX_VALUE;
    int verticeAtual; // global que indica o vértice atualmente sendo visitado
    int doInicioAteAtual; // global usada para ajustar menor caminho com Djikstra
    int nTree;

    public Grafo(int NUM_VERTICES, Caminho[][] caminhos) {
        vertices = new Vertice[NUM_VERTICES];
        adjMatrix = new int[NUM_VERTICES][NUM_VERTICES];
        numVerts = 0;
        nTree = 0;
        for (int j = 0; j < NUM_VERTICES; j++)
            for (int k = 0; k < NUM_VERTICES; k++) {
                if (caminhos[j][k] != null)
                    adjMatrix[j][k] = caminhos[j][k].getDistancia();    // usa a matriz formada na main para formar a matriz de inteiros
                else
                    adjMatrix[j][k] = infinity;
            }

        percurso = new DistOriginal[NUM_VERTICES];
    }

    public void novoVertice(Cidade cidade) {
        vertices[numVerts] = new Vertice(cidade);
        numVerts++;
    }

    public void novaAresta(int origem, int destino, int distancia) {
        adjMatrix[origem][destino] = distancia; // sobrecarga do método anterior
    }

    public String caminho(int inicioDoPercurso, int finalDoPercurso) {
        for (int j = 0; j < numVerts; j++)
            vertices[j].foiVisitado = false;
        vertices[inicioDoPercurso].foiVisitado = true;
        for (int j = 0; j < numVerts; j++) {
            // anotamos no vetor percurso a distância entre o inicioDoPercurso e cada vértice
            // se não há ligação direta, o valor da distância será infinity
            int tempDist = adjMatrix[inicioDoPercurso][j];
            percurso[j] = new DistOriginal(inicioDoPercurso, tempDist);
        }
        for (int nTree = 0; nTree < numVerts; nTree++) {
            // Procuramos a saída não visitada do vértice inicioDoPercurso com a menor distância
            int indiceDoMenor = obterMenor();
            // e anotamos essa menor distância
            int distanciaMinima = percurso[indiceDoMenor].distancia;

            // o vértice com a menor distância passa a ser o vértice atual
            // para compararmos com a distância calculada em AjustarMenorCaminho()
            verticeAtual = indiceDoMenor;
            doInicioAteAtual = percurso[indiceDoMenor].distancia;
            // visitamos o vértice com a menor distância desde o inicioDoPercurso
            vertices[verticeAtual].foiVisitado = true;
            ajustarMenorCaminho();
        }
        return exibirPercursos(inicioDoPercurso, finalDoPercurso);
    }

    private String exibirPercursos(int inicioDoPercurso, int finalDoPercurso) {
        String resultado = "";
        for (int j = 0; j < numVerts; j++) {
            resultado += vertices[j].cidade + "=";
            if (percurso[j].distancia == infinity)
                resultado += "inf";
            else
                resultado += percurso[j].distancia + " ";
            Cidade pai = vertices[percurso[j].verticePai].cidade;
            resultado += "(" + pai + ") ";
        }
        int onde = finalDoPercurso;
        Stack<Cidade> pilha = new Stack<Cidade>();
        int cont = 0;
        while (onde != inicioDoPercurso) {
            onde = percurso[onde].verticePai;
            pilha.push(vertices[onde].cidade);
            cont++;
        }


        resultado = "";
        while (pilha.size() != 0)
            resultado += " --> " + pilha.pop().getNome() + "\n";

        if ((cont == 1) && (percurso[finalDoPercurso].distancia == infinity))
            resultado = "Não há caminho";
        else
            resultado += " --> " + vertices[finalDoPercurso].cidade.getNome();

        Log.i("Caminhos", resultado);
        return resultado;
    }

    private void ajustarMenorCaminho() {
        for (int coluna = 0; coluna < numVerts; coluna++)
            if (!vertices[coluna].foiVisitado) // para cada vértice ainda não visitado
            {
                // acessamos a distância desde o vértice atual (pode ser infinity)
                int atualAteMargem = adjMatrix[verticeAtual][coluna];
                // calculamos a distância desde inicioDoPercurso passando por vertice atual
                // até esta saída
                int doInicioAteMargem = doInicioAteAtual + atualAteMargem;
                // quando encontra uma distância menor, marca o vértice a partir do
                // qual chegamos no vértice de índice coluna, e a soma da distância
                // percorrida para nele chegar
                int distanciaDoCaminho = percurso[coluna].distancia;
                if (doInicioAteMargem < distanciaDoCaminho) {
                    percurso[coluna].verticePai = verticeAtual;
                    percurso[coluna].distancia = doInicioAteMargem;
                }
            }
    }

    public void exibirVertice(int v) {
        Log.i("Cidade", vertices[v].cidade.getNome());
    }

    private int obterMenor() {
        int distanciaMinima = infinity;
        int indiceDaMinima = 0;
        for (int j = 0; j < numVerts; j++)
            if (!(vertices[j].foiVisitado) && (percurso[j].distancia < distanciaMinima)) {
                distanciaMinima = percurso[j].distancia;
                indiceDaMinima = j;
            }
        return indiceDaMinima;
    }
}