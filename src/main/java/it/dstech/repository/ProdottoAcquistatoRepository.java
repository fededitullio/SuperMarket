package it.dstech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.dstech.models.ProdottoAcquistato;
import it.dstech.models.Transazione;


public interface ProdottoAcquistatoRepository extends CrudRepository<ProdottoAcquistato, Integer>  {
	List<ProdottoAcquistato> findByTransazione(Transazione transazione);
}
