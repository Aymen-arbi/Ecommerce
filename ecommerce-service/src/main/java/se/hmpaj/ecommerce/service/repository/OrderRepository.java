package se.hmpaj.ecommerce.service.repository;

import java.util.List;

import se.hmpaj.ecommerce.model.Order;

public interface OrderRepository
{
	Order addOrder(Order order);
	
	List<Order> getAllOrdersForUser(long userId);

	Order updateOrder(Order order);

	Order deleteOrder(long id);

	Order getOrder(long id);
}
