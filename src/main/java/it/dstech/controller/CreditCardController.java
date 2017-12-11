package it.dstech.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.dstech.models.CreditCard;
import it.dstech.models.User;
import it.dstech.services.CreditCardService;
import it.dstech.services.CreditCardServiceImpl;
import it.dstech.services.UserService;

@RestController
@RequestMapping("/creditCard")
public class CreditCardController {
	private static final Logger logger = Logger.getLogger(CreditCardServiceImpl.class.getName());

	@Autowired
	private CreditCardService service;
	
	@Autowired
	private UserService userService;

	@GetMapping("/getModel")
	public ResponseEntity<CreditCard> getmodel() {
		CreditCard creditCard = new CreditCard();
		return new ResponseEntity<CreditCard>(creditCard, HttpStatus.CREATED);
	}

	@GetMapping("/getCardUser")
	public ResponseEntity<List<CreditCard>> getCardUser() {
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			logger.info("Nome: "+auth.getName());
			
			User user = userService.findByUsername(auth.getName());	
			logger.info(user.toString());

			List<CreditCard> listaCard = creditCardService.findByUser_id(user.getId());
			return null;
		}
		catch (Exception e) {
			logger.info("Errore: "+e);
			return new ResponseEntity<List<CreditCard>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}