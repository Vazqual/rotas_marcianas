package br.unicamp.rotas_marcianas;

import java.util.Stack;

// ---------- Rotas Marcianas ---------
// Felipe Stolze Vazquez -------- 21233
// Guilherme Felippe de Campos -- 21236
// Júlia Lopes De Campos -------- 20140

public class Recursao {

    int cidadeAtual;
    Cidade[] cidades;
    Stack<Movimento> pilha;
    Stack<Stack<Movimento>> movimentos; //stack que guarda todos os caminhos
    Caminho[][] caminhos;               //matriz de caminhos com nomes e distancia
    int sizeCaminhos;

    public Recursao(Caminho[][] caminhos, Cidade[] cidades){    // Recebe o vetor de cidades e a matriz de caminhos
        this.sizeCaminhos = caminhos.length;
        this.cidades = cidades;
        this.caminhos = caminhos;
    }

    public Stack<Movimento> procurarCaminhos(int origem, int destino)  {
        // Instancia as variaveis da classe
        cidadeAtual = origem;
        pilha = new Stack<>();
        movimentos = new Stack<>();

        procurarCaminhos(destino);

        return menorCaminho(movimentos);
    }

    private Stack<Stack<Movimento>> procurarCaminhos(int destino) {
        for (int i = 0; i < sizeCaminhos; i++)      // percorre a matriz de caminhos
            if (caminhos[cidadeAtual][i] != null)   // se não for null, há ligação
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
                else {      // se a saida nao foi encontrada,
                    procurarCaminhos(destino);
                    Movimento movimentoAnterior = pilha.pop();
                    cidadeAtual = movimentoAnterior.getOrigem(); // volta uma posição
                }
            }
        return movimentos;
    }

    private Stack<Movimento> menorCaminho(Stack<Stack<Movimento>> ssMovimentos){
        Stack<Movimento> stackUm = ssMovimentos.pop();  // Stack com o menor caminho que retornará
        Stack<Movimento> stackDois = new Stack<>();     // Stack auxiliar para comparar


        int distanciaUm, distanciaDois;     // distancias recebem a soma das ditancias do primeiro stack


        while(ssMovimentos.size() > 0)      //enquanto houver elementos no stack
        {
            stackDois = ssMovimentos.pop(); // stackDois recebe um novo stack

            distanciaDois = distanciaUm = 0;//zera as distancias

            for (int i = 0; i < stackUm.size(); i++)
                distanciaUm += stackUm.get(i).getDados().getDistancia(); //soma as distancias do stackUm

            for (int i = 0; i < stackDois.size(); i++)
                distanciaDois += stackDois.get(i).getDados().getDistancia();// soma as distancias do StackDois

            if(distanciaDois < distanciaUm) // Se a nova soma for menor
                stackUm = stackDois;        // a primeira recebe a nova
        }

        return stackUm; //retorna o stackUm independente do que aconteça
    }
}