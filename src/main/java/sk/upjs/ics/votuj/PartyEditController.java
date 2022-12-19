package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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

public class PartyEditController {

	private Party party;
	private PartyFxModel partyFxModel;
	private Party savedParty;
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
		///////////////////////// pridane
		termWatched = termsComboBox.getSelectionModel().getSelectedItem();

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

///////////////////////////////////////////////////////////////////////
	@FXML
	void addTermButtonClick(ActionEvent event) {
		TermEditController controller = new TermEditController();
		showTermEdit(controller, "Pridávanie volebného obdobia");
	}

	@FXML
	void editTermButtonClick(ActionEvent event) {
		Term selectedTerm = termsComboBox.getSelectionModel().getSelectedItem();
		if (selectedTerm != null) {
			TermEditController controller = new TermEditController(selectedTerm);
			showTermEdit(controller, "Editovanie volebného obdobia");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadne volebné obdobie nie je vybrané na editovanie");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
	}

	@FXML
	void deleteTermButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		boolean successful = false;
		Term term = termsComboBox.getSelectionModel().getSelectedItem();
		List<Term> terms = new ArrayList<>();
		if (term != null) {
			try {
				Alert alertW = new Alert(AlertType.WARNING);
				alertW.setTitle("Upozornenie!");
				alertW.setHeaderText("Potvrdenie vymazania");
				alertW.setContentText("Volebné obdobie od " + term.getSince() + " - do " + term.getTo() + " s id: "
						+ term.getId() + " bude vymazané. Naozaj chcete vymazať?");
				ButtonType btDelete = new ButtonType("Vymazať");
				ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

				alertW.getButtonTypes().setAll(btDelete, btCancel);

				dialog = alertW.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");

				Optional<ButtonType> result = alertW.showAndWait();
				if (result.get() == btDelete) {
					successful = DaoFactory.INSTANCE.getTermDao().delete(term.getId());
					termsModel.clear();
					terms = DaoFactory.INSTANCE.getTermDao().getAll();
					termsModel.addAll(terms);
					termsComboBox.setItems(termsModel);
				}

			} catch (ObjectUndeletableException e) {
				alert.setContentText("Snažíte sa vymazať volebné obdobie, ktoré je už používané");
				alert.show();
				e.printStackTrace();
				return;

			}
		} else {
			alert.setContentText("Žiadne volebné obdobie nie je vybrané na vymazanie");
			alert.show();
			return;
		}

		if (successful) {
			termsModel.clear();
			terms = DaoFactory.INSTANCE.getTermDao().getAll();
			termsModel.addAll(terms);
			termsComboBox.setItems(termsModel);
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

			if (controller.getSavedTerm() != null) {
				termsModel.clear();
				List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
				termsModel.addAll(terms);
				termsComboBox.setItems(termsModel);
				termsComboBox.getSelectionModel().select(controller.getSavedTerm());

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////

	@FXML
	void addCandidateButtonClick(ActionEvent event) {
		Party lokParty = null;
		if (savedParty != null) {
			lokParty = savedParty;
		} else {
			lokParty = party;
		}
		Term term = termsComboBox.getSelectionModel().getSelectedItem();
		/*
		 * System.out.println("SA SPRAVIL CANDIDAT EDIT CONTROLLER PRE ADD");
		 * System.out.println(savedParty); System.out.println(lokParty.toString()); //JA
		 * TU POSIELAM NULL PARTY
		 */
		CandidateEditController controller = new CandidateEditController(lokParty, term);
		showCandidateEdit(controller, "Pridávanie nového kandidáta");
		// TU alert netreba party bude furt vybrana
	}

	@FXML
	void editCandidateButtonClick(ActionEvent event) {
		Party lokParty = null;
		if (savedParty != null) {
			lokParty = savedParty;
		} else {
			lokParty = party;
		}
		Candidate candidate = candidatesListView.getSelectionModel().getSelectedItem();
		if (candidate != null) {
			System.out.println("SA SPRAVIL CANDIDAT EDIT CONTROLLER PRE EDIT");
			System.out.println(lokParty.toString());
			System.out.println(candidate.getParty().toString());
			CandidateEditController controller = new CandidateEditController(candidate, lokParty, termWatched);
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
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		boolean successful = false;
		Candidate candidate = candidatesListView.getSelectionModel().getSelectedItem();
		List<Candidate> candidates = new ArrayList<>();

		if (candidate != null) {
			try {
				Alert alertW = new Alert(AlertType.WARNING);
				alertW.setTitle("Upozornenie!");
				alertW.setHeaderText("Potvrdenie vymazania");
				alertW.setContentText("Kandidát " + candidate.getName() + " " + candidate.getSurname()
						+ " s volebným číslom " + candidate.getCandidateNumber() + " a s id: " + candidate.getId()
						+ " bude vymazaný. Naozaj ho chcete vymazať?");
				ButtonType btDelete = new ButtonType("Vymazať");
				ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

				alertW.getButtonTypes().setAll(btDelete, btCancel);

				dialog = alertW.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");

				Optional<ButtonType> result = alertW.showAndWait();
				if (result.get() == btDelete) {
					successful = DaoFactory.INSTANCE.getCandidateDao().delete(candidate.getId());
					candidatesModel.clear();
					candidates = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party, termWatched);
					candidatesModel.addAll(candidates);
					candidatesListView.setItems(candidatesModel);
				}

			} catch (ObjectUndeletableException e) {
				///////////////////////////////// úúúúúúú/////ú///////////////////////
				//////////////////// CO SEM?
				alert.setContentText("Snažíte sa vymazať kandidáta ktorý už je používaný");
				alert.show();
				e.printStackTrace();
				return;

			}
		} else {
			alert.setContentText("Žiaden kandidát nie je vybraný na vymazanie");
			alert.show();
			return;
		}

		if (successful) {
			candidatesModel.clear();
			candidates = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party, termWatched);
			candidatesModel.addAll(candidates);
			candidatesListView.setItems(candidatesModel);
		}

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

			if (controller.getSavedCandidate() != null) {
				Party lokParty = null;
				if (savedParty != null) {
					lokParty = savedParty;
				} else {
					lokParty = party;
				}

				candidatesModel.clear();
				List<Candidate> candidates = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(lokParty,
						termWatched);
				candidatesModel.addAll(candidates);
				candidatesListView.setItems(candidatesModel);
				System.out.println("SAAAAAAAAAAAAAaa TOOOOOOOOOOOO UPDATLO TU");
				System.out.println(candidates.toString());
			}

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
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		boolean successful = false;
		Item item = itemsTableView.getSelectionModel().getSelectedItem();
		List<Item> items = new ArrayList<>();

		if (item != null) {
			try {
				Alert alertW = new Alert(AlertType.WARNING);
				alertW.setTitle("Upozornenie!");
				alertW.setHeaderText("Potvrdenie vymazania");
				alertW.setContentText("Bod programu s názvom: " + item.getName() + " a s id " + item.getId()
						+ "bude vymazaný. Chcete skutočne vymazať?");
				ButtonType btDelete = new ButtonType("Vymazať");
				ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

				alertW.getButtonTypes().setAll(btDelete, btCancel);

				dialog = alertW.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");

				Optional<ButtonType> result = alertW.showAndWait();
				if (result.get() == btDelete) {
					successful = DaoFactory.INSTANCE.getItemDao().delete(item.getId());
					itemsModel.clear();
					// idk ze ktore z tych dvoch
					items = DaoFactory.INSTANCE.getItemDao().getByTermParty(termWatched, party);
					// items =
					// DaoFactory.INSTANCE.getItemDao().getByProgram(programsListView.getSelectionModel().getSelectedItem());
					itemsModel.addAll(items);
					itemsTableView.setItems(itemsModel);
				}

			} catch (ObjectUndeletableException e) {
				alert.setContentText("Snažíte sa vymazať bod, ktorý už je používaný");
				alert.show();
				e.printStackTrace();
				return;

			}
		} else {
			alert.setContentText("Žiaden bod nie je vybraný na vymazanie");
			alert.show();
			return;
		}

		if (successful) {
			itemsModel.clear();
			// idk ze ktore z tych dvoch
			items = DaoFactory.INSTANCE.getItemDao().getByTermParty(termWatched, party);
			// items =
			// DaoFactory.INSTANCE.getItemDao().getByProgram(programsListView.getSelectionModel().getSelectedItem());
			itemsModel.addAll(items);
			itemsTableView.setItems(itemsModel);
		}
	}

	@FXML
	void editItemButtonclick(ActionEvent event) {
		Item item = itemsTableView.getSelectionModel().getSelectedItem();
		if (item != null) {
			ItemEditController controller = new ItemEditController(item,
					programsListView.getSelectionModel().getSelectedItem());
			// ItemEditController controller = new ItemEditController(item,
			// programsModel.get(0));
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

			if (controller.getSavedItem() != null) {
				Party lokParty = null;
				if (savedParty != null) {
					lokParty = savedParty;
				} else {
					lokParty = party;
				}

				itemsModel.clear();
				List<Item> items = DaoFactory.INSTANCE.getItemDao().getByTermParty(termWatched, lokParty);
				itemsModel.addAll(items);
				itemsTableView.setItems(itemsModel);
				System.out.println("SAAAAAAAAAAAAAaa TOOOOOOOOOOOO UPDATLO TU");
				System.out.println(items.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////

	@FXML
	void addProgramButtonClick(ActionEvent event) {
		// List<Program> allPrograms =
		// DaoFactory.INSTANCE.getProgramDao().getByParty(party);
		// for (Program p : allPrograms) {
		if (!programsModel.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(
					"V tomto volebnom období už existuje program, vyberte iné volebné obdobie alebo editujte existujúci program");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
		// }
		ProgramEditController controller = new ProgramEditController(party);
		showProgramEdit(controller, "Pridávanie nového volebného programu");
	}

	@FXML
	void deleteProgramButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		boolean successful = false;
		Program program = programsListView.getSelectionModel().getSelectedItem();
		List<Program> programs = new ArrayList<>();
		if (program != null) {
			try {
				Alert alertW = new Alert(AlertType.WARNING);
				alertW.setTitle("Upozornenie!");
				alertW.setHeaderText("Potvrdenie vymazania");
				alertW.setContentText("Program " + program.getName() + " strany " + program.getParty() + " za obdobie "
						+ program.getTerm() + " bude vamazaný. Naozaj chcete vymazať?");
				ButtonType btDelete = new ButtonType("Vymazať");
				ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

				alertW.getButtonTypes().setAll(btDelete, btCancel);

				dialog = alertW.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");

				Optional<ButtonType> result = alertW.showAndWait();
				if (result.get() == btDelete) {
					successful = DaoFactory.INSTANCE.getProgramDao().delete(program.getId());
					programsModel.clear();
					programs = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
					programsModel.addAll(programs);
					programsListView.setItems(programsModel);
				}

			} catch (ObjectUndeletableException e) {
				alert.setContentText("Snažíte sa vymazať program, ktoré je má body a obdobie");
				alert.show();
				e.printStackTrace();
				return;

			}
		} else {
			alert.setContentText("Žiaden program nie je vybraný na vymazanie");
			alert.show();
			return;
		}

		if (successful) {
			programsModel.clear();
			programs = DaoFactory.INSTANCE.getProgramDao().getByTermParty(termWatched, party);
			programsModel.addAll(programs);
			programsListView.setItems(programsModel);
		}

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
			List<Program> programs = new ArrayList<>();
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

			if (controller.getSavedProgram() != null) {
				programsModel.clear();
				programs = DaoFactory.INSTANCE.getProgramDao().getByTermParty(controller.getSavedProgram().getTerm(),
						party);
				programsModel.addAll(programs);
				programsListView.setItems(programsModel);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////

	@FXML
	void savePartyButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Party party = partyFxModel.getParty();
		if (party.getName() == null || party.getName().equals("")) {
			alert.setContentText("Názov strany musí byť vyplnený, prosím doplňte.");
			alert.show();
			return;
		}
		if (party.getInfo() == null || party.getInfo().equals("")) {
			alert.setContentText("Info strany musí byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}

		Long foreignId = null;
		List<Party> allParties = DaoFactory.INSTANCE.getPartyDao().getAll();
		for (Party p : allParties) {
			if (p.getName().equals(party.getName())) {
				foreignId = p.getId();
				alert.setContentText("Strana s takým názvom už v databáze existuje s id: " + foreignId);
				alert.show();
				return;
			}
		}

		try {
			if (party != null) {
				System.out.println("Idem spravit save party");
				System.out.println(party.toString());
				savedParty = DaoFactory.INSTANCE.getPartyDao().save(party);
				System.out.println("SA ULOZILA STRANA:");
				System.out.println(savedParty.toString());
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// partyInfoTextArea.getScene().getWindow().hide();

	}

	public Party getSavedParty() {
		return savedParty;
	}

}
