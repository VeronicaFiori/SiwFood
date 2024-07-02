package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.RicetteValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.model.RigaRicetta;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicetteService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class RicetteController {
	
	@Autowired
	private RicetteService ricetteService;
	@Autowired
	private UserService userService;
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private  RicetteValidator ricetteValidator;



	@GetMapping(value="/manage")
	public String indexManage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "indexManage.html";
		}
		else {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexManageAdmin.html";
			}
			else if (credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				return "cuoco/indexManageCuochi.html";
			}
		}
		return "indexManage.html";
	}



	@GetMapping("/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model,String categoria) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Ricette ricetta = this.ricetteService.findById(id);
		model.addAttribute("ricetta", ricetta);
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "ricetta.html";
		}

		else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			categoria= ricetta.getCategoria();
			model.addAttribute("categoria", categoria);

			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "/admin/ricettaAdmin.html";
			}
			else if (credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				String currentUsername = userDetails.getUsername();
				model.addAttribute("currentUsername", currentUsername);
				return "/cuoco/ricettaCuoco.html";


			}
		}

		return "ricetta.html";
	}

	@GetMapping("/ricette")
	public String getCategorieRicette() {
		return "ricette.html";
	}


	@GetMapping("/ricette/{categoria}")
	public String getRicettePerCategoria(@PathVariable("categoria") String categoria, Model model) {
		List<Ricette> ricette = ricetteService.findByCategoria(categoria);
		model.addAttribute("ricette", ricette);
		model.addAttribute("categoria", categoria);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "ricetteSingolaCategoria.html";
		}
		else {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE) || credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				return "/cuoco/ricetteSingolaCategoria.html";
			}
		}				
		return "ricetteSingolaCategoria.html";
	}




	/**UN USER PUO ELIMINARE LA PROPRIA RICETTA MENTRE L'ADMIN PUO ELIMINARE TUTTE **/

	@GetMapping("/deleteRicetta/{id}/{categoria}")
	public String deleteIngrediente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,@PathVariable("categoria") String categoria) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			this.ricetteService.deleteRicettaById(id); 		
			redirectAttributes.addFlashAttribute("success", "Ricetta eliminata con successo!");
			return "redirect:/ricette/{categoria}";
		}

		User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("Utente non trovato"));
		try {
			ricetteService.deleteRicetta(id, currentUser.getId() );
			return "redirect:/ricette/{categoria}";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a eliminare questa ricetta");
			return "redirect:/ricette/{categoria}";
		}


	}

	/**AGGIUNGI UNA NUOVA RICETTAA**/

	@GetMapping("/addRicetta")
	public String mostraFormAggiunta(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		Ricette ricetta = new Ricette();
		model.addAttribute("ricetta", ricetta);
		List<Ingrediente> ingredienti = ingredienteService.findAll(); 
		model.addAttribute("ingredienti", ingredienti);

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			// Recupera gli utenti con ruolo "DEFAULT" solo per l'admin
			List<User> cuochi = this.userService.findByRole(Credentials.DEFAULT_ROLE);
			model.addAttribute("cuochi", cuochi);
			return "/admin/aggiungiRicetta.html";

		}
		return "/cuoco/aggiungiRicetta.html";
	}


	@PostMapping("/addRicettaCategoria")
	public String aggiungiRicetta(@Valid @ModelAttribute("ricetta") Ricette ricetta, 
			@RequestParam(required = false) List<Long> ingredientiIds, 
			@RequestParam List<String> quantita,
			@RequestParam("file") MultipartFile image,
			BindingResult bindingResult,
			Model model) throws IOException {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		this.ricetteValidator.validate(ricetta, bindingResult);
		List<Ingrediente> ingredienti = ingredienteService.findAll();;
		model.addAttribute("ingredienti", ingredienti);


		if(!bindingResult.hasErrors()) {

			Image img = new Image(image.getBytes());
			this.imageRepository.save(img);
			ricetta.setImage(img);

			if (ricetta.getRigheRicetta() == null) {
				ricetta.setRigheRicetta(new ArrayList<>());
			}
			/*Aggiungi ingredienti*/
			for (int i = 0; i < ingredientiIds.size(); i++) {
				Ingrediente ingrediente = ingredienteService.getIngrediente(ingredientiIds.get(i));
				RigaRicetta rigaRicetta = new RigaRicetta();
				rigaRicetta.setIngrediente(ingrediente);
				rigaRicetta.setQuantita(quantita.get(i));
				ricetta.addRigaRicetta(rigaRicetta);
			}
			/*Se admin assegna lo chef della ricetta*/
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				User chefId = ricetta.getUser();
				ricetta.setUser(chefId);
			}
			else {
				User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("User not found"));
				ricetta.setUser(currentUser);
			}

			ricetteService.save(ricetta);

			if (ricetta.getCategoria().equals("Primi Piatti")) {
				return "redirect:/ricette/Primi%20Piatti";
			} else if (ricetta.getCategoria().equals("Secondi Piatti")) {
				return "redirect:/ricette/Secondi%20Piatti";
			} else if (ricetta.getCategoria().equals("Dessert")) {
				return "redirect:/ricette/Dessert";
			} else if (ricetta.getCategoria().equals("Antipasti")) {
				return "redirect:/ricette/Antipasti";
			}
			return "redirect:/ricette/{categoria}";
		}
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			List<User> cuochi = this.userService.findByRole(Credentials.DEFAULT_ROLE); 
			model.addAttribute("cuochi", cuochi);
			return "/admin/aggiungiRicetta.html";	
		}
		return "/cuoco/aggiungiRicetta.html";	

	}


	/**MODIFICA RICETTA **/

	@GetMapping("/ricetta/modifica/{id}")
	public String showEditRicetta(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Ricette ricetta = this.ricetteService.findById(id); 

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		model.addAttribute("ricetta", ricetta);
		List<Ingrediente> ingredienti = ingredienteService.findAll();;
		model.addAttribute("ingredienti", ingredienti);

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			List<User> cuochi = this.userService.findByRole(Credentials.DEFAULT_ROLE); 
			model.addAttribute("cuochi", cuochi);
			return "/admin/modifica.html";
		}
		return "/cuoco/modifica.html";
	}


	@PostMapping("/ricetta/modifica/{id}")
	public String editRicetta(@PathVariable("id") Long id, @Valid @ModelAttribute("ricetta") Ricette aggRicetta, 
			BindingResult bindingResult, Model model, 
			RedirectAttributes redirectAttributes,
			@RequestParam List<Long> ingredientiIds, 
			@RequestParam List<String> quantita) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Ricette ricetta = ricetteService.getRicetta(id);

		// Aggiorna info ricetta
		ricetta.setNome(aggRicetta.getNome());
		ricetta.setDescrizione(aggRicetta.getDescrizione());
		ricetta.getRigheRicetta().clear(); // Rimuovi tutte le righe di ricetta esistenti

		//Aggiungi le nuove righe di ricetta
		for (int i = 0; i < ingredientiIds.size(); i++) {
			Ingrediente ingrediente = ingredienteService.getIngrediente(ingredientiIds.get(i));
			if (ingrediente != null) {
				RigaRicetta rigaRicetta = new RigaRicetta();
				rigaRicetta.setIngrediente(ingrediente);
				rigaRicetta.setQuantita(quantita.get(i));
				ricetta.addRigaRicetta(rigaRicetta);
			}
		}
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			User chefId = aggRicetta.getUser();  //aggiorna lo chef
			ricetta.setUser(chefId);
		}

		ricetteService.save(ricetta);  // Salva la ricetta aggiornata nel database

		return "redirect:/ricetta/{id}";
	}
	
	
}
