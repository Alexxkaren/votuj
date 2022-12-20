package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

class MysqlProgramDaoTest {

	private ProgramDao programDao;
	private Program savedProgram;
	private PartyDao partyDao;
	private Party savedParty;
	private TermDao termDao;
	private Term savedTerm;

	public MysqlProgramDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		programDao = DaoFactory.INSTANCE.getProgramDao();
		termDao = DaoFactory.INSTANCE.getTermDao();
		partyDao = DaoFactory.INSTANCE.getPartyDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Program program = new Program();
		program.setName("Name");
		savedParty = partyDao.save(new Party(null, "1", "2"));
		program.setParty(savedParty);
		program.setIsActive(true);
		program.setTerm(new Term());
		savedTerm = termDao.save(new Term(null, 2022, 2023));
		program.setTerm(savedTerm);
		savedProgram = programDao.save(program);
	}

	@AfterEach
	void tearDown() throws Exception {
		programDao.delete(savedProgram.getId());
		termDao.delete(savedTerm.getId());
	}

	@Test
	void insertTest() {
		Program program = new Program();
		program.setName("New category");
		program.setParty(savedParty);
		program.setIsActive(false);
		program.setTerm(savedTerm);
		int size = programDao.getAll().size();
		Program saved = programDao.save(program);
		assertEquals(size + 1, programDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(program.getName(), saved.getName());
		assertEquals(program.getParty().getId(), saved.getParty().getId());
		assertEquals(program.isActive(), saved.isActive());
		assertEquals(program.getTerm().getId(), saved.getTerm().getId());
		programDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> programDao.save(null), "Program cannot be null");
		assertThrows(NullPointerException.class, () -> programDao.save(new Program(null, null, null, false, null)),
				"Program name cannot be null");
		assertThrows(NullPointerException.class,
				() -> programDao.save(new Program(null, "name", null, false, null)),
				"Program party cannot be null");
		assertThrows(NullPointerException.class,
				() -> programDao
						.save(new Program(null, "name", new Party(), true, null)),
				"Program term cannot be null");
	
	}

	@Test
	void updateTest() {
		Program updated = new Program(savedProgram.getId(), savedProgram.getName(), savedProgram.getParty(),
				savedProgram.isActive(), savedProgram.getTerm());
		int size = programDao.getAll().size();
		programDao.save(updated);
		assertEquals(size, programDao.getAll().size());
		Program fromDB = programDao.getById(updated.getId());
		assertEquals(updated.getId(), fromDB.getId());
		assertEquals(updated.getName(), fromDB.getName());
		assertEquals(updated.getParty().getId(), fromDB.getParty().getId());
		assertEquals(updated.isActive(), fromDB.isActive());
		assertEquals(updated.getTerm().getId(), fromDB.getTerm().getId());
		assertThrows(NullPointerException.class, () -> programDao.save(null), "Program cannot be null");
		assertThrows(NullPointerException.class, () -> programDao.save(new Program((long) 1, "", null, false, null)),
				"Program name cannot be null");
		assertThrows(NullPointerException.class, () -> programDao.save(new Program((long) 1, "", null, false, null)),
				"Program party cannot be null");
		assertThrows(NullPointerException.class,
				() -> programDao.save(new Program((long) 1, "", new Party(), false, null)),
				"Program term cannot be null");
	}

	/*
	 * @Test void getByPartyTest() { fail("Not yet implemented"); }
	 * 
	 * @Test void getByTermPartyTest() { fail("Not yet implemented"); }
	 */
	@Test
	void getByIdTest() {
		Program fromDB = programDao.getById(savedProgram.getId());
		assertEquals(savedProgram.getId(), fromDB.getId());
		assertEquals(savedProgram.getName(), fromDB.getName());
		assertEquals(savedProgram.getParty().getId(), fromDB.getParty().getId());
		assertEquals(savedProgram.isActive(), fromDB.isActive());
		assertEquals(savedProgram.getTerm().getId(), fromDB.getTerm().getId());
		assertThrows(EmptyResultDataAccessException.class, () -> programDao.getById(null));
	}

	@Test
	void getAllTest() {
		List<Program> list = programDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));
	}
}
