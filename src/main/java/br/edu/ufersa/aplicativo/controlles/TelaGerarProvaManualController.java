package br.edu.ufersa.aplicativo.controlles;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TelaGerarProvaManualController implements Initializable {

    // ── FXML ────────────────────────────────────────────────────────
    @FXML private StackPane centerRoot;
    @FXML private ComboBox<String> nivelCombo;

    // Lista
    @FXML private VBox listaContainer;

    // Card de detalhe
    @FXML private VBox    detalheCard;
    @FXML private Label   lblCodigo;
    @FXML private Label   lblNivel;
    @FXML private Label   lblTipo;
    @FXML private Label   lblEnunciado;
    @FXML private VBox    boxGabarito;
    @FXML private HBox    linhaAlt1;
    @FXML private HBox    linhaAlt2;
    @FXML private Label   alt1a;
    @FXML private Label   alt1b;
    @FXML private Label   alt2a;
    @FXML private Label   alt2b;
    @FXML private Label   lblAssunto;
    @FXML private Label   lblDisciplina;
    @FXML private Label   lblStatusIcone;

    @FXML private StackPane btnSelecionar;
    @FXML private StackPane btnDeselecionar;
    @FXML private StackPane btnGerar;

    // Menu sidebar
    @FXML private StackPane menuDisciplinas;
    @FXML private StackPane menuBuscar;
    @FXML private StackPane menuGerarProva;
    @FXML private StackPane menuRelatorio;
    @FXML private StackPane menuProvas;

    // ── Estado ──────────────────────────────────────────────────────

    /** Quantidade máxima de questões definida na tela anterior. */
    private int quantidadePredefinida = 10;

    /** Filtro de nível atualmente escolhido (null = todos). */
    private Integer filtroNivel = null;

    /** Questão atualmente exibida no card de detalhe. */
    private QuestaoItem questaoSelecionadaNaLista = null;

    /** Conjunto (ordenado) das questões já escolhidas para compor a prova. */
    private final Set<QuestaoItem> questoesEscolhidas = new LinkedHashSet<>();

    /** Linhas da lista mapeadas por questão, para atualizar o ícone sem re-renderizar tudo. */
    private final java.util.Map<QuestaoItem, Label> iconePorQuestao = new java.util.HashMap<>();

    // ── Dados mock ──────────────────────────────────────────────────
    private final List<QuestaoItem> todasQuestoes = new ArrayList<>();
    private final List<Integer> niveisDisponiveis = Arrays.asList(1, 2, 3);

    // ================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura o ComboBox igual ao QuestoesView
        nivelCombo.setItems(FXCollections.observableArrayList(
                "Todos", "Nível 1", "Nível 2", "Nível 3"
        ));
        nivelCombo.setPromptText("nível da questão");
        nivelCombo.getSelectionModel().selectFirst();

        // Listener para quando o filtro mudar
        nivelCombo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    aplicarFiltroNivel();
                }
        );

        gerarDadosMock();
        renderizarLista();
        atualizarCardDetalhe(null);
        aplicarEstilosPadrao();
        atualizarBotoesAcao(); // Inicializa os botões como ocultos
    }

    /** Chamado pela tela anterior para definir quantas questões a prova deve ter. */
    public void setQuantidadePredefinida(int quantidade) {
        this.quantidadePredefinida = quantidade;
    }

    // ================================================================
    // ESTILOS PADRÃO
    // ================================================================

    private void aplicarEstilosPadrao() {
        // Aplica os estilos de caixinha aos labels
        lblCodigo.getStyleClass().addAll("caixinha-valor", "det-codigo");
        lblNivel.getStyleClass().add("caixinha-valor");
        lblTipo.getStyleClass().add("caixinha-valor");
        lblEnunciado.getStyleClass().add("caixinha-valor-enunciado");
        lblAssunto.getStyleClass().add("caixinha-valor");
        lblDisciplina.getStyleClass().add("caixinha-valor");
        lblStatusIcone.getStyleClass().add("status-caixinha");
        boxGabarito.getStyleClass().add("caixinha-gabarito");

        // Adiciona estilos às alternativas
        alt1a.getStyleClass().add("det-normal");
        alt1b.getStyleClass().add("det-normal");
        alt2a.getStyleClass().add("det-normal");
        alt2b.getStyleClass().add("det-normal");
    }

    // ================================================================
    // FILTRO DE NÍVEL
    // ================================================================

    private void aplicarFiltroNivel() {
        String selected = nivelCombo.getSelectionModel().getSelectedItem();
        if (selected == null || selected.equals("Todos")) {
            filtroNivel = null;
        } else if (selected.equals("Nível 1")) {
            filtroNivel = 1;
        } else if (selected.equals("Nível 2")) {
            filtroNivel = 2;
        } else if (selected.equals("Nível 3")) {
            filtroNivel = 3;
        }
        renderizarLista();
    }

    // ================================================================
    // DADOS MOCK
    // ================================================================

    private void gerarDadosMock() {
        String[] discs = {"Biologia", "Matemática", "Português", "História"};
        String[] assArr = {"fauna", "flora", "Álgebra", "Gramática"};

        for (int i = 1; i <= 12; i++) {
            int nivel = ((i - 1) % 3) + 1;
            String disc = discs[i % discs.length];
            String ass  = assArr[i % assArr.length];
            String enunciado = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum " +
                    "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum";

            List<String> alternativas;
            int respostaCorreta;

            // Cria alternativas diferentes para cada questão
            if (i % 2 == 0) {
                alternativas = Arrays.asList(
                        "a) primeira alternativa",
                        "b) segunda alternativa",
                        "c) terceira alternativa",
                        "d) quarta alternativa"
                );
                respostaCorreta = 0;
            } else {
                alternativas = Arrays.asList(
                        "a) opção A",
                        "b) opção B",
                        "c) opção C"
                );
                respostaCorreta = 1;
            }

            QuestaoItem q = new QuestaoItem(
                    "Q" + String.format("%03d", i),
                    nivel,
                    "multipla escolha",
                    enunciado,
                    alternativas,
                    respostaCorreta,
                    ass,
                    disc
            );
            todasQuestoes.add(q);
        }
    }

    // ================================================================
    // LISTA DE QUESTÕES
    // ================================================================

    private void renderizarLista() {
        listaContainer.getChildren().clear();
        iconePorQuestao.clear();

        List<QuestaoItem> filtradas = new ArrayList<>();
        for (QuestaoItem q : todasQuestoes) {
            if (filtroNivel != null && q.nivel != filtroNivel) continue;
            filtradas.add(q);
        }

        if (filtradas.isEmpty()) {
            Label vazio = new Label("Nenhuma questão encontrada.");
            vazio.setStyle("-fx-text-fill: #4a6a7a; -fx-font-size: 14px; -fx-padding: 20;");
            listaContainer.getChildren().add(vazio);
            return;
        }

        for (QuestaoItem q : filtradas) {
            StackPane row = new StackPane();
            row.getStyleClass().add("lista-item");
            row.setAlignment(Pos.CENTER_LEFT);

            HBox conteudo = new HBox();
            conteudo.setAlignment(Pos.CENTER_LEFT);
            conteudo.setSpacing(8);
            conteudo.setMaxWidth(Double.MAX_VALUE);
            StackPane.setAlignment(conteudo, Pos.CENTER_LEFT);

            Label lblTexto = new Label("Assunto: " + q.assunto);
            lblTexto.getStyleClass().add("lista-item-label");
            HBox.setHgrow(lblTexto, javafx.scene.layout.Priority.ALWAYS);

            boolean escolhida = questoesEscolhidas.contains(q);
            Label icone = new Label(escolhida ? "\u2705" : "\u2297");
            icone.getStyleClass().add(escolhida ? "lista-item-icone-on" : "lista-item-icone-off");

            javafx.scene.layout.Region espaco = new javafx.scene.layout.Region();
            HBox.setHgrow(espaco, javafx.scene.layout.Priority.ALWAYS);

            conteudo.getChildren().addAll(lblTexto, espaco, icone);
            row.getChildren().add(conteudo);

            iconePorQuestao.put(q, icone);

            row.setOnMouseClicked(e -> {
                selecionarNaLista(q, row);
                e.consume();
            });

            listaContainer.getChildren().add(row);
        }
    }

    /** Marca visualmente a linha clicada e exibe a questão no card de detalhe. */
    private void selecionarNaLista(QuestaoItem q, StackPane row) {
        for (javafx.scene.Node n : listaContainer.getChildren()) {
            n.getStyleClass().remove("lista-item-ativa");
        }
        row.getStyleClass().add("lista-item-ativa");

        questaoSelecionadaNaLista = q;
        atualizarCardDetalhe(q);
        atualizarBotoesAcao(); // Atualiza os botões baseado na seleção
    }

    // ================================================================
    // CARD DE DETALHE - VERSÃO COM CAIXINHAS
    // ================================================================

    private void atualizarCardDetalhe(QuestaoItem q) {
        if (q == null) {
            lblCodigo.setText("codigo");
            lblCodigo.getStyleClass().addAll("caixinha-valor", "det-codigo");

            lblNivel.setText("-");
            lblNivel.getStyleClass().add("caixinha-valor");

            lblTipo.setText("multipla escolha");
            lblTipo.getStyleClass().add("caixinha-valor");

            lblEnunciado.setText("Selecione uma questão na lista ao lado.");
            lblEnunciado.getStyleClass().add("caixinha-valor-enunciado");

            limparGabarito();

            lblAssunto.setText("-");
            lblAssunto.getStyleClass().add("caixinha-valor");

            lblDisciplina.setText("-");
            lblDisciplina.getStyleClass().add("caixinha-valor");

            lblStatusIcone.setText("❌");
            lblStatusIcone.getStyleClass().add("status-caixinha");

            return;
        }

        lblCodigo.setText(q.codigo);
        lblCodigo.getStyleClass().addAll("caixinha-valor", "det-codigo");

        lblNivel.setText(String.valueOf(q.nivel));
        lblNivel.getStyleClass().add("caixinha-valor");

        lblTipo.setText(q.tipo);
        lblTipo.getStyleClass().add("caixinha-valor");

        lblEnunciado.setText(q.enunciado);
        lblEnunciado.getStyleClass().add("caixinha-valor-enunciado");

        preencherGabarito(q);

        lblAssunto.setText(q.assunto);
        lblAssunto.getStyleClass().add("caixinha-valor");

        lblDisciplina.setText(q.disciplina);
        lblDisciplina.getStyleClass().add("caixinha-valor");

        atualizarStatusIcone(questoesEscolhidas.contains(q));
    }

    private void limparGabarito() {
        alt1a.setText("");
        alt1b.setText("");
        alt2a.setText("");
        alt2b.setText("");

        alt1a.getStyleClass().remove("det-gabarito");
        alt1b.getStyleClass().remove("det-gabarito");
        alt2a.getStyleClass().remove("det-gabarito");
        alt2b.getStyleClass().remove("det-gabarito");

        alt1a.getStyleClass().add("det-normal");
        alt1b.getStyleClass().add("det-normal");
        alt2a.getStyleClass().add("det-normal");
        alt2b.getStyleClass().add("det-normal");

        // Esconde as linhas vazias
        linhaAlt1.setVisible(false);
        linhaAlt1.setManaged(false);
        linhaAlt2.setVisible(false);
        linhaAlt2.setManaged(false);
    }

    private void preencherGabarito(QuestaoItem q) {
        limparGabarito();

        // Adiciona estilo de caixinha ao container do gabarito
        boxGabarito.getStyleClass().add("caixinha-gabarito");

        List<Label> alvos = Arrays.asList(alt1a, alt1b, alt2a, alt2b);
        int metade = (int) Math.ceil(q.alternativas.size() / 2.0);

        for (int i = 0; i < q.alternativas.size() && i < alvos.size(); i++) {
            Label lbl = alvos.get(i);
            lbl.setText(q.alternativas.get(i));
            lbl.getStyleClass().remove("det-normal");
            lbl.getStyleClass().remove("det-gabarito");
            lbl.getStyleClass().add("det-normal");

            if (i == q.respostaCorretaIndex) {
                lbl.getStyleClass().remove("det-normal");
                lbl.getStyleClass().add("det-gabarito");
            }

            // Define qual linha mostrar baseado na posição
            if (i < metade) {
                // Primeira linha (alt1a, alt1b)
                if (i == 0) {
                    alt1a.setText(q.alternativas.get(i));
                    alt1a.setVisible(true);
                    alt1a.setManaged(true);
                    alt1a.getStyleClass().add(i == q.respostaCorretaIndex ? "det-gabarito" : "det-normal");
                } else if (i == 1) {
                    alt1b.setText(q.alternativas.get(i));
                    alt1b.setVisible(true);
                    alt1b.setManaged(true);
                    alt1b.getStyleClass().add(i == q.respostaCorretaIndex ? "det-gabarito" : "det-normal");
                }
            } else {
                // Segunda linha (alt2a, alt2b)
                if (i == metade) {
                    alt2a.setText(q.alternativas.get(i));
                    alt2a.setVisible(true);
                    alt2a.setManaged(true);
                    alt2a.getStyleClass().add(i == q.respostaCorretaIndex ? "det-gabarito" : "det-normal");
                } else if (i == metade + 1) {
                    alt2b.setText(q.alternativas.get(i));
                    alt2b.setVisible(true);
                    alt2b.setManaged(true);
                    alt2b.getStyleClass().add(i == q.respostaCorretaIndex ? "det-gabarito" : "det-normal");
                }
            }
        }

        // Mostra as linhas que têm conteúdo
        boolean temAlt1 = !alt1a.getText().isEmpty() || !alt1b.getText().isEmpty();
        boolean temAlt2 = !alt2a.getText().isEmpty() || !alt2b.getText().isEmpty();

        linhaAlt1.setVisible(temAlt1);
        linhaAlt1.setManaged(temAlt1);
        linhaAlt2.setVisible(temAlt2);
        linhaAlt2.setManaged(temAlt2);
    }

    private void atualizarStatusIcone(boolean escolhida) {
        lblStatusIcone.setText(escolhida ? "\u2705" : "\u274C");
        lblStatusIcone.getStyleClass().add("status-caixinha");
    }

    // ================================================================
    // CONTROLE DOS BOTÕES (Dinâmico)
    // ================================================================

    /**
     * Atualiza a visibilidade dos botões baseado no estado atual:
     * - Se nenhuma questão está selecionada: ambos ocultos
     * - Se a questão não está selecionada: mostra apenas "SELECIONAR"
     * - Se a questão já está selecionada: mostra apenas "DESELECIONAR"
     */
    private void atualizarBotoesAcao() {
        boolean temQuestao = questaoSelecionadaNaLista != null;
        boolean escolhida = temQuestao && questoesEscolhidas.contains(questaoSelecionadaNaLista);

        // Botão SELECIONAR: visível apenas quando há questão NÃO selecionada
        boolean mostrarSelecionar = temQuestao && !escolhida;
        btnSelecionar.setVisible(mostrarSelecionar);
        btnSelecionar.setManaged(mostrarSelecionar);

        // Botão DESELECIONAR: visível apenas quando há questão JÁ selecionada
        boolean mostrarDeselecionar = temQuestao && escolhida;
        btnDeselecionar.setVisible(mostrarDeselecionar);
        btnDeselecionar.setManaged(mostrarDeselecionar);
    }

    // ================================================================
    // SELECIONAR / DESELECIONAR
    // ================================================================

    @FXML
    private void handleSelecionar(MouseEvent event) {
        event.consume();
        if (questaoSelecionadaNaLista == null) return;
        if (questoesEscolhidas.contains(questaoSelecionadaNaLista)) return;

        if (questoesEscolhidas.size() >= quantidadePredefinida) {
            alerta("Limite atingido",
                    "Você já selecionou o número máximo de questões definido (" +
                            quantidadePredefinida + ").");
            return;
        }

        questoesEscolhidas.add(questaoSelecionadaNaLista);
        atualizarIconeLista(questaoSelecionadaNaLista, true);
        atualizarStatusIcone(true);
        atualizarBotoesAcao(); // Atualiza os botões após selecionar
    }

    @FXML
    private void handleDeselecionar(MouseEvent event) {
        event.consume();
        if (questaoSelecionadaNaLista == null) return;
        if (!questoesEscolhidas.contains(questaoSelecionadaNaLista)) return;

        questoesEscolhidas.remove(questaoSelecionadaNaLista);
        atualizarIconeLista(questaoSelecionadaNaLista, false);
        atualizarStatusIcone(false);
        atualizarBotoesAcao(); // Atualiza os botões após deselecionar
    }

    private void atualizarIconeLista(QuestaoItem q, boolean escolhida) {
        Label icone = iconePorQuestao.get(q);
        if (icone == null) return;
        icone.setText(escolhida ? "\u2705" : "\u2297");
        icone.getStyleClass().remove("lista-item-icone-on");
        icone.getStyleClass().remove("lista-item-icone-off");
        icone.getStyleClass().add(escolhida ? "lista-item-icone-on" : "lista-item-icone-off");
    }

    // ================================================================
    // GERAR PROVA
    // ================================================================

    @FXML
    private void handleGerar(MouseEvent event) {
        event.consume();
        if (questoesEscolhidas.isEmpty()) {
            alerta("Gerar Prova", "Selecione ao menos uma questão antes de gerar a prova.");
            return;
        }
        alerta("Gerar Prova",
                "Prova gerada com " + questoesEscolhidas.size() + " de " +
                        quantidadePredefinida + " questões selecionadas.");
        // TODO: integrar com a geração/exportação real da prova.
    }

    // ================================================================
    // NAVEGAÇÃO SIDEBAR
    // ================================================================

    @FXML
    private void handleVoltar(MouseEvent e) {
        navegarPara("TelaGerarProvaView", "TelaGerarProvaStyle");
    }

    @FXML
    private void handleMenuDisciplinas(MouseEvent e) {
        navegarPara("TelaInicialView", "TelaInicialStyle");
    }

    @FXML
    private void handleMenuBuscar(MouseEvent e) {
        navegarPara("TelaBuscarView", "TelaBuscarStyle");
    }

    @FXML
    private void handleMenuGerarProva(MouseEvent e) {
        navegarPara("TelaGerarProvaView", "TelaGerarProvaStyle");
    }

    @FXML
    private void handleMenuRelatorio(MouseEvent e) {
        alerta("Relatório", "Em breve!");
    }

    @FXML
    private void handleMenuProvas(MouseEvent e) {
        alerta("Provas", "Em breve!");
    }

    private void navegarPara(String nomeView, String nomeCSS) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/aplicativo/views/" + nomeView + ".fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1280, 750);

            URL cssUrl = getClass().getResource("/br/edu/ufersa/aplicativo/css/" + nomeCSS + ".css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = (Stage) listaContainer.getScene().getWindow();
            boolean fs  = stage.isFullScreen();
            boolean max = stage.isMaximized();

            stage.setScene(scene);
            stage.setTitle("Gerador de Provas");

            if (fs)  stage.setFullScreen(true);
            if (max) stage.setMaximized(true);

        } catch (IOException ex) {
            ex.printStackTrace();
            alerta("Erro", "Não foi possível navegar para a tela solicitada.");
        }
    }

    private void alerta(String titulo, String msg) {
        javafx.scene.control.Alert a = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    // ================================================================
    // MODELO DE DADOS
    // ================================================================

    public static class QuestaoItem {
        final String codigo;
        final int nivel;
        final String tipo;
        final String enunciado;
        final List<String> alternativas;
        final int respostaCorretaIndex;
        final String assunto;
        final String disciplina;

        public QuestaoItem(String codigo, int nivel, String tipo, String enunciado,
                           List<String> alternativas, int respostaCorretaIndex,
                           String assunto, String disciplina) {
            this.codigo = codigo;
            this.nivel = nivel;
            this.tipo = tipo;
            this.enunciado = enunciado;
            this.alternativas = alternativas;
            this.respostaCorretaIndex = respostaCorretaIndex;
            this.assunto = assunto;
            this.disciplina = disciplina;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QuestaoItem that = (QuestaoItem) o;
            return codigo.equals(that.codigo);
        }

        @Override
        public int hashCode() {
            return codigo.hashCode();
        }
    }
}