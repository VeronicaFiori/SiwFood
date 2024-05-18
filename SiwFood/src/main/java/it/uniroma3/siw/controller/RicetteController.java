package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.RicetteRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RicetteService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class RicetteController {
	@Autowired
	private RicetteRepository ricetteRepository;
	@Autowired
	private RicetteService ricetteService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;

	//	@Autowired 
	//	private RicetteValidator ricetteValidator;

	@GetMapping(value="/manage")
	public String indexRicette() {
		return "indexManage.html";
	}

	@GetMapping("/ricette")
	public String getRicette(Model model) {		
		model.addAttribute("ricette", this.ricetteRepository.findAll());
		return "ricette.html";

	}

	@GetMapping("/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
		return "ricetta.html";
	}

	@GetMapping(value="/cuoco/manageCuochi")
	public String indexRicetteCuochi() {

		return "cuoco/indexManageCuochi.html";
	}


	@GetMapping("/cuoco/ricetteCuoco")
	public String getRicetteCuoco(Model model) {		
		model.addAttribute("ricette", this.ricetteRepository.findAll());
		return "/cuoco/ricetteCuoco.html";
	}



	@GetMapping("/cuoco/deleteRicetta/{id}")
	public String deleteRicetta(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("Utente non trovato"));
	    
	    try {
	        ricetteService.deleteRicetta(id, currentUser.getId());
	        return "redirect:/cuoco/ricetteCuoco";
	    } catch (RuntimeException e) {
	        redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a eliminare questa ricetta");
	        return "redirect:/cuoco/ricetteCuoco";
	    }
	}



	@GetMapping("/cuoco/addRicetteCuochi")
	public String showAddRicetta(Model model) {
		model.addAttribute("ricetta", new Ricette());
		return "/cuoco/addRicetteCuochi.html";
	}






	@GetMapping("/cuoco/ricettaCuoco/{id}")
	public String getRicettaCuoco(@PathVariable("id") Long id, Model model) {
		Ricette ricetta = ricetteService.findById(id);
		model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
		return "ricetta.html";
	}

    
	@PostMapping("/cuoco/ricettaCuoco")
	public String addRicetta(@Valid @ModelAttribute("ricetta") Ricette ricetta, BindingResult bindingResult, Model model) {
		//			this.ricetteValidator.validate(ricette, bindingResult);
		if (!bindingResult.hasErrors()) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Ottieni l'utente dal servizio UserService
	        User user = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("User not found"));
			ricetta.setUser(user);

			this.ricetteRepository.save(ricetta); 
			model.addAttribute("ricetta", ricetta);
			return "cuoco/ricettaCuoco.html";
		} else {
			model.addAttribute("messaggioErrore", "Questa ricetta esiste già");

			return "cuoco/addRicetteCuochi.html"; 
		}
		//	    	if (bindingResult.hasErrors()) {
		//	            return "/cuoco/addRicetteCuochi.html";
		//	        }
		//	        ricetteService.save(ricetta);
		//	        return "redirect:/cuoco/ricetteCuoco";
	}

	

}
