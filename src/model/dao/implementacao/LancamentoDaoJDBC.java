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
import model.dao.LancamentoDao;
import model.entidade.Lancamento;

public class LancamentoDaoJDBC implements LancamentoDao {

	private Connection connection;

	// Força injeção de dependencia (Connection) dentro da Classe.
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
			//			+ "(referencia, tipopag_id) "
						+ "(referencia) "
			//				+ "VALUES  (?, ?) ",
						+ "VALUES  (?) ",
							Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, obj.getReferencia());
	//		ps.setInt(2, obj.getTipoPagamento().getId());
			//forma de pagamento
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
			ps = connection.prepareStatement("UPDATE despesa2 " + "SET nome = ? " + "WHERE Id = ? ");
			ps.setString(1, obj.getReferencia());
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
	public Lancamento buscarPorId(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM despesa2 " + "WHERE Id = ? ");
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

	@Override
	public List<Lancamento> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM tipopag " + " ORDER BY nome ");
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
	}
}
