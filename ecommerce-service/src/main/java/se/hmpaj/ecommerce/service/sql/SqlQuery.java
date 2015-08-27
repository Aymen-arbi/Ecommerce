package se.hmpaj.ecommerce.service.sql;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class SqlQuery
{
	private String query;
	private List<Object> parameters;

	public SqlQuery()
	{
		this.parameters = new ArrayList<>();
	}

	public String getQuery()
	{
		return query;
	}

	private void setQuery(String query)
	{
		this.query = query;
	}

	public List<Object> getParameters()
	{
		return parameters;
	}

	private void setParameters(Object... parameters)
	{
		for (Object obj : parameters)
		{
			this.parameters.add(obj);
		}
	}

	public static QueryBuilder getBuilder()
	{
		return new QueryBuilder();
	}

	public static final class QueryBuilder
	{
		private final SqlQuery query;

		public QueryBuilder()
		{
			this.query = new SqlQuery()
			{
			};
		}

		public QueryBuilder query(String queryString)
		{
			query.setQuery(queryString);
			return this;
		}

		public QueryBuilder parameters(Object... parameters)
		{
			query.setParameters(parameters);
			return this;
		}

		public <T> List<T> executeSelect(Type type)
		{
			return new SqlSelect<T>(type, query.getQuery(), query.getParameters()).executeSelect();
		}

		public List<Object> executeUpdate()
		{
			return new SqlUpdate(query.getQuery(), query.getParameters()).executeUpdate();
		}
	}
}