package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface RegionDao {

	Region save(Region region) throws NoSuchElementException, NullPointerException; 

	boolean delete(Long id) throws ObjectUndeletableException; 

	Region getById(Long Id); 

	List<Region> getAll(); 

}
