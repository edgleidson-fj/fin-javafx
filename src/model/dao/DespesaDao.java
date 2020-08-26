package model.dao;

import java.util.List;

import model.entidade.Despesa;

public interface DespesaDao {

	void inserir(Despesa obj);
	void atualizar(Despesa obj);
	void excluirPorId(Integer id);
	Despesa buscarPorId(Integer id);
	List<Despesa> buscarTudo();
}