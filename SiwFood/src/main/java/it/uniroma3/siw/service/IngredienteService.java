package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.repository.IngredienteRepository;

@Service
public class IngredienteService {
	@Autowired
	private IngredienteRepository ingredienteRepository;

	public Ingrediente findById(Long id) {
		return ingredienteRepository.findById(id).get();
	}

	
	public Iterable<Ingrediente> findAll() {
		return ingredienteRepository.findAll();
	}

	public void save(Ingrediente ingrediente) {
		ingredienteRepository.save(ingrediente);
		
	}
	
	public void deleteIngrediente(Long id) {
        ingredienteRepository.deleteById(id);
    }

    public Ingrediente getIngrediente(Long id) {
        return ingredienteRepository.findById(id).orElse(null);
    }

    
//    public List<Ingrediente> findByRicetta(Ricette ricetta) {
//        return ingredienteRepository.findByRicetta(ricetta);
//    }
//    
    
    
//    public List<Ingrediente> findIngredientiNotInRicetta(Ricette ricetta) {
//        return ingredienteRepository.findIngredientiNotInRicetta(ricetta.getId());
//    }
    
    
    
    
    
    public List<Ingrediente> getAllIngredienti() {
        return (List<Ingrediente>) ingredienteRepository.findAll();
    }

}


