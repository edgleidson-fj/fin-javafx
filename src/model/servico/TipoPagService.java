package model.servico;

import java.util.ArrayList;
import java.util.List;

import model.entidade.TipoPag;

public class TipoPagService {

	public List<TipoPag> buscarTodos(){
		List<TipoPag> lista = new ArrayList<>();
		lista.add(new TipoPag(1,"Dinheiro"));
		lista.add(new TipoPag(1,"Visa Electron"));
		lista.add(new TipoPag(1,"MasterCard"));
		return lista;
	}
}
