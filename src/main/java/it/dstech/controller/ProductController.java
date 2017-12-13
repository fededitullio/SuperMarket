package it.dstech.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import it.dstech.models.Category;
import it.dstech.models.CreditCard;
import it.dstech.models.Product;
import it.dstech.models.Transazione;
import it.dstech.models.Unita;
import it.dstech.models.User;
import it.dstech.services.CreditCardService;
import it.dstech.services.ProductService;
import it.dstech.services.TransazioneService;
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
	@Autowired
	private TransazioneService transazioneService;

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
		logger.info("son prima della query");
		List<Product> listaProdotti=(List<Product>) productService.getByQuantitaDisponibileGreaterThan(0.0);
		List<Product> listaFinale=new ArrayList();
		logger.info(listaProdotti);
		for(Product prodotto: listaProdotti) {
			String[] data =prodotto.getDataScadenza().split("/");
			logger.info(data[0]+" "+data[1]+" "+data[2]);
			LocalDate dataScadenza = LocalDate.of(Integer.parseInt(data[2]),Integer.parseInt(data[1]), Integer.parseInt(data[0]));
			logger.info(dataScadenza+"");
			if(dataOggi.isBefore(dataScadenza)) {
				logger.info(prodotto);
				listaFinale.add(prodotto);
			}
		}
		return new ResponseEntity<List<Product>>(listaFinale,HttpStatus.OK);
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
	
	@GetMapping("/getListTransazioniByUserId")
	public ResponseEntity<List<Transazione>> getListProductByUserId() { 
		try {
			
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			User user=userServices.findByUsername(auth.getName());
	List<Transazione> listaTransazione=(List<Transazione>) transazioneService.getListTransazioneByUserId(user.getId());
	return new ResponseEntity<List<Transazione>>(listaTransazione,HttpStatus.OK);
		} catch(Exception e) {
			logger.info("Stampa lista prodotti da user id fallita");
			return new ResponseEntity<List<Transazione>>(HttpStatus.INTERNAL_SERVER_ERROR);
	
		}
	}
	
	@GetMapping("/getListByCategoria/{categoria}")
	public ResponseEntity<List<Product>> getListProductByCategoria(@PathVariable("categoria")Category categoria) { 
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
	CreditCard carta = creditCardService.trovaIdCarta(idCarta);
		
	boolean trovato=false;
	if (carta!=null) {
		for(CreditCard credit: creditCardService.trovaCarteIdUtente(user.getId())) {
			if(credit.getId()==carta.getId())
				trovato=true;	
		}
	}
	
	//controllo scadenza carta di credito (AAAA-MM-GG)
		LocalDate dataOggi=LocalDate.now();
		boolean codiceEstratto= false;
		int codice=0;
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
		    String date = carta.getScadenza();
		    YearMonth scadenzaMese = YearMonth.parse(date, formatter);
		    LocalDate scadenza = scadenzaMese.atEndOfMonth();
		Transazione transazione=new Transazione();
		for(Product prodotto:carrello) {     
			//controllo scadenza                 
		if( trovato&& codSegreto.equals(carta.getCcv())&& dataOggi.isBefore(scadenza)&& carta.getCredito()>=productService.getProductById(prodotto.getId()).getPrezzoIvato()) {
			if(!codiceEstratto) {
			Random random=new Random();
			codice=random.nextInt(10000);
			codiceEstratto=true;
			transazione.setCodOrdine(codice);
			transazione.setIdUser(user.getId());
			}
		
			transazione.getProduct().add(productService.getProductById(prodotto.getId()));
			transazioneService.saveTransazione(transazione);
			user.getListProduct().add(productService.getProductById(prodotto.getId()));
			double creditoAggiornato=carta.getCredito()-productService.getProductById(prodotto.getId()).getPrezzoIvato()*prodotto.getQuantitaDaAcquistare();
			carta.setCredito(creditoAggiornato);
			userServices.saveUser(user);
			creditCardService.saveCreditCard(carta);
			productService.getProductById(prodotto.getId()).getUser().add(user);
			double quantità= productService.getProductById(prodotto.getId()).getQuantitaDisponibile()-1;
			productService.getProductById(prodotto.getId()).setQuantitaDisponibile(quantità);
			productService.saveOrUpdateProduct(productService.getProductById(prodotto.getId()));
		} else {
			logger.info("aggiunta prodotto fallita");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		}
		return new ResponseEntity<Product>(HttpStatus.OK);
		}catch(Exception e) {
		return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
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
	@PostMapping("/popolaDb")
	public ResponseEntity<Void> popolaDb(){
		try {
			
			Product prodotto1 = new Product("Latte","Centrale del Latte","17/4/2018",Category.ALIMENTI,90,1,Unita.LITRO,0.4,2.00,2.50,"https://www.granarolo.it/system/granarolo_consumer/attachments/data/000/002/255/original/Latte_parzialmente_scremato_Accadi_Senza_Lattosio_1L.jpg?1490970934",0);
			Product prodotto2 = new Product("Carne di drago","Radiant Farms","9/9/9999",Category.ALIMENTI,5,1,Unita.CHILO,900,10000,15000,"https://images-na.ssl-images-amazon.com/images/I/51qGYooFtiL.jpg",0);
			Product prodotto3 = new Product("Caffè Lavazza qualità Oro","Lavazza","6/1/2018",Category.ALIMENTI,124,1,Unita.CHILO,1,4.60,5,"https://hairshop.lv/content/images/thumbs/0018308_lavazza-qualita-oro-kafijas-pupinas-1-kg.jpeg",0);
			Product prodotto4 = new Product("Pane di Segale","Gilli","21/8/2018",Category.ALIMENTI,0,1,Unita.CHILO,0.2,2.5,3,"http://www.laviticella.it/shop/1465-tm_large_default/rye-bread-landbrot-gilli-500-gr.jpg",0);
			Product prodotto5 = new Product("Fusilli 5 Cereali","Barilla","17/4/2016",Category.ALIMENTI,90,1,Unita.PEZZO,1,1.60,3,"http://www.giallozafferano.it/images/barilla/ingredienti/i-Fusilli-5-cereali_package_medium.jpg",0);
			Product prodotto6 = new Product("Omino Bianco Essenza Muschio Bianco","Omino Bianco","10/9/2080",Category.PRODOTTI_CASA,30,1,Unita.PEZZO,4,3.20,4,"https://images-na.ssl-images-amazon.com/images/I/818D%2BZFqWdL._SL1500_.jpg",0);
			Product prodotto7 = new Product("Kérastase Paris","Paris","21/8/2018",Category.PRODOTTI_PERSONA,30,1,Unita.PEZZO,3,2,4.20,"https://s4.thcdn.com/productimg/960/960/11309649-5684484572839147.jpg",0);
			Product prodotto8 = new Product("L'Oreal Paris Colour Protect","Paris","5/2/2020",Category.PRODOTTI_PERSONA,61,1,Unita.PEZZO,4,4.80,5.10,"https://www.ocado.com/productImages/269/26965011_0_640x640.jpg?identifier=440a631c3a07cb7426937a3dbcb393a0",0);
			Product prodotto9 = new Product("Biscotti per Cani","Coop","10/9/2019",Category.ANIMALI,40,1,Unita.PEZZO,4.30,5,5.50,"http://www.coopfirenze.it/uploads/22372/original/14_animali_coop.jpg",20);
			Product prodotto10 = new Product("Purina One Adult","Purina","9/7/2021",Category.ANIMALI,20,1,Unita.PEZZO,6,6.80,7,"https://images-na.ssl-images-amazon.com/images/I/917%2BYnoduaL._SL1500_.jpg",0);
			productService.saveOrUpdateProduct(prodotto1);
			productService.saveOrUpdateProduct(prodotto2);
			productService.saveOrUpdateProduct(prodotto3);
			productService.saveOrUpdateProduct(prodotto4);
			productService.saveOrUpdateProduct(prodotto5);
			productService.saveOrUpdateProduct(prodotto6);
			productService.saveOrUpdateProduct(prodotto7);
			productService.saveOrUpdateProduct(prodotto8);
			productService.saveOrUpdateProduct(prodotto9);
			productService.saveOrUpdateProduct(prodotto10);	
			return new ResponseEntity<Void>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
