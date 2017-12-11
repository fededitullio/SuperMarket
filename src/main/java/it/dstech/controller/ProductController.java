package it.dstech.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.dstech.models.CreditCard;
import it.dstech.models.Product;
import it.dstech.models.User;
import it.dstech.services.CreditCardService;
import it.dstech.services.ProductService;

import it.dstech.services.UserServices;
import it.dstech.services.auth.CustomUserDetailsService;

@RestController
@RequestMapping("/product")
public class ProductController {

private static final Logger logger=Logger.getLogger(CustomUserDetailsService.class.getName());
	
	@Autowired
	private ProductService productService;
	@Autowired
	private UserServices userServices;
	@Autowired
	private CreditCardService creditCardService;

	@GetMapping("/getModel")
	public Product getModel() {
		return new Product();
	}
	
	
	
	@PostMapping("/save")
	public ResponseEntity<Product> save(@RequestBody Product product) { 
		try {
	Product save= (Product) productService.saveOrUpdateProduct(product);
	return new ResponseEntity<Product>(save,HttpStatus.CREATED);
		} catch(Exception e) {
			logger.info("Salvataggio fallito");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getProduct/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable int id) { 
		try {
	Product prodotto=(Product) productService.getProductById(id);
	return new ResponseEntity<Product>(prodotto,HttpStatus.OK);
		} catch(Exception e) {
			logger.info("Stampa prodotto fallita");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	
	
	@GetMapping("/getListProduct")
	public ResponseEntity<List<Product>> getAllProduct() { 
		try {
	List<Product> listaProdotti=(List<Product>) productService.getAllProduct();
	return new ResponseEntity<List<Product>>(listaProdotti,HttpStatus.OK);
		} catch(Exception e) {
			logger.info("Stampa lista prodotti fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
		//Prende la lista dei prodotti disponibili e vengono eliminati i prodotti scaduti
		@GetMapping("/getListProductDisponibile")
		public ResponseEntity<List<Product>> getAllProductDisponibili() { 
			try {
		LocalDate dataOggi=LocalDate.now();
		List<Product> listaProdotti=(List<Product>) productService.getByQuantitaDisponibileGreaterThan(0.0);
		for(Product prodotto: listaProdotti) {
			if(dataOggi.isAfter(prodotto.getDataScadenza())) {
				listaProdotti.remove(prodotto);
			}
		}
		return new ResponseEntity<List<Product>>(listaProdotti,HttpStatus.OK);
			} catch(Exception e) {
				logger.info("Stampa lista prodotti fallita");
				return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable int id) { 
		try {
	productService.deleteProduct(id);
	return new ResponseEntity<Product>(HttpStatus.OK);
		} catch(Exception e) {
			logger.info("Eliminazione prodotto fallita");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	
	
	@GetMapping("/getListByUserId/{id}")
	public ResponseEntity<List<Product>> getListProductByUserId(@PathVariable("id")int id) { 
		try {
	List<Product> listaProdotti=(List<Product>) productService.getListProductByUserId(id);
	return new ResponseEntity<List<Product>>(listaProdotti,HttpStatus.OK);
		} catch(Exception e) {
			logger.info("Stampa lista prodotti da user id fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	
	@GetMapping("/getListByCategoria/{categoria}")
	public ResponseEntity<List<Product>> getListProductByCategoria(@PathVariable("categoria")String categoria) { 
		try {
	List<Product> listaCategoria=(List<Product>) productService.getListProductByCategoria(categoria);
	return new ResponseEntity<List<Product>>(listaCategoria,HttpStatus.OK);
		} catch(Exception e) {
			logger.info("Stampa lista categoria fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	@PostMapping("/addProductById/{idCarta}/{codSegreto}")
	public ResponseEntity<Product> addProdotto(@RequestBody List<Product> carrello,@PathVariable("idCarta") int idCarta, @PathVariable("codSegreto") String codSegreto) { 
		try {
	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
	User user=userServices.findByUsername(auth.getName());
	CreditCard carta=(CreditCard) creditCardService.trovaCarteIdUtente(user.getId());
		
	boolean trovato=false;
	if (carta!=null) {
		for(CreditCard credit: creditCardService.trovaCarteIdUtente(user.getId())) {
			if(credit.getId()==carta.getId())
				trovato=true;	
		}
	}
	
	//controllo scadenza carta di credito (AAAA-MM)
		LocalDate dataOggi=LocalDate.now();
		boolean codiceEstratto= false;
		int codice=0;
		for(Product prodotto:carrello) {                                                                                                                      //controllo scadenza                 
		if( trovato&& codSegreto.equals(carta.getCcv())&& dataOggi.isBefore(carta.getScadenza())&& carta.getCredito()>=productService.getProductById(prodotto.getId()).getPrezzoIvato()) {
			if(!codiceEstratto) {
			Random random=new Random();
			codice=random.nextInt(10000);
			codiceEstratto=true;
			}
			productService.getProductById(prodotto.getId()).setCodice(codice);
			user.getListProduct().add(productService.getProductById(prodotto.getId()));
			double creditoAggiornato=carta.getCredito()-productService.getProductById(prodotto.getId()).getPrezzoIvato();
			carta.setCredito(creditoAggiornato);
			userServices.saveUser(user);
			creditCardService.saveCreditCard(carta);
			productService.getProductById(prodotto.getId()).getUser().add(user);
			double quantità= productService.getProductById(prodotto.getId()).getQuantitaDisponibile();
			productService.getProductById(prodotto.getId()).setQuantitaDisponibile(quantità);
			productService.saveOrUpdateProduct(productService.getProductById(prodotto.getId()));
			return new ResponseEntity<Product>(HttpStatus.OK);
		} else {
			logger.info("aggiunta prodotto fallita");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		}
		}catch(Exception e) {
		return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
		return null;
	}
	
//	@PostMapping("/deleteProductFromListById/{id}")	
//	public ResponseEntity<Product> deleteProductFromListById(@PathVariable("id") int id) {
//		try {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		User user = userService.getByUsername(auth.getName());
//		List<Product> lista = productService.getListProductByUserId(user.getId());
//		boolean trovato = false;
//		Product prodotto = productService.getProductById(id);
//		for (Product prod : lista) {
//			if (prod.getId() == prodotto.getId())
//				trovato = true;
//		}
//
//		if (trovato) {
//			user.getListaProdotti().remove(prodotto);
//			int quantità = prodotto.getQuantità();
//			quantità++;
//			productService.getProductById(id).setQuantità(quantità);
//			userService.saveOrUpdateUser(user);
//			prodotto.getUser().remove(user);
//			productService.saveOrUpdateProduct(prodotto);
//			return new  ResponseEntity<Product>(HttpStatus.OK);
//		}
//		else {
//			logger.info("Eliminazione del prodotto dalla lista falita");
//			return new  ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//	}catch(Exception e) {
//		logger.info("Eliminazione del prodotto dalla lista falita");
//		return new  ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//	}
}
