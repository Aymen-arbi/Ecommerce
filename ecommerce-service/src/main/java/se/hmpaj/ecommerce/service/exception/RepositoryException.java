package se.hmpaj.ecommerce.service.exception;

public class RepositoryException extends RuntimeException
{
	private static final long serialVersionUID = -4792911328834895330L;
	
	public RepositoryException(String message)
	{
		super(message);
	}
}
