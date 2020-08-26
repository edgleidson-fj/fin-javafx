package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entidade.Despesa;
import model.entidade.Item;
import model.entidade.Lancamento;
import model.entidade.TipoPag;
import model.servico.DespesaService;
import model.servico.ItemService;
import model.servico.LancamentoService;

public class LController implements Initializable{

	private LancamentoService lancamentoService;
	private Lancamento lancamentoEntidade;
	private ItemService itemService;
	private Item itemEntidade;
	private DespesaService despesaService;
	private Despesa despesaEntidade;
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
	private Button btItem;
	
	/*@FXML
	public void onBtItemAction(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		
		Lancamento obj = new Lancamento();
		Despesa desp = new Despesa();
		Item item = new Item();
		
		//Lancamento
		obj.setReferencia(txtReferencia.getText());
		TipoPag pag = new TipoPag(2, null);
		obj.setTipoPagamento(pag);
		lancamentoService.salvar(obj);

		txtId.setText(String.valueOf(obj.getId()));
		
		//Despesa
		desp.setId(Utils.stringParaInteiro(txtId.getText()));
		desp.setNome(txtItem.getText());
		desp.setPreco(Utils.stringParaDouble(txtPreco.getText()));
		//Despesa d = new Despesa(1, "ProdutoTeste", 100.0);
		despesaService.salvarOuAtualizar(desp);
		System.out.println("ID "+desp.getId());
		System.out.println("Preço "+desp.getPreco());
		
		
		//Item
		item.setLancamento(obj);
		item.setDespesa(desp);
		itemService.salvarOuAtualizar(item);
		System.out.println("ID "+item.getLancamento());
		System.out.println("ID "+item.getDespesa());
		
		
		//lancamentoService.salvar(obj);
		
		//criarDialogForm(obj, "/gui/ItemDialgoFormView.fxml", parentStage);
	}*/	
	
	public void onBtItemAction(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		
		Lancamento obj = new Lancamento();
		Despesa desp = new Despesa();
		Item item = new Item();
		
		//Lancamento
		obj.setReferencia(txtReferencia.getText());
		TipoPag pag = new TipoPag(2, null);
		obj.setTipoPagamento(pag);
		lancamentoService.salvar(obj);

		//txtId.setText(String.valueOf(obj.getId()));
		
		//Despesa
	//	desp.setId(Utils.stringParaInteiro(txtId.getText()));
		desp.setNome(txtItem.getText());
		desp.setPreco(Utils.stringParaDouble(txtPreco.getText()));
		//Despesa d = new Despesa(1, "ProdutoTeste", 100.0);
		despesaService.salvarOuAtualizar(desp);
		System.out.println("ID "+desp.getId());
		System.out.println("Preço "+desp.getPreco());
		
		
		//Item
		item.setLancamento(obj);
		item.setDespesa(desp);
		itemService.salvarOuAtualizar(item);
		System.out.println("ID "+item.getLancamento());
		System.out.println("ID "+item.getDespesa());
		
		
		//lancamentoService.salvar(obj);
		
		//criarDialogForm(obj, "/gui/ItemDialgoFormView.fxml", parentStage);
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
		//-----------------------------------------------------------------
		@Override
		public void initialize(URL url, ResourceBundle rb) {
			// TODO Auto-generated method stub		
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
}
