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
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicetteRepository;
import it.uniroma3.siw.service.CredentialsService;


@Controller
public class IngredientiController {

	@Autowired
	private IngredienteRepository ingredienteRepository;
	@Autowired
	private RicetteRepository ricetteRepository;
	@Autowired
	private CredentialsService credentialsService;




	@GetMapping("/loggedIn/ingredienti")
	public String getIngredienteCuoco(Model model) {	
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		model.addAttribute("ingrediente", this.ingredienteRepository.findAll());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "/admin/ingredienti.html";
		}
		return "/cuoco/ingredienti.html";
	}
	

	@GetMapping("/loggedIn/ingrediente/{id}")
	public String getIngredienti(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteRepository.findById(id).get());
		return "/cuoco/ingrediente.html";
	}

	// Metodo per mostrare il form di aggiunta di un nuovo ingrediente
	@GetMapping("/loggedIn/addIngredienti")
	public String showAddIngredienteForm(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "cuoco/addIngredientiCuochi.html";
	}


	/*USER PUO AGGIUNGERE UN INGREDIENTE*/
	@PostMapping("/loggedIn/ingredienti")
	public String addIngrediente(@ModelAttribute Ingrediente ingrediente, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
		if (bindingResult.hasErrors()) {
			return "cuoco/addIngredientiCuochi.html";
		}
		Ingrediente existingIngrediente = ingredienteRepository.findByNome(ingrediente.getNome());

		if (existingIngrediente != null) {
			// L'ingrediente esiste già nel database
			redirectAttributes.addFlashAttribute("error", "L'ingrediente esiste già nel database.");
			return "redirect:/loggedIn/addIngredienti";
		} else {
			// L'ingrediente non esiste nel database, aggiungilo
			ingredienteRepository.save(ingrediente);
			redirectAttributes.addFlashAttribute("success", "Ingrediente aggiunto con successo!");

			model.addAttribute("ingrediente", new Ingrediente());
			model.addAttribute("ingredienti", ingredienteRepository.findAll());
			return "redirect:/loggedIn/ingredienti";
		}
	}

	//	// Metodo per gestire l'aggiunta di un nuovo ingrediente nella pagina con ricette
	//		@PostMapping("/cuoco/ricetteCuoco")
	//		public String addIngrediente(@ModelAttribute Ingrediente ingrediente, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
	//			if (bindingResult.hasErrors()) {
	//				return "cuoco/addIngredientiCuochi.html";
	//			}
	//			ingredienteRepository.save(ingrediente);
	//			redirectAttributes.addFlashAttribute("success", "Ingrediente aggiunto con successo!");
	//			model.addAttribute("ingrediente", ingrediente);
	//			model.addAttribute("ingrediente", this.ingredienteRepository.findAll());
	//			model.addAttribute("ricetta", this.ricetteRepository.findAll());
	//
	//			return "/cuoco/ricetteCuoco.html";
	//		}




}
