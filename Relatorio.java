import java.util.List;

class Relatorio{
    private List<Prova> provas;

    //encapsulamento:
    
    // integridade de provas:
    public void setProvas(List<Prova> provas){
        if(provas != null){
            this.provas = provas;
        }
    }
    public List<Prova> getProvas(){
        return provas;
    }

}