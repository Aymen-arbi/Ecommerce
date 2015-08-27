package se.hmpaj.ecommerce.service.repository;

import java.util.List;

import se.hmpaj.ecommerce.model.Product;

public interface ProductRepository
{
	Product addProduct(Product product);

	Product getProduct(long id);
	
	List<Product> getAllProducts();

	Product updateProduct(Product product);

	Product deleteProduct(long id);
}
