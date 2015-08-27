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
public class ProductListMapper implements MessageBodyWriter<List<Product>>
{
	private Gson gson;

	public ProductListMapper()
	{
		gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new ProductListAdapter()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (List.class.isAssignableFrom(type) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
			return (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Product.class));
		}
		return false;
	}

	@Override
	public long getSize(List<Product> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(List<Product> products, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(products, ArrayList.class, writer);
		}
	}

	// @Override
	// public boolean isReadable(Class<?> type, Type genericType, Annotation[]
	// annotations, MediaType mediaType)
	// {
//		if (List.class.isAssignableFrom(type) && genericType instanceof ParameterizedType)
//		{
//			ParameterizedType parameterizedType = (ParameterizedType) genericType;
//			Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
//			return (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Product.class));
//		}
	// return false;
	// }
	//
	// @Override
	// public List<Product> readFrom(Class<List<Product>> type, Type
	// genericType, Annotation[] annotations, MediaType mediaType,
	// MultivaluedMap<String, String> httpHeaders,
	// InputStream entityStream) throws IOException, WebApplicationException
	// {
//		List<Product> products = gson.fromJson(new InputStreamReader(entityStream), type);
//
//		return products;
	// return null;
	// }

	private static final class ProductListAdapter implements JsonSerializer<List<Product>>
	{

		@Override
		public JsonElement serialize(List<Product> products, Type typeOfSrc, JsonSerializationContext context)
		{
			final JsonArray jsonProducts = new JsonArray();

			for (Product product : products)
			{
				JsonObject jsonProduct = new JsonObject();
				jsonProduct.add("id", new JsonPrimitive(product.getId()));
				jsonProduct.add("title", new JsonPrimitive(product.getTitle()));
				jsonProduct.add("price", new JsonPrimitive(product.getPrice()));
				jsonProducts.add(jsonProduct);
			}
			return jsonProducts;
		}

	}

}
