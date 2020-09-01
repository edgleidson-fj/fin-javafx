package model.dao;

import java.util.List;

import model.entidade.Lancamento;

public interface LancamentoDao {

	void inserir(Lancamento obj);
	void atualizar(Lancamento obj);
	void excluirPorId(Integer id);
	Lancamento buscarPorId(Integer id);
	List<Lancamento> buscarTudo();
	void cancelar(Lancamento obj);
}
