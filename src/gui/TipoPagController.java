package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidade.TipoPag;
import model.servico.TipoPagService;

public class TipoPagController implements Initializable{
	
	private TipoPagService service;
	
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
		System.out.println("Botão salvar");
	}
	
	//Injeção da dependência.
	public void setTipoPagService(TipoPagService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComportamentoDaTabela();
	}

	private void inicializarComportamentoDaTabela() {
		tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		// Tamanho da tabela.
		Stage stage = (Stage) Main.pegarMainScene().getWindow();
		tableViewTipoPagamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void carregarTableView() {
		if(service == null) {
			throw new IllegalStateException("Service nulo");
		}
		List<TipoPag> lista = service.buscarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewTipoPagamento.setItems(obsLista);
	}
}
