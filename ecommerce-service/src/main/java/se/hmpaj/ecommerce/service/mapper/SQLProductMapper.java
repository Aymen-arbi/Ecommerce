package se.hmpaj.ecommerce.service.mapper;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.hmpaj.ecommerce.model.Product;
import se.hmpaj.ecommerce.service.sql.Mapper;
import se.hmpaj.ecommerce.service.sql.RowMapper;

@Mapper
public final class SQLProductMapper implements RowMapper<Product>
{	
	@Override
	public Product mapRow(ResultSet rs) throws SQLException
	{
		return new Product(rs.getLong("id"), rs.getString("title"), rs.getDouble("price"));
	}

	@Override
	public Type getType()
	{
		return Product.class;
	}
}