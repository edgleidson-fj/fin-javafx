package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entidade.Lancamento;
import model.entidade.TipoPag;
import model.servico.LancamentoService;
import model.servico.TipoPagService;

public class ContasQuitadasMesAtualController implements Initializable {

	private LancamentoService lancamentoService;
	private Lancamento lancamentoEntidade;
	private TipoPagService tipoPagService;
	private TipoPag tipoPagEntidade;
	// -------------------------------------------

	@FXML
	private TableView<Lancamento> tbLancamento;
	@FXML
	private TableColumn<Lancamento, Integer> colunaLanId;
	@FXML
	private TableColumn<Lancamento, Date> colunaLanData;
	@FXML
	private TableColumn<Lancamento, String> colunaLanRef;
	@FXML
	private TableColumn<Lancamento, Double> colunaLanValor;
	@FXML
	private TableColumn<Lancamento, TipoPag> colunaTipoPag;
	@FXML
	private Label lbTotal;
	// -----------------------------------------------------

	private ObservableList<Lancamento> obsListaLancamentoTbView;
	// -----------------------------------------------------
	
	
	public void setLancamentoService(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}

	public void setLancamento(Lancamento lancamentoEntidade) {
		this.lancamentoEntidade = lancamentoEntidade;
	}
	
	public void setTipoPagService(TipoPagService tipoPagService) {
		this.tipoPagService = tipoPagService;
	}

	public void setTipoPag(TipoPag tipoPagEntidade) {
		this.tipoPagEntidade = tipoPagEntidade;
	}

	//----------------------------------------------------------
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();		
	}
	
	private void inicializarNodes() {
		colunaLanData.setCellValueFactory(new PropertyValueFactory<>("data"));
		Utils.formatTableColumnData(colunaLanData, "dd/MM/yyyy");
		colunaLanId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaLanRef.setCellValueFactory(new PropertyValueFactory<>("referencia"));
		colunaLanValor.setCellValueFactory(new PropertyValueFactory<>("total"));
		Utils.formatTableColumnValorDecimais(colunaLanValor, 2); // Formatar com(0,00)
		colunaTipoPag.setCellValueFactory(new PropertyValueFactory<>("tipoPagamento"));
	}
	
	public void carregarTableView() {
		if (lancamentoService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Lancamento> lista = lancamentoService.buscarContasQuitadoMesAtual();
		obsListaLancamentoTbView = FXCollections.observableArrayList(lista);
		  tbLancamento.setItems(obsListaLancamentoTbView);			
		  // Botão Detalhe(); 
		  //Valor Total
		  Double soma  = 0.0; 
			for (Lancamento tab : obsListaLancamentoTbView) {
			    soma += tab.getTotal();
			} 
			lbTotal.setText(String.format("R$ %.2f", soma));
	}	
	// -----------------------------------------------------------------


}
