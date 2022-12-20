package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface VoteDao {

	Vote save(Vote vote) throws NoSuchElementException, NullPointerException; // NAIMPLEMENTOVANE

	boolean delete(Long id) throws ObjectUndeletableException; // NAIMPLEMENTOVANE

	List<Vote> getByParty(Party party); // NAIMPLEMENTOVANE

	List<Vote> getAll(); // NAIMPLEMENTOVANE

	Vote getById(Long id);// NAIMPLEMENTOVANE

}
