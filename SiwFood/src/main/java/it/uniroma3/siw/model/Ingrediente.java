package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
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
    
    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.REFRESH, orphanRemoval = true)
    private List<RigaRicetta> righeRicetta;
    
    
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


