package br.edu.ufersa.aplicativo.controlles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TelaRelatorioController implements Initializable {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private Label topbarTitle;
    @FXML private FlowPane provasFlow;
    @FXML private GridPane provasGrid;
    @FXML private ComboBox<String> comboTipoFiltro;
    @FXML private ComboBox<String> comboValorFiltro;
    @FXML private Button btnLimparFiltro;
    @FXML private Label labelResultado;

    // Sidebar
    @FXML private StackPane menuDisciplinas;
    @FXML private StackPane menuBuscar;
    @FXML private StackPane menuGerarProva;
    @FXML private StackPane menuRelatorio;

    // ── DADOS ─────────────────────────────────────────────────────────────
    private List<StackPane> menuItems;
    private List<ProvaInfo> todasAsProvas;
    private List<ProvaInfo> provasFiltradas;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Opções de tipo de filtro
    private static final String FILTRO_DISCIPLINA = "Disciplina";
    private static final String FILTRO_DATA       = "Data";
    private static final String FILTRO_QUESTAO    = "Tipo de Questão";

    // ── INICIALIZAÇÃO ──────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuItems = Arrays.asList(
                menuDisciplinas, menuBuscar, menuGerarProva, menuRelatorio
        );

        inicializarDadosMock();
        configurarFiltros();
        renderizarProvas(todasAsProvas);
    }

    // Dados de exemplo — substitua pela integração real com o banco/repositório
    private void inicializarDadosMock() {
        todasAsProvas = new ArrayList<>();
        String[] disciplinas = {"Matemática", "Português", "História", "Física", "Química", "Biologia"};
        String[] tipos = {"Múltipla Escolha", "Verdadeiro ou Falso", "Discursiva"};
        String[] datas = {"11/02/2026", "15/03/2026", "20/04/2026", "05/05/2026"};

        Random rnd = new Random(42);
        int seq = 21;
        for (int i = 0; i < 9; i++) {
            String disc  = disciplinas[rnd.nextInt(disciplinas.length)];
            String tipo  = tipos[rnd.nextInt(tipos.length)];
            String data  = datas[rnd.nextInt(datas.length)];
            String cod   = "PM0" + (seq++);
            todasAsProvas.add(new ProvaInfo(cod, disc, data, tipo));
        }

        provasFiltradas = new ArrayList<>(todasAsProvas);
    }

    // ── FILTROS ───────────────────────────────────────────────────────────
    private void configurarFiltros() {
        comboTipoFiltro.getItems().setAll(FILTRO_DISCIPLINA, FILTRO_DATA, FILTRO_QUESTAO);
    }

    @FXML
    private void handleTipoFiltroChanged() {
        String tipo = comboTipoFiltro.getValue();
        if (tipo == null) return;

        comboValorFiltro.getItems().clear();
        comboValorFiltro.setValue(null);
        comboValorFiltro.setPromptText("selecione...");

        switch (tipo) {
            case FILTRO_DISCIPLINA -> {
                // Disciplinas únicas cadastradas
                List<String> discs = todasAsProvas.stream()
                        .map(ProvaInfo::getDisciplina)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
                comboValorFiltro.getItems().setAll(discs);
                comboValorFiltro.setPromptText("Disciplina...");
            }
            case FILTRO_DATA -> {
                // Datas únicas, sem repetição
                List<String> datas = todasAsProvas.stream()
                        .map(ProvaInfo::getData)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
                comboValorFiltro.getItems().setAll(datas);
                comboValorFiltro.setPromptText("Data...");
            }
            case FILTRO_QUESTAO -> {
                comboValorFiltro.getItems().setAll(
                        "Múltipla Escolha",
                        "Verdadeiro ou Falso",
                        "Discursiva"
                );
                comboValorFiltro.setPromptText("Tipo de questão...");
            }
        }

        comboValorFiltro.setVisible(true);
        comboValorFiltro.setManaged(true);
        btnLimparFiltro.setVisible(true);
        btnLimparFiltro.setManaged(true);
    }

    @FXML
    private void handleValorFiltroChanged() {
        String tipo  = comboTipoFiltro.getValue();
        String valor = comboValorFiltro.getValue();
        if (tipo == null || valor == null) return;

        provasFiltradas = todasAsProvas.stream()
                .filter(p -> switch (tipo) {
                    case FILTRO_DISCIPLINA -> p.getDisciplina().equals(valor);
                    case FILTRO_DATA       -> p.getData().equals(valor);
                    case FILTRO_QUESTAO    -> p.getTipoQuestao().equals(valor);
                    default -> true;
                })
                .collect(Collectors.toList());

        int qtd = provasFiltradas.size();
        labelResultado.setText(qtd + (qtd == 1 ? " prova encontrada" : " provas encontradas"));
        renderizarProvas(provasFiltradas);
    }

    @FXML
    private void handleLimparFiltro() {
        comboTipoFiltro.setValue(null);
        comboValorFiltro.getItems().clear();
        comboValorFiltro.setValue(null);
        comboValorFiltro.setVisible(false);
        comboValorFiltro.setManaged(false);
        btnLimparFiltro.setVisible(false);
        btnLimparFiltro.setManaged(false);
        labelResultado.setText("");
        provasFiltradas = new ArrayList<>(todasAsProvas);
        renderizarProvas(provasFiltradas);
    }

    private void renderizarProvas(List<ProvaInfo> provas) {
        provasGrid.getChildren().clear();  // Mudou de provasFlow para provasGrid

        if (provas.isEmpty()) {
            Label vazio = new Label("Nenhuma prova encontrada.");
            vazio.getStyleClass().add("empty-state-label");
            provasGrid.add(vazio, 0, 0, 3, 1);  // Adiciona ocupando 3 colunas
            GridPane.setHalignment(vazio, HPos.CENTER);
            return;
        }

        int col = 0;
        int row = 0;

        for (ProvaInfo prova : provas) {
            VBox card = criarCard(prova);
            provasGrid.add(card, col, row);
            GridPane.setHgrow(card, Priority.ALWAYS);
            GridPane.setFillWidth(card, true);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox criarCard(ProvaInfo prova) {
        VBox card = new VBox();
        card.getStyleClass().add("prova-card");
        card.setSpacing(0);

        // ── Cabeçalho: "Prova de:" + código ──
        HBox header = new HBox();
        header.getStyleClass().add("card-header");
        header.setAlignment(Pos.CENTER_LEFT);
        // spacer empurra o código para a direita
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblProvaDe = new Label("Prova de:");
        lblProvaDe.getStyleClass().add("card-prova-de-label");

        Label lblCodigo = new Label(prova.getCodigo());
        lblCodigo.getStyleClass().add("card-codigo-label");

        header.getChildren().addAll(lblProvaDe, spacer, lblCodigo);

        // ── Nome da disciplina ──
        Label lblDisciplina = new Label(prova.getDisciplina());
        lblDisciplina.getStyleClass().add("card-disciplina-label");
        lblDisciplina.setWrapText(false);

        // ── Linha divisória ──
        Region divider = new Region();
        divider.getStyleClass().add("card-divider");

        // ── Rodapé: data ──
        HBox footer = new HBox();
        footer.getStyleClass().add("card-footer");
        footer.setAlignment(Pos.CENTER_LEFT);

        Label lblData = new Label("Data: " + prova.getData());
        lblData.getStyleClass().add("card-data-label");

        footer.getChildren().add(lblData);

        card.getChildren().addAll(header, lblDisciplina, divider, footer);

        // Expande a largura no FlowPane
        card.setMaxWidth(Double.MAX_VALUE);

        // Ação ao clicar — navegar para detalhe da prova
        card.setOnMouseClicked(e -> abrirDetalheProva(prova));

        return card;
    }

    // ── NAVEGAÇÃO PARA DETALHE ─────────────────────────────────────────────
    private void abrirDetalheProva(ProvaInfo prova) {
        // Implemente aqui a navegação para a tela de detalhe da prova
        System.out.println("Prova selecionada: " + prova.getCodigo() + " — " + prova.getDisciplina());
        // Exemplo futuro:
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("...TelaDetalheProvaView.fxml"));
    }

    // ── NAVEGAÇÃO SIDEBAR ──────────────────────────────────────────────────
    @FXML
    private void handleMenuDisciplinas(MouseEvent event) {
        navegarPara("/br/edu/ufersa/aplicativo/views/TelaInicialView.fxml",
                "/br/edu/ufersa/aplicativo/css/TelaInicialStyle.css",
                "Gerador de Provas - Disciplinas",
                menuDisciplinas);
    }

    @FXML
    private void handleMenuBuscar(MouseEvent event) {
        navegarPara("/br/edu/ufersa/aplicativo/views/TelaBuscarView.fxml",
                "/br/edu/ufersa/aplicativo/css/TelaBuscarStyle.css",
                "Gerador de Provas - Buscar",
                menuBuscar);
    }

    @FXML
    private void handleMenuGerarProva(MouseEvent event) {
        navegarPara("/br/edu/ufersa/aplicativo/views/TelaGerarProvaView.fxml",
                "/br/edu/ufersa/aplicativo/css/TelaGerarProvaStyle.css",
                "Gerador de Provas - Gerar Prova",
                menuGerarProva);
    }

    @FXML
    private void handleMenuRelatorio(MouseEvent event) {
        selecionarMenu(menuRelatorio);
    }



    // ── UTILITÁRIOS ────────────────────────────────────────────────────────
    private void selecionarMenu(StackPane item) {
        for (StackPane m : menuItems) {
            m.getStyleClass().remove("menu-item-selected");
        }
        if (!item.getStyleClass().contains("menu-item-selected")) {
            item.getStyleClass().add("menu-item-selected");
        }
    }

    private void navegarPara(String fxmlPath, String cssPath, String titulo, StackPane menuItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 750);

            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            Stage stage = (Stage) menuRelatorio.getScene().getWindow();
            boolean isFullScreen = stage.isFullScreen();
            boolean isMaximized  = stage.isMaximized();

            stage.setScene(scene);
            stage.setTitle(titulo);

            if (isFullScreen) stage.setFullScreen(true);
            if (isMaximized)  stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao navegar para " + fxmlPath + ": " + e.getMessage());
        }
    }

    // ── MODEL INTERNO ──────────────────────────────────────────────────────
    public static class ProvaInfo {
        private final String codigo;
        private final String disciplina;
        private final String data;
        private final String tipoQuestao;

        public ProvaInfo(String codigo, String disciplina, String data, String tipoQuestao) {
            this.codigo      = codigo;
            this.disciplina  = disciplina;
            this.data        = data;
            this.tipoQuestao = tipoQuestao;
        }

        public String getCodigo()      { return codigo; }
        public String getDisciplina()  { return disciplina; }
        public String getData()        { return data; }
        public String getTipoQuestao() { return tipoQuestao; }
    }
}