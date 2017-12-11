package it.dstech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.dstech.models.CreditCard;

public interface CreditCardRepository extends CrudRepository<CreditCard, Integer> {

	List<CreditCard> findByUser_id(int id);
}
