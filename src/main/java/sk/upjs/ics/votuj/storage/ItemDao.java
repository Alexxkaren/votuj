package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemDao {

	Item save(Item item) throws NoSuchElementException,NullPointerException;
	
	boolean delete(Long id);

	List<Item> getByProgram(Program program);

	List<Item> getByProgramCategory(Program program, Category category);
	
	Item getById(Long id);

	List<Item> getByTerm(Term term);

	List<Item> getByTermParty(Term term, Party party);

	List<Item> getByTermPartyCategory(Term term, Party party, Category category);
}
