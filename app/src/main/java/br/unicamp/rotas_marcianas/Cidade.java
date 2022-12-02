package br.unicamp.rotas_marcianas;

import com.google.gson.annotations.SerializedName;

public class Cidade {

    @SerializedName("nomeCidade")
    private String nome;

    @SerializedName("coordenadaX")
    private double x;

    @SerializedName("coordenadaY")
    private double y;

    public Cidade(String nome, double x, double y)
    {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }


    public Cidade()
    { }


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
        return "Cidade{" +
                "nome=" + nome + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
