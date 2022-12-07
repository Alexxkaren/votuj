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
		party.setInfo(party.getInfo());
		List<Program> list_p = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
		programs = FXCollections.observableArrayList(list_p);

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

	public ObservableList<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(ObservableList<Candidate> candidates) {
		this.candidates = candidates;
	}

	public Party getParty() {
		return new Party(id, getName(), getInfo());
	}
}