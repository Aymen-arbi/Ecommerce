package se.hmpaj.ecommerce.service.exception;

public class SQLRepositoryException extends RuntimeException
{
	private static final long serialVersionUID = -3068201152356497066L;
	
	public SQLRepositoryException(String message)
	{
		super(message);
	}
	
	public SQLRepositoryException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
}
