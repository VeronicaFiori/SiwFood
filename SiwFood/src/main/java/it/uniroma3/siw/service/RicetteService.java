package it.uniroma3.siw.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicetteRepository;


@Service

public class RicetteService {


		@Autowired
		private RicetteRepository ricetteRepository;

		public Ricette findById(Long id) {
			return ricetteRepository.findById(id).get();
		}

		public Iterable<Ricette> findAll() {
			return ricetteRepository.findAll();
		}

		public void save(Ricette ricette) {
			ricetteRepository.save(ricette);
		}


}
