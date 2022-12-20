package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

class MysqlItemDaoTest {

	private ItemDao itemDao;
	private Item savedItem;
	private ProgramDao programDao;
	private Program savedProgram;
	private CategoryDao categoryDao;
	private Category savedCategory;
	private PartyDao partyDao;
	private Party savedParty;
	private TermDao termDao;
	private Term savedTerm;

	public MysqlItemDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		itemDao = DaoFactory.INSTANCE.getItemDao();
		programDao = DaoFactory.INSTANCE.getProgramDao();
		categoryDao = DaoFactory.INSTANCE.getCategoryDao();
		partyDao = DaoFactory.INSTANCE.getPartyDao();
		termDao = DaoFactory.INSTANCE.getTermDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Item item = new Item();
		item.setName("Test Item");
		item.setInfo("Item info");
		savedParty = partyDao.save(new Party(null, "Name", "Info"));
		savedTerm = termDao.save(new Term(null, 1, 5));
		savedProgram = programDao.save(new Program(null, "Name", savedParty, false, savedTerm));
		savedCategory = categoryDao.save(new Category(null, "Name"));
		List<Category> categories = new ArrayList<>();
		categories.add(savedCategory);
		item.setProgram(savedProgram);

		savedItem = itemDao.save(item, categories);
	}

	@AfterEach
	void tearDown() throws Exception {
		itemDao.delete(savedItem.getId());
		categoryDao.delete(savedCategory.getId());
		programDao.delete(savedProgram.getId());
	}

	@Test
	void insertTest() {
		Item item = new Item();
		item.setName("Name");
		item.setInfo("Item info");
		item.setProgram(savedProgram);
		List<Category> categories = new ArrayList<>();
		categories.add(savedCategory);
		item.setCategories(categories);
		int size = itemDao.getAll().size();
		Item saved = itemDao.save(item, categories);
		assertNotNull(saved);
		assertEquals(size + 1, itemDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(item.getName(), saved.getName());
		assertEquals(item.getInfo(), saved.getInfo());
		assertEquals(item.getProgram().getId(), saved.getProgram().getId());
		assertEquals(item.getCategories(), saved.getCategories());
		itemDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> itemDao.save(null, null), "Item cannot be null");
		assertThrows(NullPointerException.class,
				() -> itemDao.save(new Item(null, null, null, null), new ArrayList<Category>()),
				"Item name cannot be null");
		assertThrows(NullPointerException.class,
				() -> itemDao.save(new Item(null, "name", null, null), new ArrayList<Category>()),
				"Item info cannot be null");
		assertThrows(NullPointerException.class,
				() -> itemDao.save(new Item(null, "name", "info", null), new ArrayList<Category>()),
				"Item program cannot be null");

	}

	@Test
	void updateTest() {
		Item item = new Item();
		item.setId(savedItem.getId());
		item.setName("Name");
		item.setInfo("Item info");
		item.setProgram(savedProgram);
		List<Category> categories = new ArrayList<>();
		categories.add(savedCategory);
		item.setCategories(categories);
		int size = itemDao.getAll().size();
		Item saved = itemDao.save(item, categories);
		assertNotNull(saved);
		assertEquals(size, itemDao.getAll().size());
		assertEquals(saved.getId(), savedItem.getId());
		assertEquals(item.getName(), saved.getName());
		assertEquals(item.getInfo(), saved.getInfo());
		assertEquals(item.getProgram().getId(), saved.getProgram().getId());
		assertEquals(item.getCategories(), saved.getCategories());
		itemDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> itemDao.save(null, null), "Item cannot be null");
		assertThrows(NullPointerException.class,
				() -> itemDao.save(new Item((long) 1, null, null, null), new ArrayList<Category>()),
				"Item name cannot be null");
		assertThrows(NullPointerException.class,
				() -> itemDao.save(new Item((long) 1, "name", null, null), new ArrayList<Category>()),
				"Item info cannot be null");
		assertThrows(NullPointerException.class,
				() -> itemDao.save(new Item((long) 1, "name", "info", null), new ArrayList<Category>()),
				"Item program cannot be null");
	}

	@Test
	void getAllTest() {
		List<Item> list = itemDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));

	}

	@Test
	void getByIdTest() {
		Item fromDB = itemDao.getById(savedItem.getId());
		assertEquals(savedItem.getId(), fromDB.getId());
		assertEquals(savedItem.getName(), fromDB.getName());
		assertEquals(savedItem.getInfo(), fromDB.getInfo());
		assertEquals(savedItem.getProgram().getId(), fromDB.getProgram().getId());
		assertEquals(savedItem.getCategories(), fromDB.getCategories());
		assertThrows(EmptyResultDataAccessException.class, () -> itemDao.getById(null));
	}

}
