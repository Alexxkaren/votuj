package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;

public class PartyFxModel {

	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty info = new SimpleStringProperty();
	private ObservableList<Program> programs;
	private ObservableList<Candidate> candidates;

	public PartyFxModel() {
		programs = FXCollections.observableArrayList();
		candidates = FXCollections.observableArrayList();
	}

	public PartyFxModel(Party party) {
		this.id = party.getId();
		setName(party.getName());
		setInfo(party.getInfo());
		List<Program> list_p = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
		programs = FXCollections.observableArrayList(list_p);
		// TODO -- TU treba nejako spravit list kandidatov - podla toho na com sa dohodneme v party edit controlleri
		//List<Candidate> list_c = DaoFactory.INSTANCE.getPartyDao().ge;
		//candidates = FXCollections.observableArrayList(list_c);
		
		//TODO do niektoreho daa potrebujeme dat ze get all candidates alebo get cadidates by term 

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

	public StringProperty getInfoProperty() {
		return info;
	}

	public String getInfo() {
		return info.get();
	}

	public void setInfo(String info) {
		this.info.set(info);
	}

	public ObservableList<Program> getProgramsModel() {
		return programs;
	}

	public List<Program> getPrograms() {
		return new ArrayList<>(programs);
	}

	/*
	 * public void setPrograms(ObservableList<Program> programs) { this.programs =
	 * programs; }
	 */

	public ObservableList<Candidate> getCandidatesModel() {
		return candidates;
	}
	
	public List<Candidate> getCandidates() {
		return new ArrayList<>(candidates);
	}

	public void setCandidates(ObservableList<Candidate> candidates) {
		this.candidates = candidates;
	}

	public Party getParty() {
		return new Party(id, getName(), getInfo());
	}
}
