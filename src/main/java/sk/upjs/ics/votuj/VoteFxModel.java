package sk.upjs.ics.votuj;

import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Region;
import sk.upjs.ics.votuj.storage.Vote;

public class VoteFxModel {

	private Long id;
	private StringProperty age = new SimpleStringProperty();
	private BooleanProperty male = new SimpleBooleanProperty();
	private ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
	private Party party;
	private Region region;

	public VoteFxModel() {

	}

	public VoteFxModel(Vote vote) {
		this.id = vote.getId();
		this.party = vote.getParty();
		this.region = vote.getRegion();
		setAge(vote.getAge());
		setMale(vote.getMale());
		setDate(vote.getDate());

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StringProperty getAgeProperty() {
		return age;
	}

	public String getAge() {
		return age.get();
	}

	public void setAge(String age) {
		this.age.set(age);
	}


	public BooleanProperty getMaleProperty() {
		return male;
	}

	public Boolean getMale() {
		return male.get();
	}

	public void setMale(Boolean male) {
		this.male.set(male);

	}

	public ObjectProperty<LocalDateTime> dateProperty() {
		return date;
	}

	public LocalDateTime getDate() {
		return date.get();
	}

	public void setDate(LocalDateTime date) {
		this.date.set(date);
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Vote getVoteWithoutParty() {
		return new Vote(id, getAge(), getMale(), getDate(), getRegion(), getParty());
	}

	public Vote getVote() {
		return new Vote(id, getAge(), getMale(), getDate(), getRegion());
	}
}
