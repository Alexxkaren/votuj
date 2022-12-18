package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface CategoryDao {

	Category save(Category category) throws NoSuchElementException, NullPointerException;

	boolean delete(Long id);
	
	Category getById(Long id);
	
	List<Category> getAll();
	
	List<Category> getByItem(Item item);
	
	
}
