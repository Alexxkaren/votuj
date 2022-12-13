package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface RegionDao {
	
	Region getById(Long Id);
	
	List<Region> getAll();
	

}
