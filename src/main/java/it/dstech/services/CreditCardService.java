package it.dstech.services;

import java.util.List;

import it.dstech.models.CreditCard;

public interface CreditCardService {
	
	CreditCard saveCreditCard(CreditCard card);

	void deleteCreditCard(int id);

	// TROVA TUTTE LE CARTE ASSOCIATE AD UN'UTENTE (findByUser_Id)
	List<CreditCard> trovaCarteIdUtente(int id);

	// TROVA LA CARTA TRAMITE ID (findById)
	CreditCard trovaIdCarta(int id);

}
