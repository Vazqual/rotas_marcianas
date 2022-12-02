package br.unicamp.rotas_marcianas;

public class Vertice {
    Cidade cidade;      // identifica o vértice
    boolean foiVisitado;   // permite manter rastreio de visitas
    boolean estaAtivo;

    public Vertice(Cidade cidade) {
        this.cidade = cidade;
        foiVisitado = false;
        estaAtivo = true;
    }


    public boolean getFoiVisitado() { return this.foiVisitado; }
    public void setFoiVisitado(boolean foiVisitado) { this.foiVisitado = foiVisitado; }

    public boolean getEstaAtivo() { return this.estaAtivo; }
    public void setEstaAtivo(boolean estaAtivo) { this.estaAtivo = estaAtivo; }
}
