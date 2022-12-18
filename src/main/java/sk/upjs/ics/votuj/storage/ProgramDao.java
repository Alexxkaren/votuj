package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProgramDao {

	Program save(Program program)throws NoSuchElementException,NullPointerException; // NAIMPLEMENTOVANE
	
	boolean delete(Long id)throws ObjectUndeletableException;// NAIMPLEMENTOVANE

	List<Program> getByParty(Party party); // NAIMPLEMENTOVANE

	List<Program> getByTermParty(Term term, Party partry); // NAIMPLEMENTOVANE
	
	Program getById(Long id); // NAIMPLEMENTOVANE

}
