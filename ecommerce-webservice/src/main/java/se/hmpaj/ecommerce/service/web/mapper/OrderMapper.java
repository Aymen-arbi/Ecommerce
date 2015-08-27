package se.hmpaj.ecommerce.service.web.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import se.hmpaj.ecommerce.model.Order;
import se.hmpaj.ecommerce.model.Product;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class OrderMapper implements MessageBodyReader<Order>, MessageBodyWriter<Order>
{
	private Gson gson;

	public OrderMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(Order.class, new OrderAdapter()).create();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Order.class);
	}

	@Override
	public Order readFrom(Class<Order> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		Order order = gson.fromJson(new InputStreamReader(entityStream), Order.class);

		return order;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Order.class);
	}

	@Override
	public long getSize(Order order, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(Order order, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(order, Order.class, writer);
		}

	}

	private static final class OrderAdapter implements JsonSerializer<Order>, JsonDeserializer<Order>
	{
		@Override
		public JsonElement serialize(Order order, Type typeOfSrc, JsonSerializationContext context)
		{
			final JsonObject orderJson = new JsonObject();
			final JsonArray products = new JsonArray();
			
			for(Product product : order.getProducts())
			{
				JsonObject jsonProduct = new JsonObject();
				jsonProduct.add("id", new JsonPrimitive(product.getId()));
				jsonProduct.add("title", new JsonPrimitive(product.getTitle()));
				jsonProduct.add("price", new JsonPrimitive(product.getPrice()));
				products.add(jsonProduct);
			}
			orderJson.add("orderId", new JsonPrimitive(order.getId()));
			orderJson.add("userId", new JsonPrimitive(order.getUserId()));
			orderJson.add("products", products);
			return orderJson;
		}

		@Override
		public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			final JsonObject jsonOrder = json.getAsJsonObject();
			final List<Product> products = new ArrayList<>();
			
			if(jsonOrder.has("products"))
			{
				final JsonArray jsonProducts = jsonOrder.get("products").getAsJsonArray();
				for(int i = 0; i < jsonProducts.size(); i++)
				{
					final JsonObject product = jsonProducts.get(i).getAsJsonObject();
					final long productId = product.get("id").getAsLong();
					final String title = product.get("title").getAsString();
					final double price = product.get("price").getAsDouble();
					products.add(new Product(productId, title, price));
				}
			}
			
			return new Order(products);
		}
	}

}
