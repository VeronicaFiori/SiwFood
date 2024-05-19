package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicetteRepository;


@Controller
public class IngredientiController {

	@Autowired
	private IngredienteRepository ingredienteRepository;
	@Autowired
	private RicetteRepository ricetteRepository;




	@GetMapping("/cuoco/ingredienti")
	public String getIngredienteCuoco(Model model) {		
		model.addAttribute("ingrediente", this.ingredienteRepository.findAll());
		return "/cuoco/ingredienti.html";
	}

	@GetMapping("/cuoco/ingrediente/{id}")
	public String getIngredienti(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteRepository.findById(id).get());
		return "/cuoco/ingrediente.html";
	}

	// Metodo per mostrare il form di aggiunta di un nuovo ingrediente
	@GetMapping("/cuoco/addIngredientiCuochi")
	public String showAddIngredienteForm(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "cuoco/addIngredientiCuochi.html";
	}


	/*USER PUO AGGIUNGERE UN INGREDIENTE*/
	@PostMapping("/cuoco/ingredienti")
	public String addIngrediente(@ModelAttribute Ingrediente ingrediente, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
		if (bindingResult.hasErrors()) {
			return "cuoco/addIngredientiCuochi.html";
		}
		Ingrediente existingIngrediente = ingredienteRepository.findByNome(ingrediente.getNome());

		if (existingIngrediente != null) {
			// L'ingrediente esiste già nel database
			redirectAttributes.addFlashAttribute("error", "L'ingrediente esiste già nel database.");
			return "redirect:/cuoco/addIngredientiCuochi";
		} else {
			// L'ingrediente non esiste nel database, aggiungilo
			ingredienteRepository.save(ingrediente);
			redirectAttributes.addFlashAttribute("success", "Ingrediente aggiunto con successo!");

			model.addAttribute("ingrediente", new Ingrediente());
			model.addAttribute("ingredienti", ingredienteRepository.findAll());
			return "redirect:/cuoco/ingredienti";
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
