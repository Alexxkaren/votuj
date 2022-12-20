package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface RegionDao {

	Region save(Region region) throws NoSuchElementException, NullPointerException; // NAIMPLEMENTOVANE

	boolean delete(Long id) throws ObjectUndeletableException; // NAIMPLEMENTOVANE

	Region getById(Long Id); // NAIMPLEMENTOVANE

	List<Region> getAll(); // NAIMPLEMENTOVANE

}
