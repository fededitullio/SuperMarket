package it.dstech.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.dstech.models.CreditCard;
import it.dstech.models.User;
import it.dstech.services.CreditCardService;
import it.dstech.services.CreditCardServiceImpl;
import it.dstech.services.UserServices;

@RestController
@RequestMapping("/creditCard")
public class CreditCardController {
	private static final Logger logger = Logger.getLogger(CreditCardServiceImpl.class.getName());

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private UserServices userService;

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

			List<CreditCard> listaCard = creditCardService.trovaCarteIdUtente(user.getId());
			return new ResponseEntity<List<CreditCard>>(listaCard, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.info("Errore: "+e);
			return new ResponseEntity<List<CreditCard>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/addCartaCredito")
	public ResponseEntity<CreditCard> saveOrUpdateCarta(@RequestBody CreditCard creditCard) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByUsername(auth.getName());	
			//creditCard.encryptNumero();
			
			user.getListCard().add(creditCard);
			userService.saveUser(user);
			creditCard.setUser(user);
			CreditCard saved = creditCardService.saveCreditCard(creditCard);
			
			return new ResponseEntity<CreditCard>(saved, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Errore: "+e);
			return new ResponseEntity<CreditCard>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/deletecard/{cardId}")
	public ResponseEntity<User> eliminaCard(@PathVariable("cardId") int idCard) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByUsername(auth.getName());	
			CreditCard creditCard = creditCardService.trovaIdCarta(idCard);
			
			List<CreditCard> listaCard = creditCardService.trovaCarteIdUtente(user.getId());
			listaCard.remove(creditCard);
			user.setListCard(listaCard);
			userService.saveUser(user);
			
			return new ResponseEntity<User>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}