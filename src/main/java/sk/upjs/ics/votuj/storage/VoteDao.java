package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface VoteDao {

	Vote save(Vote vote) throws NoSuchElementException, NullPointerException; 

	boolean delete(Long id) throws ObjectUndeletableException; 

	List<Vote> getByParty(Party party);

	List<Vote> getAll(); 

	Vote getById(Long id);

}
