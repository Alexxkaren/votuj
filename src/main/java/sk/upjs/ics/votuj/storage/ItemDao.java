package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface ItemDao {

	Item save(Item item);
	
	boolean delete(Long id);

	List<Item> getByProgram(Program program);

	List<Item> getByProgramCategory(Program program, Category category);
	
	Item getById(Long id);
}
