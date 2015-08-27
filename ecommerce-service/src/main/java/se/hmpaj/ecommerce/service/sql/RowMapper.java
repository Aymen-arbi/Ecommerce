package se.hmpaj.ecommerce.service.sql;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T>
{
	public T mapRow(ResultSet rs) throws SQLException;

	public Type getType();
}
