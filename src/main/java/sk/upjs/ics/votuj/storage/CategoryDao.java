package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface CategoryDao {

	Category save(Category category) throws NoSuchElementException,NullPointerException; // NAIMPLEMENTOVANE

	boolean delete(Long id) throws ObjectUndeletableException; // NAIMPLEMENTOVANE
	
	Category getById(Long id); // NAIMPLEMENTOVANE
	
	List<Category> getAll(); // NAIMPLEMENTOVANE
	
	List<Category> getByItem(Item item); // NAIMPLEMENTOVANE
	
	
}
