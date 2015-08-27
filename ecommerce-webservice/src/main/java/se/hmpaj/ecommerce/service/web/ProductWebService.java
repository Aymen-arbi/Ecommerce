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

import se.hmpaj.ecommerce.model.Product;
import se.hmpaj.ecommerce.service.ECommerceManager;
import se.hmpaj.ecommerce.service.exception.ECommerceManagerException;
import se.hmpaj.ecommerce.service.repository.sql.SQLOrderRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLProductRepository;
import se.hmpaj.ecommerce.service.repository.sql.SQLUserRepository;
import se.hmpaj.ecommerce.service.web.authentification.Authentificated.Authenticated;

@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class ProductWebService
{
	ECommerceManager manager = new ECommerceManager(new SQLUserRepository(), new SQLProductRepository(), new SQLOrderRepository());

	@Context
	private UriInfo uriInfo;

	@GET
	public Response getAllProducts()
	{
		try
		{
			List<Product> allProducts = manager.getAllProducts();
			GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(allProducts)
			{
			};
			return Response.ok(entity).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("No products found");
		}
	}

	@GET
	@Path("{productId}")
	public Response getProduct(@PathParam("productId") final long productId)
	{
		try
		{
			final Product product = manager.getProduct(productId);
			return Response.ok(product).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not find product");
		}

	}

	@PUT
	@Path("{productId}")
	public Response updateProduct(@PathParam("productId") final long id, final Product product)
	{
		try
		{
			manager.updateProduct(product);
			final URI location = uriInfo.getAbsolutePathBuilder().build();

			return Response.noContent().location(location).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not update product with id" + id);
		}
	}

	@POST
	public Response addProduct(final Product product)
	{
		try
		{
			final Product productFromDB = manager.addProduct(product);
			final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(productFromDB.getId())).build();

			return Response.created(location).build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not add product");
		}

	}

	@DELETE
	@Path("{productId}")
	public Response removeProduct(@PathParam("productId") final long productId)
	{
		try
		{
			manager.deleteProduct(productId);
			return Response.ok().build();
		}
		catch (ECommerceManagerException e)
		{
			throw new WebApplicationException("Could not delete the product");
		}
	}
}
