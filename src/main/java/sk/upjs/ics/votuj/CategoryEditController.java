package sk.upjs.ics.votuj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Category;

public class CategoryEditController {
	private Category category;
	private CategoryFxModel categoryFxModel;
	
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

	    }

}
