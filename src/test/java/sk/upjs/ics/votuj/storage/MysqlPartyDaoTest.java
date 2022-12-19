package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MysqlPartyDaoTest {
	
	private PartyDao partyDao;
	private Party savedParty;
	
	public MysqlPartyDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		partyDao = DaoFactory.INSTANCE.getPartyDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Party party = new Party();
		party.setName("Test party");
		party.setInfo("Test party info");
		savedParty = partyDao.save(party);
	}

	@AfterEach
	void tearDown() throws Exception {
		partyDao.delete(savedParty.getId());
	}

	@Test
	void insertTest() {
		Party party = new Party();
		party.setName("New party");
		party.setInfo("New party info");
		int size = partyDao.getAll().size();
		Party saved = partyDao.save(party);
		assertEquals(size+1, partyDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(party.getName(), saved.getName());
		assertEquals(party.getInfo(), saved.getInfo());
		partyDao.delete(saved.getId());
		assertThrows(NullPointerException.class,() -> partyDao.save(null), "Party model cannot be null");
		assertThrows(NullPointerException.class,() -> partyDao.save(new Party((long)0,null,"")), "Party name cannot be null");
		assertThrows(NullPointerException.class,() -> partyDao.save(new Party((long)0,"",null)), "Party info cannot be null");
	}
	
	@Test
	void updateTest() {
		Party updated = new Party(savedParty.getId(),"Changed name", "Info");
		int size = partyDao.getAll().size();
		partyDao.save(updated);
		assertEquals(size, partyDao.getAll().size());
		Party fromDB = partyDao.getById(updated.getId());
		assertEquals(updated.getId(), fromDB.getId());
		assertEquals(updated.getName(), fromDB.getName());
		assertEquals(updated.getInfo(), fromDB.getInfo());
		assertThrows(NullPointerException.class,() -> partyDao.save(null), "Party model cannot be null");
		assertThrows(NullPointerException.class,() -> partyDao.save(new Party((long)1,null,"")), "Party name cannot be null");
		assertThrows(NullPointerException.class,() -> partyDao.save(new Party((long)1,"",null)), "Party info cannot be null");
		
		
	}
	
	@Test
	void getAllTest() {
		List<Party> list = partyDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));
	}
	
	@Test
	void getByIdTest() {
		Party fromDB = partyDao.getById(savedParty.getId());
		
		assertEquals(savedParty.getId(), fromDB.getId());
		assertEquals(savedParty.getName(), fromDB.getName());
		
		assertThrows(NoSuchElementException.class,()->partyDao.getById((long) -1));
	}

}
