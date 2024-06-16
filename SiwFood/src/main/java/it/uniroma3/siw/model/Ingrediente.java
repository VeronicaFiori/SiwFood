package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ingrediente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	
	private String nome;
	
    private String quantita;
    
//    @ManyToMany(mappedBy = "ingredienti")
//    private List<Ricette> ricette;
    
//    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<RicettaIngrediente> ricetta = new HashSet<>();
    
//    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RicettaIngrediente> ricetteQuantita = new ArrayList<>();
//
//    
//    
//    
//	public List<RicettaIngrediente> getRicetteQuantita() {
//		return ricetteQuantita;
//	}
//	public void setRicetteQuantita(List<RicettaIngrediente> ricetteQuantita) {
//		this.ricetteQuantita = ricetteQuantita;
//	}
//	public Ricette getRicetta() {
//		return ricetta;
//	}
//	public void setRicetta(Ricette ricetta) {
//		this.ricetta = ricetta;
//	}
    
    
    
    
	public Long getId() {
		return id;
	}
//	public Set<RicettaIngrediente> getRicette() {
//		return ricetta;
//	}
//	public void setRicette(Set<RicettaIngrediente> ricette) {
//		this.ricetta = ricette;
//	}
	
//	public List<Ricette> getRicetta() {
//		return ricette;
//	}
//	public void setRicetta(List<Ricette> ricetta) {
//		this.ricette = ricetta;
//	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getQuantita() {
		return quantita;
	}
	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, nome, quantita);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingrediente other = (Ingrediente) obj;
		return  Objects.equals(nome, other.nome) && quantita == other.quantita;
	}
    
    
}


