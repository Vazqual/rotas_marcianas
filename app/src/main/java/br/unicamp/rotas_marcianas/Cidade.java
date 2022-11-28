package br.unicamp.rotas_marcianas;

public class Cidade {
    String codigo, nome;
    double x, y;

    public Cidade(String codigo, String nome, double x, double y)
    {
        this.codigo = codigo;
        this.nome = nome;
        this.x = x;
        this.y = y;
    }


    public Cidade()
    { }


    public String getCodigo() { return this.codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return this.nome; }

    public void setNome(String nome) { this.nome = nome; }

    public double getX() { return this.x; }

    public void setX(double x) { this.x = x; }

    public double getY() { return this.y; }

    public void setY(double y) { this.y = y; }


    public int compareTo(Cidade outro)
    {
        return nome.toUpperCase().trim().compareTo(outro.nome.toUpperCase());
    }

    @Override
    public String toString()
    {
        return this.nome;
    }
}
