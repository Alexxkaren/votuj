package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

class MysqlTermDaoTest {

	private TermDao termDao;
	private Term savedTerm;

	public MysqlTermDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		termDao = DaoFactory.INSTANCE.getTermDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Term term = new Term();
		term.setSince(2022);
		term.setTo(2024);
		savedTerm = termDao.save(term);
	}

	@AfterEach
	void tearDown() throws Exception {
		termDao.delete(savedTerm.getId());
	}

	@Test
	void insertTest() {
		Term term = new Term();
		term.setSince(2022);
		term.setTo(2024);
		int size = termDao.getAll().size();
		Term saved = termDao.save(term);
		assertEquals(size + 1, termDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(term.getSince(), saved.getSince());
		assertEquals(term.getTo(), saved.getTo());
		termDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> termDao.save(null), "Term cannot be null");
		assertThrows(NullPointerException.class, () -> termDao.save(new Term(null, null, 1)),
				"Term since cannot be null");
		assertThrows(NullPointerException.class, () -> termDao.save(new Term(null, 1, null)), "Term to cannot be null");
	}

	@Test
	void updateTest() {
		Term updated = new Term(savedTerm.getId(), savedTerm.getSince(), savedTerm.getTo());
		int size = termDao.getAll().size();
		termDao.save(updated);
		assertEquals(size, termDao.getAll().size());
		Term fromDB = termDao.getById(updated.getId());
		assertEquals(updated.getId(), fromDB.getId());
		assertEquals(updated.getSince(), fromDB.getSince());
		assertEquals(updated.getTo(), fromDB.getTo());
		assertThrows(NullPointerException.class, () -> termDao.save(null), "Term cannot be null");
		assertThrows(NullPointerException.class, () -> termDao.save(new Term((long) 0, null, 1)),
				"Term since cannot be null");
		assertThrows(NullPointerException.class, () -> termDao.save(new Term((long) 0, 1, null)),
				"Term to cannot be null");
	}

	@Test
	void getAllTest() {
		List<Term> list = termDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));
	}

	@Test
	void getByIdTest() {
		Term fromDB = termDao.getById(savedTerm.getId());
		assertEquals(savedTerm.getId(), fromDB.getId());
		assertEquals(savedTerm.getSince(), fromDB.getSince());
		assertEquals(savedTerm.getTo(), fromDB.getTo());
		assertThrows(EmptyResultDataAccessException.class, () -> termDao.getById((long) -1));
	}
}
