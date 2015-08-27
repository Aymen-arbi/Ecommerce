package se.hmpaj.ecommerce.service.repository;

import se.hmpaj.ecommerce.model.User;

public interface UserRepository
{
	User addUser(User user);

	User getUserById(long id);
	
	User getUserByEmail(String email);
	
	User deleteUser(long id);

	User updateUser(User user);
}
