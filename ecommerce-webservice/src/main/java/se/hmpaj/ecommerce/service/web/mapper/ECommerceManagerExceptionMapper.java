package se.hmpaj.ecommerce.service.web.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.hmpaj.ecommerce.service.exception.ECommerceManagerException;

@Provider
public final class ECommerceManagerExceptionMapper implements ExceptionMapper<ECommerceManagerException>
{

	@Override
	public Response toResponse(final ECommerceManagerException exception)
	{
		return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
	}

}