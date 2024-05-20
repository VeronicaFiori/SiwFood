package it.uniroma3.siw.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.repository.RicetteRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.RicetteService;

@Controller
public class CuocoController {
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private RicetteRepository ricetteRepository;
	@Autowired 
	private RicetteService ricetteService;
	/*GESTIONE CUOCHI=USER REGISTRATI*/
	@GetMapping("/cuochi")
	public String getCuochi(Model model) {		
		model.addAttribute("cuochi", this.userRepository.findAll());
		return "cuochi.html";

	}
	
	@GetMapping("/cuoco/{id}")
	public String getCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.userRepository.findById(id).get());
		List<Ricette> ricette = ricetteService.getRicetteByCuocoId(id);
        model.addAttribute("ricette", ricette);
		return "cuoco.html";
	}

	

}

	