package sk.upjs.ics.votuj.storage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MysqlItemDaoTest {
	
	private ItemDao itemDao;
	private Item savedItem;
	
	public MysqlItemDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		itemDao = DaoFactory.INSTANCE.getItemDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
