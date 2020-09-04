package model.servico;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DespesaDao;
import model.entidade.Despesa;
import model.entidade.Item;

public class DespesaService {
	
//	private Despesa entidade = new Despesa();
//	private DespesaService service; 
	private DespesaDao dao = DaoFactory.criarDespesaDao();

	public List<Despesa> buscarTodos(){
		return dao.buscarTudo();
	}
	
	
	
	
	
	public void salvar(Despesa obj) {
			dao.inserir(obj);	
	}
	
	public void atualizar(Despesa obj) {
		dao.atualizar(obj);		
	}
	
	public void excluir (Despesa obj) {
		dao.excluirPorId(obj.getId());
	}
	
	
	
	
	
	
	
	public List<Despesa> listarPorId(Integer id){
		return dao.listarPorId(id);
	}
	
	
	
	
	public Despesa buscarPorId(Integer id){
		return dao.buscarPorId(id);
	}
	
	
}
