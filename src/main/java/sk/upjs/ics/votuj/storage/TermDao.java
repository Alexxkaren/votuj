package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface TermDao {

	Term save(Term term);

	boolean delete(Long id);

	Term getById(Long id);

	List<Term> getAll();

}
