package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	Ingrediente findByNome(String nome);
    List<Ingrediente> findByRicetta(Ricette ricetta);



}
