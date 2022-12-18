package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.ObjectUndeletableException;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;
import sk.upjs.ics.votuj.storage.TermDao;

public class PartyEditController {

	private Party party;
	private PartyFxModel partyFxModel;
	// private CandidateFxModel candidateFxModel;
	private ObservableList<Candidate> candidatesModel;
	private ObservableList<Program> programsModel;
	private ObservableList<Item> itemsModel;
	private ObservableList<Term> termsModel;
	private Term termWatched;
	private Stage stage;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private ListView<Candidate> candidatesListView;

	@FXML
	private TextArea partyInfoTextArea;

	@FXML
	private TextField partyNameTextField;

	@FXML
	private TableView<Item> itemsTableView;

	@FXML
	private ListView<Program> programsListView;

	@FXML
	private ComboBox<Term> termsComboBox;

	public PartyEditController() {
		partyFxModel = new PartyFxModel();
	}

	public PartyEditController(Party party) {
		this.party = party;
		partyFxModel = new PartyFxModel(party);
	}

	@FXML
	void initialize() {
		partyInfoTextArea.textProperty().bindBidirectional(partyFxModel.getInfoProperty());
		partyNameTextField.textProperty().bindBidirectional(partyFxModel.getNameProperty());

		List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
		termsModel = FXCollections.observableArrayList(terms);
		termsComboBox.setItems(termsModel);

		termsComboBox.getSelectionModel().selectFirst();

		List<Program> list_p = new ArrayList<>();
		if (party != null) {
			list_p = DaoFactory.INSTANCE.getProgramDao()
					.getByTermParty(termsComboBox.getSelectionModel().getSelectedItem(), party);
		}
		programsModel = FXCollections.observableArrayList(list_p);
		programsListView.setItems(programsModel);

		if (party != null) {
			List<Candidate> list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party,
					termsComboBox.getSelectionModel().getSelectedItem());
			candidatesModel = FXCollections.observableArrayList(list_c);
		} else {
			candidatesModel = partyFxModel.getCandidatesModel();
		}

