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
		savedAdmin = adminDao.save(admin);
	}

	@AfterEach
	void tearDown() throws Exception {
		adminDao.delete(savedAdmin.getId());
	}

	@Test
	void getByNameTest() {
		Admin fromDB=adminDao.getByName(savedAdmin.getName());
		assertNotNull(fromDB);
		assertEquals(fromDB.getPassword(), savedAdmin.getPassword());
	}
}
