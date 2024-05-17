package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.repository.CuocoRepository;


@Service
public class CuocoService {

	@Autowired
	private CuocoRepository cuocoRepository;
	
	public Cuoco findById(Long id) {
		return cuocoRepository.findById(id).get();
	}
	
	public Iterable<Cuoco> findAll() {
		return cuocoRepository.findAll();
	}
	
//	public Iterable<Cuoco> findActorsNotInMovie(@Param("movieId") Long id){
//		 return cuocoRepository.findActorsNotInMovie(id);
//	}
	
	public void save(Cuoco cuoco) {
		cuocoRepository.save(cuoco);
	}
}
