package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;
import sk.upjs.ics.votuj.storage.Vote;

public class ComparisonController { /////////// TABULKA JE ZLEEEE!!!!

	private Vote vote;
	// private Map<Category, BooleanProperty> inheritedCategories;
	// private Map<Party, BooleanProperty> inheritedParties;
	private List<Category> selectedCategories;
	private List<Party> selectedParties;
	private ObservableList<Party> partiesModel;
	private ObservableList<Item> itemsModel;
	private Party partyWatched;
	private Program activeProgram;

	private Stage stage;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private TableView<Item> itemTableView;

	@FXML
	private ComboBox<Party> choosePartyComboBox;
	/*
	 * public ComparisonController(Vote vote, Map<Category, BooleanProperty> cats,
	 * Map<Party, BooleanProperty> parts) { this.vote = vote;
	 * this.inheritedCategories = cats; this.inheritedParties = parts; }
	 */

	public ComparisonController(Vote vote, List<Category> cats, List<Party> parts) {
		this.vote = vote;
		this.selectedCategories = cats;
		this.selectedParties = parts;
	}

	@FXML
	void initialize() {
		/*
		 * selectedCategories = new ArrayList<>(); selectedParties = new ArrayList<>();
		 * 
		 * for (Category key : inheritedCategories.keySet()) { if
		 * (inheritedCategories.get(key).get()) { selectedCategories.add(key); } }
		 * 
		 * for (Party key : inheritedParties.keySet()) { if
		 * (inheritedParties.get(key).get()) { selectedParties.add(key); } }
		 */

		List<Party> parties = selectedParties;
		partiesModel = FXCollections.observableArrayList(parties);
		choosePartyComboBox.setItems(partiesModel);
		choosePartyComboBox.getSelectionModel().selectFirst();
		partyWatched = choosePartyComboBox.getSelectionModel().getSelectedItem();

		activeProgram = null;
		List<Program> allPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(partyWatched);
		for (Program p : allPrograms) {
			if (p.isActive()) {
				activeProgram = p;
			}
		}

		choosePartyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Party>() {
			@Override
			public void changed(ObservableValue<? extends Party> observable, Party oldValue, Party newValue) {
				if (newValue != null) {
					partyWatched = choosePartyComboBox.getSelectionModel().getSelectedItem();
					updateItemTableView(partyWatched);
				}
			}
		});

		if (partyWatched != null) {
			TableColumn<Item, String> categoryColumn = new TableColumn<>("Kategória");
			categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
			itemTableView.getColumns().add(categoryColumn);

			TableColumn<Item, String> itemColumn = new TableColumn<>("Bod");
			itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("info"));
			itemTableView.getColumns().add(itemColumn);

			Party party = choosePartyComboBox.getSelectionModel().getSelectedItem();
			List<Item> list_i = new ArrayList<>();
			if (party != null) {
				for (Category c : selectedCategories) {
					list_i.addAll(DaoFactory.INSTANCE.getItemDao().getByProgramCategory(activeProgram, c));
				}
			}

			itemsModel = FXCollections.observableArrayList(list_i);
			itemTableView.setItems(itemsModel);
		}
	}

	void updateItemTableView(Party party) {

		activeProgram = null;
		List<Program> allPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(partyWatched);
		for (Program p : allPrograms) {
			if (p.isActive()) {
				activeProgram = p;
			}
		}

		List<Item> list_i = new ArrayList<>();
		if (partyWatched != null && activeProgram != null) {
			for (Category c : selectedCategories) {
				list_i.addAll(DaoFactory.INSTANCE.getItemDao().getByProgramCategory(activeProgram, c));
				System.out.println("List kategorii a bodov vybranej strany:");
				System.out.println(list_i.toString());
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Upozornenie!");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.setContentText("Nie je vybraná strana alebo strana nemá aktívny politický program");
			alert.show();
		}
		itemsModel.clear();
		itemsModel.setAll(list_i);
		itemTableView.setItems(itemsModel);

	}

	@FXML
	void allInfoButtonClick(ActionEvent event) {
		if (partyWatched != null) {
			PartyViewController controller = new PartyViewController(partyWatched, true);
			showNextWindow(controller, "Náhľad politickej strany", "partyView.fxml");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna politická strana nie je vybraná");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
	}

	@FXML
	void submitVoteButtonClick(ActionEvent event) {
		if (partyWatched != null) {
			VotingController controller = new VotingController(vote);
			showNextWindow(controller, "Hlasovanie", "voting.fxml");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna politická strana nie je vybraná");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
	}

	void showNextWindow(Object controller, String sceneName, String resource) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
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

			if (controller.getClass() == VotingController.class) {
				stage.show();
				itemTableView.getScene().getWindow().hide();
			} else {
				stage.showAndWait();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
