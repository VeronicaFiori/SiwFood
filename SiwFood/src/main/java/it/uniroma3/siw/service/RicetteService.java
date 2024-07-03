package it.uniroma3.siw.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.repository.RicetteRepository;
import jakarta.transaction.Transactional;


@Service

public class RicetteService {


	@Autowired
	private RicetteRepository ricetteRepository;
	
	@Transactional
	public Ricette findById(Long id) {
		return ricetteRepository.findById(id).get();
	}
    
	@Transactional
	public Iterable<Ricette> findAll() {
		return ricetteRepository.findAll();
	}
	
	public void save(Ricette ricette) {
		ricetteRepository.save(ricette);
	}

	@Transactional
	public void deleteRicettaById(Long id) {
		this.ricetteRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteRicetta(Long ricettaId, Long userId) {
	    Ricette ricetta = ricetteRepository.findById(ricettaId).orElse(null);
	    if (ricetta != null && ricetta.getUser().getId().equals(userId)) {
	        ricetteRepository.delete(ricetta);
	    } else {
	        throw new RuntimeException("Utente non autorizzato o ricetta non trovata");
	    }
	}
	
	@Transactional
	public Ricette getRicetta(Long id) {
		return ricetteRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public List<Ricette> getRicetteByCuocoId(Long cuocoId) {
        return ricetteRepository.findByUserId(cuocoId); 
    }
	@Transactional
    public List<Ricette> findByCategoria(String categoria) {
        return ricetteRepository.findByCategoria(categoria);
    }
    

}
