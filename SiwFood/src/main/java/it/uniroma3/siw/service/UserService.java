package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;

/**
 * The UserService handles logic for Users.
 */
@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;

   
    @Transactional
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }
    
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }

    @Transactional
	public Optional<User> getUserByCredentials(UserDetails userDetails) {
		String username = userDetails.getUsername();
        return credentialsRepository.findByUsername(username).map(Credentials::getUser);
    }
	
	@Transactional
	public User findById(Long id) {
		return this.userRepository.findById(id).get();
	}
	
	@Transactional
	public List<User> findByRole(String role){
		return this.userRepository.findByRole(role);
	}
	
	@Transactional
	public void deleteUser(Long id) {
		this.userRepository.deleteById(id);
	}
	

}
