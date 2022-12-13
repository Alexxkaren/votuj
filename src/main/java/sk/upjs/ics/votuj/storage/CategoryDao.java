package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface CategoryDao {

	Category save(Category category);

	boolean delete(Long id);
	
	Category getById(Long id);
	
	List<Category> getAll();
	
	List<Category> getByItem(Item item);
	
	
}
