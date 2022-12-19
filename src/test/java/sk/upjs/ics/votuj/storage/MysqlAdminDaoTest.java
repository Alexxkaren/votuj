package sk.upjs.ics.votuj.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MysqlAdminDaoTest {

	private AdminDao adminDao;
	private Admin savedAdmin;
	
	public MysqlAdminDaoTest() {	
		DaoFactory.INSTANCE.setTesting();
		adminDao = DaoFactory.INSTANCE.getAdminDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Admin admin = new Admin();		
		admin.setName("Test admin");
		admin.setPassword("Test admin passwd");
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void getByNameTest() {
		
		
	}

	/*Subject fromDb = subjectDao.getById(savedSubject.getId());
	assertEquals(savedSubject.getId(), fromDb.getId());
	assertEquals(savedSubject.getName(), fromDb.getName());
	
	assertThrows(NoSuchElementException.class,()->subjectDao.getById(-1));*/
}
