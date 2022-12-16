package sk.upjs.ics.votuj.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class MysqlCategoryDaoTest {

	private CategoryDao categoryDao;
	private Category savedCategory;

	public MysqlCategoryDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		categoryDao = DaoFactory.INSTANCE.getCategoryDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Category category = new Category();
		category.setName("Test category");
		savedCategory = categoryDao.save(category);
	}

	@AfterEach
	void tearDown() throws Exception {
		categoryDao.delete(savedCategory.getId());
	}

	@Test
	void getByItem() {

	}

	@Test
	void getByIdTest() {

	}

	// ide
	@Test
	void getAllTest() {
		List<Category> list = categoryDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));

	}

}