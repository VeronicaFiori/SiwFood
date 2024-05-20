package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ricette;

public interface RicetteRepository extends CrudRepository<Ricette, Long> {
    List<Ricette> findByUserId(Long userid);

}
