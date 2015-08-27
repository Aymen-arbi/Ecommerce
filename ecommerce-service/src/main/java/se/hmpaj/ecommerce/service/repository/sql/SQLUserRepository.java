package se.hmpaj.ecommerce.service.repository.sql;

import java.util.List;

import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.service.exception.RepositoryException;
import se.hmpaj.ecommerce.service.exception.SQLRepositoryException;
import se.hmpaj.ecommerce.service.repository.UserRepository;
import se.hmpaj.ecommerce.service.sql.SqlQuery;

public final class SQLUserRepository implements UserRepository
{
	private static final String SELECT_USER_ID = "SELECT * FROM users WHERE id = ?";
	private static final String SELECT_USER_EMAIL = "SELECT * FROM users WHERE email = ?";
	private static final String INSERT_USER = "INSERT INTO users (email, password) VALUES (?, ?)";
	private static final String UPDATE_USER = "UPDATE users SET email = ?, password = ? WHERE id = ?";
	private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";

	@Override
	public User addUser(final User user)
	{
		final List<Object> genKeys = executeUpdate(INSERT_USER, user.getEmail(), user.getPassword());
		long id = (long) genKeys.get(0);

		return new User(id, user.getEmail(), user.getPassword());
	}

	@Override
	public User getUserById(final long id)
	{
		final List<User> users = executeSelect(SELECT_USER_ID, id);
		
		if(users.size() > 0)
		{
			return users.get(0);
		}
		
		throw new RepositoryException("User not found");
	}

	@Override
	public User getUserByEmail(String email)
	{
		final List<User> users = executeSelect(SELECT_USER_EMAIL, email);

		if(users.size() > 0)
		{
			return users.get(0);
		}
		
		throw new RepositoryException("User not found");
	}

	@Override
	public User updateUser(final User user)
	{
		executeUpdate(UPDATE_USER, user.getEmail(), user.getPassword(), user.getId());
		return user;
	}

	@Override
	public User deleteUser(final long id)
	{
		final User user = getUserById(id);
		executeUpdate(DELETE_USER, id);

		return user;
	}

	private List<Object> executeUpdate(final String queryString, final Object... parameters)
	{
		try
		{
			final List<Object> result = SqlQuery.getBuilder()
					.query(queryString)
					.parameters(parameters)
					.executeUpdate();

			return result;
		}
		catch (SQLRepositoryException e)
		{
			throw new RepositoryException(e.getMessage());
		}
	}

	private List<User> executeSelect(final String queryString, final Object... parameters)
	{
		try
		{
			final List<User> users = SqlQuery.getBuilder()
					.query(queryString)
					.parameters(parameters)
					.executeSelect(User.class);
			
			return users;
		}
		catch (SQLRepositoryException e)
		{
			throw new RepositoryException(e.getMessage());
		}
	}
}