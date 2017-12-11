package it.dstech.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.dstech.models.Product;

public interface ProductRepository  extends CrudRepository<Product, Integer> {

	List<Product> findByUser_Id(int id);
	List<Product> findByCategoria(String categoria);
	List<Product> findByQuantitaDisponibileGreaterThan(Double quantita); //dalla quantità darà tutti i prodotti di numero maggiore
	 
}
