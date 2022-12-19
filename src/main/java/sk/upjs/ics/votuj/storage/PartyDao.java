package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface PartyDao {

	Party save(Party party) throws NoSuchElementException,NullPointerException; // NAIMPLEMENTOVANE

	boolean delete(Long id) throws ObjectUndeletableException; // NAIMPLEMENTOVANE

	List<Party> getAll() throws NoSuchElementException; // naimplementovane

	Party getById(Long id); // NAIMPLEMENTOVANE

}
