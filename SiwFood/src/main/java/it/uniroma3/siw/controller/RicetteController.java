package it.uniroma3.siw.controller;

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

}
