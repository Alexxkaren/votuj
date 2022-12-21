package sk.upjs.ics.votuj;

import java.util.NoSuchElementException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.DaoFactory;

public class CategoryEditController {
	private Category category;
	private Category savedCategory;
	private CategoryFxModel categoryFxModel;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private TextField categoryNameTextField;

	public CategoryEditController() {
		categoryFxModel = new CategoryFxModel();
	}

	public CategoryEditController(Category category) {
		this.category = category;
		categoryFxModel = new CategoryFxModel(category);
	}

	@FXML
	void initialize() {
		categoryNameTextField.textProperty().bindBidirectional(categoryFxModel.getNameProperty());

	}

	@FXML
	void saveCategoryButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Category category = categoryFxModel.getCategory();
		if (category.getName() == null || category.getName().equals("")) {
			alert.setContentText("Názov musí byť vyplnený, prosím doplňte ho.");
			alert.show();
			return;
		}

		try {
			if (category != null) {
				savedCategory = DaoFactory.INSTANCE.getCategoryDao().save(category);
			}
		} catch (NoSuchElementException e) { 
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		categoryNameTextField.getScene().getWindow().hide();

	}

	public Category getSavedCategory() {
		return this.savedCategory;
	}

}
