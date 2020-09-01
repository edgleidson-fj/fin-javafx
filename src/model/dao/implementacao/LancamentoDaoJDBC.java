package model.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bd.BD;
import bd.BDException;
import bd.BDIntegrityException;
import model.dao.LancamentoDao;
import model.entidade.Despesa;
import model.entidade.Item;
import model.entidade.Lancamento;

public class LancamentoDaoJDBC implements LancamentoDao {

	private Connection connection;

	public LancamentoDaoJDBC(Connection connection) {
		this.connection = connection;
	}
//-------------------------------------------------------------------
	@Override
	public void inserir(Lancamento obj) {
		PreparedStatement ps = null;
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
		+ "SET total = ?, "
		+ " tipopag_id = ? "
					+ "WHERE Id = ? ");
			//ps.setString(1, obj.getReferencia());
			ps.setDouble(1, obj.getTotal());
			ps.setInt(2, obj.getTipoPagamento().getId());
			ps.setInt(3, obj.getId());
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

	/*@Override
	public List<Lancamento> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM lancamento " + " ORDER BY referencia ");
			rs = ps.executeQuery();
			List<Lancamento> lista = new ArrayList<>();

			while (rs.next()) {
				Lancamento obj = new Lancamento();
				obj.setId(rs.getInt("Id"));
		//		obj.setNome(rs.getString("nome"));
				lista.add(obj);
			}
			return lista;
		} catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
			BD.fecharResultSet(rs);
		}
	}*/
	
	//Listar todos Itens do Lancamento. teste
	@Override
	public List<Lancamento> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(
					"SELECT * "
				+ "from lancamento as l, item as i, despesa as d "	
//				+	"FROM lancamento "
//			+	"inner JOIN item "
//				+	"ON lancamento.id = item.Lancamento_id "
//				+	"inner JOIN despesa "
//				+	"ON despesa.id = item.despesa_id "
//				+	"WHERE a.id = 100");
+ "where l.id = i.lancamento_id "
+ "and i.despesa_id = d.id "
+ "and l.id = 100 "
+ "limit 5 ");
					
			rs = ps.executeQuery();
			List<Lancamento> lista = new ArrayList<>();
			List<Despesa> lista2 = new ArrayList<>();
			
			while (rs.next()) {
				Lancamento obj = new Lancamento();

				Despesa d = new Despesa();
				d.setNome(rs.getString("nome"));
				
				Item i = new Item();
			//	i.setDespesa(d);
			//	i.setLancamento(obj);
				i.setDespesa(d);
				
				obj.setId(rs.getInt("Id"));
				obj.setReferencia(rs.getString("referencia"));
				obj.setItemLan(i);
			//	obj.getItemLan().setLancamento();
				lista.add(obj);
				System.out.println("lista ---- "+lista);
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
		int status = 4;
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
	//-------------------------------------------------------------------------------------
	
	private Item instantiateItem(ResultSet rs, Despesa dep, Lancamento lan) throws SQLException {
//		private Item instantiateItem(ResultSet rs, Despesa dep) throws SQLException {
			Item obj = new Item();
			obj.setLancamento(lan);
			obj.setDespesa(dep);
			return obj;
		}

		private Despesa instantiateDespesa(ResultSet rs) throws SQLException {
			Despesa dep = new Despesa();
			dep.setId(rs.getInt("Id"));
			dep.setNome(rs.getString("Nome"));
			return dep;
		}
		
		private Lancamento instantiateLancamento(ResultSet rs) throws SQLException {
			Lancamento lan = new Lancamento();
			lan.setId(rs.getInt("Id"));
			return lan;
	}
	
}
