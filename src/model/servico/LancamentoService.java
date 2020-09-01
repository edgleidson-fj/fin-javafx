package model.servico;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.LancamentoDao;
import model.entidade.Lancamento;

public class LancamentoService {
	
	private LancamentoDao dao = DaoFactory.criarLancamentoDao();

	public List<Lancamento> buscarTodos(){
		return dao.buscarTudo();
	}
	
	public void salvar(Lancamento obj) {
			dao.inserir(obj);
		}
	
	public void atualizar(Lancamento obj) {
		dao.atualizar(obj);
		}
	
	public void excluir (Lancamento obj) {
		dao.excluirPorId(obj.getId());
	}
	
	public void cancelar(Lancamento obj) {
		dao.cancelar(obj);
		}
}
