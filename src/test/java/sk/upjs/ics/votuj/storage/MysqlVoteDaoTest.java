package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

class MysqlVoteDaoTest {

	private VoteDao voteDao;
	private Vote savedVote;
	private PartyDao partyDao;
	private Party savedParty;
	private RegionDao regionDao;
	private Region savedRegion;

	public MysqlVoteDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		voteDao = DaoFactory.INSTANCE.getVoteDao();
		partyDao = DaoFactory.INSTANCE.getPartyDao();
		regionDao = DaoFactory.INSTANCE.getRegionDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Vote vote = new Vote();
		vote.setAge(1);
		vote.setMale(true);
		vote.setDate(LocalDateTime.now());
		Region region = regionDao.save(new Region(null, "ZEME CESKA DOMOV MUJ"));
		vote.setRegion(region.getId());
		savedRegion = region;
		savedParty = partyDao.save(new Party(null, "1", "2"));
		vote.setParty(savedParty);
		savedVote = voteDao.save(vote);
	}

	@AfterEach
	void tearDown() throws Exception {
		voteDao.delete(savedVote.getId());
		voteDao.delete(savedParty.getId());
	}

	@Test
	void insertTest() {
		Vote vote = new Vote();
		vote.setAge(1);
		vote.setMale(true);
		vote.setDate(LocalDateTime.now());
		vote.setRegion(savedRegion.getId());
		vote.setParty(savedParty);
		int size = voteDao.getAll().size();
		Vote saved = voteDao.save(vote);
		assertEquals(size + 1, voteDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(vote.getAge(), saved.getAge());
		assertEquals(vote.isMale(), saved.isMale());
		assertEquals(vote.getDate(), saved.getDate());
		assertEquals(vote.getRegion(), saved.getRegion());
		assertEquals(vote.getParty().getId(), saved.getParty().getId());
		partyDao.delete(saved.getId());
		assertThrows(NullPointerException.class, () -> voteDao.save(null), "Vote cannot be null");
		assertThrows(NullPointerException.class, () -> voteDao.save(new Vote(null, null, false, null, null, null)),
				"Age cannot be null");
		assertThrows(NullPointerException.class, () -> voteDao.save(new Vote(null, 1, false, null, null, null)),
				"Date cannot be null");
		assertThrows(NullPointerException.class,
				() -> voteDao.save(new Vote(null, 1, false, LocalDateTime.now(), null, null)), "Region cannot be null");
		assertThrows(NullPointerException.class,
				() -> voteDao.save(new Vote(null, 1, false, LocalDateTime.now(), savedRegion.getId(), null)),
				"Party cannot be null");

	}

	@Test
	void updateTest() {
		Vote updated = new Vote(savedVote.getId(), savedVote.getAge(), savedVote.isMale(), savedVote.getDate(),
				savedVote.getRegion(), savedVote.getParty());
		int size = voteDao.getAll().size();
		voteDao.save(updated);
		assertEquals(size, voteDao.getAll().size());
		Vote fromDB = voteDao.getById(updated.getId());
		assertEquals(updated.getId(), fromDB.getId());
		assertEquals(updated.getAge(), fromDB.getAge());
		assertEquals(updated.isMale(), fromDB.isMale());
		// assertEquals(updated.getDate(), fromDB.getDate()); //not yet working
		assertEquals(updated.getRegion(), fromDB.getRegion());
		assertEquals(updated.getParty().getId(), fromDB.getParty().getId());
		assertThrows(NullPointerException.class, () -> voteDao.save(null), "Vote cannot be null");
		assertThrows(NullPointerException.class, () -> voteDao.save(new Vote((long) 1, null, false, null, null, null)),
				"Age cannot be null");
		assertThrows(NullPointerException.class, () -> voteDao.save(new Vote((long) 1, 1, false, null, null, null)),
				"Date cannot be null");
		assertThrows(NullPointerException.class,
				() -> voteDao.save(new Vote((long) 1, 1, false, LocalDateTime.now(), null, null)),
				"Region cannot be null");
		assertThrows(NullPointerException.class,
				() -> voteDao.save(new Vote((long) 1, 1, false, LocalDateTime.now(), savedRegion.getId(), null)),
				"Party cannot be null");

	}

	@Test
	void getAllTest() {
		List<Vote> list = voteDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));
	}

	@Test
	void getByIdTest() {
		Vote fromDB = voteDao.getById(savedVote.getId());
		assertEquals(savedVote.getId(), fromDB.getId());
		assertEquals(savedVote.getAge(), fromDB.getAge());
		assertEquals(savedVote.isMale(), fromDB.isMale());
		// assertEquals(savedVote.getDate(), fromDB.getDate()); //not yet working
		assertEquals(savedVote.getRegion(), fromDB.getRegion());
		assertEquals(savedVote.getParty(), fromDB.getParty());
		assertThrows(EmptyResultDataAccessException.class, () -> voteDao.getById(null));
	}

}
