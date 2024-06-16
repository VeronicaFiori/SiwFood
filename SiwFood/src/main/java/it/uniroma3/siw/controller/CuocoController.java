package it.uniroma3.siw.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.RicetteRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RicetteService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class CuocoController {
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private RicetteRepository ricetteRepository;
	@Autowired 
	private RicetteService ricetteService;
	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
    private PasswordEncoder passwordEncoder;


	
	/*GESTIONE PER USER NON REGISTRATI */
	@GetMapping("/cuochi")
	public String getCuochi(Model model) {		
		/*stampo solo i cuochi e non l'admin*/
		List<User> cuochi = userRepository.findByRole(Credentials.DEFAULT_ROLE);
	    model.addAttribute("cuochi", cuochi);
		//model.addAttribute("cuochi", this.userRepository.findAll());
		return "cuochi.html";

	}
	
	@GetMapping("/cuoco/{id}")
	public String getCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.userRepository.findById(id).get());
		List<Ricette> ricette = ricetteService.getRicetteByCuocoId(id);
        model.addAttribute("ricette", ricette);
		return "cuoco.html";
	}
	
	/*ADMIN */
	@GetMapping("/loggedIn/cuochi")
	public String getCuochiLoggedIn(Model model) {		
		List<User> cuochi = userRepository.findByRole(Credentials.DEFAULT_ROLE);
	    model.addAttribute("cuochi", cuochi);
		//model.addAttribute("cuochi", this.userRepository.findAll());
		return "/admin/cuochi.html";
	}
	
	/*SOLO ADMIN PUO ELIMINARE GLI INGREDIENTI*/
	@GetMapping("/loggedIn/deleteCuoco/{id}")
	public String deleteIngrediente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		userRepository.deleteById(id);		
		redirectAttributes.addFlashAttribute("success", "Cuoco eliminato con successo!");
		return "redirect:/loggedIn/cuochi";
	}
	
	/*SOLO ADMIN PUO AGGIUNGERE CUOCHI*/
	@GetMapping("/loggedIn/addCuoco")
    public String showAddCuocoForm(Model model) {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User cuoco = new User();
		//cuoco.setCredentials(new Credentials());
		
        model.addAttribute("cuoco", cuoco);
        model.addAttribute("credentials",new Credentials());
        

//        model.addAttribute("username",credentialsService.getCredentials(userDetails.getUsername()));
//        model.addAttribute("password",credentialsService.getCredentials(userDetails.getPassword()));


//        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//	        return "/admin/addCuochi.html";
//
//		}
        //return "/cuoco/addRicetteCuochi.html";
        return "/admin/addCuochi.html";

    }

    @PostMapping("/formAddCuoco")
    public String addCuoco(@Valid @ModelAttribute User cuoco, 
    	                   	@Valid @ModelAttribute Credentials credentials,
    	                    	BindingResult bindingResult, 
                             @RequestParam Map<String, String> requestParams, 
                             RedirectAttributes redirectAttributes) {
    	
        if (bindingResult.hasErrors()) {
	            return "admin/addCuochi.html";
			}
        
        // Verifica se le credenziali sono null e istanzialele se necessario
            cuoco.setCredentials(credentials);
        
        // Imposta il ruolo predefinito per il nuovo utente
        cuoco.getCredentials().setRole(Credentials.DEFAULT_ROLE);

        credentials.setUser(cuoco);

        // Salva il nuovo utente nel database
        userService.saveUser(cuoco);
        credentialsService.saveCredentials(credentials);

        // Aggiungi un messaggio di successo per l'utente
        redirectAttributes.addFlashAttribute("successMessage", "Utente creato con successo!");

        return "redirect:/loggedIn/cuochi";
    }



}

	