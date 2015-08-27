package se.hmpaj.ecommerce.service.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;

import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.service.ECommerceManager;
import se.hmpaj.ecommerce.service.repository.sql.SQLOrderRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLProductRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLUserRepository;

@Path("login")
public class UserLogin
{
	private static final ECommerceManager manager = new ECommerceManager(new SQLUserRepository(), new SQLProductRepository(), new SQLOrderRepository());
	public static final Map<Long, String> users = new HashMap<>();

	@Context
	private UriInfo uriInfo;

	@POST
	public Response login(@HeaderParam("email") final String email, @HeaderParam("password") final String password)
	{
		User user = new User(email, password);
		User userFromDB = manager.getUserByEmail(email);

		if (user.equals(userFromDB))
		{
			String token = email + ":" + password;
			try
			{
				token = DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			users.put(userFromDB.getId(), token);
			final URI location = uriInfo.getAbsolutePathBuilder().path(token).build();
			return Response.created(location).build();

		}
		throw new WebApplicationException("Could not connect");
	}

	public boolean verifieUserStatus(String token)
	{
		if (users.containsValue(token))
		{
			return true;
		}
		return false;
	}

}
