package br.edu.ufersa.Aplicativo.model.entity; //localização do pacote

import java.time.LocalDate; //importa a interface LocalDate
import java.util.List;  //importa a interface List

public class Prova {
    //atributos
    //numero de questões 
    //tipo(multiplaescolha,verdadeiro ou falso , descritiva)
    //quantidade de questões de nivel 1
    //quantidade de questões de nivel 2
    //quantidade de questões de nivel 3
    //professor
    //instituição
    private LocalDate dataDeCriacao; //data da geração da prova
    private List<Questao> questoes; //quais questões
    private Disciplina disciplina; //qual disciplina

    //construtor
    public Prova(List<Questao> questoes, Disciplina disciplina) {
        setDataDeCriacao(LocalDate.now());
        setQuestoes(questoes);
        setDisciplina(disciplina);
    }

    // Setters
    public void setDataDeCriacao(LocalDate dataDeCriacao) {
        if (dataDeCriacao != null) {
            this.dataDeCriacao = dataDeCriacao;
        }
    }

    public void setQuestoes(List<Questao> questoes) {
        if (!questoes.isEmpty()) {
            this.questoes = questoes;
        }
    }

    public void setDisciplina(Disciplina disciplina) {
        if (disciplina != null) {
            this.disciplina = disciplina;
        }
    }
    
    // Getters
    public LocalDate getDataDeCriacao() {
        return dataDeCriacao;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

}
