package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ricette {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;
	@NotBlank	
    @Column( length = 1000)

    private String descrizione;
	
	@ManyToOne
    private User user;
	@NotBlank
	private String categoria;
	
	@OneToOne
	Image image;
	

	
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	@ManyToMany
    private Set<Ingrediente> ingredienti;
	
		
	@OneToMany(mappedBy = "ricetta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RigaRicetta> righeRicetta;
	
	
	
	public List<RigaRicetta> getRigheRicetta() {
		return righeRicetta;
	}
	public void setRigheRicetta(List<RigaRicetta> righeRicetta) {
		this.righeRicetta = righeRicetta;
		
	}
	 public void addRigaRicetta(RigaRicetta rigaRicetta) {
	        if (righeRicetta == null) {
	            righeRicetta = new ArrayList<>();
	        }
	        righeRicetta.add(rigaRicetta);
	        rigaRicetta.setRicetta(this);
	    }

	    public void removeRigaRicetta(RigaRicetta rigaRicetta) {
	        if (righeRicetta != null) {
	            righeRicetta.remove(rigaRicetta);
	            rigaRicetta.setRicetta(null);
	        }
	    }
	
	/**/
	
	

	public Set<Ingrediente> getIngredienti() {
		return ingredienti;
	}
	public void setIngredienti(Set<Ingrediente> ingredienti) {
		this.ingredienti = ingredienti;
	}
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	//@ManyToMany(mappedBy = "ricetta", cascade = CascadeType.ALL)
    
//	@ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//       // name = "ricetta_ingrediente",
//        joinColumns = @JoinColumn(name = "ricetta_id"),
//        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
//    )
////	private List<Ingrediente> ingredienti;
	
   
	
	
	
	
//public Set<RicettaIngrediente> getIngredienti() {
//		return ingredienti;
//	}
//	public void setIngredienti(Set<RicettaIngrediente> ingredienti) {
//		this.ingredienti = ingredienti;
//	}
	
	
	
	
	//	 @OneToMany(mappedBy = "ricetta", cascade = CascadeType.ALL, orphanRemoval = true)
//	    private List<RicettaIngrediente> ingredientiQuantita = new ArrayList<>();
//
//	
//	
//	
//	public List<RicettaIngrediente> getIngredientiQuantita() {
//		return ingredientiQuantita;
//	}
//	public void setIngredientiQuantita(List<RicettaIngrediente> ingredientiQuantita) {
//		this.ingredientiQuantita = ingredientiQuantita;
//	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
//	public List<Ingrediente> getIngredienti() {
//		return ingredienti;
//	}
//	public void setIngredienti(List<Ingrediente> ingredienti) {
//		this.ingredienti = ingredienti;
//	}
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


	
	
//    public void addIngrediente(Ingrediente ingrediente, String quantita) {
//        RicettaIngrediente ricettaIngrediente = new RicettaIngrediente(this, ingrediente, quantita);
//        this.ingredienti.add(ricettaIngrediente);
//        ingrediente.getRicette().add(ricettaIngrediente);
//    }
//
//    public void removeIngrediente(Ingrediente ingrediente) {
//        RicettaIngrediente ricettaIngrediente = new RicettaIngrediente(this, ingrediente, null);
//        this.ingredienti.remove(ricettaIngrediente);
//        ingrediente.getRicette().remove(ricettaIngrediente);
//    }


	
}

