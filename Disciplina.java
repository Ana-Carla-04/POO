import java.util.List;// Importa a interface List

class Disciplina{
    private String nomeDisciplina;
    private String codeDisciplina;
    private List<String> assuntoDisciplina;

    //encapsulamento

    // integridade de nomeDisciplina:
    public void setNomeDisc(String nome){
        if (nome != null){
            nomeDisciplina = nome;
        }
    }
    public String getNomeDisc(){
        return nomeDisciplina;
    }

    // integridade de codeDisciplina
    public void setCodeDisc(String code){
        if (code != null){
            codeDisciplina = code.toUpperCase();
        }
    }
    public String getCodeDisc(){
        return codeDisciplina;
    }

    // integridade de assuntoDisciplina:
    public void setAssunto(List<String> assunto){
        if(assunto != null){
            assuntoDisciplina=assunto;
        }
    }
    public List<String> getAssunto(){
        return assuntoDisciplina;
    }

    // metodos:

    // metodo cadastrar
    public void cadastrar(){

    }
    // metodo editar
    public void editar(){

    }
    
    // metodo excluir
    public void excluir(){

    }
}