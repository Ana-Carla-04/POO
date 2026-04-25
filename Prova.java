import java.util.Date;
import java.util.List;


class Prova {
    private Date prova;
    private Disciplina disciplinaProva;
    private List<Questao> questaoProva;

    // encapsulamento:

    // integridade de Prova:
    public void setProva(Date prova){
        if (prova != null){
            this.prova=prova;
        }
    }
    public Date getProva(){
        return prova;
    }

    // integridade de disciplinaProva:
    public void setDiscProva(Disciplina discProva){
        if(discProva != null){
            disciplinaProva = discProva;
        }
    }
    public Disciplina getDiscProva(){
        return disciplinaProva;
    }

    // integridade de questaoProva:
    public void setQuesProva(List<Questao> questoes){
        if(questoes != null){
            questaoProva = questoes;
        }
    }
    public List<Questao> getQuestProva(){
        return questaoProva;
    }

    // metodos:
    // public Prova criarProvaAleatoria(Questao questao, int QntQuestao){

    // }

    // public Relatorio gerarRelatoio(List<Prova> prova){

//    }

   //metodo alterar questao

   //metodo contar questao por nivel



}
