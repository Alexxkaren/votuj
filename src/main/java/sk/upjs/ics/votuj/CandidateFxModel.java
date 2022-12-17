package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Term;

public class CandidateFxModel {
	
	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty surname = new SimpleStringProperty();
	private StringProperty candidateNumber = new SimpleStringProperty();
	private StringProperty info = new SimpleStringProperty();
	private Party party;
	private Term term;
	private ObservableList<Term> terms;
	//private Term term; //NEVIEM CI TU NEBUDE MUSIET BYT LIST!!!
	private Candidate candidate;
	
	public CandidateFxModel(Party party, Term term)  {
		this.term = term;
		this.party = party;
	}
	
	public CandidateFxModel (Candidate candidate, Party party) {
		this.party = party;
		this.id = candidate.getId();
		setName(candidate.getName());
		setSurname(candidate.getSurname());
		setCandidateNumber(candidate.getCandidateNumber());
		setInfo(candidate.getInfo());
		setParty(party);
		setTerms(terms);
		
	}
	
	public void setTerms(ObservableList<Term> terms) {
		this.terms = terms;
		
	}
	
	public List<Term> getTerms() {
		return new ArrayList<>(terms);
	}
	
	public ObservableList<Term> getTermsModel() {
		return terms;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public StringProperty getNameProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	
	public StringProperty getSurnameProperty() {
		return surname;
	}

	public String getSurname() {
		return surname.get();
	}
	
	public void setSurname(String name) {
		this.surname.set(name);
	}
	
	
	public StringProperty getCandidateNumberProperty() {
		return candidateNumber;
	}

	public String getCandidateNumber() {
		return candidateNumber.get();
	}
	
	public void setCandidateNumber(String candidateNumber) {
		this.candidateNumber.set(candidateNumber);
	}
	
	
	public StringProperty getInfoProperty() {
		return info;
	}

	public String getInfo() {
		return info.get();
	}
	
	public void setInfo(String info) {
		this.info.set(info);
	}
	
	
	public Party getParty() {
		return party;
	}
	
	public void setParty(Party party) {
		this.party = party;
	}
	
	
	
	public Candidate getCandidate() {
		return new Candidate(id, getName(), getSurname(),getCandidateNumber(), getInfo(),getParty(), getTerms());
	}

	
}
