package model.servico;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TipoPagDao;
import model.entidade.TipoPag;

public class TipoPagService {
	
	private TipoPagDao dao = DaoFactory.criarTipoPagDao();

	public List<TipoPag> buscarTodos(){
		return dao.buscarTudo();
	}
}
