package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u JOIN u.credentials c WHERE c.role = :role")
	List<User> findByRole(@Param("role") String role);

	boolean existsByEmail(String email);

	void deleteById(Long id);


}