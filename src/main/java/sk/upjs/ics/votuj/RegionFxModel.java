package sk.upjs.ics.votuj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.ics.votuj.storage.Region;

public class RegionFxModel {

	private Long id;
	private StringProperty name = new SimpleStringProperty();

	public RegionFxModel() {

	}

	public RegionFxModel(Region region) {
		this.id = region.getId();
		setName(region.getName());

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

	public Region getRegion() {
		return new Region(id, getName());
	}

}
