package br.unicamp.rotas_marcianas;

public class Caminho {
    String cidadeOrigem, cidadeDestino;
    int distancia, tempo, custo;

    public Caminho() {
        cidadeDestino = cidadeOrigem = "";
        distancia = tempo = custo = 0;
    }

    public Caminho(String idCidadeOrigem, String idCidadeDestino, int distancia, int tempo, int custo) {
        this.cidadeOrigem = idCidadeOrigem;
        this.cidadeDestino = idCidadeDestino;
        this.distancia = distancia;
        this.tempo = tempo;
        this.custo = custo;
    }

    public String getCidadeOrigem() {
        return this.cidadeOrigem;
    }
    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public String getCidadeDestino() {
        return this.cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public int getDistancia() {
        return this.distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getTempo() {
        return this.tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getCusto() {
        return this.custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public int compareTo(Caminho outro) { return cidadeDestino.toUpperCase().trim().compareTo(outro.cidadeDestino.toUpperCase().trim()); }

    @Override
    public String toString() { return String.format("%s %s %d %d %d", cidadeOrigem, cidadeDestino, distancia, tempo, custo); }
}
