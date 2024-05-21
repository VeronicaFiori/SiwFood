package it.uniroma3.siw.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class RicettaIngredienteId implements Serializable {
    private Long ricettaId;
    private Long ingredienteId;
	public Long getRicettaId() {
		return ricettaId;
	}
	public void setRicettaId(Long ricettaId) {
		this.ricettaId = ricettaId;
	}
	public Long getIngredienteId() {
		return ingredienteId;
	}
	public void setIngredienteId(Long ingredienteId) {
		this.ingredienteId = ingredienteId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ingredienteId, ricettaId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RicettaIngredienteId other = (RicettaIngredienteId) obj;
		return Objects.equals(ingredienteId, other.ingredienteId) && Objects.equals(ricettaId, other.ricettaId);
	}

}
