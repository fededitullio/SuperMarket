package it.dstech.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.models.ProdottoAcquistato;
import it.dstech.models.Transazione;
import it.dstech.repository.ProdottoAcquistatoRepository;

@Service
public class ProdottoAcquistatoServicesImpl implements ProdottoAcquistatoServices {
	@Autowired
	ProdottoAcquistatoRepository repo;

	@Override
	public ProdottoAcquistato saveOrUpdateProduct(ProdottoAcquistato product) {
		// TODO Auto-generated method stub
		return (ProdottoAcquistato) repo.save(product);
	}

	@Override
	public ProdottoAcquistato getProductById(int id) {
		// TODO Auto-generated method stub
		return (ProdottoAcquistato) repo.findOne(id);
	}

	@Override
	public List<ProdottoAcquistato> getAllProduct() {
		// TODO Auto-generated method stub
		return (List<ProdottoAcquistato>) repo.findAll();
	}

	@Override
	public void deleteProduct(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ProdottoAcquistato> getListAcquistiByTransazazioneId(Transazione transazione) {
		// TODO Auto-generated method stub
		return repo.findByTransazioneIdTransazioneIn(transazione.getIdTransazione());
	}

}
