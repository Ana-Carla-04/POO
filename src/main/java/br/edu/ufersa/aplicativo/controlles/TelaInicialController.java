package br.edu.ufersa.aplicativo.controlles;

import br.edu.ufersa.aplicativo.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TelaInicialController implements Initializable {

    @FXML private Label topbarTitle;
    @FXML private GridPane disciplinasGrid;
    @FXML private StackPane menuDisciplinas;
    @FXML private StackPane menuBuscar;
    @FXML private StackPane menuGerarProva;
    @FXML private StackPane menuRelatorio;
    @FXML private StackPane menuProvas;

    private List<StackPane> menuItems;

    // Dados das disciplinas com suas informações
    private List<DisciplinaInfo> disciplinasInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuItems = Arrays.asList(
                menuDisciplinas, menuBuscar, menuGerarProva, menuRelatorio, menuProvas
        );

        // Inicializar dados das disciplinas
        inicializarDisciplinas();

        // Configurar GridPane responsivo
        configurarGridResponsivo();

        // Carregar dados iniciais
        carregarDisciplinas();
    }

    private void inicializarDisciplinas() {
        disciplinasInfo = Arrays.asList(
                new DisciplinaInfo("Matemática", "MAT001", 15, "Prof. Silva"),
                new DisciplinaInfo("Português", "POR001", 12, "Prof. Santos"),
                new DisciplinaInfo("História", "HIS001", 10, "Prof. Oliveira"),
                new DisciplinaInfo("Geografia", "GEO001", 8, "Prof. Costa"),
                new DisciplinaInfo("Física", "FIS001", 14, "Prof. Lima"),
                new DisciplinaInfo("Química", "QUI001", 11, "Prof. Almeida"),
                new DisciplinaInfo("Biologia", "BIO001", 9, "Prof. Ferreira"),
                new DisciplinaInfo("Inglês", "ING001", 7, "Prof. Pereira"),
                new DisciplinaInfo("Artes", "ART001", 6, "Prof. Carvalho")
        );
    }

    private void configurarGridResponsivo() {
        disciplinasGrid.getColumnConstraints().clear();
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            col.setHgrow(Priority.ALWAYS);
            col.setMinWidth(200);
            col.setMaxWidth(400);
            disciplinasGrid.getColumnConstraints().add(col);
        }

        disciplinasGrid.getRowConstraints().clear();
        for (int i = 0; i < 3; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setMinHeight(140);
            row.setPrefHeight(160);
            row.setMaxHeight(200);
            disciplinasGrid.getRowConstraints().add(row);
        }
    }

    @FXML
    private void handleMenuDisciplinas(MouseEvent event) {
        selecionarMenu(menuDisciplinas);
        topbarTitle.setText("Disciplinas: Minhas Disciplinas");
        carregarDisciplinas();
    }

    @FXML
    private void handleMenuBuscar(MouseEvent event) {
        selecionarMenu(menuBuscar);
        topbarTitle.setText("Buscar");
    }

    @FXML
    private void handleMenuGerarProva(MouseEvent event) {
        selecionarMenu(menuGerarProva);
        topbarTitle.setText("Gerar Prova");
    }

    @FXML
    private void handleMenuRelatorio(MouseEvent event) {
        selecionarMenu(menuRelatorio);
        topbarTitle.setText("Relatório");
    }

    @FXML
    private void handleMenuProvas(MouseEvent event) {
        selecionarMenu(menuProvas);
        topbarTitle.setText("Provas");
    }

    @FXML
    private void handleAddDisciplina() {
        System.out.println("Adicionar nova disciplina...");
    }

    private void selecionarMenu(StackPane item) {
        for (StackPane m : menuItems) {
            m.getStyleClass().remove("menu-item-selected");
        }
        if (!item.getStyleClass().contains("menu-item-selected")) {
            item.getStyleClass().add("menu-item-selected");
        }
    }

    private void carregarDisciplinas() {
        disciplinasGrid.getChildren().clear();

        int col = 0, row = 0;
        for (DisciplinaInfo info : disciplinasInfo) {
            StackPane card = new StackPane();
            card.getStyleClass().add("disciplina-card");

            // Criar layout do card com nome e quantidade de questões
            VBox cardContent = new VBox();
            cardContent.setAlignment(javafx.geometry.Pos.CENTER);
            cardContent.setSpacing(8);

            Label nomeLabel = new Label(info.getNome());
            nomeLabel.getStyleClass().add("disciplina-label");

            Label qtdLabel = new Label(info.getQuantidade() + " questões");
            qtdLabel.getStyleClass().add("disciplina-qtd-label");
            qtdLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #0A4174;");

            cardContent.getChildren().addAll(nomeLabel, qtdLabel);
            card.getChildren().add(cardContent);

            // Salvar dados da disciplina no card
            card.setUserData(info);

            // Clique no card - navegar para questões
            card.setOnMouseClicked(e -> handleCardDisciplina(info));

            GridPane.setColumnIndex(card, col);
            GridPane.setRowIndex(card, row);
            GridPane.setFillWidth(card, true);
            GridPane.setFillHeight(card, true);

            disciplinasGrid.getChildren().add(card);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // NAVEGAÇÃO PARA TELA DE QUESTÕES
    // ═══════════════════════════════════════════════════════════════════
    private void handleCardDisciplina(DisciplinaInfo info) {
        try {
            System.out.println("Disciplina selecionada: " + info.getNome());

            // Carregar a tela de questões
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/aplicativo/views/QuestoesView.fxml")
            );
            Parent root = loader.load();

            // Pegar o controller e passar os dados da disciplina
            QuestoesController controller = loader.getController();
            controller.setDisciplinaInfo(info);

            // Criar a nova cena
            Scene scene = new Scene(root, 1280, 750);

            // Carregar CSS
            URL cssUrl = getClass().getResource("/br/edu/ufersa/aplicativo/css/TelaInicialStyle.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            // Pegar o Stage atual
            Stage stage = (Stage) disciplinasGrid.getScene().getWindow();

            // Salvar o estado de tela cheia
            boolean isFullScreen = stage.isFullScreen();
            boolean isMaximized = stage.isMaximized();

            stage.setScene(scene);
            stage.setTitle("Gerador de Provas - " + info.getNome());

            // Restaurar estado
            if (isFullScreen) {
                stage.setFullScreen(true);
            }
            if (isMaximized) {
                stage.setMaximized(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao abrir tela de questões: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CLASSE INTERNA PARA DADOS DA DISCIPLINA
    // ═══════════════════════════════════════════════════════════════════
    public static class DisciplinaInfo {
        private String nome;
        private String codigo;
        private int quantidade;
        private String professor;

        public DisciplinaInfo(String nome, String codigo, int quantidade, String professor) {
            this.nome = nome;
            this.codigo = codigo;
            this.quantidade = quantidade;
            this.professor = professor;
        }

        public String getNome() { return nome; }
        public String getCodigo() { return codigo; }
        public int getQuantidade() { return quantidade; }
        public String getProfessor() { return professor; }

        public void setNome(String nome) { this.nome = nome; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        public void setProfessor(String professor) { this.professor = professor; }
    }
}