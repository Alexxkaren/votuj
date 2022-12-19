package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MysqlCandidateDaoTest {
	
	private CandidateDao candidateDao;
	private Candidate savedCandidate;
	private PartyDao partyDao;
	private Party savedParty;
	
	public MysqlCandidateDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		candidateDao = DaoFactory.INSTANCE.getCandidateDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Candidate candidate = new Candidate();
		candidate.setName("Test candidate name");
		candidate.setSurname("Test candidate surname");
		candidate.setCandidate_number(null);
		candidate.setInfo("Test candidate info");
		savedParty = partyDao.save(new Party(null, "1", "2"));
		candidate.setParty(savedParty);
		savedCandidate= candidateDao.save(candidate, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		candidateDao.delete(savedCandidate.getId());
		candidateDao.delete(savedParty.getId());
	}

	@Test
	void insertTest() {
		Candidate candidate = new Candidate();
		candidate.setName("Name");
		candidate.setSurname("surname");
		candidate.setCandidate_number(null);
		candidate.setInfo("candidate info");
		candidate.setParty(savedParty);
		int size = candidateDao.getAll().size();
		Candidate saved = candidateDao.save(candidate, null);
		assertEquals(size + 1, candidateDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(candidate.getName(), saved.getName());
		assertEquals(candidate.getSurname(), saved.getSurname());
		assertEquals(candidate.getCandidateNumber(), saved.getCandidateNumber());
		assertEquals(candidate.getInfo(), saved.getInfo());
		assertEquals(candidate.getParty().getId(), saved.getParty().getId());
		candidateDao.delete(saved.getId());
		/*
		assertThrows(NullPointerException.class, () -> candidateDao.save(null, null), "Vote cannot be null");
		assertThrows(NullPointerException.class, () -> candidateDao.save(new Vote((long) 0, null, false, null, null, null)),
				"Age cannot be null");
		assertThrows(NullPointerException.class, () -> candidateDao.save(new Vote((long) 0, 1, false, null, null, null)),
				"Date cannot be null");
		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Vote((long) 0, 1, false, LocalDateTime.now(), null, null)),
				"Region cannot be null");
		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Vote((long) 0, 1, false, LocalDateTime.now(), savedRegion.getId(), null)),
				"Party cannot be null");
				*/
	}		
	
	
	@Test
	void updateTest() {
		fail("Not yet implemented");
	}
	
	/*
	@Test
	void getByTermPartyTest() {
		fail("Not yet implemented");
	}
	*/
	
	@Test
	void getByIdTest() {
		Candidate fromDB = candidateDao.getById(savedCandidate.getId());
		
		assertEquals(savedCandidate.getId(), fromDB.getId());
		assertEquals(savedCandidate.getName(), fromDB.getName());
		assertEquals(savedCandidate.getSurname(), fromDB.getSurname());
		assertEquals(savedCandidate.getCandidateNumber(), fromDB.getCandidateNumber());
		assertEquals(savedCandidate.getInfo(), fromDB.getInfo());
		assertEquals(savedCandidate.getName(), fromDB.getName());
		assertEquals(savedCandidate.getParty(), fromDB.getParty());
		
		
		assertThrows(NoSuchElementException.class,()->candidateDao.getById((long) -1));
	}
	

}
