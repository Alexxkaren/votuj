package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

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
	void insertTest() {
		Category category = new Category();
		category.setName("New category");
		int size = categoryDao.getAll().size();
		Category saved = categoryDao.save(category);
		assertEquals(size + 1, categoryDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(category.getName(), saved.getName());
		categoryDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> categoryDao.save(null), "Category cannot be null");
		assertThrows(NullPointerException.class, () -> categoryDao.save(new Category(null, null)),
				"Category name cannot be null");

	}

	@Test
	void updateTest() {
		Category updated = new Category(savedCategory.getId(), "Changed name");
		int size = categoryDao.getAll().size();
		categoryDao.save(updated);
		assertEquals(size, categoryDao.getAll().size());
		Category fromDB = categoryDao.getById(updated.getId());
		assertEquals(updated.getId(), fromDB.getId());
		assertEquals(updated.getName(), fromDB.getName());
		assertThrows(NullPointerException.class, () -> categoryDao.save(null), "Category cannot be null");
		assertThrows(NullPointerException.class, () -> categoryDao.save(new Category((long) 1, null)),
				"Category name cannot be null");
	}

	/*
	 * @Test void getByItemTest() {
	 * 
	 * }
	 */

	@Test
	void getByIdTest() {
		Category fromDB = categoryDao.getById(savedCategory.getId());
		assertEquals(savedCategory.getId(), fromDB.getId());
		assertEquals(savedCategory.getName(), fromDB.getName());
		assertThrows(EmptyResultDataAccessException.class, () -> categoryDao.getById(null));
	}

	@Test
	void getAllTest() {
		List<Category> list = categoryDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));

	}

}