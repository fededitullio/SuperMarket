package it.dstech.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.models.StoricoOfferte;
import it.dstech.repository.CreditCardRepository;
import it.dstech.repository.StoricoOfferteRepository;
@Service
public class StoricoOfferteServicesImpl implements StoricoOfferteServices {
	@Autowired
	StoricoOfferteRepository repo;
	@Override
	public StoricoOfferte saveStoricoOfferte(StoricoOfferte storico) {
		// TODO Auto-generated method stub
		return (StoricoOfferte)repo.save(storico);
	}

	@Override
	public List<StoricoOfferte> getListStoricoOfferte() {
		// TODO Auto-generated method stub
		return (List<StoricoOfferte>) repo.findAll();
	}

}
