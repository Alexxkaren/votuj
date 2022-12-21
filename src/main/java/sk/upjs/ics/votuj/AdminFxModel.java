package sk.upjs.ics.votuj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.ics.votuj.storage.Admin;

public class AdminFxModel {
	
	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();
	
	
	public AdminFxModel() {
	}


	public AdminFxModel(Admin admin) {
		this.id = admin.getId();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public StringProperty getName() {
		return name;
	}


	public void setName(StringProperty name) {
		this.name = name;
	}


	public StringProperty getPassword() {
		return password;
	}


	public void setPassword(StringProperty password) {
		this.password = password;
	}
}
