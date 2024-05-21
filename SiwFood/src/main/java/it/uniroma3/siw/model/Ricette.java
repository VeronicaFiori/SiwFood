package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ricette {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;
	@NotBlank
    private String descrizione;
	
	@ManyToOne
    private User user;
	
	
	@OneToMany(mappedBy = "ricetta", cascade = CascadeType.ALL)
    private List<Ingrediente> ingredienti;
	
	 @OneToMany(mappedBy = "ricetta", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<RicettaIngrediente> ingredientiQuantita = new ArrayList<>();

	
	
	
	public List<RicettaIngrediente> getIngredientiQuantita() {
		return ingredientiQuantita;
	}
	public void setIngredientiQuantita(List<RicettaIngrediente> ingredientiQuantita) {
		this.ingredientiQuantita = ingredientiQuantita;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Ingrediente> getIngredienti() {
		return ingredienti;
	}
	public void setIngredienti(List<Ingrediente> ingredienti) {
		this.ingredienti = ingredienti;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public int hashCode() {
		return Objects.hash( descrizione, id, nome);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ricette other = (Ricette) obj;
		return Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(nome, other.nome);
	}
	


	
}

