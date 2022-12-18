package sk.upjs.ics.votuj.storage;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemDao {

	Item save(Item item) throws NoSuchElementException,NullPointerException; // NAIMPLEMENTOVANE
	
	boolean delete(Long id) throws ObjectUndeletableException; // NAIMPLEMENTOVANE
 
	List<Item> getByProgram(Program program); // NAIMPLEMENTOVANE

	List<Item> getByProgramCategory(Program program, Category category); // NAIMPLEMENTOVANE
	
	Item getById(Long id); // NAIMPLEMENTOVANE

	List<Item> getByTerm(Term term); // NAIMPLEMENTOVANE

	List<Item> getByTermParty(Term term, Party party); // NAIMPLEMENTOVANE

	List<Item> getByTermPartyCategory(Term term, Party party, Category category); // NAIMPLEMENTOVANE
}
