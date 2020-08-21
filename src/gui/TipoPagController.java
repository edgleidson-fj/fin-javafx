package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import bd.BDException;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entidade.TipoPag;
import model.servico.TipoPagService;

public class TipoPagController implements Initializable{
	
	private TipoPagService service;
	private TipoPag entidade;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	private Button btSalvar;

	@FXML
	private TableView<TipoPag> tableViewTipoPagamento;
	
	@FXML
	private TableColumn<TipoPag, Integer> tableColumnID;
	
	@FXML
	private TableColumn<TipoPag, String> tableColumnNome;
	
	private ObservableList<TipoPag> obsLista;
	
	public void onBtSalvar() {
		try {
		entidade = dadosDoCampoDeTexto();
		service.salvarOuAtualizar(entidade);
		carregarView("/gui/TipoPagView.fxml", (TipoPagController controller) -> {
			controller.setTipoPagService(new TipoPagService());
			controller.carregarTableView();
		});
		}
		catch (BDException ex){
			Alertas.mostrarAlerta("Erro ao salvar objeto", null, ex.getMessage(), AlertType.ERROR);
		}
	}
	
	public void onBtCancelar() {
			}
	
	//Injeção da dependência.
	public void setTipoPagService(TipoPagService service) {
		this.service = service;
	}
	
	public void setTipoPag(TipoPag entidade) {
		this.entidade = entidade;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComportamento();
	}

	private void inicializarComportamento() {
		tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		// Tamanho da tabela.
		Stage stage = (Stage) Main.pegarMainScene().getWindow();
		tableViewTipoPagamento.prefHeightProperty().bind(stage.heightProperty());
		
		Restricoes.setTextFieldInteger(txtId);
		Restricoes.setTextFieldTamanhoMaximo(txtNome, 20);
		}

	public void carregarTableView() {
		if(service == null) {
			throw new IllegalStateException("Service nulo");
		}
		List<TipoPag> lista = service.buscarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewTipoPagamento.setItems(obsLista);
	}
	//---------------------------------------------
	
	
	public TipoPag dadosDoCampoDeTexto() {
		TipoPag obj = new TipoPag();
		obj.setId(Utils.stringParaInteiro(txtId.getText()));
		obj.setNome(txtNome.getText());
		return obj;
	}
	
	public void carregarCamposDeCadastro() {
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
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
