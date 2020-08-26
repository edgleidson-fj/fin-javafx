package model.entidade;

import java.io.Serializable;

public class Lancamento implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String referencia;
	private TipoPag tipoPagamento;
	//Status
	
	public Lancamento() {
		}
	
	

	public Lancamento(Integer id, String referencia, TipoPag tipoPagamento) {
		super();
		this.id = id;
		this.referencia = referencia;
		this.tipoPagamento = tipoPagamento;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getReferencia() {
		return referencia;
	}



	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}



	public TipoPag getTipoPagamento() {
		return tipoPagamento;
	}



	public void setTipoPagamento(TipoPag tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
