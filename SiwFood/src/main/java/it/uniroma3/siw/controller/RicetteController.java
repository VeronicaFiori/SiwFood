package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.model.RigaRicetta;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicetteRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredienteService;
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
	private IngredienteService ingredienteService;
//	@Autowired
//	private IngredienteRepository ingredienteRepository;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
    private ImageRepository imageRepository;


	//	@Autowired 
	//	private RicetteValidator ricetteValidator;


	/*******************UTENTE NON REGISTRATO***********/

	@GetMapping(value="/manage")
	public String indexRicette() {
		return "indexManage.html";
	}

	//	@GetMapping("/ricette")
	//	public String getRicette(Model model) {		
	//		model.addAttribute("ricette", this.ricetteRepository.findAll());
	//		return "ricette.html";
	//
	//	}


	@GetMapping("/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		Ricette ricetta = ricetteRepository.findById(id).get();

		model.addAttribute("ricetta", ricetta);
		//model.addAttribute("cuochi", userRepository.findById(id).get());

		return "ricetta.html";
	}

	@GetMapping("/ricette")
	public String getRicette() {
		return "ricette.html";
	}

	@GetMapping("/ricette/{categoria}")
	public String getRicettePerCategoria(@PathVariable("categoria") String categoria, Model model) {
		List<Ricette> ricette = ricetteService.findByCategoria(categoria);
		model.addAttribute("ricette", ricette);
		model.addAttribute("categoria", categoria);
		return "ricetteSingolaCategoria.html";

	}


	/*************UTENTE REGISTRATO:CUOCO************/

	@GetMapping(value="/loggedIn/manage")
	public String indexRicetteLoggedIn() {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "admin/indexManageAdmin.html";
		}

		return "cuoco/indexManageCuochi.html";
	}

	/*VISUALIZZAZIONE DI TUTTE LE RICETTE*/
	//	@GetMapping("/loggedIn/ricette")
	//	public String getRicetteLoggedIn(Model model) {		
	//		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	//
	//		model.addAttribute("ricette", this.ricetteRepository.findAll());
	//		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	//			return "/admin/ricetteAdmin.html";
	//		}
	//
	//		return "/cuoco/ricetteCuoco.html";
	//	}	




	//    /*VISUALIZZAZIONE PAGINA RICETTA CON UN CERTO ID */
	//	@GetMapping("/cuoco/ricettaCuoco/{id}")
	//	public String getRicettaCuoco(@PathVariable("id") Long id, Model model) {
	//		Ricette ricetta = ricetteService.findById(id);
	//		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//	    String currentUsername = userDetails.getUsername();
	//		model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
	//	    model.addAttribute("currentUsername", currentUsername);
	//		return "/cuoco/ricettaCuoco.html";
	//	}
	//	
	/*VISUALIZZAZIONE PAGINA RICETTA CON UN CERTO ID */
	//		@GetMapping("/loggedIn/ricetta/{id}")
	//		public String getRicettaLoggedIn(@PathVariable("id") Long id, Model model) {
	//			Ricette ricetta = ricetteService.findById(id);
	//			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//		    String currentUsername = userDetails.getUsername();
	//		    
	//		    List<RicettaIngrediente> ingredienti = ricettaIngredienteService.findByRicetta(ricetta);
	//
	//		    
	//			//model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
	//		    List<Ingrediente> ingredienti = ingredienteService.getIngredientiByRicetta(ricetta);
	//		    model.addAttribute("ricetta", ricetta);
	//		    model.addAttribute("ingredienti", ingredienti);
	//		    model.addAttribute("currentUsername", currentUsername);
	//		    
	//			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	//			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	//				return "/admin/ricettaAdmin.html";
	//			}
	//			return "/cuoco/ricettaCuoco.html";
	//		}









	@GetMapping("/loggedIn/ricetta/{id}")
	public String getRicettaLoggedIn(@PathVariable("id") Long id, Model model) {
		//Ricette ricetta = ricetteService.findById(id);
		Ricette ricetta= ricetteRepository.findById(id).get();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUsername = userDetails.getUsername();
		
		model.addAttribute("ricetta", ricetta);
		model.addAttribute("currentUsername", currentUsername);

		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "/admin/ricettaAdmin.html";
		}
		return "/cuoco/ricettaCuoco.html";
	}


	@GetMapping("/ricette/categoria")
	public String getRicettecat() {
		return "ricetteCategoria.html";
	}
	
	
	@GetMapping("/ricette/categoria/{categoria}")
	public String getRicettePerCategoriaUser(@PathVariable("categoria") String categoria, Model model) {
		List<Ricette> ricette = ricetteService.findByCategoria(categoria);
		model.addAttribute("ricette", ricette);
		model.addAttribute("categoria", categoria);
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials != null) {		
			return "/cuoco/ricetteSingolaCategoria.html";	
		}
		return "ricetteSingolaCategoria.html";
	}



	/**UN USER PUO ELIMINARE LA PROPRIA RICETTA MENTRE L'ADMIN PUO ELIMINARE TUTTE **/
	
	@GetMapping("/loggedIn/deleteRicetta/{id}/{categoria}")
	public String deleteIngrediente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,@PathVariable("categoria") String categoria) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			ricetteRepository.deleteById(id);		
			redirectAttributes.addFlashAttribute("success", "Ricetta eliminata con successo!");
			return "redirect:/ricette/categoria/{categoria}";
		}

		User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("Utente non trovato"));
		try {
			ricetteService.deleteRicetta(id, currentUser.getId() );
			return "redirect:/ricette/categoria/{categoria}";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a eliminare questa ricetta");
			return "redirect:/ricette/categoria/{categoria}";
		}


	}



	/**AGGIUNGI UNA NUOVA RICETTAA**/
	
	@GetMapping("/addRicetta")
	public String mostraFormAggiunta(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		Ricette ricetta = new Ricette();
		model.addAttribute("ricetta", ricetta);
		List<Ingrediente> ingredienti = ingredienteService.getAllIngredienti();
		model.addAttribute("ingredienti", ingredienti);

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			// Recupera gli utenti con ruolo "DEFAULT" solo per l'admin
			List<User> cuochi = userRepository.findByRole(Credentials.DEFAULT_ROLE);
			model.addAttribute("cuochi", cuochi);
			return "/admin/aggiungiRicetta.html";

		}
		return "/cuoco/aggiungiRicetta.html";
	}

	@PostMapping("/addRicettaCategoria")
	public String aggiungiRicetta(@ModelAttribute Ricette ricetta, @RequestParam List<Long> ingredientiIds, 
			                      @RequestParam List<String> quantita,
			                      @RequestParam("file") MultipartFile image) throws IOException {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
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
			return "redirect:/ricette/categoria/Primi%20Piatti";
		} else if (ricetta.getCategoria().equals("Secondi Piatti")) {
			return "redirect:/ricette/categoria/Secondi%20Piatti";
		} else if (ricetta.getCategoria().equals("Dessert")) {
			return "redirect:/ricette/categoria/Dessert";
		} else if (ricetta.getCategoria().equals("Antipasti")) {
			return "redirect:/ricette/categoria/Antipasti";
		}
		return "redirect:/ricette/categoria/{categoria}";
	}

	
    /**MODIFICA RICETTA **/

	@GetMapping("/loggedIn/ricetta/modifica/{id}")
	public String showEditRicetta(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Ricette ricetta = ricetteRepository.findById(id).get(); 

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		model.addAttribute("ricetta", ricetta);
		List<Ingrediente> ingredienti = ingredienteService.getAllIngredienti();
		model.addAttribute("ingredienti", ingredienti);

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			List<User> cuochi = userRepository.findByRole(Credentials.DEFAULT_ROLE);
			model.addAttribute("cuochi", cuochi);
			return "/admin/modifica.html";
		}
		return "/cuoco/modifica.html";
	}


	@PostMapping("/loggedIn/ricetta/modifica/{id}")
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

		return "redirect:/loggedIn/ricetta/{id}";
	}


	
}
