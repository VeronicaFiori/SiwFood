package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.model.User;
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
	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	@Autowired
	private CredentialsService credentialsService;
	//	@Autowired 
	//	private RicetteValidator ricetteValidator;

	
	/*******************UTENTE NON REGISTRATO***********/
	
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
	    Ricette ricetta = ricetteRepository.findById(id).get();

	    model.addAttribute("ricetta", ricetta);
	    return "ricetta.html";
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
	@GetMapping("/loggedIn/ricette")
	public String getRicetteLoggedIn(Model model) {		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		model.addAttribute("ricette", this.ricetteRepository.findAll());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
		       return "/admin/ricetteAdmin.html";
            }
            
		 return "/cuoco/ricetteCuoco.html";
    }	
	
	

	
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
		@GetMapping("/loggedIn/ricetta/{id}")
		public String getRicettaLoggedIn(@PathVariable("id") Long id, Model model) {
			Ricette ricetta = ricetteService.findById(id);
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    String currentUsername = userDetails.getUsername();
			//model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
		    List<Ingrediente> ingredienti = ingredienteService.getIngredientiByRicetta(ricetta);
		    model.addAttribute("ricetta", ricetta);
		    model.addAttribute("ingredienti", ingredienti);
		    model.addAttribute("currentUsername", currentUsername);
		    
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "/admin/ricettaAdmin.html";
			}
			return "/cuoco/ricettaCuoco.html";
		}
	

    /*UN USER PUO ELIMINARE LA PROPRIA RICETTA */
	@GetMapping("/loggedIn/deleteRicetta/{id}")
	public String deleteIngrediente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			ricetteRepository.deleteById(id);		
	        redirectAttributes.addFlashAttribute("success", "Ricetta eliminata con successo!");
			return "redirect:/loggedIn/ricette";
		}
			
	    User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("Utente non trovato"));
	    try {
	        ricetteService.deleteRicetta(id, currentUser.getId());
	        return "redirect:/loggedIn/ricette";
	    } catch (RuntimeException e) {
	        redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a eliminare questa ricetta");
	        return "redirect:/loggedIn/ricette";
	    }
	    
	    
	    
	}


    


//	@GetMapping("/cuoco/ricettaCuoco/{id}")
//	public String getRicettaCuoco(@PathVariable("id") Long id, Model model) {
//		Ricette ricetta = ricetteService.findById(id);
//		model.addAttribute("ricetta", this.ricetteRepository.findById(id).get());
//		return "/cuoco/ricettaCuoco.html";
//	}

	/*TUTTI GLI USER POSSONO AGGIUNGERE NUOVE RICETTE */
	
/*	QUESTA MOD*/	
//	@GetMapping("/cuoco/addRicetteCuochi")
//	public String showAddRicetta(Model model) {
//		model.addAttribute("ricetta", new Ricette());
//		return "/cuoco/addRicetteCuochi.html";
//	}
	
/*************************************/
	
	    @GetMapping("/loggedIn/addRicetta")
	    public String showAddRicettaForm(Model model) {
	    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			
	        model.addAttribute("ricetta", new Ricette());
	        model.addAttribute("ingredienti", ingredienteService.findAll());
	        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
		        return "/admin/addRicetteAdmin.html";

			}
	        return "/cuoco/addRicetteCuochi.html";
	    }

	    @PostMapping("/loggedIn/ricetta")
	    public String addRicetta(@Valid @ModelAttribute Ricette ricetta, BindingResult bindingResult, 
	                             @RequestParam("ingredientiIds") List<Long> ingredientiIds, 
	                             @RequestParam Map<String, String> requestParams, 
	                             RedirectAttributes redirectAttributes) {
	    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	        if (bindingResult.hasErrors()) {
				if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
		            return "admin/addRicetteAdmin.html";

				}
	            return "cuoco/addRicetteCuochi.html";
	        }
	        User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("User not found"));
	        ricetta.setUser(currentUser);

	        List<Ingrediente> ingredientiSelezionati = new ArrayList<>();
	        for (Long ingredienteId : ingredientiIds) {
	            Ingrediente ingrediente = ingredienteService.getIngrediente(ingredienteId);
	            String quantita = requestParams.get("quantita_" + ingredienteId);
	            ingrediente.setQuantita(quantita);  // Assumi che Ingrediente abbia un campo 'quantita'
	            ingredientiSelezionati.add(ingrediente);
	        }

	        ricetta.setIngredienti(ingredientiSelezionati);
	        ricetteService.save(ricetta);
	        
	        
	        redirectAttributes.addFlashAttribute("success", "Ricetta aggiunta con successo!");

	        return "redirect:/loggedIn/ricette";
	    }
	

	
	
/***************************************/
    
	/*AGGIUNGI RICETTA*/
