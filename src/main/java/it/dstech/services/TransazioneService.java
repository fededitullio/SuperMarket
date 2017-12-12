package it.dstech.services;

import java.util.List;
import it.dstech.models.Transazione;

public interface TransazioneService {

	Transazione saveTransazione(Transazione transazione);
	void deleteTransazione(int id);
	List<Transazione> getListTransazioneByUserId(int id);
}
