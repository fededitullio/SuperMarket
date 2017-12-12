package it.dstech.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.dstech.models.Transazione;
import it.dstech.repository.TransazioneRepository;

@Service
public class TransazioneServiceImpl implements TransazioneService{

	@Autowired
	private TransazioneRepository repo;
	
	@Override
	public Transazione saveTransazione(Transazione transazione) {
		return (Transazione) repo.save(transazione);
	}
	@Override
	public List<Transazione> getListTransazioneByUserId(int id) {
		return (List<Transazione>) repo.findByIdUser(id);
	}
}
