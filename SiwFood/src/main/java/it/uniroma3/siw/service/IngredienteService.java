package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
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

}

