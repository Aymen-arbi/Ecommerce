package se.hmpaj.ecommerce.service.web.authentification;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.service.ECommerceManager;
import se.hmpaj.ecommerce.service.exception.ECommerceManagerException;
import se.hmpaj.ecommerce.service.repository.sql.SQLOrderRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLProductRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLUserRepository;

public class AuthenticationService
{
	private static final ECommerceManager manager = new ECommerceManager(new SQLUserRepository(), new SQLProductRepository(), new SQLOrderRepository());

	public boolean authenticate(String authCredentials)
	{

		if (null == authCredentials)
		{
			return false;
		}
		final String encodedEmailPassword = authCredentials.replaceFirst("Basic" + " ", "");
		String emailAndPassword = null;
		try
		{
			byte[] decodedBytes = Base64.getDecoder().decode(encodedEmailPassword);
			emailAndPassword = new String(decodedBytes, "UTF-8");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		final StringTokenizer tokenizer = new StringTokenizer(emailAndPassword, ":");
		final String email = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		User userFromWeb = new User(email, password);
		User userFromDB;
		try
		{
			userFromDB = manager.getUserByEmail(email);
		}
		catch (ECommerceManagerException e)
		{
			throw new ECommerceManagerException("Please check your email");
		}

		boolean authenticationStatus = userFromWeb.equals(userFromDB);
		return authenticationStatus;
	}
}
