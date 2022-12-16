package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class ProgramFxModel {
	
	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private Party party;
	private BooleanProperty isActive = new SimpleBooleanProperty();
	private Term term;
	private ObservableList<Item> items;
	
	
	public ProgramFxModel(Party party) {
		this.party = party;

	}

	public ProgramFxModel(Program program, Party party) {
		this.party = party;
		this.id = program.getId();
		setName(program.getName());
		setIsActive(program.isActive());
		setParty(party);
		setTerm(term);
		setItems(items);
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
	///
	public BooleanProperty getIsActiveProperty() {
		return isActive;
	}

	public boolean getIsActive() {
		return isActive.get();
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive.set(isActive);
		
	}
	///
	
	public Party getParty() {
		return party;
	}
	
	public void setParty(Party party) {
		this.party = party;
	}
	
	public Term getTerm() {
		return term;
	}
	
	public void setTerm(Term term) {
		this.term = term;
	}
	
	public void setItems(ObservableList<Item> items) {
		this.items = items;
		
	}
	
	public List<Item> getItems() {
		return new ArrayList<>(items);
	}
	
	public ObservableList<Item> getItemsModel() {
		return items;
	}
	
	public Program getProgram() {
		return new Program(id, getName(), getParty(), getIsActive(),getTerm());
	//POROZMYSLAJ CI TAM NETREBA KU TOMU KONSTRUKTORU PROGRAMU PRIDAT AJ 
		//LIST ITEMOV
	}

}
