package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Program;

public class ItemFxModel {

	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty info = new SimpleStringProperty();
	private Program program;
	private ObservableList<Category> categories;
	private Item item;

	public ItemFxModel() {

	}

	public ItemFxModel(Item item) {
		this.id = item.getId();
		setName(item.getName());
		setInfo(item.getInfo());
		setProgram(program);
		List<Category> list_c = DaoFactory.INSTANCE.getCategoryDao().getByItem(item);
		categories = FXCollections.observableArrayList(list_c);
	}
	
	

	public Item getItem() {
		return new Item(id, getName(), getInfo(), getProgram());
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

	private Program getProgram() {
		return program;
	}
	private void setProgram(Program program) {
		this.program = program;
		
	}

	public ObservableList<Category> getCategoriesModel() {
		return categories;
	}
	
	public List<Category> getCategories() {
		return new ArrayList<>(categories);
	}

	public void setCandidates(ObservableList<Category> categories) {
		this.categories = categories;
	}
	


	

	

}