package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

class MysqlCandidateDaoTest {

	private CandidateDao candidateDao;
	private Candidate savedCandidate;
	private PartyDao partyDao;
	private Party savedParty;
	private TermDao termDao;
	private Term savedTerm;

	public MysqlCandidateDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		candidateDao = DaoFactory.INSTANCE.getCandidateDao();
		termDao = DaoFactory.INSTANCE.getTermDao();
		partyDao = DaoFactory.INSTANCE.getPartyDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Candidate candidate = new Candidate();
		candidate.setName("Test candidate name");
		candidate.setSurname("Test candidate surname");
		candidate.setCandidate_number("1");
		candidate.setInfo("Test candidate info");
		savedParty = partyDao.save(new Party(null, "1", "2"));
		candidate.setParty(savedParty);
		savedTerm = termDao.save(new Term(null, 2022, 2023));
		List<Term> terms = new ArrayList<>();
		terms.add(savedTerm);
		savedCandidate = candidateDao.save(candidate, terms);
	}

	@AfterEach
	void tearDown() throws Exception {
		candidateDao.delete(savedCandidate.getId());
		termDao.delete(savedTerm.getId());
		partyDao.delete(savedParty.getId());
	}

	@Test
	void insertTest() {
		Candidate candidate = new Candidate();
		candidate.setName("Name");
		candidate.setSurname("surname");
		candidate.setCandidate_number("2");
		candidate.setInfo("candidate info");
		candidate.setParty(savedParty);
		int size = candidateDao.getAll().size();
		List<Term> terms = new ArrayList<>();
		terms.add(savedTerm);
		Candidate saved = candidateDao.save(candidate, terms);
		assertNotNull(saved);
		assertEquals(size + 1, candidateDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(candidate.getName(), saved.getName());
		assertEquals(candidate.getSurname(), saved.getSurname());
		assertEquals(candidate.getCandidateNumber(), saved.getCandidateNumber());
		assertEquals(candidate.getInfo(), saved.getInfo());
		assertEquals(candidate.getParty().getId(), saved.getParty().getId());
		candidateDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> candidateDao.save(null, null), "Candidate cannot be null");

		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Candidate(null, null, null, null, null, null), new ArrayList<Term>()),
				"Candidate name cannot be null");
		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Candidate(null, "name", null, null, null, null), new ArrayList<Term>()),
				"Candidate surname cannot be null");

		assertThrows(
				NullPointerException.class, () -> candidateDao
						.save(new Candidate(null, "name", "surname", null, null, null), new ArrayList<Term>()),
				"Candidate info cannot be null");

		assertThrows(
				NullPointerException.class, () -> candidateDao
						.save(new Candidate(null, "name", "Surname", "Info", null, null), new ArrayList<Term>()),
				"Candidate number cannot be null");

		assertThrows(
				NullPointerException.class, () -> candidateDao
						.save(new Candidate(null, "name", "Surname", "Info", "2", null), new ArrayList<Term>()),
				"Candidate party cannot be null");

	}

	@Test
	void updateTest() {
		List<Term> terms = new ArrayList<>();
		terms.add(savedTerm);
		Candidate updated = new Candidate(savedCandidate.getId(), savedCandidate.getName(), savedCandidate.getSurname(),
				savedCandidate.getCandidateNumber(), savedCandidate.getInfo(), savedCandidate.getParty(), terms);
		Candidate saved = candidateDao.save(updated, terms);
		assertNotNull(saved);
		assertEquals(saved.getId(), updated.getId());
		assertEquals(updated.getName(), saved.getName());
		assertEquals(updated.getSurname(), saved.getSurname());
		assertEquals(updated.getCandidateNumber(), saved.getCandidateNumber());
		assertEquals(updated.getInfo(), saved.getInfo());
		assertEquals(updated.getParty().getId(), saved.getParty().getId());
		assertThrows(NullPointerException.class, () -> candidateDao.save(null, terms), "Candidate cannot be null");

		assertThrows(NullPointerException.class, () -> candidateDao
				.save(new Candidate(savedCandidate.getId(), null, null, null, null, null), new ArrayList<Term>()),
				"Candidate name cannot be null");
		assertThrows(NullPointerException.class, () -> candidateDao
				.save(new Candidate(savedCandidate.getId(), "name", null, null, null, null), new ArrayList<Term>()),
				"Candidate surname cannot be null");

		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Candidate(savedCandidate.getId(), "name", "surname", null, null, null),
						new ArrayList<Term>()),
				"Candidate info cannot be null");

		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Candidate(savedCandidate.getId(), "name", "Surname", "Info", null, null),
						new ArrayList<Term>()),
				"Candidate number cannot be null");

		assertThrows(NullPointerException.class,
				() -> candidateDao.save(new Candidate(savedCandidate.getId(), "name", "Surname", "Info", "2", null),
						new ArrayList<Term>()),
				"Candidate party cannot be null");

	}

//	@Test
//	void getByTermPartyTest() {
//		Party smer = new Party(null, "Smer-SD", "Info");
//		Party olano = new Party(null, "OLANO", "Info");
//		smer = partyDao.save(smer);
//		olano = partyDao.save(olano);
//
//		Candidate Fico = new Candidate(null, "Robert", "Fico", "1", "Nasobny premier", smer);
//		Candidate Blaha = new Candidate(null, "Lubos", "Blaha", "2", "Konspirator", smer);
//		Candidate Matovic = new Candidate(null, "Igor", "Matovic", "3", "Sulik nenakupil testy", olano);
//
//		List<Term> terms = new ArrayList<>();
//		terms.add(savedTerm);
//		candidateDao.save(Fico, terms);
//		candidateDao.save(Blaha, terms);
//		candidateDao.save(Matovic, terms);
//
//		List<Candidate> smerResult = candidateDao.getByTermParty(smer, savedTerm);
//		assertEquals(2, smerResult.size());
//		List<Candidate> olanoResult = candidateDao.getByTermParty(olano, savedTerm);
//		assertEquals(1, olanoResult.size());
//
//		candidateDao.delete(Fico.getId());
//		candidateDao.delete(Blaha.getId());
//		candidateDao.delete(Matovic.getId());
//		partyDao.delete(smer.getId());
//		partyDao.delete(olano.getId());
//	}

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

		assertThrows(EmptyResultDataAccessException.class, () -> candidateDao.getById((long) -1));
	}

	@Test
	void getAllTest() {
		List<Candidate> list = candidateDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));
	}

}
