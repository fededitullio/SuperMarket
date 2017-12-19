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
import it.dstech.models.ProdottoAcquistato;
import it.dstech.models.Product;
import it.dstech.models.StoricoOfferte;
import it.dstech.models.Transazione;
import it.dstech.models.Unita;
import it.dstech.models.User;
import it.dstech.services.CreditCardService;
import it.dstech.services.ProdottoAcquistatoServices;
import it.dstech.services.ProductService;
import it.dstech.services.StoricoOfferteServices;
import it.dstech.services.TransazioneService;
import it.dstech.services.UserServices;
import it.dstech.services.auth.CustomUserDetailsService;

@RestController
@RequestMapping("/product")
public class ProductController {

	private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

	@Autowired
	private ProductService productService;
	@Autowired
	private UserServices userServices;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private TransazioneService transazioneService;
	@Autowired
	private StoricoOfferteServices storicoServices;
	@Autowired
	private ProdottoAcquistatoServices prodottoAcquistatoService;

	@GetMapping("/getModel")
	public Product getModel() {
		return new Product();
	}

	@PostMapping("/save")
	public ResponseEntity<Product> save(@RequestBody Product product) {
		try {
			Product save = (Product) productService.saveOrUpdateProduct(product);
			return new ResponseEntity<Product>(save, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Salvataggio fallito");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getProduct/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable int id) {
		try {
			Product prodotto = (Product) productService.getProductById(id);
			return new ResponseEntity<Product>(prodotto, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Stampa prodotto fallita");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getListProduct")
	public ResponseEntity<List<Product>> getAllProduct() {
		try {

			List<Product> listaProdotti = (List<Product>) productService.getAllProduct();
			return new ResponseEntity<List<Product>>(listaProdotti, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Stampa lista prodotti fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Prende la lista dei prodotti disponibili e vengono eliminati i prodotti
	// scaduti
	@GetMapping("/getListProductDisponibile")
	public ResponseEntity<List<Product>> getAllProductDisponibili() {
		try {
			LocalDate dataOggi = LocalDate.now();

			List<Product> listaProdotti = (List<Product>) productService.getByQuantitaDisponibileGreaterThan(0.0);
			List<Product> listaFinale = new ArrayList();

			for (Product prodotto : listaProdotti) {
				String[] data = prodotto.getDataScadenza().split("/");

				LocalDate dataScadenza = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]),
						Integer.parseInt(data[0]));

				if (dataOggi.isBefore(dataScadenza)) {

					listaFinale.add(prodotto);
				}
			}
			return new ResponseEntity<List<Product>>(listaFinale, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Stampa lista prodotti fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable int id) {
		try {
			productService.deleteProduct(id);
			return new ResponseEntity<Product>(HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Eliminazione prodotto fallita");
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getListByUserId/{id}")
	public ResponseEntity<List<Product>> getListProductByUserId(@PathVariable("id") int id) {
		try {
			List<Product> listaProdotti = (List<Product>) productService.getListProductByUserId(id);
			return new ResponseEntity<List<Product>>(listaProdotti, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Stampa lista prodotti da user id fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

	@GetMapping("/getListByCategoria/{categoria}")
	public ResponseEntity<List<Product>> getListProductByCategoria(@PathVariable("categoria") Category categoria) {
		try {
			List<Product> listaCategoria = (List<Product>) productService.getListProductByCategoria(categoria);
			return new ResponseEntity<List<Product>>(listaCategoria, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("Stampa lista categoria fallita");
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addProductById/{idCarta}")
	public ResponseEntity<Product> addProdotto(@RequestBody List<Product> carrello,
			@PathVariable("idCarta") int idCarta) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userServices.findByUsername(auth.getName());
			CreditCard carta = creditCardService.trovaIdCarta(idCarta);

			boolean trovato = false;
			if (carta != null) {
				for (CreditCard credit : creditCardService.trovaCarteIdUtente(user.getId())) {
					if (credit.getId() == carta.getId())
						trovato = true;
				}
			}

			// Trasformazione in localDate della scadenza della carta di credito (MM/AA)
			LocalDate dataOggi = LocalDate.now();
			boolean codiceEstratto = false;
			int codice = 0;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
			String date = carta.getScadenza();
			YearMonth scadenzaMese = YearMonth.parse(date, formatter);
			LocalDate scadenza = scadenzaMese.atEndOfMonth();
			Transazione transazione = new Transazione();
			transazioneService.saveTransazione(transazione);
			for (Product prodotto : carrello) {
				// controllo scadenza
				if (trovato  && dataOggi.isBefore(scadenza)
						&& carta.getCredito() >= productService.getProductById(prodotto.getId()).getPrezzoIvato()) {
					if (!codiceEstratto) {
						Random random = new Random();
						codice = random.nextInt(10000);
						codiceEstratto = true;
						transazione.setCodOrdine(codice);
						transazione.setIdUser(user.getId());
					}
					logger.info(prodotto);
					ProdottoAcquistato prodottoAcquistato = new ProdottoAcquistato(prodotto.getNome(),
							prodotto.getMarca(), prodotto.getDataScadenza(), prodotto.getCategoria(),
							prodotto.getQuantitaDisponibile(), prodotto.getQuantitaDaAcquistare(), prodotto.getUnita(),
							prodotto.getPrezzoUnitario(), prodotto.getPrezzoSenzaIva(), prodotto.getPrezzoIvato(),
							prodotto.getImg(), prodotto.getOfferta(), prodotto.getPrezzoScontato());
					logger.info(prodottoAcquistato);

					transazione.getProduct().add(prodottoAcquistato);
					logger.info(transazione.getProduct());
					prodottoAcquistato.setTransazione(transazione);
					prodottoAcquistatoService.saveOrUpdateProduct(prodottoAcquistato);
					double creditoAggiornato = carta.getCredito()
							- productService.getProductById(prodotto.getId()).getPrezzoScontato()
									* prodotto.getQuantitaDaAcquistare();
					carta.setCredito(creditoAggiornato);
					userServices.saveUser(user);
					creditCardService.saveCreditCard(carta);
					double quantità = productService.getProductById(prodotto.getId()).getQuantitaDisponibile() - 1;
					productService.getProductById(prodotto.getId()).setQuantitaDisponibile(quantità);
					productService.saveOrUpdateProduct(productService.getProductById(prodotto.getId()));
				} else {
					logger.info("aggiunta prodotto fallita");
					
					return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			transazioneService.saveTransazione(transazione);
			return new ResponseEntity<Product>(HttpStatus.OK);
		} catch (Exception e) {
			logger.info(e);
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping("/generaOfferte")
	public ResponseEntity<String> generaOfferte() {
		List<StoricoOfferte> lista = storicoServices.getListStoricoOfferte();
		LocalDate dataOggi = LocalDate.now();
		LocalDate dataOfferta = lista.get(lista.size() - 1).getDataOfferta();
		logger.info(dataOggi + " data di oggi ");
		logger.info(dataOfferta + " data dell'offerta ");
		String risposta = "";
		String[] data = {};
		int codice = 0;
		try {
			if (dataOfferta.isBefore(dataOggi)) {
				StoricoOfferte storico = new StoricoOfferte();
				storico.setDataOfferta(dataOggi);
				storicoServices.saveStoricoOfferte(storico);
				logger.info("son prima della query eeeeeeeh");
				List<Product> listaProdotti = (List<Product>) productService.getAllProduct();
				for (Product prodotto : listaProdotti) {
					productService.getProductById(prodotto.getId()).setOfferta(0);
					productService.getProductById(prodotto.getId()).sconto();
					productService.saveOrUpdateProduct(productService.getProductById(prodotto.getId()));
				}
				listaProdotti = (List<Product>) productService.getByQuantitaDisponibileGreaterThan(0.0);
				List<Product> listaFinale = new ArrayList();

				for (Product prodotto : listaProdotti) {

					data = prodotto.getDataScadenza().split("/");

					LocalDate dataScadenza = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]),
							Integer.parseInt(data[0]));

					if (dataOggi.isBefore(dataScadenza)) {
						logger.info(prodotto);
						listaFinale.add(prodotto);
					}
				}

				for (Product prodotto : listaFinale) {
					data = prodotto.getDataScadenza().split("/");

					if (Integer.parseInt(data[2]) == dataOggi.getYear()) {

						if (Integer.parseInt(data[1]) == dataOggi.getMonthValue()) {

							if ((Integer.parseInt(data[0]) - dataOggi.getDayOfMonth()) >= 0) {

								if ((Integer.parseInt(data[0]) - dataOggi.getDayOfMonth()) <= 3) {

									productService.getProductById(prodotto.getId()).setOfferta(40);

									productService.getProductById(prodotto.getId()).sconto();

									productService.saveOrUpdateProduct(productService.getProductById(prodotto.getId()));

								}
							}
						}
					}

				}
				int i = 0;

				while (i < 5) {
					Random random = new Random();
					codice = random.nextInt(listaFinale.size() - 1);

					if (listaFinale.get(codice).getOfferta() == 0) {

						productService.getProductById(listaFinale.get(codice).getId()).setOfferta(10);
						productService.getProductById(listaFinale.get(codice).getId()).sconto();

						productService
								.saveOrUpdateProduct(productService.getProductById(listaFinale.get(codice).getId()));
						i++;
					}
				}
				risposta = "offerte generate";
			}
			return new ResponseEntity<String>(risposta, HttpStatus.OK);

		} catch (Exception e) {
			risposta = "Oferta non generata";
			return new ResponseEntity<String>(risposta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	@PostMapping("/popolaDb")
	public ResponseEntity<Void> popolaDb() {
		try {
			LocalDate dataOggi = LocalDate.now();
			StoricoOfferte storico = new StoricoOfferte();
			storico.setDataOfferta(dataOggi);
			storicoServices.saveStoricoOfferte(storico);
			Product prodotto1 = new Product("Latte", "Centrale del Latte", "17/4/2018", Category.ALIMENTI, 90, 1,
					Unita.LITRO, 0.4,
					"https://www.granarolo.it/system/granarolo_consumer/attachments/data/000/002/255/original/Latte_parzialmente_scremato_Accadi_Senza_Lattosio_1L.jpg?1490970934",
					0);
			Product prodotto2 = new Product("Carne di drago", "Radiant Farms", "9/9/9999", Category.ALIMENTI, 5, 1,
					Unita.CHILO, 900, "https://images-na.ssl-images-amazon.com/images/I/51qGYooFtiL.jpg", 0);
			Product prodotto3 = new Product("Caffè Lavazza qualità Oro", "Lavazza", "6/1/2018", Category.ALIMENTI, 124,
					1, Unita.CHILO, 1,
					"https://hairshop.lv/content/images/thumbs/0018308_lavazza-qualita-oro-kafijas-pupinas-1-kg.jpeg",
					0);
			Product prodotto4 = new Product("Pane di Segale", "Gilli", "21/8/2018", Category.ALIMENTI, 0, 1,
					Unita.CHILO, 0.2,
					"http://www.laviticella.it/shop/1465-tm_large_default/rye-bread-landbrot-gilli-500-gr.jpg", 0);
			Product prodotto5 = new Product("Fusilli 5 Cereali", "Barilla", "17/4/2016", Category.ALIMENTI, 90, 1,
					Unita.PEZZO, 1,
					"http://www.giallozafferano.it/images/barilla/ingredienti/i-Fusilli-5-cereali_package_medium.jpg",
					0);
			Product prodotto6 = new Product("Omino Bianco Essenza Muschio Bianco", "Omino Bianco", "10/9/2080",
					Category.PRODOTTI_CASA, 30, 1, Unita.PEZZO, 4,
					"https://images-na.ssl-images-amazon.com/images/I/818D%2BZFqWdL._SL1500_.jpg", 0);
			Product prodotto7 = new Product("Kérastase Paris", "Paris", "21/8/2018", Category.PRODOTTI_PERSONA, 30, 1,
					Unita.PEZZO, 3, "https://s4.thcdn.com/productimg/960/960/11309649-5684484572839147.jpg", 0);
			Product prodotto8 = new Product("L'Oreal Paris Colour Protect", "Paris", "5/2/2020",
					Category.PRODOTTI_PERSONA, 61, 1, Unita.PEZZO, 4,
					"https://www.ocado.com/productImages/269/26965011_0_640x640.jpg?identifier=440a631c3a07cb7426937a3dbcb393a0",
					0);
			Product prodotto9 = new Product("Biscotti per Cani", "Coop", "10/9/2019", Category.ANIMALI, 40, 1,
					Unita.PEZZO, 4.30, "http://www.coopfirenze.it/uploads/22372/original/14_animali_coop.jpg", 20);
			Product prodotto10 = new Product("Purina One Adult", "Purina", "9/7/2021", Category.ANIMALI, 20, 1,
					Unita.PEZZO, 6, "https://images-na.ssl-images-amazon.com/images/I/917%2BYnoduaL._SL1500_.jpg", 0);
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
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
