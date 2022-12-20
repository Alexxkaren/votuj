package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Vote;

public class ChoiceController {

	private Vote vote;
	private CategoryFxModel categoryFxModel;
	private PartyFxModel partyFxModel;
	private Map<Category, BooleanProperty> selectedCategories;
	private Map<Party, BooleanProperty> selectedParties;
	private List<Category> selectedCategoriesList;
	private List<Party> selectedPartiesList;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private FlowPane categoryPane;

	@FXML
	private FlowPane partyPane;

	public ChoiceController(Vote vote) {
		this.vote = vote;
		categoryFxModel = new CategoryFxModel();
		partyFxModel = new PartyFxModel();
	}

	@FXML
	void initialize() {
		selectedCategoriesList = new ArrayList<>();
		selectedPartiesList = new ArrayList<>();
		
		for (Entry<Category, BooleanProperty> pair : categoryFxModel.getCategories().entrySet()) {
			CheckBox checkBox = new CheckBox(pair.getKey().toString()); // to tring da rovno id meno aj priezvisko
			checkBox.selectedProperty().bindBidirectional(pair.getValue());
			categoryPane.getChildren().add(checkBox);
		}

		System.out.println(categoryFxModel.getCategories().toString());

		for (Entry<Party, BooleanProperty> pair : partyFxModel.getParties().entrySet()) {
			CheckBox checkBox = new CheckBox(pair.getKey().toString()); // to tring da rovno id meno aj priezvisko
			checkBox.selectedProperty().bindBidirectional(pair.getValue());
			partyPane.getChildren().add(checkBox);
		}
		System.out.println(partyFxModel.getParties().toString());
	}

	@FXML
	void allCategoriesButtonClick(ActionEvent event) {
		for (Entry<Category, BooleanProperty> pair : categoryFxModel.getCategories().entrySet()) {
			pair.getValue().set(true);
		}
	}

	@FXML
	void allPartiesButtonClick(ActionEvent event) {
		for (Entry<Party, BooleanProperty> pair : partyFxModel.getParties().entrySet()) {
			pair.getValue().set(true);
		}
	}

	@FXML
	void filtrationButtonClick(ActionEvent event) {
		selectedCategories = categoryFxModel.getCategories();
		selectedParties = partyFxModel.getParties();
		
		for (Category key : selectedCategories.keySet()) {
			if (selectedCategories.get(key).get()) {
				selectedCategoriesList.add(key);
			}
		}
		
		for (Party key : selectedParties.keySet()) {
			if (selectedParties.get(key).get()) {
				selectedPartiesList.add(key);
			}
		}
		
		System.out.println(selectedCategoriesList);
		System.out.println(selectedPartiesList);
		
		if(selectedCategoriesList.size()==0 || selectedPartiesList.size()==0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna volebná strana nebola vybraná alebo žiadna kategória nebola vybraná. Urobte výber.");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
		
		//ComparisonController controller = new ComparisonController(vote, selectedCategories, selectedParties);
		ComparisonController controller = new ComparisonController(vote, selectedCategoriesList, selectedPartiesList);
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("comparison.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();

			scene.getStylesheets().add(css);
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);

			stage.setTitle("Porovnanie kategórií a bodov");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();

			categoryPane.getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
