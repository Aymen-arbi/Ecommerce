package se.hmpaj.ecommerce.service.web.authentification;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import se.hmpaj.ecommerce.service.web.authentification.Authentificated.Authenticated;
import se.hmpaj.ecommerce.service.web.exception.WebServiceAuthetificationException;


@Provider
@Authenticated
public class JaxRsFilterAuthentication implements ContainerRequestFilter
{
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void filter(ContainerRequestContext containerRequest) throws WebApplicationException
	{

		String authCredentials = containerRequest.getHeaderString(AUTHENTICATION_HEADER);

		AuthenticationService authenticationService = new AuthenticationService();

		boolean authenticationStatus = authenticationService.authenticate(authCredentials);

		if (!authenticationStatus)
		{
			throw new WebServiceAuthetificationException("Please check your password");
		}

	}


}
