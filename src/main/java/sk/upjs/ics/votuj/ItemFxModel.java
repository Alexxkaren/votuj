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
import sk.upjs.ics.votuj.storage.Term;

public class ItemFxModel {

	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private StringProperty info = new SimpleStringProperty();
	private Program program;
	private ObservableList<Category> categories;
	private Item item;
	private ObservableList<Category> selectedCategories;

	public ItemFxModel(Program program) {
		this.program = program;
	}

	public ItemFxModel(Item item, Program program) {
		this.program = program;
		this.id = item.getId();
		setName(item.getName());
		setInfo(item.getInfo());
		setProgram(program);

		List<Category> list_c = DaoFactory.INSTANCE.getCategoryDao().getByItem(item);
		this.categories = FXCollections.observableArrayList(list_c);
		setCategories(categories);
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

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;

	}

	public ObservableList<Category> getCategoriesModel() {
		return categories;
	}

	public List<Category> getCategories() {
		return new ArrayList<>(categories);
	}

	public void setCategories(ObservableList<Category> categories) {
		this.categories = categories;
	}

}
