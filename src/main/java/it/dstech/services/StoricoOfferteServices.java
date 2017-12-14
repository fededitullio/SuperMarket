package it.dstech.services;

import java.util.List;

import it.dstech.models.StoricoOfferte;

public interface StoricoOfferteServices {
	StoricoOfferte saveStoricoOfferte (StoricoOfferte storico);
	List<StoricoOfferte> getListStoricoOfferte ();
}
