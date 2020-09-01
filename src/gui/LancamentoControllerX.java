package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alertas;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidade.Lancamento;
import model.entidade.TipoPag;
import model.servico.LancamentoService;

public class LancamentoControllerX implements Initializable{

	private LancamentoService service;
	private Lancamento entidade;
	//----------------------------------------------------------------
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtReferencia;
	@FXML
	private Button btItem;
	
	@FXML
	public void onBtItemAction(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Lancamento obj = new Lancamento();
	obj.setReferencia(txtReferencia.getText());
		
		TipoPag pag = new TipoPag(2, null);
		obj.setTipoPagamento(pag);
		service.salvar(obj);
		
		criarDialogForm(obj, "/gui/ItemDialgoFormView.fxml", parentStage);
	}	
	//------------------------------------------------------------------
	
	public void setLancamentoService(LancamentoService service) {
		this.service = service;
	}
	
	public void setLancamento(Lancamento entidade) {
		this.entidade = entidade;
	}
	//-----------------------------------------------------------------
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub		
	}	
	//-------------------------------------------------------------------
	
	private void criarDialogForm(Lancamento obj, String string, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			Pane pane = loader.load();

			ItemDialogFormController controller = loader.getController();
			controller.setLancamento(obj);
		//	controller.setDespesaService(new LancamentoService(), new DespesaService());
		//	controller.loadAssociatedObjects();
		//	controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alertas.mostrarAlerta("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}	
	}
	//------------------------------------------------------------------------------
}
