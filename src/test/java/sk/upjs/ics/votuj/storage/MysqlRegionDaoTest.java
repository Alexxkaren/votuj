package sk.upjs.ics.votuj.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

class MysqlRegionDaoTest {
	
	private RegionDao regionDao;
	private Region savedRegion;

	public MysqlRegionDaoTest() {
		DaoFactory.INSTANCE.setTesting();
		regionDao = DaoFactory.INSTANCE.getRegionDao();
	}
	
	@BeforeEach
	void setUp() throws Exception {
		Region region = new Region();
		region.setName("Name");
		savedRegion = regionDao.save(region);
	}

	@AfterEach
	void tearDown() throws Exception {
		regionDao.delete(savedRegion.getId());
	}

	@Test
	void insertTest() {
		Region region = new Region();
		region.setName("New region");
		int size = regionDao.getAll().size();
		Region saved = regionDao.save(region);
		assertEquals(size+1, regionDao.getAll().size());
		assertNotNull(saved.getId());
		assertEquals(region.getName(), saved.getName());
		regionDao.delete(saved.getId());
		assertThrows(NullPointerException.class,() -> regionDao.save(null), "Region cannot be null");
		assertThrows(NullPointerException.class,() -> regionDao.save(new Region((long)0,null)), "Region name cannot be null");
	}

	@Test
	void updateTest() {
		Region updated = new Region(savedRegion.getId(),"Changed name");
		int size = regionDao.getAll().size();
		regionDao.save(updated);
		assertEquals(size, regionDao.getAll().size());
		Region fromDB = regionDao.getById(updated.getId());
		assertEquals(updated.getId(), fromDB.getId());
		assertEquals(updated.getName(), fromDB.getName());
		assertThrows(NullPointerException.class,() -> regionDao.save(null), "Region cannot be null");
		assertThrows(NullPointerException.class,() -> regionDao.save(new Region((long)1,null)), "Region name cannot be null");
	}
	
	@Test
	void getAllTest() {
		List<Region> list = regionDao.getAll();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertNotNull(list.get(0));
	}
	
	@Test
	void getByIdTest() {
		Region fromDB = regionDao.getById(savedRegion.getId());
		
		assertEquals(savedRegion.getId(), fromDB.getId());
		assertEquals(savedRegion.getName(), fromDB.getName());
		
		assertThrows(EmptyResultDataAccessException.class,()->regionDao.getById((long) -1));
	}
	
}
