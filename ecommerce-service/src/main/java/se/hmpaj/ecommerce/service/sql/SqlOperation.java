package se.hmpaj.ecommerce.service.sql;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import se.hmpaj.ecommerce.service.exception.SQLRepositoryException;

public abstract class SqlOperation extends SqlConnection
{
	protected static final Map<Type, RowMapper<?>> rowMappers;
	protected final String queryString;
	protected final List<Object> parameters;

	static
	{
		rowMappers = new HashMap<>();

		Reflections ref = new Reflections("se.hmpaj.ecommerce");
		Set<Class<?>> annotated = ref.getTypesAnnotatedWith(Mapper.class);
		for (Class<?> type : annotated)
		{
			try
			{
				RowMapper<?> mapper = (RowMapper<?>) type.newInstance();

				rowMappers.put(mapper.getType(), mapper);
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				throw new SQLRepositoryException(e.getMessage());
			}
		}
	}

	public SqlOperation(String queryString, List<Object> parameters)
	{
		this.queryString = queryString;
		this.parameters = parameters;
	}

	protected void setStatementValues(PreparedStatement stmt) throws SQLException
	{
		for (int i = 1; i <= parameters.size(); i++)
		{
			Object obj = parameters.get(i - 1);
			stmt.setObject(i, obj);
		}
	}
}
