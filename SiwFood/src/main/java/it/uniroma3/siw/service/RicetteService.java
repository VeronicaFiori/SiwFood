package it.uniroma3.siw.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicetteRepository;


@Service

public class RicetteService {


	@Autowired
	private RicetteRepository ricetteRepository;
	@Autowired
	private IngredienteRepository ingredienteRepository;

	public Ricette findById(Long id) {
		return ricetteRepository.findById(id).get();
	}

	public Iterable<Ricette> findAll() {
		return ricetteRepository.findAll();
	}

	public void save(Ricette ricette) {
		ricetteRepository.save(ricette);
	}


//	public void deleteRicetta(Long id,currentUser.getId()) {
//		ricetteRepository.deleteById(id);
//	}

	public void deleteRicetta(Long ricettaId, Long userId) {
	    Ricette ricetta = ricetteRepository.findById(ricettaId).orElse(null);
	    if (ricetta != null && ricetta.getUser().getId().equals(userId)) {
	        ricetteRepository.delete(ricetta);
	    } else {
	        throw new RuntimeException("Utente non autorizzato o ricetta non trovata");
	    }
	}
	
	
	public Ricette getRicetta(Long id) {
		return ricetteRepository.findById(id).orElse(null);
	}
	
	// Metodo per ottenere le ricette create da un cuoco con un determinato ID
    public List<Ricette> getRicetteByCuocoId(Long cuocoId) {
        return ricetteRepository.findByUserId(cuocoId); 
    }

    public List<Ricette> findByCategoria(String categoria) {
        return ricetteRepository.findByCategoria(categoria);
    }
    
    
    /*PROVA*/
//    public Ingrediente getIngrediente(Long id) {
//        return ingredienteRepository.findById(id).orElse(null);
//    }

//    public List<Ingrediente> findAllIngredienti() {
//        return (List<Ingrediente>) ingredienteRepository.findAll();
//    }
}
