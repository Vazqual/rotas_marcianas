package br.unicamp.rotas_marcianas;

import android.util.Log;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class Grafo extends AppCompatActivity {
        private final int NUM_VERTICES = 20;
        private Vertice[] vertices;
        private int[][] adjMatrix;
        int numVerts;
        ScrollView scrCaminhos = findViewById(R.id.scrCaminhos);; // para exibir a matriz de adjacência num formulário

        /// DIJKSTRA
        DistOriginal[] percurso;
        int infinity = Integer.MAX_VALUE;
        int verticeAtual; // global que indica o vértice atualmente sendo visitado
        int doInicioAteAtual; // global usada para ajustar menor caminho com Djikstra
        int nTree;

        public Grafo(ScrollView scr, Caminho[][] caminhos)
        {
            vertices = new Vertice[NUM_VERTICES];
            adjMatrix = new int[NUM_VERTICES][NUM_VERTICES];
            numVerts = 0;
            nTree = 0;
            for (int j = 0; j < NUM_VERTICES; j++) // zera toda a matriz
                for (int k = 0; k < NUM_VERTICES; k++)
                    adjMatrix[j][k] = caminhos[j][k].distancia; // distância tão grande que não existe

            percurso = new DistOriginal[NUM_VERTICES];
        }

        public void novoVertice(Cidade cidade)
        {
            vertices[numVerts] = new Vertice(cidade);
            numVerts++;
        }

        public void novaAresta(int origem, int destino, int distancia)
        {
            adjMatrix[origem][destino] = distancia; // sobrecarga do método anterior
        }

            public void exibirVertice(int v)
        {
            Log.i("Cidade", vertices[v].cidade.getNome());
        }

        public int semSucessores()
        {
            boolean temAresta;
            for (int linha = 0; linha < numVerts; linha++)
            {
                temAresta = false;
                for (int col = 0; col < numVerts; col++)
                    if (adjMatrix[linha][col] > 0)
                    {
                        temAresta = true;
                        break;
                    }

                if (!temAresta)
                    return linha;
            }
            return -1;
        }
    public void removerVertice(int vert)
    {
        String quemSai = vertices[vert].Rotulo;
        if (scrCaminhos != null)
        {
            MessageBox.Show("Matriz de Adjacências antes de remover vértice +" + quemSai + " na posição "+ vert);
            ExibirAdjacencias();
        }
        if (vert != numVerts - 1)
        {
            for (int j = vert; j < numVerts - 1; j++) // remove vértice do vetor
                vertices[j] = vertices[j + 1];
            // remove vértice da matriz
            for (int row = vert; row < numVerts; row++)
                MoverLinhas(row, numVerts - 1);
            for (int col = vert; col < numVerts; col++)
                MoverColunas(col, numVerts - 1);
        }
        numVerts--;
        if (dgv != null)
        {
            MessageBox.Show("Matriz de Adjacências após remover vértice +" + quemSai + " na posição "+ vert);
            ExibirAdjacencias();
            MessageBox.Show("Retornando à ordenação");
        }
    }

    private void MoverLinhas(int row, int length)
    {
        if (row != numVerts - 1)
            for (int col = 0; col < length; col++)
                adjMatrix[row, col] = adjMatrix[row + 1, col]; // desloca para excluir
    }
    private void MoverColunas(int col, int length)
    {
        if (col != numVerts - 1)
            for (int row = 0; row < length; row++)
                adjMatrix[row, col] = adjMatrix[row, col + 1]; // desloca para excluir
    }

    public void ExibirAdjacencias()
    {
        dgv.RowCount = numVerts + 1;
        dgv.ColumnCount = numVerts + 1;
        for (int j = 0; j < numVerts; j++)
        {
            dgv.Rows[j + 1].Cells[0].Value = vertices[j].Rotulo;
            dgv.Rows[0].Cells[j + 1].Value = vertices[j].Rotulo;
            for (int k = 0; k < numVerts; k++)
                dgv.Rows[j + 1].Cells[k + 1].Value = Convert.ToString(adjMatrix[j, k]);
        }
    }

    public string OrdenacaoTopologica()
    {
        PilhaVetor<String> gPilha = new PilhaVetor<String>(); //guarda a sequência de vértices
        while (numVerts > 0)
        {
            int currVertex = SemSucessores();
            if (currVertex == -1)
                return "Erro: grafo possui ciclos.";
            gPilha.Empilhar(vertices[currVertex].Rotulo); // empilha vértice
            RemoverVertice(currVertex);
        }
        string resultado = "Sequência da Ordenação Topológica: ";
        while (!gPilha.EstaVazia)
            resultado += gPilha.Desempilhar() + " "; // desempilha para exibir
        return resultado;
    }


    //// percurso em profundidade

    private int ObterVerticeAdjacenteNaoVisitado(int v)
    {
        for (int j = 0; j < numVerts; j++)
            if ((adjMatrix[v][ j] != 0) && (!vertices[j].FoiVisitado))
        return j;
        return -1;
    }

    public void PercursoEmProfundidade(TextBox txt)
    {
        txt.Clear();
        PilhaVetor<int> gPilha = new PilhaVetor<int>(); // para guardar a sequência de vértices
        vertices[0].FoiVisitado = true;
        gPilha.Empilhar(0);
        ExibirVertice(0, txt);
        int v;
        while (!gPilha.EstaVazia)
        {
            v = ObterVerticeAdjacenteNaoVisitado(gPilha.OTopo());
            if (v == -1)
                gPilha.Desempilhar();
            else
            {
                vertices[v].FoiVisitado = true;
                ExibirVertice(v, txt);
                gPilha.Empilhar(v);
            }
        }

    }
