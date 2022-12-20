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
	// TO DO DO BUDUCNA -- AK budeme mat cas tak poupratvat duplicitny a triplicitny
	// kod do samostatnych metod!!
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

		// listOfSelectedCategories = item.getCategories();
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
			alert.setContentText("Žiadna kategória nie je vybraná, vyberte prosím kategóriu");
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
					"Žiadna kategória nie je vybraná, vyberte prosím kategóriu, ktorú chcete odstrániť z vybraných");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	// sa bude mazat rovno
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
					alertW.setContentText("Kategória s názvom: " + category.getName() + " a id: " + category.getId()
							+ " bude vymazaná. Naozaj chcete vymazať?");
					ButtonType btDelete = new ButtonType("Vymazať");
					ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

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
							"Snažíte sa vymazať kategóriu, ktorá už obsahuje body - odstráňte najprv body ktoré obsahuje");
					alert.show();
					// e.printStackTrace();
					return;
				}
			} else {
				alert.setContentText(
						"Kategória ktorú chcete odstrániť je medzi vybranými kategóriami. Odstráňte ju z vybraných kategórií a potom opakujte pokus o vymazanie.");
				alert.show();
				return;
			}
		} else {
			alert.setContentText("Žiadna kategória nie je vybraná, vyberte prosím kategóriu, ktorú chcete odstrániť.");
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
		showCategoryEdit(controller, "Pridávanie novej kategórie");

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
				showCategoryEdit(controller, "Editovanie kategórie");
			} else {
				alert.setContentText(
						"Kategória, ktorú chcete upraviť je medzi vybranými kategóriami. Najprv ju odstráňte z vybraných kategórií a potom ju editujte.");
				alert.show();
				return;
			}
		} else {
			alert.setContentText("Žiadna kategória nie je vybraná, vyberte prosím kategóriu, ktorú chcete editovať.");
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
				// UVIDIME CI TOTO
				// itemCategoryComboBox.getSelectionModel().select(controller.getSavedCategory());

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
			alert.setContentText("Názov musí byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}
		if (item.getInfo() == null || item.getInfo().equals("")) {
			alert.setContentText("Info musí byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}
		if (item.getProgram() == null) {
			alert.setContentText("Program musí byť vyplnený, prosím doplňte.");
			alert.show();
			return;
		}
		if (categoriess.isEmpty()) {
			alert.setContentText("Nejaká kategória musí byť medzi vybranými, prosím doplňte.");
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
				System.out.println("sa uložil ITEMMM!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
