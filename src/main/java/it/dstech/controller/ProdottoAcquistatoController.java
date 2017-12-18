package it.dstech.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.dstech.models.ProdottoAcquistato;
import it.dstech.models.Transazione;
import it.dstech.models.User;
import it.dstech.repository.TransazioneRepository;
import it.dstech.services.ProdottoAcquistatoServices;
import it.dstech.services.TransazioneService;
import it.dstech.services.UserServices;
import it.dstech.services.auth.CustomUserDetailsService;

@RestController
@RequestMapping("/acquistato")
public class ProdottoAcquistatoController {
	@Autowired
	private TransazioneService transazioneService;
	@Autowired
	private ProdottoAcquistatoServices prodottoAcquistatoService;
	@Autowired
	private UserServices userServices;
	@Autowired
	TransazioneRepository repository;

	private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());
	
	
	@GetMapping("/getListaProdottiByUserId")
	public ResponseEntity<List<Transazione>> getListProductAcquistatiByUserId(){
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userServices.findByUsername(auth.getName());
			List<Transazione>listaTransazione= transazioneService.getListTransazioneByUserId(user.getId());
			return new ResponseEntity<List<Transazione>>(listaTransazione, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<List<Transazione>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getListProdottiByTransazioneId/{id}")
	public ResponseEntity<List<ProdottoAcquistato>> getListProductAcquistatiByTransazioneId(@PathVariable("id") int id) {
		try {
			List<ProdottoAcquistato> listaTransazione = (List<ProdottoAcquistato>) prodottoAcquistatoService
					.getListAcquistiByTransazazioneId(transazioneService.findById(id));
//			List<ProdottoAcquistato> list = repository.findProductByIdTransazione(id);
			listaTransazione.forEach(System.out::println);
			// logger.info(listaTransazione);
			return new ResponseEntity<List<ProdottoAcquistato>>(listaTransazione, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Stampa lista prodotti da user id fallita" + e);
			logger.error(e.getMessage());
			return new ResponseEntity<List<ProdottoAcquistato>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
