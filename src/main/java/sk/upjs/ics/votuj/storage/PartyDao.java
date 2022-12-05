package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface PartyDao {

	Party save(Party party);

	boolean delete(Long id);
	
	List<Party> getAll();
	
	Party getById(Long id);
}
