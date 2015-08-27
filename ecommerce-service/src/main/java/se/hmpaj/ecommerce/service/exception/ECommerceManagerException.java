package se.hmpaj.ecommerce.service.exception;

public class ECommerceManagerException extends RuntimeException
{
	private static final long serialVersionUID = 4801647711066290446L;
	
	public ECommerceManagerException(String message)
	{
		super(message);
	}
	
	public ECommerceManagerException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
}