//	@PostMapping("/cuoco/ricettaCuoco")
//	public String addRicetta(@Valid @ModelAttribute("ricetta") Ricette ricetta, BindingResult bindingResult, Model model) {
//		//			this.ricetteValidator.validate(ricette, bindingResult);
//		if (!bindingResult.hasErrors()) {
//			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			// Ottieni l'utente dal servizio UserService
//	        User user = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("User not found"));
//			ricetta.setUser(user);
//
//			this.ricetteRepository.save(ricetta); 
//			model.addAttribute("ricetta", ricetta);
//	      //  model.addAttribute("ingrediente", ingredienteRepository.findAll()); //ricetta.getIngredienti());
//
//
//			return "cuoco/ricettaCuoco.html";
//		} else {
//			model.addAttribute("messaggioErrore", "Questa ricetta esiste già");
//
//			return "cuoco/addRicetteCuochi.html"; 
//		}
//	}
	

	
/*MODIFICA RICETTA E INGREDIENTI DA PARTE DELL'USER */
	
//	@GetMapping("/cuoco/ricettaCuoco/modifica")
//	public String modificaRicetta(@PathVariable("id") Long id, @Valid @ModelAttribute("ricetta") Ricette aggRicetta,RedirectAttributes redirectAttributes) {
//	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	    User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("Utente non trovato"));
//	    
//	    Ricette ricetta = ricetteService.findById(id);
//	    
//    if (ricetta.getUser().getCredentials().getUsername().equals(userDetails.getUsername())) {
//    	ricetta.setNome(aggRicetta.getNome());
//	    ricetta.setDescrizione(aggRicetta.getDescrizione());
//	    ricetta.setIngredienti(aggRicetta.getIngredienti());  // Assicurati di avere i getter e setter per ingredienti
//	    
//	    ricetteService.save(ricetta);
//    	return "/cuoco/modifica.html";
//	        
//	    } else {
//	        redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a modificare questa ricetta");
//	        return "redirect:/cuoco/ricettaCuoco.html";
//	    }
//	}
	
	

	
	@GetMapping("/loggedIn/ricetta/modifica/{id}")
	public String showEditRicetta(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
	    Ricette ricetta = ricetteService.getRicetta(id);
	    
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("ricetta", ricetta);
			model.addAttribute("ingrediente", this.ingredienteRepository.findAll());
	     //   model.addAttribute("ingredienti", ingredienteService.findAll());
			return "/admin/modifica.html";
		}
	    if (!ricetta.getUser().getCredentials().getUsername().equals(userDetails.getUsername())) {
	        redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a modificare questa ricetta");
	        return "redirect:/loggedIn/ricette";
	    }
	    
	    model.addAttribute("ricetta", ricetta);
		model.addAttribute("ingrediente", this.ingredienteRepository.findAll());
     //   model.addAttribute("ingredienti", ingredienteService.findAll());

		
	    return "/cuoco/modifica.html";
	}

	
	
	
	@PostMapping("/loggedIn/ricetta/modifica/{id}")
	public String editRecipe(@PathVariable("id") Long id, @Valid @ModelAttribute("ricetta") Ricette aggRicetta, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (bindingResult.hasErrors()) {
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "/admin/modifica.html";
			}
	        return "/cuoco/modifica.html";
	    }
		
		
	    Ricette ricetta = ricetteService.getRicetta(id);
//	    if (!ricetta.getUser().getCredentials().getUsername().equals(userDetails.getUsername())) {
//	        redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a modificare questa ricetta");
//	        return "redirect:/loggedIn/ricette";
//	    }
	    
	    ricetta.setNome(aggRicetta.getNome());
	    ricetta.setDescrizione(aggRicetta.getDescrizione());
	  //  ricetta.setIngredienti(aggRicetta.getIngredienti());  
	  //  ricetta.getIngredienti().clear();
	  //  ricetta.getIngredienti().addAll(aggRicetta.getIngredienti());
	    
	    ricetteService.save(ricetta);
//	    if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//	    	return "/admin/modifica.html";
//	    }
	    return "redirect:/loggedIn/ricette";
	}



//	NON VA IL MAPPPING !!!!
//	@GetMapping(value="/cuoco/ricettaCuoco/modifica/' + ${ricetta.id}}")
//	public String mostraAggiungiIngrediente(@PathVariable("ricetteId") Long ricetteId, 
//	                                       @PathVariable("ingredienteId") Long ingredienteId, 
//	                                       Model model, 
//	                                       RedirectAttributes redirectAttributes) {
//	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	    User currentUser = userService.getUserByCredentials(userDetails).orElseThrow(() -> new RuntimeException("Utente non trovato"));
//
//	    Ricette ricetta = ricetteRepository.findById(ricetteId).orElseThrow(() -> new RuntimeException("Ricetta non trovata"));
//	    Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId).orElseThrow(() -> new RuntimeException("Ingrediente non trovato"));
//
//	    if (!ricetta.getUser().getId().equals(currentUser.getId())) {
//	        redirectAttributes.addFlashAttribute("error", "Utente non autorizzato a modificare questa ricetta");
//	        return "redirect:/cuoco/ricettaCuoco";
//	    }
//
//	    ricetta.getIngredienti().add(ingrediente);
//	    ricetteRepository.save(ricetta);
//
//	    model.addAttribute("ricetta", ricetta);
//	    model.addAttribute("ingredienteAggiungi", ingrediente);
//
//	    return "/cuoco/modifica.html";  // La pagina HTML per modificare la ricetta
//	}

	
	

}
