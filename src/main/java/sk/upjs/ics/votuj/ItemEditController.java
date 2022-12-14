package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.ObjectUndeletableException;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class ItemEditController {
	
	private Program program;
	private Item savedItem;
	private Item item;
	private ItemFxModel itemFxModel;
	private ObservableList<Category> categoriesModel;
	private ObservableList<Category> selectedCategoriesModel = FXCollections
			.observableArrayList(new ArrayList<Category>());
	private Stage stage;
	private List<Category> categoriesToDelete = new ArrayList<>(); // TOTO NETREBA PTM VYMAZ
	private List<Category> listOfSelectedCategories = new ArrayList<>();
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

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

		if (item != null) {
			System.out.println("tento item.get cat je problem:");
			System.out.println(item.toString());
			System.out.println(item.getCategories().toString());
			listOfSelectedCategories = item.getCategories();
		} else {
			// listOfSelectedCategories ;
		}

		
		selectedCategoriesModel.setAll(listOfSelectedCategories);
		selectedCategoriesListView.setItems(selectedCategoriesModel);

	}

	@FXML
	void addCategoryButtonClick(ActionEvent event) {
		Category category = itemCategoryComboBox.getSelectionModel().getSelectedItem();
		if (category != null) {
			if (!listOfSelectedCategories.contains(category)) {
				listOfSelectedCategories.add(category);
				selectedCategoriesModel.setAll(listOfSelectedCategories);
				selectedCategoriesListView.setItems(selectedCategoriesModel);
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("??iadna kateg??ria nie je vybran??, vyberte pros??m kateg??riu");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	void deleteAddedCategoryButtonClick(ActionEvent event) {
		Category cat = selectedCategoriesListView.getSelectionModel().getSelectedItem();
		if (cat != null) {
			listOfSelectedCategories.remove(cat);
			selectedCategoriesModel.setAll(listOfSelectedCategories);
			selectedCategoriesListView.setItems(selectedCategoriesModel);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(
					"??iadna kateg??ria nie je vybran??, vyberte pros??m kateg??riu, ktor?? chcete odstr??ni?? z vybran??ch");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	
	void deleteCategoryButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		boolean successful = false;
		Category category = itemCategoryComboBox.getSelectionModel().getSelectedItem();
		List<Category> categories = new ArrayList<>();
		if (category != null) {
			if (!listOfSelectedCategories.contains(category)) {
				try {
					Alert alertW = new Alert(AlertType.WARNING);
					alertW.setTitle("Upozornenie!");
					alertW.setHeaderText("Potvrdenie vymazania");
					alertW.setContentText("Kateg??ria s n??zvom: " + category.getName() + " a id: " + category.getId()
							+ " bude vymazan??. Naozaj chcete vymaza???");
					ButtonType btDelete = new ButtonType("Vymaza??");
					ButtonType btCancel = new ButtonType("Zru??i??", ButtonData.CANCEL_CLOSE);

					alertW.getButtonTypes().setAll(btDelete, btCancel);

					dialog = alertW.getDialogPane();
					dialog.getStylesheets().add(css);
					dialog.getStyleClass().add("dialog");

					Optional<ButtonType> result = alertW.showAndWait();
					if (result.get() == btDelete) {
						successful = DaoFactory.INSTANCE.getCategoryDao().delete(category.getId());
						categoriesModel.clear();
						categories = DaoFactory.INSTANCE.getCategoryDao().getAll();
						categoriesModel.addAll(categories);
						itemCategoryComboBox.setItems(categoriesModel);
					}

				} catch (ObjectUndeletableException e) {
					alert.setContentText(
							"Sna????te sa vymaza?? kateg??riu, ktor?? u?? obsahuje body - odstr????te najprv body ktor?? obsahuje");
					alert.show();
					// e.printStackTrace();
					return;
				}
			} else {
				alert.setContentText(
						"Kateg??ria ktor?? chcete odstr??ni?? je medzi vybran??mi kateg??riami. Odstr????te ju z vybran??ch kateg??ri?? a potom opakujte pokus o vymazanie.");
				alert.show();
				return;
			}
		} else {
			alert.setContentText("??iadna kateg??ria nie je vybran??, vyberte pros??m kateg??riu, ktor?? chcete odstr??ni??.");
			alert.show();
			return;
		}

		if (successful) {
			categoriesModel.clear();
			categories = DaoFactory.INSTANCE.getCategoryDao().getAll();
			categoriesModel.addAll(categories);
			itemCategoryComboBox.setItems(categoriesModel);
		}
	}

	@FXML
	void createCategoryButtonClick(ActionEvent event) {
		CategoryEditController controller = new CategoryEditController();
		showCategoryEdit(controller, "Prid??vanie novej kateg??rie");

	}

	@FXML
	void editCategoryButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Category category = itemCategoryComboBox.getSelectionModel().getSelectedItem();

		if (category != null) {
			if (!listOfSelectedCategories.contains(category)) {
				CategoryEditController controller = new CategoryEditController(category);
				showCategoryEdit(controller, "Editovanie kateg??rie");
			} else {
				alert.setContentText(
						"Kateg??ria, ktor?? chcete upravi?? je medzi vybran??mi kateg??riami. Najprv ju odstr????te z vybran??ch kateg??ri?? a potom ju editujte.");
				alert.show();
				return;
			}
		} else {
			alert.setContentText("??iadna kateg??ria nie je vybran??, vyberte pros??m kateg??riu, ktor?? chcete editova??.");
			alert.show();
			return;
		}
	}

	void showCategoryEdit(CategoryEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("categoryEdit.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();

			scene.getStylesheets().add(css);
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);

			stage.setTitle(sceneName);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			if (controller.getSavedCategory() != null) {
				categoriesModel.clear();
				List<Category> categories = DaoFactory.INSTANCE.getCategoryDao().getAll();
				categoriesModel.addAll(categories);
				itemCategoryComboBox.setItems(categoriesModel);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void saveItemButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");
		Item item = itemFxModel.getItem();
		item.setCategories(listOfSelectedCategories);

		System.out.println("UKLADAM ITEM TENTO:");
		System.out.println(item.toString());
		System.out.println("DO Programu TOHTO");
		System.out.println(item.getProgram());

		List<Category> categoriess = listOfSelectedCategories;

		if (item.getName() == null || item.getName().equals("")) {
			alert.setContentText("N??zov mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (item.getInfo() == null || item.getInfo().equals("")) {
			alert.setContentText("Info mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (item.getProgram() == null) {
			alert.setContentText("Program mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (categoriess.isEmpty()) {
			alert.setContentText("Nejak?? kateg??ria mus?? by?? medzi vybran??mi, pros??m dopl??te.");
			alert.show();
			return;
		}

		try {
			if (item != null) {
				System.out.println("posielam takyto item:");
				System.out.println(item.toString());
				System.out.println("s takymi to kategoriami:");
				System.out.println(categoriess.toString());
				savedItem = DaoFactory.INSTANCE.getItemDao().save(item, categoriess);
				System.out.println("sa ulo??il ITEMMM!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println("jeho meno: " + savedItem.getName());
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		itemNameTextField.getScene().getWindow().hide();
	}

	public Item getSavedItem() {
		return this.savedItem;
	}

}
