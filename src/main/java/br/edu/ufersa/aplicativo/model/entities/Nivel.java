package br.edu.ufersa.aplicativo.model.entities;

public enum Nivel {
    FACIL(1),
    MEDIO(2),
    DIFICIL(3);

    private int valor;
    Nivel(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
