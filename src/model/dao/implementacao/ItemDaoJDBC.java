package model.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bd.BD;
import bd.BDException;
import bd.BDIntegrityException;
import model.dao.ItemDao;
import model.entidade.Item;

public class ItemDaoJDBC implements ItemDao {

	private Connection connection;

	// Força injeção de dependencia (Connection) dentro da Classe.
	public ItemDaoJDBC(Connection connection) {
		this.connection = connection;
	}
//-------------------------------------------------------------------
	@Override
	public void inserir(Item obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"INSERT INTO item "
						+ "(lancamento_id, despesa_id) "
							+ "VALUES  (?, ?) ",
							Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, obj.getLancamento().getId());
			ps.setInt(2, obj.getDespesa().getId());
	
			ps.executeUpdate();

		/*	if (linhasAfetadas > 0) {
				ResultSet rs = ps.getGeneratedKeys(); // ID gerado no Insert.
				if (rs.next()) {
					int id = rs.getInt(1); // ID do Insert.
					obj.setId(id);
				}
				BD.fecharResultSet(rs);
			} else {
				throw new BDException("Erro no INSERT, nenhuma linha foi afetada!");
			}*/
		} catch (SQLException ex) {
			new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}
//--------------------------------------------------------------------------
	@Override
	public void atualizar(Item obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("UPDATE despesa2 " + "SET nome = ? " + "WHERE Id = ? ");
		//	ps.setString(1, obj.getNome());
	//		ps.setInt(2, obj.getId());
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
			ps = connection.prepareStatement("DELETE FROM despesa2  " + "WHERE Id = ? ");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException ex) {
			new BDIntegrityException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}

	@Override
	public Item buscarPorId(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM despesa2 " + "WHERE Id = ? ");
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Item obj = new Item();
	//			obj.setId(rs.getInt("Id"));
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

	@Override
	public List<Item> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM tipopag " + " ORDER BY nome ");
			rs = ps.executeQuery();
			List<Item> lista = new ArrayList<>();

			while (rs.next()) {
				Item obj = new Item();
		//		obj.setId(rs.getInt("Id"));
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
	}
}
