package br.unicamp.rotas_marcianas;

import java.util.Stack;


public class Recursao {
    int matriz[][] ;
    int cidadeAtual;
    Cidade[] cidades;
    Stack<Caminho> pilha;
    Stack<Stack<Caminho>> movimentos;
    Caminho[][] caminhos;
    int sizeCaminhos;

    public Recursao(Caminho[][] caminhos, Cidade[] cidades){
//        matriz = new int[caminhos.length][caminhos.length];
//        for (int i = 0; i< caminhos.length; i++)
//            if (caminhos[i]!= null)
//                for(int j = 0; j< caminhos.length; j++)
//                    matriz[i][j] = caminhos[i][j].getDistancia();


        this.sizeCaminhos = caminhos.length;
        this.cidades = cidades;
        this.caminhos = caminhos;
    }

    public Stack<Stack<Caminho>> procurarCaminhos(int origem, int destino)  {
        cidadeAtual = origem;
        pilha = new Stack<Caminho>();
        movimentos = new Stack<Stack<Caminho>>();

        return procurarCaminhos(destino);
    }

    // Método que realiza a busca de caminhos entre duas cidades
    private Stack<Stack<Caminho>> procurarCaminhos(int destino) {
        for (int i = 0; i < sizeCaminhos; i++) // Testa todas cidades da matriz de adjacências
        {
            if (caminhos[cidadeAtual][i] != null) // Verifica se existe ligação
            {
                Caminho movimentoObtido = caminhos[cidadeAtual][i];
                pilha.add(movimentoObtido);
                cidadeAtual = i;

                if (cidadeAtual == destino) // Um caminho foi encontrado
                    achouCaminho();
                else {
                    procurarCaminhos(destino);
                    Caminho movimentoAnterior = pilha.pop(); // Uma saída não foi encontrada, portanto volta para uma cidade anterior

                    cidadeAtual = cidadePorNome(movimentoAnterior.getCidadeOrigem());
                }
            }
        }
        return movimentos;
    }
    private void achouCaminho()  // Procedimento feito ao encontrar-se um caminho
    {
        Stack<Caminho> pilhaClone = (Stack<Caminho>) pilha;
        movimentos.add(pilhaClone);
        Caminho movimentoAnterior = pilha.pop();
        cidadeAtual = cidadePorNome(movimentoAnterior.getCidadeOrigem());
    }

    private int cidadePorNome(String nome)
    {
        for (int i = 0; i < cidades.length; i++ )
            if (nome.compareTo(cidades[i].getNome())== 0)
                return i;

        return -1;
    }
}
