package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Program;

public class ItemEditController {

	private Program program;
	private Item item;
	private ItemFxModel itemFxModel;
	private ObservableList<Category> categoriesModel;
	private ObservableList<Category> selectedCategoriesModel = FXCollections
			.observableArrayList(new ArrayList<Category>());
	private Stage stage;
	private List<Category> categoriesToDelete = new ArrayList<>();

	@FXML
	private ComboBox<Category> itemCategoryComboBox;

	@FXML
	private TextArea itemInfoTextArea;

	@FXML
	private TextField itemNameTextField;

	@FXML
	private ListView<Category> selectedCategoriesListView;

	public ItemEditController(Program program) {
		this.program = program;
		itemFxModel = new ItemFxModel(program);
	}

	public ItemEditController(Item item, Program program) {
		this.program = program;
		this.item = item;
		itemFxModel = new ItemFxModel(item, program);
	}

	@FXML
	void initialize() {
		itemInfoTextArea.textProperty().bindBidirectional(itemFxModel.getInfoProperty());
		itemNameTextField.textProperty().bindBidirectional(itemFxModel.getNameProperty());
		selectedCategoriesListView.setItems(selectedCategoriesModel);

		List<Category> categories = DaoFactory.INSTANCE.getCategoryDao().getAll();
		categoriesModel = FXCollections.observableArrayList(categories);
		itemCategoryComboBox.setItems(categoriesModel);

		// itemCategoryComboBox.getSelectionModel().selectFirst();

	}

	@FXML
	void addCategoryButtonClick(ActionEvent event) {
		Category category = itemCategoryComboBox.getSelectionModel().getSelectedItem();
		if (category != null) {
			if (!selectedCategoriesModel.contains(category)) {
				selectedCategoriesModel.add(category);
				selectedCategoriesListView.setItems(selectedCategoriesModel);
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna kategória nie je vybraná, vyberte prosím kategóriu");
			alert.show();
			return;
		}

	}

	@FXML
	// TODO nech sa stazuje ked neni nic vybrate
	void deleteCategoryButtonClick(ActionEvent event) {
		Category category = itemCategoryComboBox.getSelectionModel().getSelectedItem();
		if (category != null) {
			itemFxModel.getCategoriesModel().remove(category);
			if (category.getId() != null) {
				categoriesToDelete.add(category);
			}
		}
	}

	@FXML
	void createCategoryButtonClick(ActionEvent event) {
		CategoryEditController controller = new CategoryEditController();
		showCategoryEdit(controller, "Pridávanie novej kategórie");

	}

	@FXML
	void editCategoryButtonClick(ActionEvent event) {
		Category category = itemCategoryComboBox.getSelectionModel().getSelectedItem();
		if (category!=null) {
			CategoryEditController controller = new CategoryEditController(category);
			showCategoryEdit(controller, "Editovanie kategórie");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna kategoria nie je vybrana, vyberte prosím kategoriu");
			alert.show();
			return;
		}
	}

	@FXML
	void saveItemButtonClick(ActionEvent event) {

	}
	
	void showCategoryEdit(CategoryEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("categoryEdit.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();
			
			String css = this.getClass().getResource("votuj.css").toExternalForm();
			scene.getStylesheets().add(css); 
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);
			
			stage.setTitle(sceneName);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
