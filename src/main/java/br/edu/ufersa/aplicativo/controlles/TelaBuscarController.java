package br.edu.ufersa.aplicativo.controlles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TelaBuscarController implements Initializable {

    // ── FXML ────────────────────────────────────────────────────────
    @FXML private StackPane centerRoot;

    // Lista
    @FXML private VBox listaContainer;

    // Filtro principal
    @FXML private StackPane selectorTipo;
    @FXML private Label     selectorTipoLabel;
    @FXML private HBox      boxTipoBotoes;
    @FXML private StackPane btnProva;
    @FXML private StackPane btnQuestoes;

    // Sub-filtros
    @FXML private VBox subfiltroProva;
    @FXML private VBox subfiltroQuestoes;

    // Chips Prova
    @FXML private StackPane chipDisciplinaProva;
    @FXML private StackPane chipSemestre;

    // Chips Questões
    @FXML private StackPane chipDisciplinaQuest;
    @FXML private StackPane chipAssunto;
    @FXML private StackPane chipDificuldade;

    // Popups flutuantes
    @FXML private VBox popupTipo;
    @FXML private VBox popupDisciplinaProva;
    @FXML private VBox popupSemestre;
    @FXML private VBox popupDisciplinaQuest;
    @FXML private VBox popupAssunto;
    @FXML private VBox popupDificuldade;

    // Menu sidebar
    @FXML private StackPane menuDisciplinas;
    @FXML private StackPane menuBuscar;
    @FXML private StackPane menuGerarProva;
    @FXML private StackPane menuRelatorio;
    @FXML private StackPane menuProvas;

    // ── Estado ──────────────────────────────────────────────────────
    private enum ModoBusca { NENHUM, PROVA, QUESTOES }
    private ModoBusca modoAtual = ModoBusca.NENHUM;

    // Filtros ativos
    private String filtroTipo        = null; // "Prova" | "Questões"
    private String filtroDisciplina  = null;
    private String filtroSemestre    = null;
    private String filtroAssunto     = null;
    private String filtroDificuldade = null;

    // Chips com estado ativo
    private final List<StackPane> chipsAtivos = new ArrayList<>();

    // Popup visível no momento
    private VBox popupAtual = null;

    // ── Dados mock ──────────────────────────────────────────────────
    private final List<String> disciplinas = Arrays.asList(
            "Matemática", "Português", "História",
            "Geografia", "Física", "Química", "Biologia"
    );

    private final List<String> semestres = Arrays.asList(
            "2024.1", "2024.2", "2025.1", "2025.2"
    );

    private final List<String> assuntos = Arrays.asList(
            "Álgebra", "Geometria", "Trigonometria",
            "Literatura", "Gramática", "Revolução Industrial"
    );

    // Itens da lista (mock)
    private final List<ItemBusca> todosItens = new ArrayList<>();
    private final List<ItemBusca> itensFiltrados = new ArrayList<>();

    // ================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gerarDadosMock();
        preencherPopupsDisciplina();
        preencherPopupSemestre();
        preencherPopupAssunto();
        preencherPopupDificuldade();
        atualizarLista();
    }

    // ================================================================
    // DADOS MOCK
    // ================================================================

    private void gerarDadosMock() {
        String[] tipos  = {"Prova", "Questão"};
        String[] discs  = {"Matemática", "Português", "História", "Física"};
        String[] sems   = {"2024.1", "2024.2", "2025.1"};
        String[] diffs  = {"Fácil", "Médio", "Difícil"};
        String[] assArr = {"Álgebra", "Gramática", "Revolução Industrial", "Cinemática"};

        for (int i = 1; i <= 24; i++) {
            String tipo = tipos[i % 2];
            String disc = discs[i % discs.length];
            String sem  = sems[i % sems.length];
            String diff = diffs[i % diffs.length];
            String ass  = assArr[i % assArr.length];
            todosItens.add(new ItemBusca(
                    tipo + " " + i + " — " + disc,
                    tipo, disc, sem, ass, diff
            ));
        }
        itensFiltrados.addAll(todosItens);
    }

    // ================================================================
    // PREENCHIMENTO DOS POPUPS COM DADOS
    // ================================================================

    private void preencherPopupsDisciplina() {
        preencherPopupLista(popupDisciplinaProva, disciplinas, opcao -> {
            filtroDisciplina = opcao;
            atualizarLabelChip(chipDisciplinaProva, "disciplina", opcao);
            fecharPopupAtual();
            atualizarLista();
        });

        preencherPopupLista(popupDisciplinaQuest, disciplinas, opcao -> {
            filtroDisciplina = opcao;
            atualizarLabelChip(chipDisciplinaQuest, "disciplina", opcao);
            fecharPopupAtual();
            atualizarLista();
        });
    }

    private void preencherPopupSemestre() {
        preencherPopupLista(popupSemestre, semestres, opcao -> {
            filtroSemestre = opcao;
            atualizarLabelChip(chipSemestre, "semestre", opcao);
            fecharPopupAtual();
            atualizarLista();
        });
    }

    private void preencherPopupAssunto() {
        preencherPopupLista(popupAssunto, assuntos, opcao -> {
            filtroAssunto = opcao;
            atualizarLabelChip(chipAssunto, "assuntos", opcao);
            fecharPopupAtual();
            atualizarLista();
        });
    }

    private void preencherPopupDificuldade() {
        // já declarado no FXML, só precisamos conectar ação
        for (javafx.scene.Node n : popupDificuldade.getChildren()) {
            if (n instanceof Label l && l.getStyleClass().contains("popup-opcao")) {
                l.setOnMouseClicked(e -> {
                    filtroDificuldade = l.getText();
                    atualizarLabelChip(chipDificuldade, "dificuldade", l.getText());
                    fecharPopupAtual();
                    atualizarLista();
                    e.consume();
                });
            }
        }
    }

    /** Popula um popup com uma lista de strings e uma ação por item */
    private void preencherPopupLista(VBox popup, List<String> itens, java.util.function.Consumer<String> acao) {
        popup.getChildren().clear();
        // Opção "Todos" para limpar filtro
        Label todos = new Label("— Todos —");
        todos.getStyleClass().add("popup-opcao");
        todos.setOnMouseClicked(e -> { acao.accept(null); e.consume(); });
        popup.getChildren().add(todos);

        for (String item : itens) {
            Label l = new Label(item);
            l.getStyleClass().add("popup-opcao");
            l.setOnMouseClicked(e -> { acao.accept(item); e.consume(); });
            popup.getChildren().add(l);
        }
    }

    // ================================================================
    // POPUP PRINCIPAL (Prova / Questões)
    // ================================================================

    @FXML
    private void handleAbrirPopupTipo(MouseEvent event) {
        event.consume();
        togglePopup(popupTipo, selectorTipo);
    }

    @FXML
    private void handlePopupEscolherProva(MouseEvent event) {
        event.consume();
        selecionarModo(ModoBusca.PROVA, "Prova");
        fecharPopupAtual();
    }

    @FXML
    private void handlePopupEscolherQuestoes(MouseEvent event) {
        event.consume();
        selecionarModo(ModoBusca.QUESTOES, "Questões");
        fecharPopupAtual();
    }

    // ================================================================
    // BOTÕES PROVA / QUESTÕES (mostrados após selecionar no popup)
    // ================================================================

    @FXML
    private void handleSelecionarProva(MouseEvent event) {
        event.consume();
        selecionarModo(ModoBusca.PROVA, "Prova");
    }

    @FXML
    private void handleSelecionarQuestoes(MouseEvent event) {
        event.consume();
        selecionarModo(ModoBusca.QUESTOES, "Questões");
    }

    /** Aplica modo e atualiza visibilidade de sub-filtros e botões */
    private void selecionarModo(ModoBusca modo, String labelTexto) {
        modoAtual = modo;
        filtroTipo        = labelTexto;
        filtroDisciplina  = null;
        filtroSemestre    = null;
        filtroAssunto     = null;
        filtroDificuldade = null;

        // Label do selector
        selectorTipoLabel.setText(labelTexto);
        selectorTipoLabel.getStyleClass().remove("filtro-selector-label");
        if (!selectorTipoLabel.getStyleClass().contains("filtro-selector-label-active"))
            selectorTipoLabel.getStyleClass().add("filtro-selector-label-active");

        // Mostra botões Prova/Questões
        boxTipoBotoes.setVisible(true);
        boxTipoBotoes.setManaged(true);

        // Destaque no botão ativo
        resetEstiloBotoes();
        if (modo == ModoBusca.PROVA) {
            ativarBotaoTipo(btnProva);
            subfiltroProva.setVisible(true);
            subfiltroProva.setManaged(true);
            subfiltroQuestoes.setVisible(false);
            subfiltroQuestoes.setManaged(false);
        } else {
            ativarBotaoTipo(btnQuestoes);
            subfiltroQuestoes.setVisible(true);
            subfiltroQuestoes.setManaged(true);
            subfiltroProva.setVisible(false);
            subfiltroProva.setManaged(false);
        }

        // Reseta chips
        resetarChips();
        atualizarLista();
    }

    // ================================================================
    // CHIPS DE SUB-FILTRO
    // ================================================================

    @FXML
    private void handleToggleChipDisciplinaProva(MouseEvent event) {
        event.consume();
        if (chipAtivo(chipDisciplinaProva)) {
            desativarChip(chipDisciplinaProva, "disciplina");
            filtroDisciplina = null;
            fecharPopupAtual();
            atualizarLista();
        } else {
            ativarChip(chipDisciplinaProva);
            togglePopup(popupDisciplinaProva, chipDisciplinaProva);
        }
    }

    @FXML
    private void handleToggleChipSemestre(MouseEvent event) {
        event.consume();
        if (chipAtivo(chipSemestre)) {
            desativarChip(chipSemestre, "semestre");
            filtroSemestre = null;
            fecharPopupAtual();
            atualizarLista();
        } else {
            ativarChip(chipSemestre);
            togglePopup(popupSemestre, chipSemestre);
        }
    }

    @FXML
    private void handleToggleChipDisciplinaQuest(MouseEvent event) {
        event.consume();
        if (chipAtivo(chipDisciplinaQuest)) {
            desativarChip(chipDisciplinaQuest, "disciplina");
            filtroDisciplina = null;
            fecharPopupAtual();
            atualizarLista();
        } else {
            ativarChip(chipDisciplinaQuest);
            togglePopup(popupDisciplinaQuest, chipDisciplinaQuest);
        }
    }

    @FXML
    private void handleToggleChipAssunto(MouseEvent event) {
        event.consume();
        if (chipAtivo(chipAssunto)) {
            desativarChip(chipAssunto, "assuntos");
            filtroAssunto = null;
            fecharPopupAtual();
            atualizarLista();
        } else {
            ativarChip(chipAssunto);
            togglePopup(popupAssunto, chipAssunto);
        }
    }

    @FXML
    private void handleToggleChipDificuldade(MouseEvent event) {
        event.consume();
        if (chipAtivo(chipDificuldade)) {
            desativarChip(chipDificuldade, "dificuldade");
            filtroDificuldade = null;
            fecharPopupAtual();
            atualizarLista();
        } else {
            ativarChip(chipDificuldade);
            togglePopup(popupDificuldade, chipDificuldade);
        }
    }

    @FXML
    private void handleEscolherDificuldade(MouseEvent event) {
        // tratado via preencherPopupDificuldade()
    }

    // ================================================================
    // FECHAR POPUPS AO CLICAR NO FUNDO
    // ================================================================

    @FXML
    private void handleFecharTodosPopups(MouseEvent event) {
        fecharPopupAtual();
    }

    /** Consome o clique dentro do popup para que não propague ao fundo */
    @FXML
    private void handleConsumir(MouseEvent event) {
        event.consume();
    }

    // ================================================================
    // LISTA / FILTRO
    // ================================================================

    private void atualizarLista() {
        itensFiltrados.clear();

        for (ItemBusca item : todosItens) {
            if (filtroTipo != null && !item.tipo.equals(filtroTipo))         continue;
            if (filtroDisciplina != null && !item.disciplina.equals(filtroDisciplina)) continue;
            if (filtroSemestre != null && !item.semestre.equals(filtroSemestre))       continue;
            if (filtroAssunto != null && !item.assunto.equals(filtroAssunto))          continue;
            if (filtroDificuldade != null && !item.dificuldade.equals(filtroDificuldade)) continue;
            itensFiltrados.add(item);
        }

        renderizarLista();
    }

    private void renderizarLista() {
        listaContainer.getChildren().clear();

        if (itensFiltrados.isEmpty()) {
            Label vazio = new Label("Nenhum resultado encontrado.");
            vazio.setStyle("-fx-text-fill: #4a6a7a; -fx-font-size: 14px; -fx-padding: 20;");
            listaContainer.getChildren().add(vazio);
            return;
        }

        for (ItemBusca item : itensFiltrados) {
            StackPane row = new StackPane();
            row.getStyleClass().add("lista-item");
            row.setAlignment(Pos.CENTER_LEFT);

            Label lbl = new Label(item.descricao);
            lbl.getStyleClass().add("lista-item-label");

            row.getChildren().add(lbl);

            // ═══════════════════════════════════════════════════════════════
            // CORREÇÃO: Clique no item
            // ═══════════════════════════════════════════════════════════════
            row.setOnMouseClicked(e -> {
                // Remove seleção anterior
                for (javafx.scene.Node n : listaContainer.getChildren()) {
                    n.getStyleClass().remove("lista-item-selected");
                    // Verifica se o nó é um StackPane e tem filhos
                    if (n instanceof StackPane) {
                        StackPane sp = (StackPane) n;
                        for (javafx.scene.Node child : sp.getChildren()) {
                            if (child instanceof Label) {
                                Label ll = (Label) child;
                                ll.getStyleClass().remove("lista-item-label-selected");
                                if (!ll.getStyleClass().contains("lista-item-label")) {
                                    ll.getStyleClass().add("lista-item-label");
                                }
                            }
                        }
                    }
                }

                // Marca o item atual como selecionado
                row.getStyleClass().add("lista-item-selected");
                lbl.getStyleClass().remove("lista-item-label");
                lbl.getStyleClass().add("lista-item-label-selected");

                // Mostra qual item foi clicado
                System.out.println("Item selecionado: " + item.descricao);

                e.consume();
            });

            listaContainer.getChildren().add(row);
        }
    }

    // ================================================================
    // GERENCIAMENTO DE POPUPS
    // ================================================================

    /**
     * Abre o popup se estiver fechado; fecha se estiver aberto.
     * Posiciona o popup abaixo do nó âncora dentro do StackPane central.
     */
    private void togglePopup(VBox popup, javafx.scene.Node ancora) {
        if (popupAtual != null && popupAtual != popup) {
            fecharPopup(popupAtual);
        }

        boolean estaAberto = popup.isVisible();
        if (estaAberto) {
            fecharPopup(popup);
        } else {
            abrirPopup(popup, ancora);
        }
    }

    private void abrirPopup(VBox popup, javafx.scene.Node ancora) {
        popup.setVisible(true);
        popup.setManaged(true);
        popupAtual = popup;

        // Posicionar via translate — calcula offset relativo ao centerRoot
        javafx.application.Platform.runLater(() -> {
            javafx.geometry.Bounds bounds = ancora.localToScene(ancora.getBoundsInLocal());
            javafx.geometry.Bounds rootB  = centerRoot.localToScene(centerRoot.getBoundsInLocal());

            double tx = bounds.getMinX() - rootB.getMinX();
            double ty = bounds.getMaxY() - rootB.getMinY() + 4;

            // Garante que não sai da tela pela direita
            double popupW = popup.prefWidth(-1);
            if (popupW <= 0) popupW = 200;
            double maxX = centerRoot.getWidth() - popupW - 10;
            if (tx > maxX) tx = maxX;

            // Traduz o popup para a posição correta dentro do StackPane
            StackPane.setAlignment(popup, javafx.geometry.Pos.TOP_LEFT);
            popup.setTranslateX(tx);
            popup.setTranslateY(ty);
        });
    }

    private void fecharPopup(VBox popup) {
        popup.setVisible(false);
        popup.setManaged(false);
        if (popupAtual == popup) popupAtual = null;
    }

    private void fecharPopupAtual() {
        if (popupAtual != null) fecharPopup(popupAtual);
    }

    // ================================================================
    // HELPERS DE ESTILO
    // ================================================================

    private void resetEstiloBotoes() {
        for (StackPane btn : List.of(btnProva, btnQuestoes)) {
            btn.getStyleClass().remove("tipo-btn-selected");
            if (btn.getChildren().get(0) instanceof Label l) {
                l.getStyleClass().remove("tipo-btn-label-selected");
                if (!l.getStyleClass().contains("tipo-btn-label"))
                    l.getStyleClass().add("tipo-btn-label");
            }
        }
    }

    private void ativarBotaoTipo(StackPane btn) {
        btn.getStyleClass().add("tipo-btn-selected");
        if (btn.getChildren().get(0) instanceof Label l) {
            l.getStyleClass().remove("tipo-btn-label");
            l.getStyleClass().add("tipo-btn-label-selected");
        }
    }

    private boolean chipAtivo(StackPane chip) {
        return chipsAtivos.contains(chip);
    }

    private void ativarChip(StackPane chip) {
        if (!chipsAtivos.contains(chip)) chipsAtivos.add(chip);
        chip.getStyleClass().add("chip-filtro-ativo");
        if (chip.getChildren().get(0) instanceof Label l) {
            l.getStyleClass().add("chip-label-ativo");
        }
    }

    private void desativarChip(StackPane chip, String labelOriginal) {
        chipsAtivos.remove(chip);
        chip.getStyleClass().remove("chip-filtro-ativo");
        if (chip.getChildren().get(0) instanceof Label l) {
            l.getStyleClass().remove("chip-label-ativo");
            l.setText(labelOriginal);
        }
    }

    private void atualizarLabelChip(StackPane chip, String prefixo, String valor) {
        if (chip.getChildren().get(0) instanceof Label l) {
            l.setText(valor != null ? valor : prefixo);
        }
    }

    private void resetarChips() {
        for (StackPane chip : List.of(chipDisciplinaProva, chipSemestre,
                chipDisciplinaQuest, chipAssunto, chipDificuldade)) {
            chip.getStyleClass().remove("chip-filtro-ativo");
            if (chip.getChildren().get(0) instanceof Label l) {
                l.getStyleClass().remove("chip-label-ativo");
            }
        }
        chipsAtivos.clear();
        // Restaura labels originais
        setChipLabel(chipDisciplinaProva, "disciplina");
        setChipLabel(chipSemestre,        "semestre");
        setChipLabel(chipDisciplinaQuest, "disciplina");
        setChipLabel(chipAssunto,         "assuntos");
        setChipLabel(chipDificuldade,     "dificuldade");
    }

    private void setChipLabel(StackPane chip, String texto) {
        if (chip.getChildren().get(0) instanceof Label l) l.setText(texto);
    }

    // ================================================================
    // NAVEGAÇÃO SIDEBAR
    // ================================================================

    @FXML private void handleVoltar(MouseEvent e)           { navegarPara("TelaInicialView"); }
    @FXML private void handleMenuDisciplinas(MouseEvent e)  { navegarPara("TelaInicialView"); }
    @FXML private void handleMenuBuscar(MouseEvent e)       { /* já estou aqui */ }
    @FXML private void handleMenuGerarProva(MouseEvent e)   { navegarPara("TelaGerarProvaView"); }
    @FXML private void handleMenuRelatorio(MouseEvent e)    { alerta("Relatório",   "Em breve!"); }
    @FXML private void handleMenuProvas(MouseEvent e)       { alerta("Provas",      "Em breve!"); }

    private void navegarPara(String nomeView) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/aplicativo/views/" + nomeView + ".fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) listaContainer.getScene().getWindow();
            boolean fs  = stage.isFullScreen();
            boolean max = stage.isMaximized();
            stage.setScene(new Scene(root, 1280, 750));
            if (fs)  stage.setFullScreen(true);
            if (max) stage.setMaximized(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void alerta(String titulo, String msg) {
        javafx.scene.control.Alert a = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    // ================================================================
    // MODELO DE DADOS
    // ================================================================

    public static class ItemBusca {
        final String descricao;
        final String tipo;        // "Prova" | "Questão"
        final String disciplina;
        final String semestre;
        final String assunto;
        final String dificuldade; // "Fácil" | "Médio" | "Difícil"

        public ItemBusca(String descricao, String tipo, String disciplina,
                         String semestre, String assunto, String dificuldade) {
            this.descricao   = descricao;
            this.tipo        = tipo;
            this.disciplina  = disciplina;
            this.semestre    = semestre;
            this.assunto     = assunto;
            this.dificuldade = dificuldade;
        }
    }
}