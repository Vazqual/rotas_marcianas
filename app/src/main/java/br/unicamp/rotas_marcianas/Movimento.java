package br.unicamp.rotas_marcianas;

public class Movimento {
    public int getOrigem() {
        return origem;
    }

    public int getDestino() {
        return destino;
    }

    private int origem;
    private int destino;
    private Caminho dados;

    // Construtor da classe
    public Movimento (int origem, int destino, Caminho dados)
    {
        this.origem = origem;
        this.destino = destino;
        this.dados = dados;
    }
}
