package sk.upjs.ics.votuj;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sk.upjs.ics.votuj.storage.Term;

public class TermFxModel {

	private Long id;
	private IntegerProperty since = new SimpleIntegerProperty();
	private IntegerProperty to = new SimpleIntegerProperty();

	public TermFxModel() {

	}

	public TermFxModel(Term term) {
		this.id = term.getId();
		setSince(term.getSince());
		setTo(term.getTo());
	}
	
	public Term getTerm(){
		return new Term(id, getSince(), getTo());
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getSince() {
		return this.since.get();
	}
	
	public IntegerProperty getSinceProperty() {
		return since;
	}

	public void setSince(Integer since) {
		this.since.set(since);
		
	}
	
	public Integer getTo() {
		return this.to.get();
	}
	
	public IntegerProperty getToProperty() {
		return to;
	}

	public void setTo(Integer to) {
		this.to.set(to);
		
	}

}
