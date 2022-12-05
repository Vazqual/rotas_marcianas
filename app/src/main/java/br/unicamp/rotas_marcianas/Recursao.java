package br.unicamp.rotas_marcianas;

import java.util.Stack;


public class Recursao {
    int matriz[][] ;
    int cidadeAtual;
    Cidade[] cidades;
    Stack<Movimento> pilha;
    Stack<Stack<Movimento>> movimentos;
    Caminho[][] caminhos;
    int sizeCaminhos;

    public Recursao(Caminho[][] caminhos, Cidade[] cidades){
        this.sizeCaminhos = caminhos.length;
        this.cidades = cidades;
        this.caminhos = caminhos;
    }

    public Stack<Stack<Movimento>> procurarCaminhos(int origem, int destino)  {
        cidadeAtual = origem;
        pilha = new Stack<>();
        movimentos = new Stack<>();

        return procurarCaminhos(destino);
    }

    // Método que realiza a busca de caminhos entre duas cidades
    private Stack<Stack<Movimento>> procurarCaminhos(int destino) {
        for (int i = 0; i < sizeCaminhos; i++) // Testa todas cidades da matriz de adjacências
        {
            if (caminhos[cidadeAtual][i] != null) // Verifica se existe ligação
            {
                Movimento movimentoObtido = new Movimento(cidadeAtual, i, caminhos[cidadeAtual][i]);
                pilha.add(movimentoObtido);
                cidadeAtual = i;

                if (cidadeAtual == destino) // Um caminho foi encontrado
                {
                    Stack<Movimento> pilhaClone = (Stack<Movimento>) pilha.clone();
                    movimentos.add(pilhaClone);
                    Movimento movimentoAnterior = pilha.pop();
                    cidadeAtual = movimentoAnterior.getOrigem();
                }
                else {
                    procurarCaminhos(destino);
                    Movimento movimentoAnterior = pilha.pop(); // Uma saída não foi encontrada, portanto volta para uma cidade anterior
                    cidadeAtual = movimentoAnterior.getOrigem();
                }
            }
        }
        return movimentos;
    }
}
