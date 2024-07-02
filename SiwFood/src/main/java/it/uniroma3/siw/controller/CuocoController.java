package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
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

import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UserValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RicetteService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class CuocoController {

	
	@Autowired 
	private RicetteService ricetteService;
	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
    private ImageRepository imageRepository;
	@Autowired
	private CredentialsValidator credentialsValidator;
	@Autowired
	private UserValidator userValidator;


	/*GESTIONE PER USER NON REGISTRATI */
	@GetMapping("/cuochi")
	public String getCuochi(Model model) {		
		/*stampo solo i cuochi e non l'admin*/
		List<User> cuochi = this.userService.findByRole(Credentials.DEFAULT_ROLE);//  userRepository.findByRole(Credentials.DEFAULT_ROLE);
		model.addAttribute("cuochi", cuochi);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "cuochi.html";
		}
		else {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)){
				return "/admin/cuochi.html";
			}
		}
	    return "cuochi.html";
	}
	
	
	@GetMapping("/cuoco/{id}")
	public String getCuoco(@PathVariable("id") Long id, Model model) {

		model.addAttribute("cuoco", this.userService.findById(id));
		List<Ricette> ricette = ricetteService.getRicetteByCuocoId(id);
		model.addAttribute("ricette", ricette);	

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "cuoco.html";
		}
		else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			String currentUsername = userDetails.getUsername();
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE) || credentials.getRole().equals(Credentials.DEFAULT_ROLE)){
				
				model.addAttribute("credentials",credentials);
				model.addAttribute("currentUsername", currentUsername);
				return "/cuoco/cuoco.html";			
			}
		}
		return "cuoco.html";
	}
	

	
	/*SOLO ADMIN PUO ELIMINARE GLI INGREDIENTI*/
	@GetMapping("/deleteCuoco/{id}")
	public String deleteIngrediente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		userService.deleteUser(id);		
		redirectAttributes.addFlashAttribute("success", "Cuoco eliminato con successo!");
		return "redirect:/cuochi";
	}
	
	/*SOLO ADMIN PUO AGGIUNGERE CUOCHI*/
	@GetMapping("/addCuoco")
    public String showAddCuocoForm(Model model) {
		User user = new User();		
        model.addAttribute("user", user);
        model.addAttribute("credentials",new Credentials());
        
        return "/admin/addCuochi.html";

    }

    @PostMapping("/formAddCuoco")
    public String addCuoco(@Valid @ModelAttribute User user, 
    		@Valid @ModelAttribute Credentials credentials,
    		BindingResult userBindingResult,
    		BindingResult credentialsBindingResult, 
    	//	@RequestParam Map<String, String> requestParams, 
    		RedirectAttributes redirectAttributes,
    		@RequestParam("file") MultipartFile image) throws IOException {

    	this.userValidator.validate(user, userBindingResult);
    	this.credentialsValidator.validate(credentials,credentialsBindingResult);

    	if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {

    		Image img = new Image(image.getBytes());
    		this.imageRepository.save(img);
    		user.setImage(img);

    		user.setCredentials(credentials);

    		// Ruolo predefinito per il nuovo utente
    		user.getCredentials().setRole(Credentials.DEFAULT_ROLE);

    		credentials.setUser(user);

    		userService.saveUser(user);
    		credentialsService.saveCredentials(credentials);

    		redirectAttributes.addFlashAttribute("success", "Utente creato con successo!");

    		return "redirect:/cuochi";
    	}
    	return "admin/addCuochi";
    }

    
	 @GetMapping("/modificaCuoco/{id}")
     public String modificaCuoco(@PathVariable("id") Long id, Model model) {
		 
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		 model.addAttribute("credentials",credentials);
		 User user=  this.userService.findById(id); //this.userRepository.findById(id).get();
		 model.addAttribute("user",user);
		 return "admin/modificaCuoco"; 
	 }

     
     @PostMapping("/formModificaCuoco/{id}")
     public String formModificaCuoco(@PathVariable("id") Long id,
                                     @ModelAttribute("user") User nuovoUser,
                                     @RequestParam("file") MultipartFile image,
                                     BindingResult bindingResult,
                                     Model model)  throws IOException{
         UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
         model.addAttribute("credentials", credentials);
        

         User user = this.userService.findById(id);// this.userRepository.findById(id).get();
         user.setName(nuovoUser.getName());
         user.setSurname(nuovoUser.getSurname());
         LocalDate dataOriginal= user.getData();
         user.setData(nuovoUser.getData());
         LocalDate dataNew= user.getData();

         if( dataNew==null) {
        	 user.setData(dataOriginal);
        	 
         }
         user.setEmail(nuovoUser.getEmail());
         
         if (!image.isEmpty()) {
     		Image img = new Image(image.getBytes());
     		this.imageRepository.save(img);
     		user.setImage(img);
         }

         this.userService.saveUser(user);
         return "redirect:/cuoco/{id}" ;
     }
}
	