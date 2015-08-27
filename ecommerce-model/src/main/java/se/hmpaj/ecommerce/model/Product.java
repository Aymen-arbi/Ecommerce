package se.hmpaj.ecommerce.model;

import se.hmpaj.ecommerce.model.util.Constants;

public final class Product
{
	private final long id;
	private final String title;
	private final double price;
	
	public Product(final String title, final double price)
	{
		this(Constants.EMPTY_ID, title, price);
	}
	
	public Product(final long id, final String title, final double price)
	{
		this.id = id;
		this.title = title;
		this.price = price;
	}
	
	public long getId()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	@Override
	public String toString()
	{
		return "id: " + id + "\n" + "title: " + title + "\n" + "price: " + price;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		result *= 31 + id;
		result *= 31 + title.hashCode();
		
		return result;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Product)
		{
			Product otherProduct = (Product) other;
			return id == otherProduct.getId() && title.equals(otherProduct.getTitle());
		}
		return false;
	}
}