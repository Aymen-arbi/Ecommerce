package se.hmpaj.ecommerce.service.repository.sql;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.model.Product;
import se.hmpaj.ecommerce.service.exception.RepositoryException;
import se.hmpaj.ecommerce.service.exception.SQLRepositoryException;
import se.hmpaj.ecommerce.service.repository.OrderRepository;
import se.hmpaj.ecommerce.service.sql.SqlQuery;

public class SQLOrderRepository implements OrderRepository
{
	private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders INNER JOIN users ON orders.user_id = users.id WHERE user_id = ?";
	private static final String DELETE_PRODUCTS_ORDER = "DELETE FROM orders_has_products WHERE orders_id = ?";
	private static final String SELECT_ORDER = "SELECT * FROM orders INNER JOIN users ON orders.user_id = users.id WHERE orders.id = ?";
	private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
	private static final String INSERT_ORDER = "INSERT INTO orders (user_id) VALUES (?)";
	private static final String INSERT_PRODUCT_ORDER = "INSERT INTO orders_has_products VALUES (?, ?)";
	private static final String SELECT_PRODUCT_ORDER = "SELECT id, title, price FROM products INNER JOIN orders_has_products ON products.id = orders_has_products.products_id WHERE orders_has_products.orders_id = ?";

	@Override
	public Order addOrder(Order order)
	{
		final List<Object> genKeys = executeUpdate(INSERT_ORDER, order.getUserId());
		final Order newOrder = new Order((long) genKeys.get(0), order.getUserId());
		
		addAllProductsForOrder(order.getProducts(), newOrder.getId());
		
		return new Order(newOrder, order.getProducts());
	}

	@Override
	public Order getOrder(long id)
	{
		final List<Order> orders = executeSelect(SELECT_ORDER, Order.class, id);
		final List<Product> products = getAllProductsForOrder(id);
		
		if(orders.size() > 0)
		{
			return new Order(orders.get(0), products);
		}
		throw new RepositoryException("Order not found");
	}

	@Override
	public Order updateOrder(Order order)
	{
		removeAllProductsForOrder(order.getId());
		addAllProductsForOrder(order.getProducts(), order.getId());

		return new Order(order, order.getProducts());
	}

	@Override
	public Order deleteOrder(long id)
	{
		final Order order = getOrder(id);

		executeUpdate(DELETE_ORDER, id);

		return order;
	}

	@Override
	public List<Order> getAllOrdersForUser(final long userId)
	{
		final List<Order> orders = executeSelect(SELECT_ALL_ORDERS, Order.class, userId);
		
		final List<Order> ordersWithProducts = new ArrayList<>();
		
		for(Order order : orders)
		{
			List<Product> products = getAllProductsForOrder(order.getId());
			ordersWithProducts.add(new Order(order, products));
		}

		return ordersWithProducts;
	}
	
	private void addAllProductsForOrder(List<Product> products, long orderId)
	{	
		for(Product product : products)
		{
			executeUpdate(INSERT_PRODUCT_ORDER, orderId, product.getId());
		}		
	}
	
	private List<Product> getAllProductsForOrder(long id)
	{
		final List<Product> products = executeSelect(SELECT_PRODUCT_ORDER, Product.class, id);
		return products;
	}
	
	private void removeAllProductsForOrder(long orderId)
	{
		executeUpdate(DELETE_PRODUCTS_ORDER, orderId);
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
	
	private <T> List<T> executeSelect(String queryString, Type type, Object... parameters)
	{
		try
		{
			final List<T> result = SqlQuery.getBuilder()
					.query(queryString)
					.parameters(parameters)
					.executeSelect(type);
			
			return result;
		}
		catch(SQLRepositoryException e)
		{
			throw new RepositoryException(e.getMessage());
		}
	}
}