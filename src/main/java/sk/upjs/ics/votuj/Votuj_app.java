package sk.upjs.ics.votuj;

import java.util.List;

import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Term;
import sk.upjs.ics.votuj.storage.TermDao;

public class Votuj_app {

	public static void main(String[] args) {
		//dam ctrl+1 a mi to spravi čo chcem -> lokálnu premennú 
		DaoFactory.INSTANCE.setTesting();
		TermDao termDao = DaoFactory.INSTANCE.getTermDao();
		List<Term> terms = termDao.getAll();
		System.out.println(terms);

	}

}
