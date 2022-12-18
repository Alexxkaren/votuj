package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface PartyDao {

	Party save(Party party) throws NoSuchElementException,NullPointerException;

	boolean delete(Long id);

	List<Party> getAll() throws NoSuchElementException; // naimplementovane

	Party getById(Long id);
}
