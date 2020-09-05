package model.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.BD;
import bd.BDException;
import bd.BDIntegrityException;
import model.dao.LancamentoDao;
import model.entidade.Lancamento;
import model.entidade.TipoPag;

public class LancamentoDaoJDBC implements LancamentoDao {

	private Connection connection;

	public LancamentoDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	//-------------------------------------------------------------------------------------
	
			private TipoPag instanciaTipoPag(ResultSet rs) throws SQLException {
				TipoPag pag = new TipoPag();
			//	pag.setId(rs.getInt("id"));
				pag.setNome(rs.getString("nome"));
				return pag;
			}
			
			private Lancamento instanciaLancamento(ResultSet rs,TipoPag pag) throws SQLException {
				Lancamento obj = new Lancamento();
				obj.setData(new java.util.Date(rs.getTimestamp("data").getTime()));
				obj.setId(rs.getInt("id"));
				obj.setReferencia(rs.getString("referencia"));
				obj.setTotal(rs.getDouble("total"));
				obj.setTipoPagamento(pag);
				return obj;
			}				
			//--------------------------------------------------------------------------
	
	@Override
	public void inserir(Lancamento obj) {
		PreparedStatement ps = null;
		if(obj.getData() == null) {
			Date hoje = new Date();
			System.out.println(hoje);
		}
		try {
			ps = connection.prepareStatement(
					"INSERT INTO lancamento "
						+ "(referencia, total, data) "
							+ "VALUES  (?, ?, ?) ",
							Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, obj.getReferencia());
			ps.setDouble(2, obj.getTotal());
			ps.setDate(3, new java.sql.Date(obj.getData().getTime()));
			
			int linhasAfetadas = ps.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet rs = ps.getGeneratedKeys(); // ID gerado no Insert.
				if (rs.next()) {
					int id = rs.getInt(1); // ID do Insert.
					obj.setId(id);
				}
				BD.fecharResultSet(rs);
			} else {
				throw new BDException("Erro no INSERT, nenhuma linha foi afetada!");
			}
		} catch (SQLException ex) {
			new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}
//--------------------------------------------------------------------------
	@Override
	public void atualizar(Lancamento obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("UPDATE lancamento " 
		+ "SET total = ? "
		//+ " tipopag_id = ? "
					+ "WHERE Id = ? ");
			//ps.setString(1, obj.getReferencia());
			ps.setDouble(1, obj.getTotal());
			//ps.setInt(2, obj.getTipoPagamento().getId());
			ps.setInt(2, obj.getId());
			ps.executeUpdate();
		} catch (SQLException ex) {
			new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}

	@Override
	public void excluirPorId(Integer id) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("DELETE FROM lancamento  " + "WHERE Id = ? ");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException ex) {
			new BDIntegrityException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}

	@Override
	public Lancamento buscarPorId(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM lancamento " + "WHERE Id = ? ");
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Lancamento obj = new Lancamento();
				obj.setId(rs.getInt("Id"));
	//			obj.setNome(rs.getString("nome"));
				return obj;
			}
			return null;
		} catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
			BD.fecharResultSet(rs);
		}
	}
	
	


	//Listar todos Itens do Lancamento. teste
	@Override
	public List<Lancamento> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM Lancamento l "
					+ "INNER JOIN TipoPag t "
					+ "ON l.tipoPag_id = t.id");   
		
					
			rs = ps.executeQuery();
			List<Lancamento> lista = new ArrayList<>();
			
			while (rs.next()) {
			TipoPag pag = new TipoPag();
				pag.setId(rs.getInt("id"));
				pag.setNome(rs.getString("t.nome"));
					
				Lancamento obj = new Lancamento();
				obj.setData(new java.util.Date(rs.getTimestamp("data").getTime()));
				obj.setId(rs.getInt("id"));
				obj.setReferencia(rs.getString("referencia"));
				obj.setTotal(rs.getDouble("total"));
				obj.setTipoPagamento(pag);						
				
			
			
		
				lista.add(obj);
			}
			return lista;
		} catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
			BD.fecharResultSet(rs);
		}
	}
	
	// Cancelar LAncamento
	@Override
	public void cancelar(Lancamento obj) {
		PreparedStatement ps = null;
		int status = 4; //Cancelado.
		try {
			ps = connection.prepareStatement("UPDATE lancamento " 
		+ "SET status_id = '"+status+"' "
				+ "WHERE Id = ? ");
		//	ps.setInt(1, obj.getTipoPagamento().getId());
			ps.setInt(1, obj.getId());
			ps.executeUpdate();
		} catch (SQLException ex) {
			new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}
	
	// Confirmar Lancamento Quitado
	@Override
	public void confirmarLanQuitado(Lancamento obj) {
		PreparedStatement ps = null;
		int status = 2; //Pago.
		try {
			ps = connection.prepareStatement("UPDATE lancamento " 
		+ "SET tipopag_id = ?, "
		+" status_id = '"+status+"' "
		+ "WHERE Id = ? ");
			ps.setInt(1, obj.getTipoPagamento().getId());
			ps.setInt(2, obj.getId());
			ps.executeUpdate();
		} catch (SQLException ex) {
			new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}

	// Confirmar Lancamento A Pagar
		@Override
		public void confirmarLanAPagar(Lancamento obj) {
			PreparedStatement ps = null;
			int status = 1; //Em Aberto.
			try {
				ps = connection.prepareStatement("UPDATE lancamento " 
			+ "SET status_id = '"+status+"' "
			+ "WHERE Id = ? ");
				ps.setInt(1, obj.getId());
				ps.executeUpdate();
			} catch (SQLException ex) {
				new BDException(ex.getMessage());
			} finally {
				BD.fecharStatement(ps);
			}
		}
	
}
