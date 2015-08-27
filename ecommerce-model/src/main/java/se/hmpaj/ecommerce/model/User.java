package se.hmpaj.ecommerce.model;

import se.hmpaj.ecommerce.model.util.Constants;

public final class User
{
	private final long id;
	private final String email;
	private final String password;
	
	public User(final String email, final String password)
	{
		this(Constants.EMPTY_ID, email, password);
	}
	
	public User(final long id, final String email, final String password)
	{
		this.id = id;
		this.email = email.toLowerCase();
		this.password = password;
	}
	
	public long getId()
	{
		return id;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof User)
		{
			User otherUser = (User) other;
			return email.equals(otherUser.getEmail()) && password.equals(otherUser.getPassword());
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		result *= 31 + id;
		result *= 31 + email.hashCode();
		
		return result;
	}
}