package it.dstech.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.models.Product;
import it.dstech.repository.ProductRepository;
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository repo;
	
	@Override
	public Product saveOrUpdateProduct(Product product) {
		return (Product) repo.save(product);
	}

	@Override
	public Product getProductById(int id) {
		return (Product) repo.findOne(id);
	}

	@Override
	public List<Product> getAllProduct() {
		return(List<Product>) repo.findAll();
	}

	@Override
	public void deleteProduct(int id) {
		repo.delete(id);
		
	}

	@Override
	public List<Product> getListProductByUserId(int id) {
		return (List<Product>) repo.findByUser_Id(id);
	}

	@Override
	public List<Product> getListProductByCategoria(String categoria) {
		return (List<Product>) repo.findByCategoria(categoria);
	}

	@Override
	public List<Product> getByQuantitaDisponibileGreaterThan(Double quantita) {
		return (List<Product>) repo.findByQuantitaDisponibileGreaterThan(quantita);
	}

}
