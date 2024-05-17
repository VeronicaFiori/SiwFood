package it.uniroma3.siw.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.repository.CuocoRepository;

@Controller
public class CuocoController {
	@Autowired 
	private CuocoRepository cuocoRepository;
	
	
	@GetMapping("/cuoco")
	public String getCuochi(Model model) {
		model.addAttribute("cuochi", this.cuocoRepository.findAll());
		return "cuochi.html";
	}
	
	@GetMapping("/cuoco/{id}")
	public String getCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.cuocoRepository.findById(id).get());
		return "cuoco.html";
	}

	
	
//	@GetMapping(value="/admin/formNewArtist")
//	public String formNewArtist(Model model) {
//		model.addAttribute("artist", new Artist());
//		return "admin/formNewArtist.html";
//	}
//	
//	@GetMapping(value="/admin/indexArtist")
//	public String indexArtist() {
//		return "admin/indexArtist.html";
//	}
//	
//	@PostMapping("/admin/artist")
//	public String newArtist(@ModelAttribute("artist") Artist artist, Model model) {
//		if (!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
//			this.artistRepository.save(artist); 
//			model.addAttribute("artist", artist);
//			return "artist.html";
//		} else {
//			model.addAttribute("messaggioErrore", "Questo artista esiste già");
//			return "admin/formNewArtist.html"; 
//		}
}

	