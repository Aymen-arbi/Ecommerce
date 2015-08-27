package se.hmpaj.ecommerce.service.mapper;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.service.sql.Mapper;
import se.hmpaj.ecommerce.service.sql.RowMapper;

@Mapper
public final class SQLOrderMapper implements RowMapper<Order>
{
	@Override
	public Order mapRow(ResultSet rs) throws SQLException
	{
		return new Order(rs.getLong("id"), rs.getLong("user_id"));
	}

	@Override
	public Type getType()
	{
		return Order.class;
	}
}