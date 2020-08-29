package model.entidade;

import java.io.Serializable;

public class Lancamento implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String referencia;
	private TipoPag tipoPagamento;
	private Double total;
	private Status status;
	
	
	//Composto
	private Item itemLan;
	
	public Lancamento() {
		}
	
	public Lancamento(Integer id, String referencia, TipoPag tipoPagamento, Double total, Status status, Item itemLan) {
		super();
		this.id = id;
		this.referencia = referencia;
		this.tipoPagamento = tipoPagamento;
		this.total = total;
		this.status = status;
		this.itemLan = itemLan;
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

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	//-------
	public Item getItemLan() {
		return itemLan;
	}

	public void setItemLan(Item itemLan) {
		this.itemLan = itemLan;
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
