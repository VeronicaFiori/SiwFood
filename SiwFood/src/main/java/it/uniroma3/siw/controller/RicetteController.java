package it.uniroma3.siw.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import it.uniroma3.siw.repository.RicetteRepository;

@Controller
public class RicetteController {
	@Autowired
	private RicetteRepository ricetteRepository;

	@GetMapping(value="/manage")
	public String indexRicette() {
		return "indexManage.html";
	}
	
	@GetMapping("/ricette")
	public String getMovies(Model model) {		
		model.addAttribute("ricette", this.ricetteRepository.findAll());
		return "ricette.html";
		
	}
	
	@GetMapping("/ricetta/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
		return "ricetta.html";
	}
	
	@GetMapping(value="/cuoco/manageCuochi")
	public String indexRicetteCuochi() {
		return "cuoco/indexManageCuochi.html";
	}
	
//	@GetMapping(value="/cuoco/addRicetteCuochi/{ricetteId}")
//	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
//		Movie movie = this.movieRepository.findById(movieId).get();
//		Artist actor = this.artistRepository.findById(actorId).get();
//		Set<Artist> actors = movie.getActors();
//		actors.add(actor);
//		this.movieRepository.save(movie);
//		
//		List<Artist> actorsToAdd = actorsToAdd(movieId);
//		
//		model.addAttribute("movie", movie);
//		model.addAttribute("actorsToAdd", actorsToAdd);
//
//		return "admin/actorsToAdd.html";
//	}

}
