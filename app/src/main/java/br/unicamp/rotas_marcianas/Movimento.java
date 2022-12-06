package br.unicamp.rotas_marcianas;

// ---------- Rotas Marcianas ---------
// Felipe Stolze Vazquez -------- 21233
// Guilherme Felippe de Campos -- 21236
// Júlia Lopes De Campos -------- 20140

public class Movimento {

    private int origem;
    private int destino;
    private Caminho dados;

    public int getOrigem() {
        return origem;
    }

    public int getDestino() { return destino; }

    public Caminho getDados() { return dados; }

    // Construtor da classe
    public Movimento (int origem, int destino, Caminho dados)
    {
        this.origem = origem;
        this.destino = destino;
        this.dados = dados;
    }

}
