package se.hmpaj.ecommerce.model;

import java.util.List;

import se.hmpaj.ecommerce.model.util.Constants;

public final class Order
{
	private final long id;
	private final long userId;
	private final List<Product> products;

	public Order(final long userId, final List<Product> products)
	{
		this(Constants.EMPTY_ID, userId, products);
	}

	public Order(final long id, final long userId)
	{
		this(id, userId, null);
	}
	
	public Order(final List<Product> products)
	{
		this(Constants.EMPTY_ID, Constants.EMPTY_ID, products);
	}

	public Order(final Order order, final List<Product> products)
	{
		this(order.getId(), order.getUserId(), products);
	}

	public Order(final long id, final long userId, final List<Product> products)
	{
		this.id = id;
		this.userId = userId;
		this.products = products;
	}

	public long getId()
	{
		return id;
	}

	public long getUserId()
	{
		return userId;
	}

	public List<Product> getProducts()
	{
		return products;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Order)
		{
			Order otherOrder = (Order) other;
			return id == otherOrder.getId() && userId == otherOrder.getUserId();
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result *= 31 + id;
		result *= 31 + userId;

		return result;
	}

}