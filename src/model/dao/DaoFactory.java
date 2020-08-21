package model.dao;

import bd.BD;
import model.dao.implementacao.TipoPagDaoJDBC;
import model.dao.implementacao.VendedorDaoJDBC;

public class DaoFactory {

	public static VendedorDao criarVendedorDao() {
		return new VendedorDaoJDBC(BD.abrirConexao());
	}
	
	public static TipoPagDao criarTipoPagDao() {
		return new TipoPagDaoJDBC(BD.abrirConexao());
	}
}
