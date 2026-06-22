package br.edu.ufersa.aplicativo.application;

import br.edu.ufersa.aplicativo.model.entities.Professor;

public class Contexto {
    private static Professor professorLogado;

    public static void setProfessorLogado(Professor professorLogado) {
        if (professorLogado != null) Contexto.professorLogado = professorLogado;
        else throw new IllegalArgumentException("Professor logado invalido");
    }
}
