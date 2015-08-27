package se.hmpaj.main;

import java.util.List;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.service.ECommerceManager;
import se.hmpaj.ecommerce.service.repository.sql.SQLOrderRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLProductRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLUserRepository;

public class Main
{
	public static void main(String[] args)
	{
		ECommerceManager manager = new ECommerceManager(new SQLUserRepository(), new SQLProductRepository(), new SQLOrderRepository());
		User user = manager.getUserById(1);
		List<Order> orders = manager.getAllOrdersForUser(1);
		
		orders.forEach((order) -> {
			System.out.println(order.getId());
			order.getProducts().forEach((product) -> {
				System.out.println(product.getId() + "\n" + product.getTitle() + "\n" + product.getPrice());
			});
		});
		
		System.out.println(user.getEmail());
	}
}
