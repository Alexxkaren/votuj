package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface ItemDao {

	Item save(Item item);

	List<Item> getByProgram(Program program);

	List<Item> getByProgramCategory(Program program, Category category);
}
