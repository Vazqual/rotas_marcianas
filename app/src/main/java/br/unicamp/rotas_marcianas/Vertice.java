package br.unicamp.rotas_marcianas;

public class Vertice {
    String rotulo;      // identifica o vértice
    boolean foiVisitado;   // permite manter rastreio de visitas
    double x, y;        // coordenadas cartesianas do vértice

    public Vertice(String label, double x, double y) throws Exception {
        rotulo = label;
        foiVisitado = false;
        this.x = x;
        this.y = y;
    }
    public Vertice(String label) throws Exception {
        rotulo = label;
        foiVisitado = false;
        this.x = 0;
        this.y = 0;
    }
    public String getRotulo() { return this.rotulo;}
    public void setRotulo(String rotulo) { this.rotulo = rotulo; }

    public boolean getFoiVisitado() { return this.foiVisitado; }
    public void setFoiVisitado(boolean foiVisitado) { this.foiVisitado = foiVisitado; }

    public double getX() { return this.x; }
    public void setX(double x) throws Exception {
        if (x < 0)
            throw new Exception("X deve ser >= 0!");

        this.x = x;
    }

    public double getY() { return y; }
    public void setY(double y) throws Exception{
        if (y < 0)
            throw new Exception("Y deve ser >= 0!");

        this.y = y;
    }
}
