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
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredienteService;


@Controller
public class IngredientiController {

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private CredentialsService credentialsService;



	@GetMapping("/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "/cuoco/ingrediente.html";
	}

	@GetMapping("/ingredienti")
	public String getIngredienti(Model model) {	
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		model.addAttribute("ingrediente", this.ingredienteService.findAll());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "/admin/ingredienti.html";
		}
		return "/cuoco/ingredienti.html";
	}

	/*SOLO ADMIN PUO ELIMINARE GLI INGREDIENTI*/
	@GetMapping("/deleteIngrediente/{id}")
	public String deleteIngrediente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		ingredienteService.deleteIngrediente(id);		
		redirectAttributes.addFlashAttribute("success", "Ingrediente eliminato con successo!");
		return "redirect:/ingredienti";
	}


	/* Aggiunta di un nuovo ingrediente*/
	@GetMapping("/addIngredienti")
	public String showAddIngredienteForm(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "cuoco/addIngredientiCuochi.html";
	}


	/*USER PUO AGGIUNGERE UN INGREDIENTE*/
	@PostMapping("/ingredienti")
	public String addIngrediente(@ModelAttribute Ingrediente ingrediente, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
		if (bindingResult.hasErrors()) {
			return "cuoco/addIngredientiCuochi.html";
		}
		Ingrediente existingIngrediente = ingredienteService.findByNome(ingrediente.getNome());

		if (existingIngrediente != null) {
			// Se esiste già nel database
			redirectAttributes.addFlashAttribute("error", "L'ingrediente esiste già nel database.");
			return "redirect:/addIngredienti";
		} else {			
			ingredienteService.save(ingrediente);
			redirectAttributes.addFlashAttribute("success", "Ingrediente aggiunto con successo!");

			model.addAttribute("ingrediente", new Ingrediente());
			model.addAttribute("ingredienti", ingredienteService.findAll());
			return "redirect:/ingredienti";
		}
	}
}
