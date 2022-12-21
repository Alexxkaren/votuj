package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface PartyDao {

	Party save(Party party) throws NoSuchElementException, NullPointerException;

	boolean delete(Long id) throws ObjectUndeletableException; 

	List<Party> getAll() throws NoSuchElementException; 

	Party getById(Long id); 

}
