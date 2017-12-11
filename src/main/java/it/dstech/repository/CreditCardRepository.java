package it.dstech.repository;

import java.util.List;

import it.dstech.models.CreditCard;

public interface CreditCardRepository {

	List<CreditCard> findByUser_id(int id);
	CreditCard findById(int id);
}
