package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.CategoryDao;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;

public class CategoryFxModel {

	private Category category;
	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private Map<Category, BooleanProperty> categoryMap = new HashMap<>();
	List<Category> allCategories;
	private CategoryDao categoryDao;
	private ObservableList<Item> items;

	public CategoryFxModel() {
		categoryDao = DaoFactory.INSTANCE.getCategoryDao();
		allCategories = categoryDao.getAll();
		for (Category c : allCategories) {
			categoryMap.put(c, new SimpleBooleanProperty());
		}
	}

	public CategoryFxModel(Category category) {
		categoryDao = DaoFactory.INSTANCE.getCategoryDao();
		allCategories = categoryDao.getAll();
		this.id = category.getId();
		setName(category.getName());
		setItems(items);
		for (Category c : allCategories) {
			categoryMap.put(c, new SimpleBooleanProperty());
		}
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

	public Map<Category, BooleanProperty> getCategories() {
		return categoryMap;
	}

	public Category getCategory() {
		return new Category(id, getName());
	}
}
