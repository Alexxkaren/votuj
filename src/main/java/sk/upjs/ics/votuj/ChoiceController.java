package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

		ComparisonController controller = new ComparisonController(vote, selectedCategories, selectedParties);
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
			stage.showAndWait();

			//categoryPane.getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
