package it.dstech.services;

import java.util.List;

import it.dstech.models.Product;

public interface ProductService {

    Product saveOrUpdateProduct(Product product);
	
	Product getProductById(int id);
	
	List<Product> getAllProduct();
	
	void deleteProduct(int id);
	
	List<Product> getListProductByUserId(int id);

	List<Product> getListProductByCategoria(String categoria);
	
	List<Product> getByQuantitaDisponibileGreaterThan(Double quantita);
}
