package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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

public class LanQuitadoController implements Initializable {

	private LancamentoService lancamentoService;
	private Lancamento lancamentoEntidade;
	private ItemService itemService;
	private Item itemEntidade;
	private DespesaService despesaService;
	private Despesa despesaEntidade;
	private TipoPagService tipoPagService;
	private TipoPag tipoPagEntidade;
	private StatusService statusService;
	private Status statusEntidade;
	// ----------------------------------------------------------------

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtReferencia;
	@FXML
	private TextField txtItem;
	@FXML
	private TextField txtPreco;
	@FXML
	private TextField txtTotal;
	@FXML
	private DatePicker datePickerData;
	@FXML
	private ComboBox<TipoPag> cmbTipoPag;
	@FXML
	private Button btCriarRegistroDeLancamento;
	@FXML
	private Button btItem;
	@FXML
	private Button btConfirmar;
	@FXML
	private Button btCancelar;
	@FXML
	private TableView<Despesa> tbDespesa;
	@FXML
	private TableColumn<Despesa, Integer> colunaDespId;
	@FXML
	private TableColumn<Despesa, String> colunaDespNome;
	@FXML
	private TableColumn<Despesa, Double> colunaDespValor;
//--------------------------------------------------------
	private ObservableList<TipoPag> obsListaTipoPag;
	private ObservableList<Despesa> obsListaDespesaTbView;
	// ---------------------------------------------------------

	double total;
	int idLan;
	int idDesp;
	int idItem;

	@FXML
	public void onBtCriarRegistroDeLancamento(ActionEvent evento) {
		total += 0.0;
		Date hoje = new Date();
		Stage parentStage = Utils.stageAtual(evento);
		Lancamento obj = new Lancamento();
		obj.setReferencia(txtReferencia.getText());
		obj.setTotal(total);
		if(datePickerData.getValue() == null) {
			obj.setData(hoje);
		}else {
		Instant instant = Instant.from(datePickerData.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setData(Date.from(instant));
		}
		lancamentoService.salvar(obj);
		txtId.setText(String.valueOf(obj.getId()));
		datePickerData.setValue(LocalDate.ofInstant(obj.getData().toInstant(), ZoneId.systemDefault()));
	//	int id = obj.getId();
	//	idLan = id;
	}
	
	@FXML
	public void onBtItemAction(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);		
		Locale.setDefault(Locale.US); 
		//Lancamento
		Lancamento obj = new Lancamento();
		txtTotal.setText(String.valueOf(obj.getTotal()));
		obj.setId(Utils.stringParaInteiro(txtId.getText()));
		obj.setReferencia(txtReferencia.getText());
		obj.setTotal((total));
		lancamentoService.atualizar(obj);
		txtId.setText(String.valueOf(obj.getId()));	
		//Despesa
		Despesa desp = new Despesa();
		desp.setNome(txtItem.getText());	
		desp.setPreco(Utils.stringParaDouble(txtPreco.getText()));
		despesaService.salvarOuAtualizar(desp);
		//Item
		Item item = new Item();
		item.setLancamento(obj);
		item.setDespesa(desp);
		itemService.salvarOuAtualizar(item);		
		//Total
		total += desp.getPreco();
		txtTotal.setText(""+total);	
		obj.setId(Utils.stringParaInteiro(txtId.getText()));
		obj.setTotal(total);
		lancamentoService.atualizar(obj);		
		//Limpando os campos
		txtItem.setText("");
		txtPreco.setText(String.valueOf(0));		
		//Carregar TableView do Lançamento
		List<Despesa> listaDespesa = despesaService.listarPorId(obj.getId());
		obsListaDespesaTbView = FXCollections.observableArrayList(listaDespesa);
		tbDespesa.setItems(obsListaDespesaTbView);
		// initEditButtons();
		// initRemoveButtons();
	}
	
	@FXML
	public void onBtConfirmar(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Lancamento obj = new Lancamento();
		try {
		obj.setId(Utils.stringParaInteiro(txtId.getText()));
		obj.setTipoPagamento(cmbTipoPag.getValue());
		obj.setTipoPagamento(cmbTipoPag.getValue());
		lancamentoService.confirmarLanQuitado(obj);
		carregarPropriaView("/gui/LanQuitadoView.fxml", (LanQuitadoController controller) -> {
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
		}catch (RuntimeException ex) {
			Alertas.mostrarAlerta("Pendente", null, "Favor informar o tipo de pagamento", AlertType.WARNING);
		}		
	}
	
	@FXML
	public void onBtCancelar(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Lancamento obj = new Lancamento();
		obj.setId(Utils.stringParaInteiro(txtId.getText()));
		lancamentoService.cancelar(obj);
		carregarPropriaView("/gui/L.fxml", (LanQuitadoController controller) -> {
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
	// ------------------------------------------------------------------

	public void setLancamentoService(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}

	public void setLancamento(Lancamento lancamentoEntidade) {
		this.lancamentoEntidade = lancamentoEntidade;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public void setItem(Item itemEntidade) {
		this.itemEntidade = itemEntidade;
	}

	public void setDespesaService(DespesaService despesaService) {
		this.despesaService = despesaService;
	}

	public void setDespesa(Despesa despesaEntidade) {
		this.despesaEntidade = despesaEntidade;
	}

	public void setTipoPagService(TipoPagService tipoPagService) {
		this.tipoPagService = tipoPagService;
	}

	public void setTipoPag(TipoPag tipoPagEntidade) {
		this.tipoPagEntidade = tipoPagEntidade;
	}

	public void setStatusService(StatusService statusService) {
		this.statusService = statusService;
	}

	public void setStatus(Status tipoPagEntidade) {
		this.statusEntidade = statusEntidade;
	}

	// -----------------------------------------------------------------
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComboBoxTipoPag();
		inicializarNodes();
	}

	// ------------------------------------------------------------------
	public void carregarCamposDeCadastro() {
		txtId.setText(String.valueOf(lancamentoEntidade.getId()));
	}
	// -----------------------------------------------------------------------------------------------------

	private void inicializarNodes() {
		Restricoes.setTextFieldInteger(txtId);
		Restricoes.setTextFieldTamanhoMaximo(txtReferencia, 50);
		Restricoes.setTextFieldDouble(txtPreco);
		Restricoes.setTextFieldTamanhoMaximo(txtItem, 30);
		Utils.formatDatePicker(datePickerData, "dd/MM/yyyy");

		colunaDespNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colunaDespValor.setCellValueFactory(new PropertyValueFactory<>("preco"));
		Utils.formatTableColumnValorDecimais(colunaDespValor, 2); //Formatar com(0,00)

		Stage stage = (Stage) Main.pegarMainScene().getWindow();
		tbDespesa.prefHeightProperty().bind(stage.heightProperty());
	}
	// -----------------------------------------------------------------

	public void carregarObjetosAssociados() {
		List<TipoPag> listaTipoPag = tipoPagService.buscarTodos();
		obsListaTipoPag = FXCollections.observableArrayList(listaTipoPag);
		cmbTipoPag.setItems(obsListaTipoPag);
	}

	private void inicializarComboBoxTipoPag() {
		Callback<ListView<TipoPag>, ListCell<TipoPag>> factory = lv -> new ListCell<TipoPag>() {
			@Override
			protected void updateItem(TipoPag item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cmbTipoPag.setCellFactory(factory);
		cmbTipoPag.setButtonCell(factory.call(null));
	}	
	//--------------------------------------------------
	
	private synchronized <T> void carregarPropriaView(String caminhoDaView, Consumer<T> acaoDeInicializacao) {
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
