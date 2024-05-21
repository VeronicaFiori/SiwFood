package it.uniroma3.siw.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class RicettaIngrediente {
    @EmbeddedId
    private RicettaIngredienteId id = new RicettaIngredienteId();

    @ManyToOne
    @MapsId("ricettaId")
    @JoinColumn(name = "ricetta_id")
    private Ricette ricetta;

    @ManyToOne
    @MapsId("ingredienteId")
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    private String quantita;

	
    

	public RicettaIngredienteId getId() {
		return id;
	}

	public void setId(RicettaIngredienteId id) {
		this.id = id;
	}

	public Ricette getRicetta() {
		return ricetta;
	}

	public void setRicetta(Ricette ricetta) {
		this.ricetta = ricetta;
	}

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}

	public String getQuantita() {
		return quantita;
	}

	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}

    // getters and setters
    
}
