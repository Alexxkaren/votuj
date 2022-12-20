package sk.upjs.ics.votuj.storage;

import java.util.NoSuchElementException;

public interface AdminDao {

	Admin getByName(String name) throws NoSuchElementException;

	Admin save(Admin admin) throws NoSuchElementException, NullPointerException;

	boolean delete(Long id) throws ObjectUndeletableException;
}
