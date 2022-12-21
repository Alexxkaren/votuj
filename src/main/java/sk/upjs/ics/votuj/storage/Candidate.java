package sk.upjs.ics.votuj.storage;

import java.util.ArrayList;
import java.util.List;

public class Candidate {

	private Long id;
	private String name;
	private String surname;
	private String candidateNumber;
	private String info;
	private Party party;
	private List<Term> terms;

	public Candidate() {
	}

	public Candidate(Long id, String name, String surname, String candidateNumber, String info, Party party,
			List<Term> terms) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.candidateNumber = candidateNumber;
		this.info = info;
		this.party = party;
	
		this.terms = terms;
	}

	public Candidate(Long id, String name, String surname, String candidateNumber, String info, Party party) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.candidateNumber = candidateNumber;
		this.info = info;
		this.party = party;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	public void setCandidate_number(String candidate_number) {
		this.candidateNumber = candidate_number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCandidateNumber() {
		return candidateNumber;
	}

	public void setCandidateNumber(String candidateNumber) {
		this.candidateNumber = candidateNumber;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	@Override
	public String toString() {
		return  name + " " + surname;
	}

}
