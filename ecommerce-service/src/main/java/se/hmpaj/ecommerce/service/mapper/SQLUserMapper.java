package se.hmpaj.ecommerce.service.mapper;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.hmpaj.ecommerce.model.User;
import se.hmpaj.ecommerce.service.sql.Mapper;
import se.hmpaj.ecommerce.service.sql.RowMapper;

@Mapper
public final class SQLUserMapper implements RowMapper<User>
{
	@Override
	public User mapRow(ResultSet rs) throws SQLException
	{
		return new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
	}

	@Override
	public Type getType()
	{
		return User.class;
	}
}