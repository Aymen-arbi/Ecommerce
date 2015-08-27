package se.hmpaj.ecommerce.service.web;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.service.ECommerceManager;
import se.hmpaj.ecommerce.service.exception.ECommerceManagerException;
import se.hmpaj.ecommerce.service.repository.sql.SQLOrderRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLProductRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLUserRepository;
import se.hmpaj.ecommerce.service.web.authentification.Authentificated.Authenticated;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public final class UserWebService
{
	private static final ECommerceManager manager = new ECommerceManager(new SQLUserRepository(), new SQLProductRepository(), new SQLOrderRepository());
	UserLogin login = new UserLogin();

	@Context
	private UriInfo uriInfo;


	@GET
	@Path("{userId}")

	public Response getUser(@PathParam("userId") final long userId)
	{
		try
		{
			final User user = manager.getUserById(userId);

			return Response.ok(user).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("No user found on id: " + userId, 404);
		}

	}

	@POST
	public Response addUser(User user)
	{
		try
		{
			final User userFromDB = manager.addUser(user);
			final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userFromDB.getId())).build();

			return Response.created(location).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not add user", 400);
		}
	}

	@PUT
	@Path("{userId}")
	public Response updateUser(@PathParam("userId") final long userId, User user)
	{
		try
		{
			manager.updateUser(user);
			final URI location = uriInfo.getAbsolutePathBuilder().build();
			
			return Response.noContent().location(location).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("could not update the user with id: " + user.getId(), 400);
		}
	}

	@DELETE
	@Path("{userId}")
	public Response deleteUser(@PathParam("userId") final long userId)
	{
		try
		{
			manager.deleteUser(userId);

			return Response.ok().build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("could not delete the user with id: " + userId, 400);
		}
	}
	
}