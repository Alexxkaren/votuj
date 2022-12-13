package sk.upjs.ics.votuj;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Term;

public class CandidateFxModel {
	
	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty surname = new SimpleStringProperty();
	private IntegerProperty candidateNumber = new SimpleIntegerProperty();
	private StringProperty info = new SimpleStringProperty();
	private Party party;
	private Term term; //NEVIEM CI TU NEBUDE MUSIET BYT LIST!!!
	private Candidate candidate;
	
	public CandidateFxModel()  {
		
	}
	
	public CandidateFxModel (Candidate candidate) {
		this.id = candidate.getId();
		setName(candidate.getName());
		setSurname(candidate.getSurname());
		setCandidateNumber(candidate.getCandidate_number());
		setInfo(candidate.getInfo());
		setParty(party);
		
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
	
	
	public IntegerProperty getCandidateNumberProperty() {
		return candidateNumber;
	}

	public Integer getCandidateNumber() {
		return candidateNumber.get();
	}
	
	public void setCandidateNumber(int candidateNumber) {
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
		return new Candidate(id, getName(), getSurname(),getCandidateNumber(), getInfo(),getParty());
	}
}
