package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import application.Main;
import gui.util.Alertas;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entidade.Despesa;
import model.entidade.Item;
import model.entidade.Lancamento;
import model.entidade.TipoPag;
import model.entidade.Status;
import model.servico.DespesaService;
import model.servico.ItemService;
import model.servico.LancamentoService;
import model.servico.StatusService;
import model.servico.TipoPagService;

public class LController implements Initializable{

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
	//----------------------------------------------------------------
	
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
	private ComboBox<TipoPag> cmbTipoPag;
	@FXML
	private ComboBox<Status> cmbStatus;
	@FXML
	private Button btItem;
	@FXML	
	private Button btCriarRegistroDeLancamento;
	//--------------------------------------------------------
	private ObservableList<TipoPag> obsListaTipoPag;
	private ObservableList<Status> obsListaStatus;	
	//---------------------------------------------------------
	
	double total;
	@FXML
	public void onBtCriarRegistroDeLancamento(ActionEvent evento) {
		total += 0.0;
		Stage parentStage = Utils.stageAtual(evento);		
		Lancamento obj = new Lancamento();
		obj.setReferencia(txtReferencia.getText());
		obj.setTotal(total);
		lancamentoService.salvar(obj);
		txtId.setText(String.valueOf(obj.getId()));
	}
		
	@FXML
	public void onBtItemAction(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		
		Lancamento obj = new Lancamento();
		Despesa desp = new Despesa();
		Item item = new Item();
						
//		cmbStatus.setValue(lancamentoEntidade.getStatus());
//		cmbTipoPag.setValue(lancamentoEntidade.getTipoPagamento());
		
		//Lancamento
		txtTotal.setText(String.valueOf(obj.getTotal()));
		obj.setId(Utils.stringParaInteiro(txtId.getText()));
		obj.setReferencia(txtReferencia.getText());
		obj.setTotal((total));
		
//	obj.setStatus(cmbStatus.getValue());		
//	obj.setTipoPagamento(cmbTipoPag.getValue());
		obj.setStatus(cmbStatus.getValue());
		obj.setTipoPagamento(cmbTipoPag.getValue());
		
		
		lancamentoService.atualizar(obj);
		txtId.setText(String.valueOf(obj.getId()));
	
		//Despesa
		desp.setNome(txtItem.getText());
		desp.setPreco(Utils.stringParaDouble(txtPreco.getText()));
		despesaService.salvarOuAtualizar(desp);
					
		//Item
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
		}
	//------------------------------------------------------------------
	
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
		//-----------------------------------------------------------------
		@Override
		public void initialize(URL url, ResourceBundle rb) {
			initializeComboBoxTipoPag();
			initializeComboBoxStatus();
		}	
		//------------------------------------------------------------------
		public void carregarCamposDeCadastro() {
			txtId.setText(String.valueOf(lancamentoEntidade.getId()));
//			txtNome.setText(entidade.getNome());
		}
		//-------------------------------------------------------------------
		private  void atualizarPropriaView(TipoPag obj, String caminhoDaView) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
				VBox novoVBox = loader.load();			
				
			/*	TipoPagController controller = loader.getController();
				controller.setTipoPag(obj);
				controller.carregarCamposDeCadastro();
				controller.setTipoPagService(new TipoPagService());
				controller.carregarTableView();*/
				        	
				Scene mainScene = Main.pegarMainScene();
				VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

				Node mainMenu = mainVBox.getChildren().get(0);
				mainVBox.getChildren().clear();
				mainVBox.getChildren().add(mainMenu);
				mainVBox.getChildren().addAll(novoVBox);
			} catch (IOException ex) {
				Alertas.mostrarAlerta("IO Exception", "Erro ao carregar a tela.", ex.getMessage(), AlertType.ERROR);
			}
		}		
		//-----------------------------------------------------------------

		public void loadAssociatedObjects() {
			List<TipoPag> listaTipoPag = tipoPagService.buscarTodos();
			obsListaTipoPag = FXCollections.observableArrayList(listaTipoPag);
			cmbTipoPag.setItems(obsListaTipoPag);
			
			List<Status> listaStatus = statusService.buscarTodos();
			obsListaStatus = FXCollections.observableArrayList(listaStatus);
			cmbStatus.setItems(obsListaStatus);		
		}
		
		private void initializeComboBoxTipoPag() {
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
		
		private void initializeComboBoxStatus() {
			Callback<ListView<Status>, ListCell<Status>> factory = lv -> new ListCell<Status>() {
				@Override
				protected void updateItem(Status item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty ? "" : item.getNome());
				}
			};
			cmbStatus.setCellFactory(factory);
			cmbStatus.setButtonCell(factory.call(null));
		}
}
