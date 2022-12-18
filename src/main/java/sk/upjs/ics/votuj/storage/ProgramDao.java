package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProgramDao {

	Program save(Program program)throws NoSuchElementException,NullPointerException;
	
	boolean delete(Long id);

	List<Program> getByParty(Party party); //naimplementovane

	List<Program> getByTermParty(Term term, Party partry);
	
	Program getById(Long id);

}
