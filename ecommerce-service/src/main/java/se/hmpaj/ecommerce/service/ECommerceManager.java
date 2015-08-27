package se.hmpaj.ecommerce.service;

import java.util.List;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.model.Product;
import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.model.util.Constants;
import se.hmpaj.ecommerce.service.exception.ECommerceManagerException;
import se.hmpaj.ecommerce.service.exception.RepositoryException;
import se.hmpaj.ecommerce.service.repository.OrderRepository;
import se.hmpaj.ecommerce.service.repository.ProductRepository;
import se.hmpaj.ecommerce.service.repository.UserRepository;

public final class ECommerceManager implements OrderRepository, ProductRepository, UserRepository
{
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	
	public ECommerceManager(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository)
	{
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	public Order addOrder(Order order)
	{
		if(order.getId() == Constants.EMPTY_ID)
		{
			try
			{
				return orderRepository.addOrder(order);
			}
			catch(RepositoryException e)
			{
				throw new ECommerceManagerException("Could not add user", e);
			}	
		}
		
		throw new ECommerceManagerException("Order already added, Use updateOrder instead");
	}

	public Order getOrder(long id)
	{
		try
		{
			return orderRepository.getOrder(id);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not get order with id: " + id, e);
		}
	}

	public List<Order> getAllOrdersForUser(long userId)
	{
		try
		{
			return orderRepository.getAllOrdersForUser(userId);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not get orders for user with id: " + userId, e);
		}
	}

	public Order updateOrder(Order order)
	{
		try
		{
			return orderRepository.updateOrder(order);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not update order with id: " + order.getId(), e);
		}
	}

	public Order deleteOrder(long id)
	{
		try
		{
			return orderRepository.deleteOrder(id);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not delete order with id: " + id, e);
		}
	}

	public Product addProduct(Product product)
	{
		if(product.getId() == Constants.EMPTY_ID)
		{
			try
			{
				return productRepository.addProduct(product);
			}
			catch(RepositoryException e)
			{
				throw new ECommerceManagerException("Could not add product", e);
			}
		}
		
		throw new ECommerceManagerException("Product already added, Use updateOrder instead"); 
	}

	public Product getProduct(long id)
	{
		try
		{
			return productRepository.getProduct(id);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not get product with id: " + id, e);
		}
	}

	public List<Product> getAllProducts()
	{
		try
		{
			return productRepository.getAllProducts();
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not get all products", e);
		}
	}

	public Product updateProduct(Product product)
	{
		try
		{
			return productRepository.updateProduct(product);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not update product with id: " + product.getId(), e);
		}
	}

	public Product deleteProduct(long id)
	{
		try
		{
			return productRepository.deleteProduct(id);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not delete product with id: " + id, e);
		}
	}

	public User addUser(User user)
	{
		if(user.getId() == Constants.EMPTY_ID)
		{
			try
			{
				return userRepository.addUser(user);
			}
			catch(RepositoryException e)
			{
				throw new ECommerceManagerException("Could not add user", e);
			}
		}
		throw new ECommerceManagerException("User already added, use updateUser instead");
	}

	public User getUserById(long id)
	{
		try
		{
			return userRepository.getUserById(id);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not get user with id: " + id, e);
		}
	}

	public User getUserByEmail(String email)
	{
		try
		{
			return userRepository.getUserByEmail(email);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not get user with email: " + email, e);
		}
	}

	public User deleteUser(long id)
	{
		try
		{
			return userRepository.deleteUser(id);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not inactivate user with id: " + id, e);
		}
	}

	public User updateUser(User user)
	{
		try
		{
			return userRepository.updateUser(user);
		}
		catch(RepositoryException e)
		{
			throw new ECommerceManagerException("Could not update user with id: " + user.getId(), e);
		}
	}
}