		candidatesListView.setItems(candidatesModel);
		termsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Term>() {

			@Override
			public void changed(ObservableValue<? extends Term> observable, Term oldValue, Term newValue) {
				if (newValue != null) {
					termWatched = termsComboBox.getSelectionModel().getSelectedItem();
					updateCandidatesListView(termWatched);
					updateProgramsListview(termWatched);
					updateItemTableView(termWatched);
				}
			}
		});

		TableColumn<Item, String> categoryColumn = new TableColumn<>("Kategória");
		categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
		itemsTableView.getColumns().add(categoryColumn);

		TableColumn<Item, String> itemColumn = new TableColumn<>("Bod");
		itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("info"));
		itemsTableView.getColumns().add(itemColumn);

		Term term = termsComboBox.getSelectionModel().getSelectedItem();
		List<Item> list_i = new ArrayList<>();
		if (party != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(term, party);
		}
		itemsModel = FXCollections.observableArrayList(list_i);
		itemsTableView.setItems(itemsModel);
	};

	void updateCandidatesListView(Term term) {
		List<Candidate> list_c = new ArrayList<>();
		if (party != null) {
			list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party, term);
		}
		candidatesModel = FXCollections.observableArrayList(list_c);
		candidatesListView.setItems(candidatesModel);

	}

	void updateProgramsListview(Term term) {
		List<Program> list_p = new ArrayList<>();
		if (party != null) {
			list_p = DaoFactory.INSTANCE.getProgramDao().getByTermParty(term, party);
		}
		programsModel = FXCollections.observableArrayList(list_p);
		programsListView.setItems(programsModel);

	}

	void updateItemTableView(Term term) {
		List<Item> list_i = new ArrayList<>();
		if (party != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(term, party);
		}
		itemsModel.setAll(list_i);

	}

	@FXML
	void addTermButtonClick(ActionEvent event) {
		TermEditController controller = new TermEditController();
		showTermEdit(controller, "Pridávanie volebného obdobia");
	}

	@FXML
	void editTermButtonClick(ActionEvent event) {
		Term selectedTerm = termsComboBox.getSelectionModel().getSelectedItem();
		TermEditController controller = new TermEditController(selectedTerm);
		showTermEdit(controller, "Editovanie volebného obdobia");
	}

	@FXML
	void deleteTermButtonClick(ActionEvent event) {
		Term term = termsComboBox.getSelectionModel().getSelectedItem();
		if (term != null) {
			try {
				DaoFactory.INSTANCE.getTermDao().delete(term.getId());
			} catch (ObjectUndeletableException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Snažíte sa vymazať volebné obdobie, ktoré je už používané");
				dialog = alert.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");
				alert.show();
				e.printStackTrace();
				return;
				
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadne volebné obdobie nie je vybrané na vymazanie");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	void showTermEdit(TermEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("termEdit.fxml"));
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
			
			if (controller.getSavedTerm() !=null) {
				System.out.println("TUUUUUUU SMEEEEEEEEE");
				termsModel.clear();
				List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
				termsModel.addAll(terms);
				termsComboBox.setItems(termsModel);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////

	@FXML
	void addCandidateButtonClick(ActionEvent event) {
		Term term = termsComboBox.getSelectionModel().getSelectedItem();
		CandidateEditController controller = new CandidateEditController(party, term);
		showCandidateEdit(controller, "Pridávanie nového kandidáta");
		// TU alert netreba party bude furt vybrana
	}

	@FXML
	void editCandidateButtonClick(ActionEvent event) {
		Candidate candidate = candidatesListView.getSelectionModel().getSelectedItem();
		if (candidate != null) {
			CandidateEditController controller = new CandidateEditController(candidate, party);
			showCandidateEdit(controller, "Editovanie kandidáta");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiaden kandidát nie je vybraný, vyberte prosím kandidáta");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	void deleteCandidateButtonClick(ActionEvent event) {
		// TODO
	}

	void showCandidateEdit(CandidateEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("candidateEdit.fxml"));
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

	////////////////////////////////////////////////////////////////

	@FXML
	void addItemButtonclick(ActionEvent event) {
		Program program = programsListView.getSelectionModel().getSelectedItem();
		if (program != null) {
			ItemEditController controller = new ItemEditController(program);
			showItemEdit(controller, "Pridávanie nového bodu volebného programu");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiaden program nie je vybraný, bod musíte priradiť programu, vyberte prosím program");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	void deleteItemButtonClick(ActionEvent event) {
		// TODO
	}

	@FXML
	void editItemButtonclick(ActionEvent event) {
		Item item = itemsTableView.getSelectionModel().getSelectedItem();
		if (item != null) {
			ItemEditController controller = new ItemEditController(item,
					programsListView.getSelectionModel().getSelectedItem());
			showItemEdit(controller, "Editovanie bodu volebného programu");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiaden bod nie je vybraný, vyberte prosím bod");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	void showItemEdit(ItemEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("itemEdit.fxml"));
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

	////////////////////////////////////////////////////////////////

	@FXML
	void addProgramButtonClick(ActionEvent event) {
		ProgramEditController controller = new ProgramEditController(party);
		showProgramEdit(controller, "Pridávanie nového volebného programu");
	}

	@FXML
	void deleteProgramButtonClick(ActionEvent event) {
		// TODO
	}

	@FXML
	void editProgramButtonClick(ActionEvent event) {

		Program program = programsListView.getSelectionModel().getSelectedItem();
		if (program != null) {
			ProgramEditController controller = new ProgramEditController(program, party);
			showProgramEdit(controller, "Editovanie volebného programu");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiaden program nie je vybraný, vyberte prosím program");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	void showProgramEdit(ProgramEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("programEdit.fxml"));
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////

	@FXML
	void savePartyButtonClick(ActionEvent event) {

	}

}
