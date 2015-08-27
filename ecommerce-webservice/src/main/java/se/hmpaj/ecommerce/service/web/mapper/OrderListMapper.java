package se.hmpaj.ecommerce.service.web.mapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.model.Product;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderListMapper implements MessageBodyWriter<List<Order>>
{
	private Gson gson;

	public OrderListMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new OrderListAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (List.class.isAssignableFrom(type) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
			return (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Order.class));
		}
		return false;
	}

	@Override
	public long getSize(List<Order> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(List<Order> orders, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(orders, ArrayList.class, writer);
		}

	}

	private static final class OrderListAdapter implements JsonSerializer<List<Order>>
	{

		@Override
		public JsonElement serialize(List<Order> orders, Type typeOfSrc, JsonSerializationContext context)
		{
			final JsonArray jsonOrders = new JsonArray();

			for (Order order : orders)
			{
				final JsonObject jsonOrder = new JsonObject();
				final JsonArray jsonProducts = new JsonArray();
				jsonOrder.add("orderId", new JsonPrimitive(order.getId()));
				jsonOrder.add("userId", new JsonPrimitive(order.getUserId()));
				for (Product product : order.getProducts())
				{
					JsonObject jsonProduct = new JsonObject();
					jsonProduct.add("id", new JsonPrimitive(product.getId()));
					jsonProduct.add("title", new JsonPrimitive(product.getTitle()));
					jsonProduct.add("price", new JsonPrimitive(product.getPrice()));
					jsonProducts.add(jsonProduct);
				}
				jsonOrder.add("products", jsonProducts);
				jsonOrders.add(jsonOrder);
			}
			return jsonOrders;
		}
	}

}
