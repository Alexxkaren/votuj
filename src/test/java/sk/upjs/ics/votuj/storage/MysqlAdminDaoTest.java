package sk.upjs.ics.votuj.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class MysqlAdminDaoTest {

	private AdminDao adminDao;
	
	public MysqlAdminDaoTest() {
		// prepli sme vzdialenu premennu na testovanie
		DaoFactory.INSTANCE.setTesting();
		adminDao = DaoFactory.INSTANCE.getAdminDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testgetByName() {
		
		
		
		
		
		
		
		
		
		
	}

}
