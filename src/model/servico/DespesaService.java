package model.servico;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DespesaDao;
import model.entidade.Despesa;

public class DespesaService {
	
//	private Despesa entidade = new Despesa();
//	private DespesaService service; 
	private DespesaDao dao = DaoFactory.criarDespesaDao();

	public List<Despesa> buscarTodos(){
		return dao.buscarTudo();
	}
	
	public void salvarOuAtualizar(Despesa obj) {
	/*	if(entidade == null) {
			System.out.println("Entidade Despesa nulo");
		}
		if(service == null) {
			System.out.println("Service Despesa nulo");
		}*/
		//if(obj.getId() == null) {
			dao.inserir(obj);
		/*	}
		else {
			dao.atualizar(obj);
		}*/
	}
	
	public void excluir (Despesa obj) {
		dao.excluirPorId(obj.getId());
	}
}