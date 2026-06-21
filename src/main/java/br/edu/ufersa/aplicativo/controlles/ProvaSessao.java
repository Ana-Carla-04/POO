package br.edu.ufersa.aplicativo.controlles;

import br.edu.ufersa.aplicativo.model.entities.Disciplina;
import br.edu.ufersa.aplicativo.model.entities.MultiplaEscolha;
import br.edu.ufersa.aplicativo.model.entities.Nivel;
import br.edu.ufersa.aplicativo.model.entities.Prova;
import br.edu.ufersa.aplicativo.model.entities.Questao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ProvaSessao {

    private static final ProvaSessao INSTANCE = new ProvaSessao();

    private String professor = "";
    private String instituicao = "";
    private String disciplina = "";
    private String tipo = "";
    private int totalQuestoes = 0;

    private final Set<Questao> questoes = new LinkedHashSet<>();
    private final List<Prova> provasSalvas = new ArrayList<>();

    private ProvaSessao() { }

    public static ProvaSessao getInstance() {
        return INSTANCE;
    }

    public void limpar() {
        professor = "";
        instituicao = "";
        disciplina = "";
        tipo = "";
        totalQuestoes = 0;
        questoes.clear();
    }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    public String getInstituicao() { return instituicao; }
    public void setInstituicao(String instituicao) { this.instituicao = instituicao; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getTotalQuestoes() { return totalQuestoes; }
    public void setTotalQuestoes(int totalQuestoes) { this.totalQuestoes = totalQuestoes; }

    public List<Questao> getQuestoes() {
        return new ArrayList<>(questoes);
    }

    public void setQuestoes(List<Questao> novasQuestoes) {
        questoes.clear();
        questoes.addAll(novasQuestoes);
    }

    public Prova registrarProvaSalva() {
        List<Questao> lista = getQuestoes();
        Disciplina disciplina = lista.isEmpty() ? null : lista.get(0).getDisciplina();
        String codigo = "PROVA-" + System.currentTimeMillis();

        Prova prova = new Prova(lista, disciplina, codigo);
        prova.setInstituicao(instituicao);
        prova.setDataDeCriacao(LocalDate.now());

        provasSalvas.add(prova);
        return prova;
    }

    public List<Prova> getProvasSalvas() {
        return Collections.unmodifiableList(provasSalvas);
    }

    private static List<Questao> bancoCache = null;

    public static synchronized List<Questao> bancoDeQuestoes() {
        if (bancoCache != null) {
            return bancoCache;
        }
        List<Questao> banco = new ArrayList<>();

        Disciplina biologia    = new Disciplina("Biologia", "BIO101");
        Disciplina matematica  = new Disciplina("Matemática", "MAT101");
        Disciplina portugues   = new Disciplina("Português", "POR101");
        Disciplina historia    = new Disciplina("História", "HIS101");
        Disciplina[] disciplinas = {biologia, matematica, portugues, historia};

        String[] assArr = {"fauna", "flora", "Álgebra", "Gramática"};
        Nivel[] niveis = {Nivel.FACIL, Nivel.MEDIO, Nivel.DIFICIL};

        for (int i = 1; i <= 24; i++) {
            Nivel nivel = niveis[(i - 1) % 3];
            Disciplina disc = disciplinas[i % disciplinas.length];
            String ass = assArr[i % assArr.length];
            String enunciado = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum " +
                    "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum";

            List<String> alternativas;
            String resposta;

            if (i % 2 == 0) {
                alternativas = Arrays.asList(
                        "O método, o operador e o instrumento.",
                        "A medição, o operador e o método.",
                        "A técnica, o operador e o instrumento.",
                        "O padrão, o método e o instrumento."
                );
                resposta = alternativas.get(0);
            } else {
                alternativas = Arrays.asList(
                        "Comprimento, massa e pressão.",
                        "Bastante espaço.",
                        "Iluminação potente.",
                        "Vibração constante."
                );
                resposta = alternativas.get(1);
            }

            banco.add(new MultiplaEscolha(
                    i, enunciado, ass, disc, nivel, alternativas, resposta
            ));
        }
        bancoCache = banco;
        return banco;
    }
}