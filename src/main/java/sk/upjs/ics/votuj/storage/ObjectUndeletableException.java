package sk.upjs.ics.votuj.storage;

public class ObjectUndeletableException extends RuntimeException {

	private static final long serialVersionUID = 6310395448839444909L;

	public ObjectUndeletableException(String info) {
		super(info);
	}

}
