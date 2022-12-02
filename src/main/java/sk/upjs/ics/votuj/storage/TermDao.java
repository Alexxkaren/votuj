package sk.upjs.ics.votuj.storage;

import java.util.List;

public interface TermDao {

	Term save(Term term);

	boolean delete(Long id);

	Term getByPeriod(Integer since, Integer to);

	List<Term> getAll(Term term);

}
