package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ricette;
import it.uniroma3.siw.repository.RicetteRepository;

//@Component
//public class RicetteValidator implements Validator {
//	@Autowired
//	private RicetteRepository ricetteRepository;
//
//	@Override
//	public void validate(Object o, Errors errors) {
//		Ricette ricette = (Ricette)o;
//		if (ricette.getNome()!=null) {
//			errors.reject("ricette.duplicate");
//		}
//	}
//	@Override
//	public boolean supports(Class<?> aClass) {
//		return Ricette.class.equals(aClass);
//	}
//
//	
//
//}