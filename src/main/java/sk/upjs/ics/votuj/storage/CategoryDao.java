package sk.upjs.ics.votuj.storage;

public interface CategoryDao {

	Category save(Category category);

	boolean delete(Long id);
}
