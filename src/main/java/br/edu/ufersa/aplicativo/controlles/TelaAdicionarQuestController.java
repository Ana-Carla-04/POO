package br.edu.ufersa.aplicativo.controlles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaAdicionarQuestController implements Initializable {

    // ── FXML fixos ──────────────────────────────────────────────────
    @FXML private TextField    fieldCodigo;
    @FXML private StackPane    tipoSelector;
    @FXML private Label        tipoLabel;
    @FXML private VBox         camposDinamicos;
    @FXML private HBox         boxBtnAdicionar;
    @FXML private Button       btnAdicionar;

    // Popup de tipo
    @FXML private VBox  tipoPopup;
    @FXML private Label popupOpcaoMultipla;
    @FXML private Label popupOpcaoDiscursiva;
    @FXML private Label popupOpcaoVF;

    // Menu sidebar
    @FXML private StackPane menuDisciplinas;
    @FXML private StackPane menuBuscar;
    @FXML private StackPane menuGerarProva;
    @FXML private StackPane menuRelatorio;
    @FXML private StackPane menuProvas;

    // ── Estado ──────────────────────────────────────────────────────
    private enum TipoQuestao { MULTIPLA_ESCOLHA, DISCURSIVA, VERDADEIRO_FALSO, NENHUM }
    private TipoQuestao tipoAtual = TipoQuestao.NENHUM;

    // Campos dinâmicos reutilizados
    private TextField   fieldEnunciado;
    private TextField   fieldGabarito;
    private TextField   fieldDisciplina;
    private TextField   fieldAssunto;
    private ToggleGroup grupoGabarito;
    private ToggleGroup grupoNivel;

    // Alternativas (múltipla escolha)
    private TextField fieldAltA, fieldAltB, fieldAltC, fieldAltD;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialização vazia
    }

    // ================================================================
    // POPUP DE TIPO
    // ================================================================

    @FXML
    private void handleAbrirTipoPopup(MouseEvent event) {
        event.consume();
        boolean estaAberto = tipoPopup.isVisible();
        tipoPopup.setVisible(!estaAberto);
        tipoPopup.setManaged(!estaAberto);
    }

    @FXML
    private void handleSelecionarMultipla(MouseEvent event) {
        event.consume();
        fecharPopup();
        aplicarTipo(TipoQuestao.MULTIPLA_ESCOLHA);
    }

    @FXML
    private void handleSelecionarDiscursiva(MouseEvent event) {
        event.consume();
        fecharPopup();
        aplicarTipo(TipoQuestao.DISCURSIVA);
    }

    @FXML
    private void handleSelecionarVF(MouseEvent event) {
        event.consume();
        fecharPopup();
        aplicarTipo(TipoQuestao.VERDADEIRO_FALSO);
    }

    private void fecharPopup() {
        tipoPopup.setVisible(false);
        tipoPopup.setManaged(false);
    }

    // ================================================================
    // MONTAGEM DINÂMICA DOS CAMPOS
    // ================================================================

    private void aplicarTipo(TipoQuestao tipo) {
        tipoAtual = tipo;
        camposDinamicos.getChildren().clear();

        switch (tipo) {
            case MULTIPLA_ESCOLHA  -> setTipoLabel("multipla escolha");
            case DISCURSIVA        -> setTipoLabel("discursiva");
            case VERDADEIRO_FALSO  -> setTipoLabel("verdadeiro ou falso");
        }

        // Recria os campos comuns
        fieldEnunciado  = criarTextField(340);
        fieldDisciplina = criarTextField(180);
        fieldAssunto    = criarTextField(180);
        grupoNivel      = new ToggleGroup();

        switch (tipo) {
            case MULTIPLA_ESCOLHA  -> montarCamposMultipla();
            case DISCURSIVA        -> montarCamposDiscursiva();
            case VERDADEIRO_FALSO  -> montarCamposVF();
        }

        boxBtnAdicionar.setVisible(true);
        boxBtnAdicionar.setManaged(true);
    }

    // ── Múltipla Escolha ────────────────────────────────────────────
    private void montarCamposMultipla() {
        // Criar campos de texto para as alternativas
        fieldAltA = criarTextField(280);
        fieldAltB = criarTextField(280);
        fieldAltC = criarTextField(280);
        fieldAltD = criarTextField(280);

        // Criar grupo para os RadioButtons do gabarito
        grupoGabarito = new ToggleGroup();

        RadioButton rbA = criarRadio("a", grupoGabarito);
        RadioButton rbB = criarRadio("b", grupoGabarito);
        RadioButton rbC = criarRadio("c", grupoGabarito);
        RadioButton rbD = criarRadio("d", grupoGabarito);

        // ── 1. ENUNCIADO ──────────────────────────────────────────────
        camposDinamicos.getChildren().add(labeledField("Enunciado:", fieldEnunciado));

        // ── 2. ALTERNATIVAS ───────────────────────────────────────────
        VBox colAlternativas = new VBox(4);
        colAlternativas.getChildren().add(label("alternativas:"));
        colAlternativas.getChildren().add(altRow("a:", fieldAltA));
        colAlternativas.getChildren().add(altRow("b:", fieldAltB));
        colAlternativas.getChildren().add(altRow("c:", fieldAltC));
        colAlternativas.getChildren().add(altRow("d:", fieldAltD));
        camposDinamicos.getChildren().add(colAlternativas);

        // ── 3. GABARITO ───────────────────────────────────────────────
        HBox linhaGabarito = new HBox(12);
        linhaGabarito.setAlignment(Pos.CENTER_LEFT);
        Label gabLabel = new Label("Gabarito:");
        gabLabel.getStyleClass().add("field-label");
        linhaGabarito.getChildren().addAll(gabLabel, rbA, rbB, rbC, rbD);
        camposDinamicos.getChildren().add(linhaGabarito);

        // ── 4. ESPAÇO ──────────────────────────────────────────────────
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        camposDinamicos.getChildren().add(spacer);

        // ── 5. DISCIPLINA + ASSUNTO (lado a lado) ─────────────────────
        HBox linhaDiscAssunto = new HBox(30);
        linhaDiscAssunto.setAlignment(Pos.TOP_LEFT);

        VBox colDisc = new VBox(4);
        colDisc.getChildren().addAll(label("Disciplina:"), fieldDisciplina);

        VBox colAssunto = new VBox(4);
        colAssunto.getChildren().addAll(label("Assunto:"), fieldAssunto);

        linhaDiscAssunto.getChildren().addAll(colDisc, colAssunto);
        camposDinamicos.getChildren().add(linhaDiscAssunto);

        // ── 6. NÍVEL DA DIFICULDADE (ÚLTIMO) ──────────────────────────
        camposDinamicos.getChildren().add(criarNivelRow());
    }

    // ── Discursiva ───────────────────────────────────────────────────
    private void montarCamposDiscursiva() {
        fieldGabarito = criarTextField(340);

        // ── 1. ENUNCIADO ──────────────────────────────────────────────
        camposDinamicos.getChildren().add(labeledField("Enunciado:", fieldEnunciado));

        // ── 2. GABARITO ───────────────────────────────────────────────
        camposDinamicos.getChildren().add(labeledField("Gabarito:", fieldGabarito));

        // ── 3. ESPAÇO ──────────────────────────────────────────────────
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        camposDinamicos.getChildren().add(spacer);

        // ── 4. DISCIPLINA + ASSUNTO (lado a lado) ─────────────────────
        HBox linhaDiscAssunto = new HBox(30);
        linhaDiscAssunto.setAlignment(Pos.TOP_LEFT);

        VBox colDisc = new VBox(4);
        colDisc.getChildren().addAll(label("Disciplina:"), fieldDisciplina);

        VBox colAssunto = new VBox(4);
        colAssunto.getChildren().addAll(label("Assunto:"), fieldAssunto);

        linhaDiscAssunto.getChildren().addAll(colDisc, colAssunto);
        camposDinamicos.getChildren().add(linhaDiscAssunto);

        // ── 5. NÍVEL DA DIFICULDADE (ÚLTIMO) ──────────────────────────
        camposDinamicos.getChildren().add(criarNivelRow());
    }

    // ── Verdadeiro ou Falso ─────────────────────────────────────────
    private void montarCamposVF() {
        grupoGabarito = new ToggleGroup();
        RadioButton rbFalso      = criarRadio("Falso",      grupoGabarito);
        RadioButton rbVerdadeiro = criarRadio("Verdadeiro", grupoGabarito);

        // ── 1. ENUNCIADO ──────────────────────────────────────────────
        camposDinamicos.getChildren().add(labeledField("Enunciado:", fieldEnunciado));

        // ── 2. GABARITO ───────────────────────────────────────────────
        VBox vbGab = new VBox(5);
        vbGab.getChildren().add(label("Gabarito:"));
        HBox radios = new HBox(16, rbFalso, rbVerdadeiro);
        radios.setAlignment(Pos.CENTER_LEFT);
        vbGab.getChildren().add(radios);
        camposDinamicos.getChildren().add(vbGab);

        // ── 3. ESPAÇO ──────────────────────────────────────────────────
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        camposDinamicos.getChildren().add(spacer);

        // ── 4. DISCIPLINA + ASSUNTO (lado a lado) ─────────────────────
        HBox linhaDiscAssunto = new HBox(30);
        linhaDiscAssunto.setAlignment(Pos.TOP_LEFT);

        VBox colDisc = new VBox(4);
        colDisc.getChildren().addAll(label("Disciplina:"), fieldDisciplina);

        VBox colAssunto = new VBox(4);
        colAssunto.getChildren().addAll(label("Assunto:"), fieldAssunto);

        linhaDiscAssunto.getChildren().addAll(colDisc, colAssunto);
        camposDinamicos.getChildren().add(linhaDiscAssunto);

        // ── 5. NÍVEL DA DIFICULDADE (ÚLTIMO) ──────────────────────────
        camposDinamicos.getChildren().add(criarNivelRow());
    }

    // ── Nível da dificuldade (separado) ─────────────────────────────
    private HBox criarNivelRow() {
        HBox hb = new HBox(12);
        hb.setAlignment(Pos.CENTER_LEFT);

        Label nivelLabel = new Label("Nível da dificuldade:");
        nivelLabel.getStyleClass().add("field-label");

        RadioButton rbFacil  = criarRadio("Fácil",  grupoNivel);
        RadioButton rbMedio  = criarRadio("Médio",  grupoNivel);
        RadioButton rbDificil= criarRadio("Difícil",grupoNivel);

        hb.getChildren().addAll(nivelLabel, rbFacil, rbMedio, rbDificil);
        return hb;
    }

    // ================================================================
    // BOTÃO ADICIONAR
    // ================================================================

    @FXML
    private void handleAdicionar() {
        String codigo = fieldCodigo.getText().trim();
        if (codigo.isEmpty()) {
            alerta("Campo obrigatório", "Preencha o código da questão.");
            return;
        }
        if (tipoAtual == TipoQuestao.NENHUM) {
            alerta("Tipo não selecionado", "Escolha o tipo da questão.");
            return;
        }

        // Validar campos comuns
        if (fieldEnunciado.getText().trim().isEmpty()) {
            alerta("Campo obrigatório", "Preencha o enunciado.");
            return;
        }
        if (fieldDisciplina.getText().trim().isEmpty()) {
            alerta("Campo obrigatório", "Preencha a disciplina.");
            return;
        }
        if (fieldAssunto.getText().trim().isEmpty()) {
            alerta("Campo obrigatório", "Preencha o assunto.");
            return;
        }

        // Validar nível
        if (grupoNivel.getSelectedToggle() == null) {
            alerta("Campo obrigatório", "Selecione o nível da dificuldade.");
            return;
        }

        // Validar específico por tipo
        if (tipoAtual == TipoQuestao.MULTIPLA_ESCOLHA) {
            if (grupoGabarito.getSelectedToggle() == null) {
                alerta("Campo obrigatório", "Selecione o gabarito (a, b, c ou d).");
                return;
            }
            if (fieldAltA.getText().trim().isEmpty() ||
                    fieldAltB.getText().trim().isEmpty() ||
                    fieldAltC.getText().trim().isEmpty() ||
                    fieldAltD.getText().trim().isEmpty()) {
                alerta("Campo obrigatório", "Preencha todas as alternativas.");
                return;
            }
        }

        // TODO: integrar com DAO/Service real
        System.out.println("=== Questão cadastrada ===");
        System.out.println("Código    : " + codigo);
        System.out.println("Tipo      : " + tipoAtual);
        System.out.println("Enunciado : " + fieldEnunciado.getText());
        System.out.println("Disciplina: " + fieldDisciplina.getText());
        System.out.println("Assunto   : " + fieldAssunto.getText());
        System.out.println("Nível     : " + ((RadioButton) grupoNivel.getSelectedToggle()).getText());

        if (tipoAtual == TipoQuestao.MULTIPLA_ESCOLHA) {
            System.out.println("Gabarito  : " + ((RadioButton) grupoGabarito.getSelectedToggle()).getText());
            System.out.println("Alt A     : " + fieldAltA.getText());
            System.out.println("Alt B     : " + fieldAltB.getText());
            System.out.println("Alt C     : " + fieldAltC.getText());
            System.out.println("Alt D     : " + fieldAltD.getText());
        }

        alerta("Sucesso", "Questão cadastrada com sucesso!");

        resetarFormulario();
    }

    private void resetarFormulario() {
        fieldCodigo.clear();
        tipoLabel.setText("escolha o tipo de questão");
        tipoLabel.getStyleClass().remove("tipo-label-selected");
        camposDinamicos.getChildren().clear();
        boxBtnAdicionar.setVisible(false);
        boxBtnAdicionar.setManaged(false);
        tipoAtual = TipoQuestao.NENHUM;
        if (grupoGabarito != null) grupoGabarito.selectToggle(null);
        if (grupoNivel != null) grupoNivel.selectToggle(null);
    }

    // ================================================================
    // NAVEGAÇÃO SIDEBAR
    // ================================================================

    @FXML private void handleVoltar(MouseEvent e)            { voltarInicial(); }
    @FXML private void handleMenuDisciplinas(MouseEvent e)   { voltarInicial(); }
    @FXML private void handleMenuBuscar(MouseEvent e)        { alerta("Buscar", "Em breve!"); }
    @FXML private void handleMenuGerarProva(MouseEvent e)    { alerta("Gerar Prova", "Em breve!"); }
    @FXML private void handleMenuRelatorio(MouseEvent e)     { alerta("Relatório", "Em breve!"); }
    @FXML private void handleMenuProvas(MouseEvent e)        { alerta("Provas", "Em breve!"); }

    private void voltarInicial() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/aplicativo/views/TelaInicialView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fieldCodigo.getScene().getWindow();
            boolean fs  = stage.isFullScreen();
            boolean max = stage.isMaximized();
            stage.setScene(new Scene(root, 1280, 750));
            stage.setTitle("Gerador de Provas - Disciplinas");
            if (fs)  stage.setFullScreen(true);
            if (max) stage.setMaximized(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ================================================================
    // HELPERS DE UI
    // ================================================================

    private void setTipoLabel(String texto) {
        tipoLabel.setText(texto);
        tipoLabel.getStyleClass().remove("tipo-label");
        tipoLabel.getStyleClass().add("tipo-label-selected");
    }

    private VBox labeledField(String labelText, TextField field) {
        VBox vb = new VBox(4);
        vb.getChildren().addAll(label(labelText), field);
        return vb;
    }

    private HBox altRow(String letra, TextField field) {
        HBox hb = new HBox(6);
        hb.setAlignment(Pos.CENTER_LEFT);
        Label l = new Label(letra);
        l.getStyleClass().add("radio-label");
        l.setMinWidth(18);
        hb.getChildren().addAll(l, field);
        return hb;
    }

    private Label label(String texto) {
        Label l = new Label(texto);
        l.getStyleClass().add("field-label");
        return l;
    }

    private TextField criarTextField(double largura) {
        TextField tf = new TextField();
        tf.getStyleClass().add("field-input");
        tf.setPrefWidth(largura);
        tf.setMaxWidth(largura);
        return tf;
    }

    private RadioButton criarRadio(String texto, ToggleGroup grupo) {
        RadioButton rb = new RadioButton(texto);
        rb.setToggleGroup(grupo);
        rb.getStyleClass().add("radio-btn");
        return rb;
    }

    private void alerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}