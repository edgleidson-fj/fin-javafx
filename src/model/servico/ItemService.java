package model.servico;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ItemDao;
import model.entidade.Item;

public class ItemService {
	
	private ItemDao dao = DaoFactory.criarItemDao();

	public List<Item> buscarTodos(){
		return dao.buscarTudo();
	}
	
	public void salvarOuAtualizar(Item obj) {
		//if(obj.getId() == null) {
			dao.inserir(obj);
		/*	}
		else {
			dao.atualizar(obj);
		}*/
	}
	
	public void excluir (Item obj) {
//		dao.excluirPorId(obj.getId());
	}
}
