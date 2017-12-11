package it.dstech.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.dstech.models.CreditCard;
import it.dstech.repository.CreditCardRepository;

public class CreditCardServiceImpl implements CreditCardService{
	@Autowired
	CreditCardRepository repo;
	
	@Override
	public CreditCard saveCreditCard(CreditCard card) {
		return repo.save(card);
	}

	@Override
	public void deleteCreditCard(int id) {
		repo.delete(id);
	}

	@Override
	public List<CreditCard> trovaCarteIdUtente(int id) {
		return repo.findByUser_id(id);
	}

	@Override
	public CreditCard trovaIdCarta(int id) {
		return repo.findOne(id);
	}
}
