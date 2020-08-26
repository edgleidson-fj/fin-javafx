package model.entidade;

import java.io.Serializable;

public class ItemBKP implements Serializable{
	private static final long serialVersionUID = 1L;

	private Lancamento lancamento_id;
	private Despesa despesa_id;
	
	public ItemBKP() {
	}
	
	public ItemBKP(Lancamento lancamento_id, Despesa despesa_id) {
		super();
		this.lancamento_id = lancamento_id;
		this.despesa_id = despesa_id;
	}
	public Lancamento getLancamento_id() {
		return lancamento_id;
	}
	public void setLancamento_id(Lancamento lancamento_id) {
		this.lancamento_id = lancamento_id;
	}
	public Despesa getDespesa_id() {
		return despesa_id;
	}
	public void setDespesa_id(Despesa despesa_id) {
		this.despesa_id = despesa_id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((despesa_id == null) ? 0 : despesa_id.hashCode());
		result = prime * result + ((lancamento_id == null) ? 0 : lancamento_id.hashCode());
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
		ItemBKP other = (ItemBKP) obj;
		if (despesa_id == null) {
			if (other.despesa_id != null)
				return false;
		} else if (!despesa_id.equals(other.despesa_id))
			return false;
		if (lancamento_id == null) {
			if (other.lancamento_id != null)
				return false;
		} else if (!lancamento_id.equals(other.lancamento_id))
			return false;
		return true;
	}	
}
