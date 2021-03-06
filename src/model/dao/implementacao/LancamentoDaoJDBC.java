package model.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	/*/-------------------------------------------------------------------------------------
	
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
			//--------------------------------------------------------------------------*/
	
	//Registrar Lancamento. ok
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

	//Atualizar valor Total ao adicionar Itens no Lan�amento. ok
	@Override
	public void atualizar(Lancamento obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"UPDATE lancamento " 
					+ "SET total = ? "
					+ "WHERE Id = ? ");
			ps.setDouble(1, obj.getTotal());
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
	
	//Listar todos Lancamento ok.
	@Override
	public List<Lancamento> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(
					"SELECT * FROM Lancamento l "
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
	
	//Listar todos Lancamento (Quitados) ok.
		@Override
		public List<Lancamento> buscarTudoQuitado() {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = connection.prepareStatement(
						"SELECT * FROM Lancamento l "
						+ "INNER JOIN TipoPag t "
						+ "ON l.tipoPag_id = t.id "
						+ "WHERE status_id = 2 "
						+ "ORDER BY data ASC");			
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
					obj.setDesconto(rs.getDouble("desconto"));
					obj.setAcrescimo(rs.getDouble("acrescimo"));
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
			
		//Listar todos Lancamento (Em Aberto) ok.
		@Override
		public List<Lancamento> buscarTudoEmAberto() {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = connection.prepareStatement(
						"SELECT * FROM Lancamento "
						+ "WHERE status_id = 1 OR status_id = 3 ");      
						
				rs = ps.executeQuery();
				List<Lancamento> lista = new ArrayList<>();				
				while (rs.next()) {
				TipoPag pag = new TipoPag();
					Lancamento obj = new Lancamento();
					obj.setData(new java.util.Date(rs.getTimestamp("data").getTime()));
					obj.setId(rs.getInt("id"));
					obj.setReferencia(rs.getString("referencia"));
					obj.setTotal(rs.getDouble("total"));
					obj.setDesconto(rs.getDouble("desconto"));
					obj.setAcrescimo(rs.getDouble("acrescimo"));
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
	
	// Cancelar LAncamento ok
	@Override
	public void cancelar(Lancamento obj) {
		PreparedStatement ps = null;
		int status = 4; //Cancelado.
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
	
	// Confirmar Lancamento (Quitado) ok
	@Override
	public void confirmarLanQuitado(Lancamento obj) {
		PreparedStatement ps = null;
		int status = 2; //Pago.
		try {
			ps = connection.prepareStatement("UPDATE lancamento " 
		+ "SET tipopag_id = ?, "
		+"status_id = '"+status+"' "
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
	
	@Override
	public void confirmarPagamento(Lancamento obj) {
		PreparedStatement ps = null;
		int status = 2; //Pago.
		try {
			ps = connection.prepareStatement("UPDATE lancamento " 
		+ "SET tipopag_id = ?, "
		+"status_id = '"+status+"', "
		+"total = ?, "
		+"desconto = ?, "
		+"acrescimo = ? "
		+ "WHERE Id = ? ");
			ps.setInt(1, obj.getTipoPagamento().getId());
			ps.setDouble(2, obj.getTotal());
			ps.setDouble(3, obj.getDesconto());
			ps.setDouble(4, obj.getAcrescimo());
			ps.setInt(5, obj.getId());
			ps.executeUpdate();
		} catch (SQLException ex) {
			new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
		}
	}


	// Confirmar Lancamento (A Pagar) ok
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
	
		//Listar todos Lan�amentos (Em Aberto do m�s) ok
		public ArrayList<Lancamento> buscarContasAPagarMesAtual() {	
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
			Calendar datahoje = Calendar.getInstance();
			int mesAtual = datahoje.get(Calendar.MONTH);
			
			switch (mesAtual) {
			case 0:
			ps = connection.prepareStatement("SELECT * FROM lancamento " 
			+"WHERE Month(data) =  '01' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
			+ "ORDER BY data ASC ");
			break;
			case 1:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '02' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 2:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '03' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 3:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '04' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 4:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '05' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 5:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '06' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 6:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '07' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 7:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '08' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 8:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '09' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3 "
				+ "ORDER BY data ASC ");
				break;
			case 9:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '10' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			case 10:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '11' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			default:
				ps = connection.prepareStatement("SELECT * FROM lancamento " 
				+"WHERE Month(data) =  '12' and Status_id = 1 and Year(data) = Year(now()) OR status_id = 3  "
				+ "ORDER BY data ASC ");
				break;
			}
			rs = ps.executeQuery();
			ArrayList<Lancamento> itens = new ArrayList<Lancamento>();
			while (rs.next()) {
				Lancamento l = new Lancamento(); 
				l.setId(rs.getInt("lancamento.id"));
				l.setReferencia(rs.getString("lancamento.referencia"));
				l.setData(rs.getDate("lancamento.data"));
				l.setTotal(rs.getDouble("lancamento.total"));
				l.setDesconto(rs.getDouble("lancamento.desconto"));
				l.setAcrescimo(rs.getDouble("lancamento.acrescimo"));					
		        itens.add(l);
			}			
			return itens;
		}
			catch(SQLException ex) {
				return null;
				}
			finally {
				BD.fecharStatement(ps);
				BD.fecharResultSet(rs);
			}
			}
		
		//Listar todos Lan�amentos (Quitados do m�s) ok
		public ArrayList<Lancamento> buscarContasQuitadoMesAtual() {	
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
			Calendar datahoje = Calendar.getInstance();
			int mesAtual = datahoje.get(Calendar.MONTH);
		
			switch (mesAtual) {
			case 0:
			ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
			+"WHERE Month(data) =  '01' and Status_id = 2 and Year(data) = Year(now()) "
			+ "AND tipopag.id = tipopag_id "
			+ "ORDER BY data ASC ");
			break;
			case 1:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '02' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "
				+ "ORDER BY data ASC ");
				break;
			case 2:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '03' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "
				+ "ORDER BY data ASC ");
				break;
			case 3:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag " 
				+"WHERE Month(data) =  '04' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "
				+ "ORDER BY data ASC ");
				break;
			case 4:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '05' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			case 5:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '06' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			case 6:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '07' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			case 7:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '08' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			case 8:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '09' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			case 9:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '10' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			case 10:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '11' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			default:
				ps = connection.prepareStatement("SELECT * FROM lancamento, tipopag "
				+"WHERE Month(data) =  '12' and Status_id = 2 and Year(data) = Year(now()) "
				+ "AND tipopag.id = tipopag_id "		
				+ "ORDER BY data ASC ");
				break;
			}	
			rs = ps.executeQuery();
			ArrayList<Lancamento> itens = new ArrayList<Lancamento>();
			while (rs.next()) {
				TipoPag tp = new TipoPag();
				tp.setId(rs.getInt("tipopag.id"));
				tp.setNome(rs.getString("tipopag.nome"));

				Lancamento l = new Lancamento(); 
				l.setId(rs.getInt("lancamento.id"));
				l.setReferencia(rs.getString("lancamento.referencia"));
				l.setData(rs.getDate("lancamento.data"));
				l.setTotal(rs.getDouble("lancamento.total"));
				l.setDesconto(rs.getDouble("lancamento.desconto"));
				l.setAcrescimo(rs.getDouble("lancamento.acrescimo"));					
		        l.setTipoPagamento(tp);
				itens.add(l);
			}			
			return itens;
		}
			catch(SQLException ex) {
				return null;
				}
			finally {
				BD.fecharStatement(ps);
				BD.fecharResultSet(rs);
			}
			}			
}
