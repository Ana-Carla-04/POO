
class Questao{
    private String codeQuestao;
    private String enunciadoQuestao;
    private String[] alternativaQuestao;
    private String gabaritoQuestao;
    private String assuntoQuestao;
    private Disciplina disciplina;
    private boolean subjetiva;
    private boolean objetiva;
    private int nivel;

    //falta o encapsulamento de todos os argumentos
    // integridade de codeQuestao;
    public void setCode(String code){
        if(code != null ){
            codeQuestao = code;
        }
    }
    public String getCode(){
        return codeQuestao;
    }

    //  private String enunciadoQuestao;
    public void setEnunciado(String enunciado){
        if(enunciado != null){
            enunciadoQuestao = enunciado;
        }
    }
    public String getEnunciado(){
        return enunciadoQuestao;
    }

    // private String[] alternativaQuestao;
    public void setAternativa(String[] alternativa){
        // ter pelomenos duas escolhas
        if(alternativa != null && alternativa.length >= 2){
            alternativaQuestao = alternativa;
        }
    }
    public String[] getAlternativa(){
        return alternativaQuestao;
    }

    // private String gabaritoQuestao;
    public void setGabarito(String gabarito){
        if (gabarito != null){
            gabaritoQuestao = gabarito;
        }
    }
    public String getGabarito(){
        return gabaritoQuestao;
    }

    // private String assuntoQuestao;
    public void setAssunto(String assunto){
        if(assunto != null){
            assuntoQuestao = assunto;
        }
    }
    public String getAssunto(){
        return assuntoQuestao;
    }

    // private Disciplina disciplina;
    public void setDisc(Disciplina disc){
        if ( disc != null){
            disciplina = disc;
        }
    }
    public Disciplina getDisc(){
        return disciplina;
    }

    //  private boolean subjetiva;
    public void setSubjetiva(boolean sub){
        subjetiva = sub;
        if(sub){
        objetiva = false; // exclusão mútua
        }
    }
    public boolean getSubjetiva(){
        return subjetiva;
    } 

    // private boolean objetiva;
     public void setObjetiva(boolean obj){
        objetiva = obj;
        if(obj){
        subjetiva = false; // exclusão mútua
        } 
    }
    public boolean getObjetiva(){
        return objetiva;
    } 
    // private int nivel;
    public void setNivel(int nivel){
        if(nivel == 1 || nivel == 2 || nivel == 3){
            this.nivel = nivel;
        }else{
            this.nivel = 1;
        }
    }
    public int getNivel(){
        return nivel;
    } 


    // metodos:
    //metodo void cadastrar()
    //metodo void editar()
    //metodo void excluir()
    //metodo String tipo()
    //metodo int nivel()
}