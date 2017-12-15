package it.dstech.services;

import java.util.List;

import it.dstech.models.ProdottoAcquistato;
import it.dstech.models.Transazione;

public interface ProdottoAcquistatoServices {
	ProdottoAcquistato saveOrUpdateProduct(ProdottoAcquistato product);
	
    ProdottoAcquistato getProductById(int id);
	
	List<ProdottoAcquistato> getAllProduct();
	
	void deleteProduct(int id);


	List<ProdottoAcquistato> getListAcquistiByTransazazioneId(Transazione transazione);
}
