package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Term;

public class CategoryFxModel {
	
	private Category category;
	private Long id;
	private StringProperty name = new SimpleStringProperty();
	
	//idk ci toto treba:
	private ObservableList<Item> items;

	public CategoryFxModel() {

	}
	
	public CategoryFxModel(Category category) {
		this.id = category.getId();
		setName(category.getName());
		setItems(items);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public String getName() {
		return name.get();
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public void setName(String name) {
		this.name.set(name);
		
	}
	
	public Category getCategory() {
		return new Category(id, getName());
	}

	
	
	
	
	

}
