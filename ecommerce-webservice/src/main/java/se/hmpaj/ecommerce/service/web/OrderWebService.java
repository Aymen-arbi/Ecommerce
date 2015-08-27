package se.hmpaj.ecommerce.service.web;

import java.net.URI;
import java.util.List;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.service.ECommerceManager;
import se.hmpaj.ecommerce.service.exception.ECommerceManagerException;
import se.hmpaj.ecommerce.service.repository.sql.SQLOrderRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLProductRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLUserRepository;
import se.hmpaj.ecommerce.service.web.authentification.Authentificated.Authenticated;

@Path("users/{userId}/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class OrderWebService
{
	private static final ECommerceManager manager = new ECommerceManager(new SQLUserRepository(), new SQLProductRepository(), new SQLOrderRepository());

	@Context
	private UriInfo uriInfo;

	@GET
	public Response getAllUserOrders(@PathParam("userId") final long userId)
	{
		try
		{
			final List<Order> allOrders = manager.getAllOrdersForUser(userId);
			GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(allOrders)
			{
			};
			return Response.ok(entity).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("No order for user with id: " + userId, 400);
		}
	}

	@POST
	public Response addUserOrder(@PathParam("userId") final long userId, final Order postOrder)
	{
		try
		{
			final Order orderFromDb = manager.addOrder(new Order(userId, postOrder.getProducts()));
			final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(orderFromDb.getId())).build();

			return Response.created(location).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not add order", 400);
		}
	}

	@Path("{orderId}")
	@GET
	public Response getUserOrder(@PathParam("userId") final long userId, @PathParam("orderId") final long orderId)
	{
		try
		{
			final Order order = manager.getOrder(orderId);
			final List<Order> allOrders = manager.getAllOrdersForUser(userId);
			if (allOrders.contains(order))
			{
				return Response.ok(order).build();
			}
			throw new WebApplicationException(400);
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not find order for user " + userId + " with order id: " + orderId, 404);
		}
	}

	@Path("{orderId}")
	@DELETE
	public Response removeOrder(@PathParam("userId") final long userId, @PathParam("orderId") final long orderId)
	{
		try
		{
			manager.deleteOrder(orderId);

			return Response.ok().build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not delete order with order id: " + orderId, 400);
		}
	}

	@Path("{orderId}")
	@PUT
	public Response updateOrder(@PathParam("userId") final long userId, @PathParam("orderId") final long orderId, final Order order)
	{
		try
		{
			manager.updateOrder(new Order(orderId, manager.getUserById(userId).getId(), order.getProducts()));
			final URI location = uriInfo.getAbsolutePathBuilder().build();

			return Response.noContent().location(location).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException(e.getMessage(), 400);
		}
	}
}
