package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ricette;

public interface RicetteRepository extends CrudRepository<Ricette, Long> {
    List<Ricette> findByUserId(Long userid);
    
    @Query("SELECT r FROM Ricette r WHERE r.categoria = :categoria")
    List<Ricette> findByCategoria(@Param("categoria") String categoria);

	public boolean existsByNome(String nome);	

}
