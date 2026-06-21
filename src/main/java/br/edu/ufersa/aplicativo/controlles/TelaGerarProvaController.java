package br.edu.ufersa.aplicativo.controlles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TelaGerarProvaController implements Initializable {

    @FXML private Label topbarTitle;

    @FXML private StackPane menuDisciplinas;
    @FXML private StackPane menuBuscar;
    @FXML private StackPane menuGerarProva;
    @FXML private StackPane menuRelatorio;
    @FXML private StackPane menuProvas;

    @FXML private StackPane cardGerarAleatorio;
    @FXML private StackPane cardGerarManual;

    @FXML private TextField numeroQuestoesField;
    @FXML private TextField tipoField;
    @FXML private TextField professorField;
    @FXML private TextField instituicaoField;

    @FXML private RadioButton nivel1Op1, nivel1Op2, nivel1Op3, nivel1Op4, nivel1Op5;
    @FXML private RadioButton nivel2Op1, nivel2Op2, nivel2Op3, nivel2Op4, nivel2Op5;
    @FXML private RadioButton nivel3Op1, nivel3Op2, nivel3Op3, nivel3Op4, nivel3Op5;

    @FXML private Button gerarButton;

    private List<StackPane> menuItems;
    private int totalQuestoes = 0;
    private boolean isUpdating = false;

    // Arrays para facilitar o gerenciamento
    private RadioButton[][] niveisRadioButtons;
    private RadioButton[][] todosRadioButtons;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuItems = Arrays.asList(
                menuDisciplinas, menuBuscar, menuGerarProva, menuRelatorio, menuProvas
        );
        selecionarMenu(menuGerarProva);

        // Inicializa o array de radio buttons
        niveisRadioButtons = new RadioButton[][]{
                {nivel1Op1, nivel1Op2, nivel1Op3, nivel1Op4, nivel1Op5},
                {nivel2Op1, nivel2Op2, nivel2Op3, nivel2Op4, nivel2Op5},
                {nivel3Op1, nivel3Op2, nivel3Op3, nivel3Op4, nivel3Op5}
        };

        // Desabilita todos os níveis inicialmente
        desabilitarTodosNiveis();

        // Adiciona listener para o campo de número de questões
        numeroQuestoesField.textProperty().addListener((obs, oldVal, newVal) -> {
            handleNumeroQuestoesChanged();
        });

        // Adiciona listeners para todos os radio buttons
        adicionarListenersNiveis();
    }

    // ═══════════════════════════════════════════════════════════════════
    // NAVEGAÇÃO DO MENU LATERAL
    // ═══════════════════════════════════════════════════════════════════
    @FXML
    private void handleMenuDisciplinas(MouseEvent event) {
        navegarPara(
                "/br/edu/ufersa/aplicativo/views/TelaInicialView.fxml",
                "/br/edu/ufersa/aplicativo/css/TelaInicialStyle.css",
                "Gerador de Provas - Disciplinas",
                menuDisciplinas
        );
    }

    @FXML
    private void handleMenuBuscar(MouseEvent event) {
        navegarPara(
                "/br/edu/ufersa/aplicativo/views/TelaBuscarView.fxml",
                "/br/edu/ufersa/aplicativo/css/TelaBuscarStyle.css",
                "Gerador de Provas - Buscar",
                menuBuscar
        );
    }

    @FXML
    private void handleMenuGerarProva(MouseEvent event) {
        try {
            System.out.println(" Abrindo tela de gerar prova...");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/aplicativo/views/TelaGerarProvaView.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root, 1280, 750);

            // Carregar CSS específico
            URL cssUrl = getClass().getResource("/br/edu/ufersa/aplicativo/css/TelaGerarProvaStyle.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = (Stage) menuGerarProva.getScene().getWindow();
            boolean isFullScreen = stage.isFullScreen();
            boolean isMaximized = stage.isMaximized();

            stage.setScene(scene);
            stage.setTitle("Gerador de Provas - Gerar Prova");

            if (isFullScreen) {
                stage.setFullScreen(true);
            }
            if (isMaximized) {
                stage.setMaximized(true);
            }

            System.out.println(" Tela de gerar prova aberta com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(" Erro ao abrir tela de gerar prova: " + e.getMessage());
        }
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

    private void navegarPara(String fxmlPath, String cssPath, String tituloJanela, StackPane origem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1280, 750);

            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = (Stage) origem.getScene().getWindow();
            boolean isFullScreen = stage.isFullScreen();
            boolean isMaximized = stage.isMaximized();

            stage.setScene(scene);
            stage.setTitle(tituloJanela);

            if (isFullScreen) {
                stage.setFullScreen(true);
            }
            if (isMaximized) {
                stage.setMaximized(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao navegar para " + fxmlPath + ": " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CARDS DE OPÇÃO (GERAR ALEATÓRIO / GERAR MANUAL)
    // ═══════════════════════════════════════════════════════════════════
    @FXML
    private void handleGerarAleatorio(MouseEvent event) {
        System.out.println("🎲 Modo de geração: Aleatório");
        // TODO: lógica de seleção aleatória de questões
    }

    @FXML
    private void handleGerarManual(MouseEvent event) {
        System.out.println("✌️ Modo de geração: Manual");

        try {
            // Carrega a tela de gerar prova manual
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/aplicativo/views/TelaGerarProvaManualView.fxml"));
            Parent root = loader.load();

            // Cria a nova cena
            Scene scene = new Scene(root, 1280, 750);

            // Adiciona o CSS
            URL cssUrl = getClass().getResource("/br/edu/ufersa/aplicativo/css/TelaGerarProvaManualStyle.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            // Obtém a janela atual
            Stage stage = (Stage) cardGerarManual.getScene().getWindow();

            // Salva o estado da janela (fullscreen/maximizado)
            boolean isFullScreen = stage.isFullScreen();
            boolean isMaximized = stage.isMaximized();

            // Troca a cena
            stage.setScene(scene);
            stage.setTitle("Gerador de Provas - Manual");

            // Restaura o estado
            if (isFullScreen) {
                stage.setFullScreen(true);
            }
            if (isMaximized) {
                stage.setMaximized(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao navegar para TelaGerarProvaManualView: " + e.getMessage());
            showAlert("Erro", "Não foi possível abrir a tela de geração manual.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // GERENCIAMENTO DE QUANTIDADE DE QUESTÕES
    // ═══════════════════════════════════════════════════════════════════
    @FXML
    private void handleNumeroQuestoesChanged() {
        try {
            String text = numeroQuestoesField.getText();
            if (text == null || text.isEmpty()) {
                totalQuestoes = 0;
                desabilitarTodosNiveis();
                return;
            }

            totalQuestoes = Integer.parseInt(text);
            if (totalQuestoes < 1 || totalQuestoes > 10) {
                totalQuestoes = 0;
                numeroQuestoesField.setText("");
                showAlert("Valor inválido", "Digite um número entre 1 e 10.");
                desabilitarTodosNiveis();
                return;
            }

            // Reset all selections when total changes
            isUpdating = true;
            limparTodasSelecoes();
            atualizarOpcoesNiveis();
            isUpdating = false;

        } catch (NumberFormatException e) {
            totalQuestoes = 0;
            desabilitarTodosNiveis();
        }
    }

    private void limparTodasSelecoes() {
        for (RadioButton[] nivel : niveisRadioButtons) {
            for (RadioButton rb : nivel) {
                rb.setSelected(false);
            }
        }
    }

    private void desabilitarTodosNiveis() {
        isUpdating = true;
        for (RadioButton[] nivel : niveisRadioButtons) {
            for (RadioButton rb : nivel) {
                rb.setDisable(true);
                rb.setSelected(false);
            }
        }
        isUpdating = false;
    }

    private void atualizarOpcoesNiveis() {
        if (totalQuestoes == 0) {
            desabilitarTodosNiveis();
            return;
        }

        isUpdating = true;

        // 🔥 NOVA LÓGICA: Cada nível pode ter no máximo 5, independente do total
        // A limitação será feita na lógica de seleção (handleNivelChanged)
        int maxPorNivel = 5; // Sempre permite até 5

        // Atualiza cada nível
        for (RadioButton[] nivel : niveisRadioButtons) {
            atualizarGrupoNivel(nivel, maxPorNivel);
        }

        isUpdating = false;
    }

    private void atualizarGrupoNivel(RadioButton[] options, int max) {
        for (int i = 0; i < options.length; i++) {
            // Habilita todas as opções (sempre permite até 5)
            options[i].setDisable(false);
            // Se estiver selecionado, mantém
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // LISTENERS PARA RADIO BUTTONS - LÓGICA CORRIGIDA
    // ═══════════════════════════════════════════════════════════════════
    private void adicionarListenersNiveis() {
        for (RadioButton[] nivel : niveisRadioButtons) {
            for (RadioButton rb : nivel) {
                rb.setOnAction(e -> handleNivelChanged());
            }
        }
    }

    @FXML
    private void handleNivelChanged() {
        if (isUpdating || totalQuestoes == 0) return;

        // Obtém quantidades atuais
        int[] quantidades = obterQuantidadesAtuais();
        int somaAtual = quantidades[0] + quantidades[1] + quantidades[2];

        // 🔥 NOVA LÓGICA: Se soma ultrapassou o total, desmarca a última seleção
        if (somaAtual > totalQuestoes) {
            isUpdating = true;
            desmarcarUltimaSelecao();
            isUpdating = false;
            mostrarResumoDistribuicao();
            return;
        }

        // Se soma é igual ao total ou menor, mantém como está
        mostrarResumoDistribuicao();
    }

    private void desmarcarUltimaSelecao() {
        // Verifica qual nível tem a última seleção e desmarca
        for (int nivel = 2; nivel >= 0; nivel--) {
            RadioButton[] options = niveisRadioButtons[nivel];
            for (int i = options.length - 1; i >= 0; i--) {
                if (options[i].isSelected()) {
                    options[i].setSelected(false);
                    showAlert("Aviso", "A distribuição ultrapassou o total de " + totalQuestoes + " questões.\n" +
                            "A última seleção foi desmarcada automaticamente.");
                    return;
                }
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ═══════════════════════════════════════════════════════════════════
    private int obterQuantidadeSelecionada(RadioButton[] options) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                return i + 1;
            }
        }
        return 0;
    }

    private int[] obterQuantidadesAtuais() {
        return new int[]{
                obterQuantidadeSelecionada(niveisRadioButtons[0]),
                obterQuantidadeSelecionada(niveisRadioButtons[1]),
                obterQuantidadeSelecionada(niveisRadioButtons[2])
        };
    }

    private void mostrarResumoDistribuicao() {
        int[] qtds = obterQuantidadesAtuais();
        int soma = qtds[0] + qtds[1] + qtds[2];
        System.out.println("📊 Distribuição: N1=" + qtds[0] + " | N2=" + qtds[1] + " | N3=" + qtds[2] +
                " | Total=" + soma + "/" + totalQuestoes);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GERAR PROVA
    // ═══════════════════════════════════════════════════════════════════
    @FXML
    private void handleGerar() {
        // Valida se o número de questões foi definido
        if (totalQuestoes == 0) {
            showAlert("Campo obrigatório", "Defina o número de questões (1 a 10).");
            return;
        }

        String tipo = tipoField.getText();
        String professor = professorField.getText();
        String instituicao = instituicaoField.getText();

        // Valida campos obrigatórios
        if (professor.isBlank() || instituicao.isBlank()) {
            showAlert("Campos obrigatórios", "Preencha o Professor e a Instituição.");
            return;
        }

        // Obtém quantidades
        int qtdNivel1 = obterQuantidadeSelecionada(niveisRadioButtons[0]);
        int qtdNivel2 = obterQuantidadeSelecionada(niveisRadioButtons[1]);
        int qtdNivel3 = obterQuantidadeSelecionada(niveisRadioButtons[2]);

        // Verifica se a soma total é igual ao número de questões
        int somaTotal = qtdNivel1 + qtdNivel2 + qtdNivel3;
        if (somaTotal != totalQuestoes) {
            showAlert("Distribuição incompleta",
                    String.format("A distribuição de questões (%d) não corresponde ao total definido (%d).\nAjuste a distribuição antes de gerar.",
                            somaTotal, totalQuestoes));
            return;
        }

        // Verifica se pelo menos uma questão foi selecionada
        if (somaTotal == 0) {
            showAlert("Nenhuma questão", "Selecione pelo menos uma questão para gerar a prova.");
            return;
        }

        // Se passou em todas as validações
        System.out.println("📄 Gerando prova com " + totalQuestoes + " questões...");
        System.out.println("   Tipo: " + (tipo.isBlank() ? "Padrão" : tipo));
        System.out.println("   Nível 1: " + qtdNivel1 + " questões");
        System.out.println("   Nível 2: " + qtdNivel2 + " questões");
        System.out.println("   Nível 3: " + qtdNivel3 + " questões");
        System.out.println("   Professor: " + professor);
        System.out.println("   Instituição: " + instituicao);

        // Mostra mensagem de sucesso
        showAlert("Sucesso", "Prova gerada com sucesso!\n\n" +
                "Distribuição:\n" +
                "• Nível 1: " + qtdNivel1 + " questões\n" +
                "• Nível 2: " + qtdNivel2 + " questões\n" +
                "• Nível 3: " + qtdNivel3 + " questões\n\n" +
                "Professor: " + professor + "\n" +
                "Instituição: " + instituicao);

        // TODO: chamar serviço de geração da prova com os dados acima
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void selecionarMenu(StackPane item) {
        for (StackPane m : menuItems) {
            m.getStyleClass().remove("menu-item-selected");
        }
        if (!item.getStyleClass().contains("menu-item-selected")) {
            item.getStyleClass().add("menu-item-selected");
        }
    }
}