package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;
import jakarta.transaction.Transactional;

@Service
public class IngredienteService {
	@Autowired
	private IngredienteRepository ingredienteRepository;

	@Transactional
	public Ingrediente findById(Long id) {
		return ingredienteRepository.findById(id).get();
	}
	@Transactional
	public Ingrediente findByNome(String nome) {
		return this.ingredienteRepository.findByNome(nome);
	}

	@Transactional
	public List<Ingrediente> findAll() {
		return ingredienteRepository.findAll();
	}

	public void save(Ingrediente ingrediente) {
		ingredienteRepository.save(ingrediente);
		
	}
	@Transactional
	public void deleteIngrediente(Long id) {
        ingredienteRepository.deleteById(id);
    }
	@Transactional
    public Ingrediente getIngrediente(Long id) {
        return ingredienteRepository.findById(id).orElse(null);
    }
    
	

}


