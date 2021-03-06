package it.dstech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.dstech.models.ProdottoAcquistato;
import it.dstech.models.Transazione;

public interface TransazioneRepository extends CrudRepository<Transazione, Integer>{

	List<Transazione> findByIdUser(int id);
	
	List<ProdottoAcquistato> findProductByIdTransazione(int id);
	
	
}
