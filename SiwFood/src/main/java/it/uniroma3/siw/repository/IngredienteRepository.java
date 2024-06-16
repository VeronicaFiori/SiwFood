package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	Ingrediente findByNome(String nome);
	
 //  List<Ingrediente> findByRicetta(Ricette ricette);
   
//   @Query("SELECT i FROM Ingrediente i WHERE i.id NOT IN (SELECT ri.ingrediente.id FROM RicettaIngrediente ri WHERE ri.ricetta.id = :ricettaId)")
//   List<Ingrediente> findIngredientiNotInRicetta(@Param("ricettaId") Long ricettaId);

   
   
   
   
   
   
   
   
//   @Query("SELECT i FROM Ingrediente i WHERE i.id NOT IN (SELECT ri.id.ingredienteId FROM RicettaIngrediente ri WHERE ri.ricetta.id = :ricettaId)")
//   List<Ingrediente> findIngredientiNotInRicetta(@Param("ricettaId") Long ricettaId);
}



//@Query(value="select * "
//+ "from artist a "
//+ "where a.id not in "
//+ "(select actors_id "
//+ "from movie_actors "
//+ "where movie_actors.starred_movies_id = :movieId)", nativeQuery=true)
//public Iterable<Cuoco> findCuochiNotInMovie(@Param("movieId") Long id);


