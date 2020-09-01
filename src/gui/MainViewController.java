package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entidade.Despesa;
import model.entidade.Item;
import model.entidade.Lancamento;
import model.entidade.Status;
import model.entidade.TipoPag;
import model.servico.DespesaService;
import model.servico.ItemService;
import model.servico.LancamentoService;
import model.servico.StatusService;
import model.servico.TipoPagService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSair;

	@FXML
	private MenuItem menuItemLancamentoQuitado;

	@FXML
	private MenuItem menuItemTipoPagamento;

	@FXML
	public void onMenuItemSair() {
		
		}

	@FXML
	public void onMenuItemLancamentoQuitado() {
		carregarView("/gui/LanQuitadoView.fxml", (LanQuitadoController controller) -> {
			controller.setLancamentoService(new LancamentoService());
			controller.setLancamento(new Lancamento());
			controller.setDespesaService(new DespesaService());
			controller.setDespesa(new Despesa());
			controller.setItemService(new ItemService());
			controller.setItem(new Item());			
			controller.setTipoPag(new TipoPag());
			controller.setTipoPagService(new TipoPagService());
			controller.setStatus(new Status());
			controller.setStatusService(new StatusService());
			controller.carregarObjetosAssociados();			
		});
	}

	@FXML
	public void onMenuItemTipoPagamento() {
		carregarView("/gui/TipoPagView.fxml", (TipoPagController controller) -> {
			controller.setTipoPagService(new TipoPagService());
			controller.carregarTableView();
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized <T> void carregarView(String caminhoDaView, Consumer<T> acaoDeInicializacao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			VBox novoVBox = loader.load();

			Scene mainScene = Main.pegarMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(novoVBox);

			// Pegando segundo parametro dos onMenuItem()
			T controller = loader.getController();
			acaoDeInicializacao.accept(controller);
		} catch (IOException ex) {
			Alertas.mostrarAlerta("IO Exception", "Erro ao carregar a tela.", ex.getMessage(), AlertType.ERROR);
		}
	}	
}
