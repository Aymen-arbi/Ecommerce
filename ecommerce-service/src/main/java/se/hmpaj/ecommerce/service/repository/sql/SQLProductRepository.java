package se.hmpaj.ecommerce.service.repository.sql;

import java.util.List;

import se.hmpaj.ecommerce.model.Product;
import se.hmpaj.ecommerce.service.exception.RepositoryException;
import se.hmpaj.ecommerce.service.exception.SQLRepositoryException;
import se.hmpaj.ecommerce.service.repository.ProductRepository;
import se.hmpaj.ecommerce.service.sql.SqlQuery;

public final class SQLProductRepository implements ProductRepository
{
	private static final String INSERT_PRODUCT = "INSERT INTO products (title, price) VALUES (?, ?)";
	private static final String SELECT_PRODUCT = "SELECT * FROM products WHERE id = ?";
	private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
	private static final String UPDATE_PRODUCT = "UPDATE products SET title = ?, price = ? WHERE id = ?";
	private static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";

	@Override
	public Product addProduct(Product product)
	{
		final List<Object> genKeys = executeUpdate(INSERT_PRODUCT, product.getTitle(), product.getPrice());
		final long id = (long) genKeys.get(0);
		
		return new Product(id, product.getTitle(), product.getPrice());
	}

	@Override
	public Product getProduct(long id)
	{
		final List<Product> products = executeSelect(SELECT_PRODUCT, id);
		
		if(products.size() > 0)
		{
			return products.get(0);
		}
		
		throw new RepositoryException("Product not found");
	}

	@Override
	public List<Product> getAllProducts()
	{
		final List<Product> products = executeSelect(SELECT_ALL_PRODUCTS);

		return products;
	}

	@Override
	public Product updateProduct(Product product)
	{
		executeUpdate(UPDATE_PRODUCT, product.getTitle(), product.getPrice(), product.getId());

		return product;
	}

	@Override
	public Product deleteProduct(long id)
	{
		final Product product = getProduct(id);
		executeUpdate(DELETE_PRODUCT, id);
		
		return product;
	}

	private List<Object> executeUpdate(String queryString, Object... parameters)
	{
		try
		{
			final List<Object> genKeys = SqlQuery.getBuilder()
					.query(queryString)
					.parameters(parameters)
					.executeUpdate();

			return genKeys;
		}
		catch (SQLRepositoryException e)
		{
			throw new RepositoryException(e.getMessage());
		}
	}

	private List<Product> executeSelect(String queryString, Object... parameters)
	{
		try
		{
			final List<Product> products = SqlQuery.getBuilder()
					.query(queryString)
					.parameters(parameters)
					.executeSelect(Product.class);
			
			return products;
		}
		catch(SQLRepositoryException e)
		{
			throw new RepositoryException(e.getMessage());
		}
	}
}