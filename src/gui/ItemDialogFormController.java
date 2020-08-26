package gui;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.util.Alertas;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entidade.Despesa;
import model.entidade.Lancamento;
import model.servico.DespesaService;
import model.servico.LancamentoService;

public class ItemDialogFormController implements Initializable{

	private Lancamento lancamentoEntidade;
	private LancamentoService lancamentoService;
	private Despesa despesaEntidade;
	private DespesaService despesaService;
//	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	//----------------------------------------------------------------------
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtPreco;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	//----------------------------------------------------------------------
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
	/*	if (lancamentoEntidade == null) {
			System.out.println("lancamentoEntidade nulo");
		}
		if (lancamentoService == null) {
			System.out.println("lancamentoService nulo");
		}*/
		try {
		/*	entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();*/ 
			Despesa objdesp = new Despesa();
			lancamentoEntidade = getFormData();
			//despesaEntidade = updateFormData();
			objdesp.setId(Utils.stringParaInteiro(txtId.getText()));
			objdesp.setNome(txtNome.getText());
			objdesp.setPreco(Utils.stringParaDouble(txtPreco.getText()));
			despesaService.salvarOuAtualizar(objdesp);
			
		} catch (RuntimeException e) {
		//	setErrorMessages(e.getMessage());
	//	} catch (BDException e) {
			Alertas.mostrarAlerta("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	//--------------------------------------------------------------------
	public void setLancamentoService(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}

	public void setLancamento(Lancamento lancamentoEntidade) {
		this.lancamentoEntidade = lancamentoEntidade;
	}
	
	public void setDespesaService(DespesaService despesaService) {
		this.despesaService = despesaService;
	}

	public void setDespesa(Despesa despesaEntidade) {
		this.despesaEntidade = despesaEntidade;
	}
	//-----------------------------------------------------------------

	private Lancamento getFormData() {
		Lancamento objlan = new Lancamento();
		objlan.setId(Utils.stringParaInteiro(txtId.getText()));
//		obj.setNome(txtNome.getText());
//		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
//		obj.setDepartment(comboBoxDepartment.getValue());
		return objlan;
	}
//------------------------------------------------------------------------------
	
	public void updateFormData() {
		txtId.setText(String.valueOf(lancamentoEntidade.getId()));
	//	txtNome.setText(despesaEntidade.getNome());
	//	Locale.setDefault(Locale.US);
	//	txtPreco.setText(String.format("%.2f", despesaEntidade.getPreco()));		
	}
	//---------------------------------------------------------------------------
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	// TODO Auto-generated method stub	
}

//-----------------------------------------------------------------

}
